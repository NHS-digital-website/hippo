package uk.nhs.digital.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;

import java.util.function.Supplier;

public class RedisCache implements HeavyContentCache<String, String> {
    private static final Logger log = LoggerFactory.getLogger(RedisCache.class);
    private String name = toString();
    private final JedisPooled jedisPooled;

    public RedisCache(JedisPooled jedisPooled) {
        this.jedisPooled = jedisPooled;
    }

    @Override
    public String get(String key, Supplier<String> valueFactory) {
        if (jedisPooled == null) {
            log.warn("No cache has been configured; acting as no-op and returning value produced by the supplier for key {}", key);
            return valueFactory.get();
        }

        log.debug("Cache '{}': loading value for key {} from cache.", name, key);
        String value = jedisPooled.get(key); // returns null on no matching entry
        log.debug("Cache '{}': value loaded for key {}.", name, key);

        if (value == null) {
            log.info("Cache '{}': no value found for key {}; generating new value.", name, key);
            value = valueFactory.get();
            log.debug("Cache '{}': storing new value for key {}.", name, key);
            jedisPooled.set(key, value);
            log.info("Cache '{}': new value stored for key {}.", name, key);

        } else {
            log.info("Cache '{}': value found for key {}.", name, key);
        }

        return value;
    }

    @Override
    public void remove(String key) {
        log.info("Cache '{}': evicting entry with key {}.", name, key);
        jedisPooled.del(key);
        log.info("Cache '{}': evicted entry with key {}.", name, key);
    }

    @Override
    public void purge() {
        log.info("Cache '{}': purging entire content.", name);

        jedisPooled.functionFlush();

        log.info("Cache '{}': purged entire content.", name);
    }

    @Override public void setBeanName(final String name) {
        this.name = name;
    }

}
