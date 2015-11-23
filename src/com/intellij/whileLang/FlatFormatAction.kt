package com.intellij.whileLang

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiDocumentManager

/**
 * Created by Mikhail on 02.11.2015.
 */

public class FlatFormatAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT) ?: return
        val psiJavaFile = e.getPsiJavaFileFromContext() ?: return
        val document = PsiDocumentManager.getInstance(project).getDocument(psiJavaFile) ?: return

        val text = document.getText()
        val newText = text.replace('\n', ' ')
        project.performUndoWrite { document.replaceString(0, text.length(), newText) }
    }

    override fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        val editor = e.getData(CommonDataKeys.PROJECT)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val presentation = e.getPresentation()

        if (psiFile == null || project == null || editor == null) {
            presentation.setEnabled(false)
            return
        }

        val isLangJava = psiFile.getLanguage().equals(WhileLanguage.INSTANCE)
        presentation.setEnabled(false)
    }
}