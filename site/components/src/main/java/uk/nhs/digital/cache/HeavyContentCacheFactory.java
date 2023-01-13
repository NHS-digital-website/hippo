package uk.nhs.digital.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeavyContentCacheFactory {
    private static final Logger log = LoggerFactory.getLogger(HeavyContentCacheFactory.class);

    public static <K, V> HeavyContentCache<K, V> factory(
        String type,
        HeavyContentCache<K, V> redisCache,
        HeavyContentCache<K, V> diskCache) {

        type = type == null ? "" : type.trim();

        if (type.equals("redis")) {
            return redisCache;
        } else if (type.equals("disk")) {
            return diskCache;
        } else {
            log.warn("No heavy content cache found for config: \"{}\". Will always re-render.", type);

            return new NoopCache<>();
        }
    }
}
