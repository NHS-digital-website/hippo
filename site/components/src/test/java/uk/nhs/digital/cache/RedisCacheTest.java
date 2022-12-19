package uk.nhs.digital.cache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.GetExParams;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

@SuppressWarnings("resource")
public class RedisCacheTest extends MockitoSessionTestBase {

    @Mock private JedisPool jedisPool;
    @Mock private Jedis jedis;

    @Mock private Supplier<String> valueFactory;

    private final String key = randomString();

    RedisCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new RedisCache(jedisPool, "PT24H");
        cache.setBeanName("testCache");
    }

    @Test
    public void get_retrievesCachedValue_whenEntryPresentInCache() {

        // given
        final String cachedValue = randomString();

        given(jedisPool.getResource()).willReturn(jedis);
        given(jedis.getEx(eq(key), any(GetExParams.class))).willReturn(cachedValue);

        // when
        final String actualValue = cache.get(key, valueFactory);

        assertThat(
            "Returned value is that stored in Ehcache.",
            actualValue,
            is(cachedValue)
        );
    }

    @Test
    public void get_producesNewValueAndCachesIt_whenEntryAbsentFromCache() {

        // given
        final String generatedValue = randomString();

        given(jedisPool.getResource()).willReturn(jedis);
        given(jedis.getEx(eq(key), any(GetExParams.class))).willReturn(null);
        given(valueFactory.get()).willReturn(generatedValue);

        // when
        final String actualValue = cache.get(key, valueFactory);

        // then
        then(jedis).should().setex(eq(key), anyLong(), eq(generatedValue));

        assertThat(
            "Returned value is that returned from value factory.",
            actualValue,
            is(generatedValue)
        );
    }

    @Test
    public void remove_removesEntryWithGivenKey() {

        // given
        given(jedisPool.getResource()).willReturn(jedis);

        // when
        cache.remove(key);

        // then
        then(jedis).should().del(key);
    }

    @Test
    public void purge_removesAllEntriesFromCache() {
        // given
        given(jedisPool.getResource()).willReturn(jedis);

        // when
        cache.purge();

        // then
        then(jedis).should().flushDB();
    }
}