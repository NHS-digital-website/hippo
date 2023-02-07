package uk.nhs.digital.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class NoopCache<K, V> implements HeavyContentCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(NoopCache.class);

    @Override
    public V get(K key, Supplier<V> valueFactory) {
        log.warn("No heavy content cache configured. Re-rendering key {}.", key);

        return valueFactory.get();
    }

    @Override
    public void remove(K key) {

    }

    @Override
    public void purge() {

    }

    @Override
    public void setBeanName(String name) {

    }
}
