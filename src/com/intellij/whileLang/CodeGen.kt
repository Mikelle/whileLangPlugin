package com.intellij.whileLang

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.whileLang.psi.WhileTypes
import com.intellij.whileLang.parser.WhileParser
import com.intellij.whileLang.psi.impl.*
import generated.psi.impl.*

import org.jetbrains.org.objectweb.asm.ClassWriter
import org.jetbrains.org.objectweb.asm.Label
import org.jetbrains.org.objectweb.asm.MethodVisitor
import org.jetbrains.org.objectweb.asm.tree.*
import org.jetbrains.org.objectweb.asm.Opcodes.*
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

/**
 * Created by Mikhail on 05.11.2015.
 */
class CodeGen {
    public val MAX_MEM = 32767
    private val lbls = Stack<Label>()
    private var firstLoop = true
    var numberOfAssign = 0

    public fun toByteCode(program: PsiFile?, outFileName: String): ByteArray {
        val cw = ClassWriter(0)
        cw.visit(V1_7, ACC_PUBLIC, outFileName, null, "java/lang/Object", null)
        val mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
                "([Ljava/lang/String;)V", null, null)
        mw.visitCode()
        mw.traverse(program)
        mw.visitInsn(RETURN)
        mw.visitMaxs(1600, 1600)
        mw.visitEnd()
        cw.visitEnd()
        return cw.toByteArray() //получаем байткод
    }

    private fun MethodVisitor.traverse(myFile: PsiFile?) {
        val whileFile = myFile as WhileFile
        val stmtList = whileFile.getStmtList()
        visitWhileStatementList(stmtList as PsiStmtListImpl)
//        val procList = whileFile.getProcList()
//        visitWhileProcList(procList as PsiProcListImpl)
    }

    public fun MethodVisitor.visitWhileAssignStmt(expr: PsiAssignStmtImpl) {
        val c = expr.getExpr()
        if (c is PsiLiteralExprImpl) {
            visitWhilePsiLiteralExpr(c)
            visitVarInsn(ISTORE, numberOfAssign)
            numberOfAssign++
        }
        // как обработать объявление переменных вне функций
        // visitFieldInsn(PUTFIELD, "адрес", "название переменной", "I")
    }

    public fun MethodVisitor.visitWhilePsiLiteralExpr(expr: PsiLiteralExprImpl) {
        val num = expr.number.text.toInt()
        when (num) {
            -1 -> visitInsn(ICONST_M1)
            0  -> visitInsn(ICONST_0)
            1  -> visitInsn(ICONST_1)
            2  -> visitInsn(ICONST_2)
            3  -> visitInsn(ICONST_3)
            4  -> visitInsn(ICONST_4)
            5  -> visitInsn(ICONST_5)
            else ->
                if (num <= 127 && num >= -128) visitIntInsn(BIPUSH, num)
                else if (num <= 32767 && num >= -32768) visitIntInsn(SIPUSH, num)
                else visitIntInsn(LDC, num)
        }
    }

    public fun MethodVisitor.visitWhileWriteStmt(expr: PsiWriteStmtImpl) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "out",
                "Ljava/io/PrintStream;")
        visitWhileExpr(expr.expr as PsiExprImpl)
        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                "print", "(I)V", false)
    }

    public fun MethodVisitor.visitWhileReadStmt(expr: PsiReadStmtImpl) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;")
        visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false)
        numberOfAssign++
        visitVarInsn(ISTORE, numberOfAssign)
    }

    public fun MethodVisitor.visitWhileIfStmt(expr: PsiIfStmtImpl) {
        //Если есть ветка else, то нужно заводить label для true, если нету, то не нужно
        val c = expr.bexpr
        if (c is PsiRelBexprImpl) {
            val rel = c.rel.text
            val label_true = Label()
            val label_false = Label()
            val elseBr = expr.getElseBranch()
            var op = 0
            when (rel) {
            /** rel ::= '<'|'<='|'='|'>='|'>' */
                "="  -> op = IF_ICMPEQ
                "<"  -> op = IF_ICMPLT
                "<=" -> op = IF_ICMPLE
                ">"  -> op = IF_ICMPGT
                ">=" -> op = IF_ICMPGE
            }
            if (elseBr != null) {
                lbls.push(label_true)
                lbls.push(label_false)
                visitJumpInsn(op, label_false)
                visitWhileStatementList(elseBr as PsiStmtListImpl)
                visitJumpInsn(GOTO, label_true)
            } else {
                lbls.push(label_false)
                visitJumpInsn(op, label_false)
                visitWhileStatementList(expr.thenBranch as PsiStmtListImpl)
            }
            visitFrame(F_SAME, 0, null, 0, null)
        }
    }

    //    public fun MethodVisitor.visitWhileRelBexpr(expr: PsiRelBexpr) {
    //        visitWhileExpr(expr.left)
    //        visitWhileExpr(expr.right)
    //        val rel = expr.rel.text
    //        val labelFalse = Label()
    //        when (rel) {
    //        /** rel ::= '<'|'<='|'='|'>='|'>' */
    //            "="  -> visitJumpInsn(IF_ICMPEQ, labelFalse)
    //            "<"  -> visitJumpInsn(IF_ICMPLT, labelFalse)
    //            "<=" -> visitJumpInsn(IF_ICMPLE, labelFalse)
    //            ">"  -> visitJumpInsn(IF_ICMPGT, labelFalse)
    //            ">=" -> visitJumpInsn(IF_ICMPGE, labelFalse)
    //        }
    //    }

    public fun MethodVisitor.visitWhileBinaryBexpr(expr: PsiBinaryBexprImpl) {
        val blOp = expr.blOp.text

        visitWhileBexpr(expr.left as PsiBexprImpl)
        visitWhileBexpr(expr.right as PsiBexprImpl)

        when (blOp) {
            "and" -> visitInsn(IAND)
            "or"  -> visitInsn(IOR)
        }
    }

    public fun MethodVisitor.visitWhileOrBexpr(expr: PsiOrBexprImpl) {
        val list = expr.bexprList
        for (i in expr.bexprList.indices) {

        }
    }

    public fun MethodVisitor.visitWhileNotBexpr(expr: PsiNotBexprImpl) {
        visitInsn(INEG)
    }

    public fun MethodVisitor.visitWhileProcList(expr: PsiProcListImpl) {
        //cw.visitMethod(ACC_PUBLIC, expr.getText(), "()I", null, null)
        for (i in expr.procedureList.indices) {
            visitWhileStatementList(expr.procedureList[i].stmtList as PsiStmtListImpl)
        }
        val label = Label()
        visitLabel(label)
        visitLocalVariable("this", ""/*расположение файла?*/, null,
                label, label/* заглушки для labels*/, 0)
        visitMaxs(1600, 1600)
        visitEnd()
    }


    public fun MethodVisitor.visitWhileBinaryExpr(expr: PsiBinaryExprImpl) {
        val op = expr.getArOp().text

        visitWhileExpr(expr.left as PsiExprImpl)
        visitWhileExpr(expr.right as PsiExprImpl)
        when (op) {
            "+" -> visitInsn(IADD)
            "-" -> visitInsn(ISUB)
            "*" -> visitInsn(IMUL)
            "/" -> visitInsn(IDIV)
            "%" -> visitInsn(IREM)
        }
    }

    public fun MethodVisitor.visitWhilePlusExpr(expr: PsiPlusExprImpl) {
        val op = expr.plusOp.text
        when (op) {
            "+" -> visitInsn(IADD)
            "-" -> visitInsn(ISUB)
        }
    }

    public fun MethodVisitor.visitWhileMulExpr(expr: PsiMulExprImpl) {
        val op = expr.mulOp.text
        when (op) {
            "*" -> visitInsn(IMUL)
            "/" -> visitInsn(IDIV)
            "%" -> visitInsn(IREM)
        }
    }

    public fun MethodVisitor.visitWhileStmt(expr: PsiWhileStmtImpl) {
        val startLabel = Label()
        val endLabel = Label()
        lbls.push(endLabel)
        lbls.push(startLabel)
        visitLabel(startLabel)
        visitFrame(F_APPEND, 1, arrayOf("[I", INTEGER), 0, null)
        visitWhileStatementList(expr.stmtList as PsiStmtListImpl)
        visitJumpInsn(IFEQ, endLabel)
        visitJumpInsn(GOTO, lbls.pop())
        visitLabel(lbls.pop())
        visitFrame(F_SAME, 0, null, 0, null)
    }

    public fun MethodVisitor.visitWhileRefExpr(expr: PsiRefExprImpl) {
        visitVarInsn(ILOAD, numberOfAssign - 1)
    }

    public fun MethodVisitor.visitWhileStatementList(expr: PsiStmtListImpl) {
        val list = expr.getStmtList()
        for (i in list.indices) {
            var stmt = list.get(i)
            when (stmt) {
                is PsiAssignStmtImpl -> visitWhileAssignStmt(stmt)
                is PsiIfStmtImpl     -> visitWhileIfStmt(stmt)
                is PsiWhileStmtImpl  -> visitWhileStmt(stmt)
                is PsiWriteStmtImpl  -> visitWhileWriteStmt(stmt)
                is PsiReadStmtImpl   -> visitWhileReadStmt(stmt)
                //is PsiSkipStmt   ->
            }
        }
    }

    public fun MethodVisitor.visitWhileExpr(expr: PsiExprImpl?) {
        when (expr) {
            is PsiBinaryExprImpl -> visitWhileBinaryExpr(expr)
            is PsiReadStmtImpl   -> visitWhileReadStmt(expr)
            is PsiMulExprImpl    -> visitWhileMulExpr(expr)
            is PsiPlusExprImpl   -> visitWhilePlusExpr(expr)
            is PsiRefExprImpl    -> visitWhileRefExpr(expr)
        }
    }

    public fun MethodVisitor.visitWhileBexpr(expr: PsiBexprImpl?) {
        when (expr) {
            //is PsiAndBexpr ->
            is PsiBinaryBexprImpl -> visitWhileBinaryBexpr(expr)
            //is PsiLiteralBexpr ->
            is PsiNotBexprImpl -> visitWhileNotBexpr(expr)
            //is PsiOrBexpr ->
            //is PsiRelBexpr ->
        }
    }
}

public fun main(args: Array<String>) {
    val test = "proc qwe (q, w)" +
    "q := q - w;" +
    "w := q + w;" +
    "q := q + w;" +
    "endp"
    val parse = WhileParser()
    val program: PsiFile? = null
    val className = "Test"
    val classByteArray = CodeGen().toByteCode(program, className)
    val targetFile = Paths.get("$className.class")
    Files.write(
            targetFile,
            classByteArray,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
    )
}

