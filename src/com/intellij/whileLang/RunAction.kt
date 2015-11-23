package com.intellij.whileLang

import com.intellij.lexer.FlexAdapter
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.tree.IElementType
import com.intellij.whileLang.psi.WhileTypes

/**
 * Created by Mikhail on 02.11.2015.
 */
public class RunAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        // TODO: insert action logic here
        val project = e.getData(CommonDataKeys.PROJECT) ?: return
    }

//    public fun comp(tokenType: IElementType) {
//        when (tokenType) {
//            WhileTypes.SEP ->
//        }
//    }
    override public fun update(e: AnActionEvent) {
        val project = e.getData(CommonDataKeys.PROJECT)
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val presentation = e.getPresentation()

        if (psiFile == null || project == null || editor == null) {
            presentation.setEnabled(false)
            return
        }
    }

    public fun runCompiler(project: Project, auto: Boolean) {
        if (project == null) return
        val editor = FileEditorManager.getInstance(project).getSelectedTextEditor()
        if (editor == null) return

        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument())



    }

}
