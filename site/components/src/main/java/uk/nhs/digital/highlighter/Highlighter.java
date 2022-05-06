package uk.nhs.digital.highlighter;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A Java wrapped version of highlight.js using Nashorn.
 *
 * Because Nashorn has a slow start up time the engine is cached one per instance of Hippo, so that start up happens only once.
 */
public enum Highlighter {

    INSTANCE; // Enum Singleton.

    private static final String POLYFILL = "highlighter/nashorn-polyfill.js";
    private static final String HIGHLIGHTER = "highlighter/highlight-js/highlight.pack.js"; // build from https://highlightjs.org/download/
    private static final String DECORATOR = "highlighter/highlighter-decorator.js";
    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    static {
        try {
            engine.eval(loadScript(POLYFILL));
            engine.eval(loadScript(HIGHLIGHTER));
            engine.eval(loadScript(DECORATOR ));
        } catch (ScriptException | IOException e) {
            // As we have control of the scripts this will not happen.
            e.printStackTrace();
        }
    }

    private static String loadScript(String script) throws IOException {
        ClassLoader classloader = Highlighter.class.getClassLoader();
        InputStream in = classloader.getResourceAsStream(script);
        byte[] data = IOUtils.toByteArray(in);
        return new String(data, UTF_8);
    }

    public String paint(final String source, final Language lang) {
        try {
            return (String) ((Invocable) engine).invokeFunction("paint", source, lang.getKey());
        } catch (ScriptException | NoSuchMethodException e) {
            // As we have control of the 'print' function this will not happen.
            e.printStackTrace();
            return source;
        }
    }
}
