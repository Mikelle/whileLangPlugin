package com.intellij.whileLang

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.whileLang.psi.WhileTypes
import com.intellij.whileLang.parser.WhileParser
import com.intellij.whileLang.psi.impl.*

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

    val cw = ClassWriter(0)
    val mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
            "([Ljava/lang/String;)V", null, null)

    abstract class ASTNodeIterator: ASTNode, Iterable<com.intellij.psi.PsiElement> {
        override fun iterator(): Iterator<com.intellij.psi.PsiElement> = LCRNodeIterator(this)
    }

    private class EmptyIterator<PsiElement>(): Iterator<PsiElement> {
        override fun hasNext(): Boolean = false
        override fun next(): PsiElement { throw NoSuchElementException() }
    }

    private abstract class NodeIterator<PsiElement>(
            protected val node: ASTNodeIterator?
    ): Iterator<com.intellij.psi.PsiElement> {
        val treePrev = node?.treePrev as ASTNodeIterator
        val treeNext = node?.treeNext as ASTNodeIterator
        protected val lIterator: Iterator<com.intellij.psi.PsiElement> =
                treePrev.iterator() ?: EmptyIterator()
        protected val rIterator: Iterator<com.intellij.psi.PsiElement> =
                treeNext.iterator() ?: EmptyIterator()
        protected var  observed: Boolean = false
        protected var lHasNext: Boolean = true
            get() =
            if (field) { field = lIterator.hasNext(); field } else false
        protected var rHasNext: Boolean = true
            get() =
            if (field) { field = rIterator.hasNext(); field } else false

        override fun hasNext(): Boolean {
            if (!observed) { return true }
            if (lHasNext ) { return true }
            if (rHasNext ) { return true }
            return false
        }
        //        !observed || lIterator.hasNext() || rIterator.hasNext()
    }

    private class LCRNodeIterator(
            node: ASTNodeIterator?
    ): NodeIterator<com.intellij.psi.PsiElement>(node) {
        override fun next(): com.intellij.psi.PsiElement {
            if (lHasNext ) { return lIterator.next() }
            if (!observed) { observed = true; return node?.psi as PsiElement
            }
            if (rHasNext ) { return rIterator.next() }
            throw NoSuchElementException()
        }
    }

    public fun toByteCode(program: ASTNode?, outFileName: String): ByteArray {

        cw.visit(V1_7, ACC_PUBLIC, outFileName, null, "java/lang/Object", null)
        mw.visitCode()
        //TODO: MainMethod
        val programIter: ASTNodeIterator = program as ASTNodeIterator
        for (op in programIter) {
            when(op) {
                is PsiAssignStmt -> mw.visitWhileAssignStmt(op)
                is PsiIfStmt     -> mw.visitWhileIfStmt(op)
                is PsiReadStmt   -> mw.visitWhileReadStmt(op)
                is PsiProcList   -> mw.visitWhileFunction(op)
                is PsiStmtList   -> mw.visitWhileStatementList(op)
            }
        }
        cw.visitEnd()
        return cw.toByteArray() //получаем байткод
    }

    public fun MethodVisitor.visitWhileAssignStmt(expr: PsiAssignStmt) {
        val c = expr.getExpr()
        if (c is PsiLiteralExpr) {
            visitWhilePsiLiteralExpr(c)
            val id = expr.id.text.toInt()
            visitVarInsn(ISTORE, id)
        }
        // как обработать объявление переменных вне функций
        // visitFieldInsn(PUTFIELD, "адрес", "название переменной", "I")
    }

    public fun MethodVisitor.visitWhilePsiLiteralExpr(expr: PsiLiteralExpr) {
        val num = expr.number.text.toInt()
        when (num) {
            -1 -> visitInsn(ICONST_M1)
            0 -> visitInsn(ICONST_0)
            1 -> visitInsn(ICONST_1)
            2 -> visitInsn(ICONST_2)
            3 -> visitInsn(ICONST_3)
            4 -> visitInsn(ICONST_4)
            5 -> visitInsn(ICONST_5)
            else ->
                if (num <= 127 && num >= -128) visitIntInsn(BIPUSH, num)
                else if (num <= 32767 && num >= -32768) visitIntInsn(SIPUSH, num)
                else visitIntInsn(LDC, num)
        }
    }

    public fun MethodVisitor.visitWhileWriteStmt(expr: PsiWriteStmt) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "out",
                "Ljava/io/PrintStream;")
        visitWhileExpr(expr.getExpr())
        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                "print", "(I)V", false)
    }

    public fun MethodVisitor.visitWhileReadStmt(expr: PsiReadStmt) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;")
        visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false)
        val c = expr.id.text.toInt()
        visitVarInsn(ISTORE, c)
    }

    public fun MethodVisitor.visitWhileIfStmt(expr: PsiIfStmt) {
        //Если есть ветка else, то нужно заводить label для true, если нету, то не нужно
        val c = expr.bexpr
        if (c is PsiRelBexpr) {
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
                visitWhileStatementList(elseBr)
                visitJumpInsn(GOTO, label_true)
            } else {
                lbls.push(label_false)
                visitJumpInsn(op, label_false)
                visitWhileStatementList(expr.thenBranch)
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

    public fun MethodVisitor.visitWhileBinaryBexpr(expr: PsiBinaryBexpr) {
        val blOp = expr.blOp.text

        visitWhileBexpr(expr.left)
        visitWhileBexpr(expr.right)

        when (blOp) {
            "and" -> visitInsn(IAND)
            "or"  -> visitInsn(IOR)
        }
    }

    public fun MethodVisitor.visitWhileOrBexpr(expr: PsiOrBexpr) {
        val list = expr.bexprList
        for (i in expr.bexprList.indices) {

        }
    }

    public fun MethodVisitor.visitWhileWhileNotBexpr(expr: PsiNotBexpr) {
        val c = expr.bexpr

    }

    public fun MethodVisitor.visitWhileFunction(expr: PsiProcList) {
        cw.visitMethod(ACC_PUBLIC, expr.getText(), "()I", null, null)
        for (i in expr.procedureList.indices) {
            visitWhileStatementList(expr.procedureList[i].stmtList)
        }
        val label = Label()
        visitLabel(label)
        visitLocalVariable("this", ""/*расположение файла?*/, null,
                label, label/* заглушки для labels*/, 0)
        visitMaxs(1600, 1600)
        visitEnd()
    }


    public fun MethodVisitor.visitWhileBinaryExpr(expr: PsiBinaryExpr) {
        val op = expr.getArOp().text

        visitWhileExpr(expr.left)
        visitWhileExpr(expr.right)
        when (op) {
            "+" -> visitInsn(IADD)
            "-" -> visitInsn(ISUB)
            "*" -> visitInsn(IMUL)
            "/" -> visitInsn(IDIV)
            "%" -> visitInsn(IREM)
        }
    }

    public fun MethodVisitor.visitWhilePlusExpr(expr: PsiPlusExpr) {
        val op = expr.plusOp.text
        when (op) {
            "+" -> visitInsn(IADD)
            "-" -> visitInsn(ISUB)
        }
    }

    public fun MethodVisitor.visitWhileMulExpr(expr: PsiMulExpr) {
        val op = expr.mulOp.text
        when (op) {
            "*" -> visitInsn(IMUL)
            "/" -> visitInsn(IDIV)
            "%" -> visitInsn(IREM)
        }
    }

    public fun MethodVisitor.visitWhileStmt(expr: PsiWhileStmt) {
        val startLabel = Label()
        val endLabel = Label()
        lbls.push(endLabel)
        lbls.push(startLabel)
        visitLabel(startLabel)
        visitFrame(F_APPEND, 1, arrayOf("[I", INTEGER), 0, null)
        visitWhileStatementList(expr.stmtList)
        visitJumpInsn(IFEQ, endLabel)
        visitJumpInsn(GOTO, lbls.pop())
        visitLabel(lbls.pop())
        visitFrame(F_SAME, 0, null, 0, null)
    }

    public fun MethodVisitor.visitWhileRefExpr(expr: PsiRefExpr) {
        visitVarInsn(ILOAD, expr.id.text.toInt())
    }

    public fun MethodVisitor.visitWhileStatementList(expr: PsiStmtList) {
        val list = expr.getStmtList()
        for (i in list.indices) {
            var stmt = list.get(i)
            when (stmt) {
                is PsiAssignStmt -> visitWhileAssignStmt(stmt)
                is PsiIfStmt     -> visitWhileIfStmt(stmt)
                is PsiWhileStmt  -> visitWhileStmt(stmt)
                is PsiWriteStmt  -> visitWhileWriteStmt(stmt)
                is PsiReadStmt   -> visitWhileReadStmt(stmt)
                //is PsiSkipStmt   ->
            }
        }
    }

    public fun MethodVisitor.visitWhileExpr(expr: PsiExpr?) {
        when (expr) {
            is PsiBinaryExpr -> visitWhileBinaryExpr(expr)
            is PsiReadStmt   -> visitWhileReadStmt(expr)
            is PsiMulExpr    -> visitWhileMulExpr(expr)
            is PsiPlusExpr   -> visitWhilePlusExpr(expr)
            is PsiRefExpr    -> visitWhileRefExpr(expr)
        }
    }

    public fun MethodVisitor.visitWhileBexpr(expr: PsiBexpr?) {
        when (expr) {
            //is PsiAndBexpr ->
            is PsiBinaryBexpr -> visitWhileBinaryBexpr(expr)
            //is PsiLiteralBexpr ->
            //is PsiNotBexpr ->
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
    val program: ASTNode? = null
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

