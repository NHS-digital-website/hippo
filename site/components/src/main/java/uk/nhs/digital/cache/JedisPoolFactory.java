package uk.nhs.digital.cache;

import redis.clients.jedis.JedisPool;

import java.net.URI;

public class JedisPoolFactory {
    public static JedisPool factory(String url, int timeout) {
        URI uri = URI.create(url);
        JedisPool jedisPool = new JedisPool(uri, timeout);

        return jedisPool;
    }
}
