package com.intellij.whileLang

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.util.PsiTreeUtil

/**
 * Created by Mikhail on 02.11.2015.
 */

public fun Project.performUndoWrite(task: () -> Unit) {
    WriteCommandAction.runWriteCommandAction(this) {
        CommandProcessor.getInstance().runUndoTransparentAction { task() }
    }
}

public fun AnActionEvent.getPsiJavaFileFromContext(): PsiJavaFile? {
    val psiFile = getData(CommonDataKeys.PSI_FILE) ?: return null
    val elementAt = psiFile.findElementAt(0)
    return PsiTreeUtil.getParentOfType(elementAt, javaClass<PsiJavaFile>())
}

public fun AnActionEvent.getPsiKotlinFileFromContext() : PsiJavaFile? {
    val psiFile = getData(CommonDataKeys.PSI_FILE) ?: return null
    val elementAt = psiFile.findElementAt(0)
    return PsiTreeUtil.getParentOfType(elementAt, javaClass<PsiJavaFile>())
}