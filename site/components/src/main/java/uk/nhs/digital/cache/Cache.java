package uk.nhs.digital.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * <p>
 * Convenience wrapper for the actual Ehcache instances. Frees the callers from having to deal with the logic of:
 * <ul>
 * <li>checking for presence of cached elements,</li>
 * <li>making sure that the costly rendering of heavy content only happens when corresponding entry is not found in the cache,</li>
 * <li>adding the newly generated content to the cache.</li>
 * </ul>
 *
 * @param <K> Type of the key used in the cache.
 * @param <V> Type of the cached value associated with the key.
 */
public class Cache<K, V> implements BeanNameAware {

    private static final Logger log = LoggerFactory.getLogger(Cache.class);
    public static final int PURGE_BATCH_SIZE = 100;

    private String name = toString();

    private final org.ehcache.Cache<K, V> ehchache;

    public Cache(final org.ehcache.Cache<K, V> ehcache) {
        this.ehchache = ehcache;
    }

    /**
     * <p>
     * For given {@code key}, returns value from the cache if it's present there.
     * <p>
     * Otherwise, it invokes the {@code valueFactory} to produce the value, stores it in the cache,
     * and then returns the value to the caller.
     *
     * @param key           Key to use when looking up and storing value in the cache.
     * @param valueFactory Produces actual value when it has not been found in the cache.
     * @return Actual value, whether retrieved from the cache or produced on the spot.
     */
    public V get(final K key, final Supplier<V> valueFactory) {

        if (ehchache == null) {
            log.warn("No cache has been configured; acting as no-op and returning value produced by the supplier for key {}", key);
            return valueFactory.get();
        }

        log.debug("Cache '{}': loading value for key {} from cache.", name, key);
        V value = ehchache.get(key); // returns null on no matching entry
        log.debug("Cache '{}': value loaded for key {}.", name, key);

        if (value == null) {
            log.info("Cache '{}': no value found for key {}; generating new value.", name, key);
            value = valueFactory.get();
            log.debug("Cache '{}': storing new value for key {}.", name, key);
            ehchache.put(key, value);
            log.info("Cache '{}': new value stored for key {}.", name, key);

        } else {
            log.info("Cache '{}': value found for key {}.", name, key);
        }

        return value;
    }

    /**
     * Evicts entry from cache, identified by given key.
     *
     * @param key Key identifying the entry to evict from cache.
     */
    public void remove(K key) {
        log.info("Cache '{}': evicting entry with key {}.", name, key);
        ehchache.remove(key);
        log.info("Cache '{}': evicted entry with key {}.", name, key);
    }

    /**
     * Removes all elements currently held in the cache.
     */
    public void purge() {
        log.info("Cache '{}': purging entire content.", name);

        // In org.ehcache.Cache all entry removal methods require that keys are specified.
        //
        // We ask for keys and delete entries in batches to avoid loading
        // a potentially large collection into memory. Doing it in batches allows Ehcache
        // apply its own optimisations to dealing with large numbers of entries.

        while (ehchache.iterator().hasNext()) {
            final Set<K> keysToPurge = StreamSupport.stream(ehchache.spliterator(), false)
                .limit(PURGE_BATCH_SIZE)
                .map(org.ehcache.Cache.Entry::getKey)
                .collect(Collectors.toSet());

            log.debug("Cache '{}': purging keys: {}", name, keysToPurge);

            ehchache.removeAll(keysToPurge);
        }
        log.info("Cache '{}': purged entire content.", name);
    }

    @Override public void setBeanName(final String name) {
        this.name = name;
    }
}
