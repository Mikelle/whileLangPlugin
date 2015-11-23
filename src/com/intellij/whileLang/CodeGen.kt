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

import java.util.Stack

/**
 * Created by Mikhail on 05.11.2015.
 */
class CodeGen  {
    public val MAX_MEM = 32767
    private val lbls = Stack<Label>()
    private var firstLoop = true
    var numberOfAssign = 0

    val cw = ClassWriter(0)
    val mw = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main",
            "([Ljava/lang/String;)V", null, null)
    fun toByteCode(program: ASTNode?, outFileName: String): ByteArray {

        cw.visit(V1_7, ACC_PUBLIC, outFileName, null, "java/lang/Object", null)
        mw.visitCode()
        //TODO: MainMethod
        if (program?.treePrev?.psi != null) { program?.treePrev?.psi?.text}
        if (program?.psi != null) { program?.psi?.text}
        if (program?.treeNext?.psi != null) { program?.treeNext?.psi?.text}
        cw.visitEnd()
        return cw.toByteArray() //получаем байткод
    }

//    public fun generate_assign(node: ASTNode) {
//        var lvalue = node
//        val expr_type = node.elementType
//        if (lvalue.elementType == WhileTypes.ASSIGN_STMT) {
//        }
//
//    }

    public fun MethodVisitor.visitWhileExprConstInt(expr: PsiLiteralExpr) {
        val c = expr.getNumber().text.toInt()
        when (c) {
            -1 -> visitInsn(ICONST_M1)
            0 -> visitInsn(ICONST_0)
            1 -> visitInsn(ICONST_1)
            2 -> visitInsn(ICONST_2)
            3 -> visitInsn(ICONST_3)
            4 -> visitInsn(ICONST_4)
            5 -> visitInsn(ICONST_5)
            else ->
                if (c <= 127 && c >= -128) visitIntInsn(BIPUSH, c)
                else if (c <= 32767 && c >= -32768) visitIntInsn(SIPUSH, c)
                else visitIntInsn(LDC, c)
        }
        numberOfAssign++
        visitVarInsn(ISTORE, numberOfAssign)
        // как обработать объявление переменных вне функций
        // visitFieldInsn(PUTFIELD, "адрес", "название переменной", "I")
    }

    public fun MethodVisitor.visitWhileWriteSmtm(expr: PsiWriteStmt) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "out",
                "Ljava/io/PrintStream;")
        // TODO: need to write call for function
        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                "print", "(I)V", false)
    }

    public fun MethodVisitor.visitWhileRead(expr: PsiReadStmt) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;")
        visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false)
        val c = expr.id.text.toInt()
        visitVarInsn(ISTORE, c)
    }

    public fun MethodVisitor.visitWhileBinaryBexpr(expr: PsiRelBexpr, expr1: PsiIfStmt) {
        //Если есть ветка else, то нужно заводить label для true, если нету, то не нужно
        val c = expr.getRel().text
        val label_true = Label()
        val label_false = Label()
        var op = 0
        when (c) {
            /** rel ::= '<'|'<='|'='|'>='|'>' */
            "="  -> op = IF_ICMPEQ
            "<"  -> op = IF_ICMPLT
            "<=" -> op = IF_ICMPLE
            ">"  -> op = IF_ICMPGT
            ">=" -> op = IF_ICMPGE
        }
        if (expr1.getElseBranch() != null) {
            lbls.push(label_true)
            lbls.push(label_false)
            visitJumpInsn(op, label_false)
            //TODO: need to write call for function (generate_list_stmt)
            visitJumpInsn(GOTO, label_true)
        } else {
            lbls.push(label_false)
            visitJumpInsn(op, label_false)
            //TODO: call for function (generate_list_stmt)
        }
        visitFrame(F_SAME, 0, null, 0, null)
    }

    public fun MethodVisitor.visitWhileFunction(expr: PsiProcedure) {
        cw.visitMethod(ACC_PUBLIC, expr.getText()/* Не знаю, как взять
        название функции*/ ,"()I", null, null)
        //TODO: generate_list_stmt
        val label = Label()
        visitLabel(label)
        visitLocalVariable("this", ""/*расположение файла?*/, null,
                label, label/* заглушки для labels*/, 0 )
        visitMaxs(1600, 1600)
        visitEnd()
    }

    public fun MethodVisitor.visitBinaryExpr(expr: PsiBinaryExpr) {
        val c = expr.getArOp().text
        var op = 0
        when (c) {
            "+" -> op = IADD
            "-" -> op = ISUB
            "*" -> op = IMUL
            "/" -> op = IDIV
            "%" -> op = IREM
        }
        //TODO: generate_expr(expr.getLeft), generate_expr(expr.getRight)
        visitInsn(op)
    }

    public fun MethodVisitor.visitWhileStmt(expr: PsiWhileStmt) {
        val startLabel = Label()
        val endLabel = Label()
        lbls.push(endLabel)
        lbls.push(startLabel)
        visitLabel(startLabel)
        visitFrame(F_APPEND, 1, arrayOf("[I", INTEGER), 0, null)
        //TODO: generate_list_expr(expr.getStmtList()), bexpr
        visitJumpInsn(IFEQ, endLabel)
        visitJumpInsn(GOTO, lbls.pop())
        visitLabel(lbls.pop())
        visitFrame(F_SAME, 0, null, 0, null)

    }

    public fun MethodVisitor.visitWhileStatementList(expr: PsiStmtList) {
        val list = expr.getStmtList()
        for (i in list.indices) {
            when (i) {

            }
        }
        /** хотелось бы как-то получать тип PsiExpr, и в зависимости от него
         * вызывать функцию
          */

    }

    public fun generateWhileExpr(expr: PsiExpr) {
        when (expr) {
            is PsiReadStmt -> mw.visitWhileRead(expr)

        }
    }
//    fun genTest(node:ASTNode, t: IElementType) {
//        //val t: PsiElement = node.getPsi()
//        //val t: IElementType = node.elementType
//        var lvalue = node.firstChildNode
//        var expr_type = node
//    }
}
public fun main(args: Array<String>) {
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

