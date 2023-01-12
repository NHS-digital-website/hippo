package uk.nhs.digital.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.GetExParams;
import uk.nhs.digital.common.util.DateUtils;

import java.util.function.Supplier;

public class RedisCache implements HeavyContentCache<String, String> {
    private static final Logger log = LoggerFactory.getLogger(RedisCache.class);
    private String name = toString();
    private final JedisPool jedisPool;
    private final long expirySeconds;
    private final String environmentName;
    private final String nodeId;

    /**
     * @param expiryDuration ISO duration string.
     */
    public RedisCache(JedisPool jedisPool, String expiryDuration, String environmentName, String nodeId) {
        this.jedisPool = jedisPool;
        this.expirySeconds = DateUtils.durationFromIso(expiryDuration).getSeconds();
        this.environmentName = environmentName;
        this.nodeId = nodeId;
    }

    public String buildCacheKey(String key) {
        return String.format("%s:%s:%s", environmentName, nodeId, key);
    }

    @Override
    public String get(String key, Supplier<String> valueFactory) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis == null) {
                log.error("Could not instantiate a Redis connection; acting as no-op and returning value produced by the supplier for key {}", key);
                return valueFactory.get();
            }

            String cacheKey = buildCacheKey(key);

            log.debug("Cache '{}': loading value for key {} from cache.", name, cacheKey);
            String value = jedis.getEx(cacheKey, GetExParams.getExParams().ex(expirySeconds)); // returns null on no matching entry

            if (value == null) {
                log.info("Cache '{}': no value found for key {}; generating new value.", name, cacheKey);
                value = valueFactory.get();
                log.debug("Cache '{}': storing new value for key {}.", name, cacheKey);
                jedis.setex(cacheKey, expirySeconds, value);
                log.info("Cache '{}': new value stored for key {}.", name, cacheKey);

            } else {
                log.info("Cache '{}': value found for key {}.", name, cacheKey);
            }

            return value;
        } catch (JedisConnectionException e) {
            log.error(
                String.format("Could not instantiate a Redis connection; acting as no-op and returning value produced by the supplier for key \"%s\"}", key),
                e);
            return valueFactory.get();
        }
    }

    @Override
    public void remove(String key) {
        String cacheKey = buildCacheKey(key);
        log.info("Cache '{}': evicting entry with key {}.", name, cacheKey);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(cacheKey);
        }
        log.info("Cache '{}': evicted entry with key {}.", name, cacheKey);
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
