package uk.nhs.digital.highlighter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
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

    private static final String ENGINE_NAME = "nashorn";
    private static final String POLYFILL = "highlighter/nashorn-polyfill.js";
    private static final String HIGHLIGHTER = "highlighter/highlight-js/highlight.pack.js"; // build from https://highlightjs.org/download/
    private static final ScriptEngine engine = createEngine();

    private static ScriptEngine createEngine() {
        final ScriptEngineManager manager = new ScriptEngineManager(Highlighter.class.getClassLoader());
        final ScriptEngine scriptEngine = manager.getEngineByName(ENGINE_NAME);

        if (scriptEngine == null) {
            LOGGER.warn(
                "Nashorn script engine is not available. Code blocks will be rendered without syntax highlighting. Available JavaScript engines: {}.",
                availableEngines(manager)
            );
            return null;
        }

        try {
            LOGGER.info("Initialising syntax highlighter with JavaScript engine {}.", engineDescription(scriptEngine));
            scriptEngine.eval(loadScript(POLYFILL));
            scriptEngine.eval(loadScript(HIGHLIGHTER));
            scriptEngine.eval("function highlight(source, lang) { return hljs.highlight(lang, source).value }");
            LOGGER.info("Syntax highlighter initialised.");
            return scriptEngine;
        } catch (ScriptException | IOException | RuntimeException | LinkageError e) {
            LOGGER.warn(
                "Failed to initialise the syntax highlighter with JavaScript engine {}. Code blocks will be rendered without syntax highlighting.",
                engineDescription(scriptEngine),
                e
            );
            return null;
        }
    }

    private static String loadScript(String script) throws IOException {
        ClassLoader classloader = Highlighter.class.getClassLoader();
        try (InputStream in = classloader.getResourceAsStream(script)) {
            if (in == null) {
                throw new FileNotFoundException("Syntax highlighter script resource not found on classpath: " + script);
            }

            byte[] data = IOUtils.toByteArray(in);
            return new String(data, UTF_8);
        }
    }

    private static String engineDescription(final ScriptEngine scriptEngine) {
        return String.format("'%s' version '%s'",
            scriptEngine.getFactory().getEngineName(),
            scriptEngine.getFactory().getEngineVersion()
        );
    }

    private static String availableEngines(final ScriptEngineManager manager) {
        final List<String> engineNames = manager.getEngineFactories().stream()
            .map(factory -> factory.getEngineName() + " " + factory.getEngineVersion() + " names=" + factory.getNames())
            .collect(Collectors.toList());

        return engineNames.isEmpty() ? "none" : String.join("; ", engineNames);
    }

    public String paint(final String source, final Language lang) {
        if (source == null) {
            return "";
        }

        if (engine == null) {
            LOGGER.debug("Syntax highlighter unavailable; rendering unhighlighted code block.");
            return unhighlighted(source);
        }

        if (lang == null) {
            LOGGER.warn("Unable to syntax highlight code block because no supported language was supplied. Rendering without syntax highlighting.");
            return unhighlighted(source);
        }

        try {
            return (String) ((Invocable) engine).invokeFunction("highlight", source, lang.getKey());
        } catch (ScriptException | NoSuchMethodException | RuntimeException | LinkageError e) {
            LOGGER.warn("Failed to syntax highlight code block as {}. Rendering without syntax highlighting.", lang.getKey(), e);
            return unhighlighted(source);
        }
    }

    private String unhighlighted(final String source) {
        return escapeHtml4(source);
    }
}
