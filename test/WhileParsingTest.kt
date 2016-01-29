/**
 * Created by Mikhail on 26.11.2015.
 */

import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.ParsingTestCase
import com.intellij.whileLang.CodeGen
import com.intellij.whileLang.WhileFile
import com.intellij.whileLang.WhileParserDefinition
import com.intellij.whileLang.psi.impl.*
import generated.psi.impl.*
import org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument
import org.jetbrains.annotations.NonNls

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.NoSuchElementException

class WhileParsingTest : ParsingTestCase("", "l", WhileParserDefinition()) {

    fun tryToCompile() {

        val name = "AssignTestData"
        try {
            val text = loadFile(name + "." + "l")
            val myFile = createFile(name, text)
            ParsingTestCase.toParseTreeText(myFile, skipSpaces(), includeRanges())
            //myFile
            //iter(myFile);
            val className = "Test"
            val classByteArray = CodeGen().toByteCode(myFile, className)
            val targetFile = Paths.get("$className.class")
            Files.write(
                    targetFile,
                    classByteArray,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            )
            val whilef = myFile as WhileFile
            val proc = whilef.getProcList()?.procedureList
            for (element in proc.orEmpty()) {
                println(element)
            }
            val stmt = whilef.getStmtList()?.stmtList
            for (element in stmt.orEmpty()) {
                println(element)
            }

            //ensureParsed(myFile);
            //System.out.println(myFile.getLastChild().getLastChild());
            println(myFile.lastChild.lastChild.originalElement)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    fun testParsingTestData() {
        doTest(true)
        tryToCompile()

    }

    //    public interface LinkedTreeIterator {
    //        boolean hasNext();
    //        PsiElement next();
    //    }
    //    public LinkedTreeIterator preoderIterator() {
    //        return new PreoderIterator();
    //    }
    //    PsiElement root = myFile.getOriginalElement();
    //    private class PreoderIterator implements LinkedTreeIterator {
    //        private PsiElement nextPsi;
    //
    //        private PreoderIterator() {
    //            nextPsi = root;
    //        }
    //
    //        public PsiElement next() {
    //            if (nextPsi == null) throw new NoSuchElementException();
    //
    //            PsiElement key = nextPsi.getOriginalElement();
    //
    //            if (nextPsi.getPrevSibling() != null)
    //                nextPsi = nextPsi.getPrevSibling();
    //            else if (nextPsi.getNextSibling() != null)
    //                nextPsi = nextPsi.getNextSibling();
    //            else {
    //                PsiElement parent = nextPsi.getParent();
    //                PsiElement child  = nextPsi;
    //                while (parent != null && (parent.getPrevSibling() == child || parent.getPrevSibling() == null)) {
    //                    child = parent;
    //                    parent = parent.getParent();
    //                }
    //                if (parent == null)
    //                    nextPsi = null;
    //                else
    //                    nextPsi = parent.getPrevSibling();
    //            }
    //            return key;
    //        }
    //        public boolean hasNext() {
    //            return (nextPsi != null);
    //        }
    //    }

    override fun createFile(@NonNls name: String, text: String): PsiFile {
        return super.createFile(name, text)
    }

    override fun getTestDataPath(): String {
        return "C:/Users/Mikhail/Desktop/WhileLangPlugin/testData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}


