package uk.nhs.digital.toolbox.secrets;

import static java.lang.System.getProperty;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import java.util.Map;
import java.util.Objects;

public class ApplicationSecrets {

    private static final Logger log = getLogger(ApplicationSecrets.class);

    private Map<String, String> cache;
    private RemoteSecrets remote;

    public ApplicationSecrets(Map<String, String> cache, RemoteSecrets remote) {
        this.cache = cache;
        this.remote = remote;
    }

    public String getValue(String key) {
        if (!this.cache.containsKey(key)) {
            if (Objects.nonNull(getProperty(key))) {
                if (remote.isRemoteValue(getProperty(key))) {
                    this.cache.put(key, remote.getRemoteValue(remote.toRemoteAddress(getProperty(key))));
                } else {
                    this.cache.put(key, getProperty(key));
                }
            } else if (Objects.nonNull(System.getenv(key))) {
                if (remote.isRemoteValue(System.getenv(key))) {
                    this.cache.put(key, remote.getRemoteValue(remote.toRemoteAddress(System.getenv(key))));
                } else {
                    this.cache.put(key, System.getenv(key));
                }

            } else {
                log.warn("The key/value (or address of a remote value) for '" + key + "', should be set as a Java Property or an Environment variable.");
            }
        }
        return this.cache.get(key);
    }
}
