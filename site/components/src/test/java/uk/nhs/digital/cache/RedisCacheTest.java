package uk.nhs.digital.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.GetExParams;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.function.Supplier;

@SuppressWarnings("resource")
public class RedisCacheTest extends MockitoSessionTestBase {

    @Mock private JedisPool jedisPool;
    @Mock private Jedis jedis;

    @Mock private Supplier<String> valueFactory;

    private final String key = randomString();
    private String cacheKey = null;
    private final String environmentName = randomString();
    private final String nodeId = randomString();

    RedisCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new RedisCache(jedisPool, "PT24H", environmentName, nodeId);
        cache.setBeanName("testCache");
        cacheKey = cache.buildCacheKey(key);
        given(jedisPool.getResource()).willReturn(jedis);
    }

    @Test
    public void get_retrievesCachedValue_whenEntryPresentInCache() {

        // given
        final String cachedValue = randomString();

        given(jedis.getEx(eq(cacheKey), any(GetExParams.class))).willReturn(cachedValue);

        // when
        final String actualValue = cache.get(key, valueFactory);

        // then
        assertThat(
            "Returned value is that stored in Redis.",
            actualValue,
            is(cachedValue)
        );
    }

    @Test
    public void get_producesNewValueAndCachesIt_whenEntryAbsentFromCache() {

        // given
        final String generatedValue = randomString();

        given(jedis.getEx(eq(cacheKey), any(GetExParams.class))).willReturn(null);
        given(valueFactory.get()).willReturn(generatedValue);

        // when
        final String actualValue = cache.get(key, valueFactory);

        // then
        then(jedis).should().setex(eq(cacheKey), anyLong(), eq(generatedValue));

        // then
        assertThat(
            "Returned value is that returned from value factory.",
            actualValue,
            is(generatedValue)
        );
    }

    @Test
    public void remove_removesEntryWithGivenKey() {

        // given

        // when
        cache.remove(key);

        // then
        then(jedis).should().del(cacheKey);
    }

    @Test
    public void purge_removesAllEntriesFromCache() {
        // given

        // when
        cache.purge();

        // then
        then(jedis).should().flushDB();
    }

    @Test
    public void get_usesCorrectCacheFormat() {
        // given
        final String cachedValue = randomString();
        given(jedis.getEx(eq(cacheKey), any(GetExParams.class))).willReturn(cachedValue);

        String correctKey = String.format("%s:%s:%s", environmentName, nodeId, key);

        // when
        cache.get(key, valueFactory);

        // then

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(jedis).getEx(captor.capture(), any(GetExParams.class));
        String actualCacheKey = captor.getValue();

        assertThat(
            "Cache key used is correct.",
            actualCacheKey,
            is(correctKey)
        );
    }
}