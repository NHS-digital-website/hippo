package uk.nhs.digital.highlighter;

import static org.junit.Assert.*;

import org.junit.Test;

public class LanguageTest {

    @Test
    public void enumByNameJava() {
        Language language = Language.getByKey("java");
        assertEquals(Language.JAVA, language);
    }

    @Test
    public void enumByNameVbScriptHtml() {
        Language language = Language.getByKey("vbscript-html");
        Language variation = Language.getByKey("vbscripthtml");
        assertEquals(Language.VBSCRIPTHTML, language);
        assertEquals(Language.VBSCRIPTHTML, variation);
    }

}
