package uk.nhs.digital.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.GetExParams;
import uk.nhs.digital.common.util.DateUtils;

import java.util.function.Supplier;

public class RedisCache implements HeavyContentCache<String, String> {
    private static final Logger log = LoggerFactory.getLogger(RedisCache.class);
    private String name = toString();
    private final JedisPool jedisPool;
    private final String timeToIdle;

    public RedisCache(JedisPool jedisPool, String timeToIdle) {
        this.jedisPool = jedisPool;
        this.timeToIdle = timeToIdle;
    }

    @Override
    public String get(String key, Supplier<String> valueFactory) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis == null) {
                log.warn("No cache has been configured; acting as no-op and returning value produced by the supplier for key {}", key);
                return valueFactory.get();
            }

            long expiryDurationSeconds = DateUtils.durationFromIso(timeToIdle).getSeconds();

            log.debug("Cache '{}': loading value for key {} from cache.", name, key);
            String value = jedis.getEx(key, GetExParams.getExParams().ex(expiryDurationSeconds)); // returns null on no matching entry
            log.debug("Cache '{}': value loaded for key {}.", name, key);

            if (value == null) {
                log.info("Cache '{}': no value found for key {}; generating new value.", name, key);
                value = valueFactory.get();
                log.debug("Cache '{}': storing new value for key {}.", name, key);
                jedis.setex(key, expiryDurationSeconds, value);
                log.info("Cache '{}': new value stored for key {}.", name, key);

            } else {
                log.info("Cache '{}': value found for key {}.", name, key);
            }

            return value;
        }
    }

    @Override
    public void remove(String key) {
        log.info("Cache '{}': evicting entry with key {}.", name, key);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
        log.info("Cache '{}': evicted entry with key {}.", name, key);
    }

    @Override
    public void purge() {
        log.info("Cache '{}': purging entire content.", name);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushDB();
        }
        log.info("Cache '{}': purged entire content.", name);

    }

    @Override public void setBeanName(final String name) {
        this.name = name;
    }

}
