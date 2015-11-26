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
class CodeGen {
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
        if (program?.treePrev?.psi != null) {
            program?.treePrev?.psi?.text
        }
        if (program?.psi != null) {
            program?.psi?.text
        }
        if (program?.treeNext?.psi != null) {
            program?.treeNext?.psi?.text
        }
        cw.visitEnd()
        return cw.toByteArray() //получаем байткод
    }

    public fun MethodVisitor.visitWhileExprConstInt(expr: PsiAssignStmt) {
        val c = expr.getExpr()
        if (c is PsiLiteralExpr) {
            val num = c.number.text.toInt()
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
            numberOfAssign++
            visitVarInsn(ISTORE, numberOfAssign)
        }
        // как обработать объявление переменных вне функций
        // visitFieldInsn(PUTFIELD, "адрес", "название переменной", "I")
    }

    public fun MethodVisitor.visitWhileWriteSmtm(expr: PsiWriteStmt) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "out",
                "Ljava/io/PrintStream;")
        generateWhileExpr(expr.getExpr())
        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                "print", "(I)V", false)
    }

    public fun MethodVisitor.visitWhileRead(expr: PsiReadStmt) {
        visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;")
        visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false)
        val c = expr.id.text.toInt()
        visitVarInsn(ISTORE, c)
    }

    public fun MethodVisitor.visitWhileBinaryBexpr(expr: PsiIfStmt) {
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
                "=" -> op = IF_ICMPEQ
                "<" -> op = IF_ICMPLT
                "<=" -> op = IF_ICMPLE
                ">" -> op = IF_ICMPGT
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

    public fun MethodVisitor.visitWhileFunction(expr: PsiProcedure) {
        cw.visitMethod(ACC_PUBLIC, expr.getText(), "()I", null, null)
        //TODO: generate_list_stmt
        val label = Label()
        visitLabel(label)
        visitLocalVariable("this", ""/*расположение файла?*/, null,
                label, label/* заглушки для labels*/, 0)
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
        generateWhileExpr(expr.left)
        generateWhileExpr(expr.right)
        visitInsn(op)
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

    public fun MethodVisitor.visitWhileStatementList(expr: PsiStmtList) {
        val list = expr.getStmtList()
        for (i in list.indices) {
            var x = list.get(i)
            when (x) {
                is PsiAssignStmt -> visitWhileExprConstInt(x)
                is PsiIfStmt -> visitWhileBinaryBexpr(x)
                is PsiWhileStmt -> visitWhileStmt(x)
                is PsiWriteStmt -> visitWhileWriteSmtm(x)
            }
        }
    }

    public fun MethodVisitor.generateWhileExpr(expr: PsiExpr?) {
        when (expr) {
            is PsiReadStmt -> visitWhileRead(expr)
            is PsiBinaryExpr -> visitBinaryExpr(expr)

        }
    }
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

