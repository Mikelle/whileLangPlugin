package com.intellij.whileLang;

/**
 * Created by Mikhail on 02.11.2015.
 */
import com.intellij.lang.Language;

public class WhileLanguage extends Language {
    public static final WhileLanguage INSTANCE = new WhileLanguage();

    private WhileLanguage() {
        super("While");
    }
}
