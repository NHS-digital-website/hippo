package uk.nhs.digital.highlighter;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(Highlighter.class);

    private static final String POLYFILL = "highlighter/nashorn-polyfill.js";
    private static final String HIGHLIGHTER = "highlighter/highlight-js/highlight.pack.js"; // build from https://highlightjs.org/download/
    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    static {
        try {
            if (engine == null) {
                LOGGER.warn("Nashorn script engine is not available. Code blocks will be rendered without syntax highlighting.");
            } else {
                engine.eval(loadScript(POLYFILL));
                engine.eval(loadScript(HIGHLIGHTER));
                engine.eval("function highlight(source, lang) { return hljs.highlight(lang, source).value }");
            }
        } catch (ScriptException | IOException e) {
            // As we have control of the scripts this will not happen.
            LOGGER.warn("Failed to initialise the syntax highlighter. Code blocks will be rendered without syntax highlighting.", e);
        }
    }

    private static String loadScript(String script) throws IOException {
        ClassLoader classloader = Highlighter.class.getClassLoader();
        InputStream in = classloader.getResourceAsStream(script);
        byte[] data = IOUtils.toByteArray(in);
        return new String(data, UTF_8);
    }

    public String paint(final String source, final Language lang) {
        if (engine == null) {
            return source;
        }

        try {
            return (String) ((Invocable) engine).invokeFunction("highlight", source, lang.getKey());
        } catch (ScriptException | NoSuchMethodException e) {
            // As we have control of the 'print' function this will not happen.
            LOGGER.warn("Failed to syntax highlight code block as {}. Rendering without syntax highlighting.", lang.getKey(), e);
            return source;
        }
    }
}
