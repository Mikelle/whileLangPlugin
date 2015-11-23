package com.intellij.whileLang

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiFileFactory
import java.lang.management.ManagementFactory

/**
 * Created by Mikhail on 02.11.2015.
 */
public class CodeFormatAction: AnAction() {
    companion object {
        private val ourNeedsToShowPerfomanceDialog = false
    }

    override public fun actionPerformed(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        val factory = PsiFileFactory.getInstance(project)
        val psiJavaFile = e.getPsiJavaFileFromContext() ?: return
        val threadMXBean = ManagementFactory.getThreadMXBean()
        val startTime = threadMXBean.currentThreadCpuTime
        val endTime = threadMXBean.currentThreadCpuTime
    }

    override public fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val presentation = e.getPresentation()

        if (psiFile == null || project == null || editor == null) {
            presentation.setEnabled(false)
            return
        }

        val isLangJava = psiFile.getLanguage().equals(WhileLanguage.INSTANCE)
        presentation.setEnabled(isLangJava)
    }
}