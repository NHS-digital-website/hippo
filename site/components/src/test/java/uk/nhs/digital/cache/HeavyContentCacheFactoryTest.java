package uk.nhs.digital.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

@SuppressWarnings("JUnitMalformedDeclaration")
@RunWith(DataProviderRunner.class)
public class HeavyContentCacheFactoryTest extends MockitoSessionTestBase {

    @Mock private DiskCache<String, String> diskCache;
    @Mock private RedisCache redisCache;

    private HeavyContentCache<String, String> callFactoryWithMocks(String type)  {
        return HeavyContentCacheFactory.factory(type, redisCache, diskCache);
    }

    @Test
    public void given_typeIsRedis_thenReturnsRedisCache() {

        // given
        final String type = "redis";

        // when
        final HeavyContentCache<String, String> actualCache = callFactoryWithMocks(type);

        // then
        assertThat(
            "Returned value should be same as Redis cache passed in.",
            actualCache,
            is(sameInstance(redisCache))
        );
    }

    @Test
    public void given_typeIsDisk_thenReturnsDiskCache() {

        // given
        final String type = "disk";

        // when
        final HeavyContentCache<String, String> actualCache = callFactoryWithMocks(type);

        // then
        assertThat(
            "Returned value should be same as Disk cache passed in.",
            actualCache,
            is(sameInstance(diskCache))
        );
    }

    @Test
    @DataProvider(value = { "redis ", " redis", " redis " }, trimValues = false)
    public void given_typeIsPadded_thenReturnsCorrectCache(String type) {

        // given
        // when
        final HeavyContentCache<String, String> actualCache = callFactoryWithMocks(type);

        // then
        assertThat(
            "Returned value should be same as Redis cache passed in.",
            actualCache,
            is(sameInstance(redisCache))
        );
    }


    @Test
    @DataProvider(value = {"null", "", " "}, trimValues = false)
    public void given_typeIsUnspecified_thenReturnsNoopCache(String type) {

        // given
        // when
        final HeavyContentCache<String, String> actualCache = callFactoryWithMocks(type);

        // then
        assertThat(
            "Returned value should be a NoopCache.",
            actualCache,
            is(instanceOf(NoopCache.class))
        );
    }
}