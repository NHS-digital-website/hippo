package uk.nhs.digital.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.URI;

public class JedisPoolFactory {

    private static final Logger logger = LoggerFactory.getLogger(JedisPoolFactory.class);
    public static JedisPool factory(String url, int timeout) {
        URI uri = URI.create(url);
        JedisPool jedisPool = new JedisPool(uri, timeout);

        try (Jedis jedis = jedisPool.getResource()) {
            logger.info("Connected to Redis at {} says {}", url, jedis.ping());
        } catch (Exception e) {
            logger.error("Failed to connect to Redis at {}", url, e);
        }

        return jedisPool;
    }
}
