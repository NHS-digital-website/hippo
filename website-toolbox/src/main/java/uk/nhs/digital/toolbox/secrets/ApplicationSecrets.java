package uk.nhs.digital.toolbox.secrets;

import static java.lang.System.getProperty;
import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ApplicationSecrets {

    private static final Logger log = getLogger(ApplicationSecrets.class);

    private Map<String, String> cache;
    private RemoteSecrets remote;

    public ApplicationSecrets(Map<String, String> cache) {
        this(cache, null);
    }

    public ApplicationSecrets(Map<String, String> cache, RemoteSecrets remote) {
        this.cache = cache;
        this.remote = remote;
    }

    private String getValueImpl(String key, Runnable onValueNotFound) {
        if (!this.cache.containsKey(key)) {
            if (StringUtils.isNotBlank(getProperty(key))) {
                if (Objects.nonNull(remote) && remote.isRemoteValue(getProperty(key))) {
                    this.cache.put(key, remote.getRemoteValue(remote.toRemoteAddress(getProperty(key))));
                } else {
                    this.cache.put(key, getProperty(key));
                }
            } else if (StringUtils.isNotBlank(System.getenv(key))) {
                if (Objects.nonNull(remote) && remote.isRemoteValue(System.getenv(key))) {
                    this.cache.put(key, remote.getRemoteValue(remote.toRemoteAddress(System.getenv(key))));
                } else {
                    this.cache.put(key, System.getenv(key));
                }
            } else if (StringUtils.isNotBlank(getFromFile(key))) {
                this.cache.put(key, getFromFile(key));
            } else {
                onValueNotFound.run();
            }
        }

        return this.cache.get(key);
    }

    public String getValue(String key) {
        String value = this.getValueImpl(key, () ->
            log.warn("The key/value (or address of a remote value) for '"
                + key
                + "', should be set as a Java Property or an Environment variable or a file in {catalina.base}/conf."));

        return value;
    }

    public String getFromFile(final String filename) {
        String baseDir = getProperty("catalina.base");
        try {
            String data = new String(Files.readAllBytes(Paths.get(baseDir, "conf", filename)));
            return data;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Will use the found value as a key repeatedly until a blank value is found.
     * @return The last not-blank value or null if the first value is blank.
     */
    public String getValueChained(final String key) {
        // Done this way to not log the final missing key.
        Function<String, String> getValueChainedImpl = new Function<String, String>() {
            @Override
            public String apply(String implKey) {
                String value = getValueImpl(implKey, () -> { });

                if (StringUtils.isNotBlank(value)) {
                    String valueIndirect = this.apply(value);
                    return valueIndirect;
                } else {
                    return implKey;
                }
            }
        };

        // Get first value normally to as to still log on blank.
        // For nested values it can't be known if blank is wrong or just the end of the line.
        String value = getValue(key);

        if (StringUtils.isNotBlank(value)) {
            String valueIndirect = getValueChainedImpl.apply(value);
            return valueIndirect;
        } else {
            return key;
        }
    }

    public static Logger getLog() {
        return log;
    }

    /**
     * Intended for, strictly necessary, debug logging of a secret. This returns a
     * mostly starred-out secret (credit card receipt style redaction), where only
     * the last four digits remain intact.
     *
     * Please do not log secrets if you do not need to! And always make sure they
     * are masked.
     *
     * For safety, the first ten characters are always starred out, such that a
     * secret less than ten characters will be fully starred-out. Moreover, for four characters
     * to remain intact, the secret must have a length of at least fourteen characters.
     *
     * @param secret in plain text.
     * @return A starred out secret with upto the last four characters intact.
     */
    public static String mask(final String secret) {
        final int length =  secret.length();
        final int end = length - (length - 10 <= 0 ? 0 : length - 10 >= 4 ? 4 : length - 10);
        return secret.substring(0, end).replaceAll("(?s).", "*") + secret.substring(end);
    }
}
