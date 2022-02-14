package uk.nhs.digital.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static uk.nhs.digital.cache.CacheConcurrentAccessTest.RoundRobinIterableValueSupplier.valuesFrom;
import static uk.nhs.digital.test.util.TestFileUtils.deleteFileOrDirectoryRecursivelyQuietly;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.env.MockEnvironment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class CacheConcurrentAccessTest {

    private static final int CACHED_ELEMENTS_COUNT = 10;
    private static final int REQUEST_COUNT_TOTAL = 100;
    private static final int REQUEST_COUNT_CONCURRENT = 10;
    private static final int FILE_SIZE_BYTES = 1024 * 1024;

    public static final Supplier<String> RANDOM_STRING_VALUE_SUPPLIER = () -> RandomStringUtils.random(FILE_SIZE_BYTES, true, true);
    public static final Supplier<String> DO_NOT_CALL_ME_STRING_VALUE_SUPPLIER = () -> {
        throw new AssertionError("Value generator should not have been called - the cache is expected to be fully populated for all pre-defined keys, see setUp() method.");
    };

    private final Map<String, String> elementsToCache = new ConcurrentHashMap<>();

    private Path tempDirectory;

    private Cache<String, String> cache;
    private ClassPathXmlApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        tempDirectory = Files.createTempDirectory(CacheConcurrentAccessTest.class.getSimpleName());

        givenCustomCacheWiredUp();

        givenCachePopulated();
    }

    @After
    public void tearDown() {
        // To invoke all 'destroy' methods, including the one on uk.nhs.digital.cache.listener.CacheEvictingListenerRegisteringComponent
        // to ensure we deregister our listener, so that we don't leak state to other tests through PersistedHippoEventListenerRegistry
        // class.
        applicationContext.close();

        Optional.ofNullable(tempDirectory).ifPresent(path -> deleteFileOrDirectoryRecursivelyQuietly(tempDirectory));
    }

    @Test
    public void get_supportsConcurrentRequests_forTheSameExistingEntry() throws InterruptedException {

        // given
        final ExecutorService executorService = Executors.newFixedThreadPool(REQUEST_COUNT_CONCURRENT);
        final String key = valuesFrom(elementsToCache.keySet()).next();

        // when
        IntStream.rangeClosed(1, REQUEST_COUNT_TOTAL).forEach(i -> executorService.submit(() -> {
            final String actualCachedValue = cache.get(key, DO_NOT_CALL_ME_STRING_VALUE_SUPPLIER);

            assertThat(
                "Retrieved element from cache for key " + key,
                actualCachedValue,
                is(elementsToCache.get(key))
            );
        }));
        executorService.shutdown();

        // then
        assertTrue(
            "Finished before timeout.",
            executorService.awaitTermination(1, TimeUnit.MINUTES)
        );
    }

    @Test
    public void get_supportsConcurrentRequests_forVariousExistingEntries() throws InterruptedException {

        // given
        final ExecutorService executorService = Executors.newFixedThreadPool(REQUEST_COUNT_CONCURRENT);
        final RoundRobinIterableValueSupplier<String, Set<String>> keySupplier = valuesFrom(elementsToCache.keySet());

        // when
        IntStream.rangeClosed(1, REQUEST_COUNT_TOTAL)
            .mapToObj(i -> keySupplier.next())
            .forEach(key -> executorService.submit(() -> {
                final String actualCachedValue = cache.get(key, DO_NOT_CALL_ME_STRING_VALUE_SUPPLIER);

                assertThat(
                    "Retrieved element from cache for key " + key,
                    actualCachedValue,
                    is(elementsToCache.get(key))
                );

            }));
        executorService.shutdown();

        // then
        assertTrue(
            "Finished before timeout.",
            executorService.awaitTermination(1, TimeUnit.MINUTES)
        );
    }

    @Test
    public void getsAndSavesEntryUpdatedAndReadConcurrently() throws InterruptedException {

        // given
        final ExecutorService executorService = Executors.newFixedThreadPool(REQUEST_COUNT_CONCURRENT);
        final String randomKey = valuesFrom(elementsToCache.keySet()).next();

        // when
        IntStream.rangeClosed(0, REQUEST_COUNT_TOTAL)
            .forEach(i -> executorService.submit(() -> {

                // for every fifth element purge and re-create it
                if (i % 5 == 0) {
                    final String key = String.valueOf(i);
                    cache.remove(key);
                    cache.get(key, () -> elementsToCache.get(key));

                    return;
                }

                // for the rest - just look the element up and return it
                final String actualCachedValue = cache.get(randomKey, DO_NOT_CALL_ME_STRING_VALUE_SUPPLIER);

                assertThat(
                    "Retrieved element from cache for key " + randomKey,
                    actualCachedValue,
                    is(elementsToCache.get(randomKey))
                );
            }));
        executorService.shutdown();

        // then
        assertTrue(
            "Finished before timeout.",
            executorService.awaitTermination(1, TimeUnit.MINUTES)
        );
    }

    private void givenCustomCacheWiredUp() {

        // Loads application context using bean definitions actually used in production.

        final MockEnvironment environment = new MockEnvironment()
            .withProperty("siteCache.heavyContentPageCache.maxMegabytesLocalDisk", "512")
            .withProperty("siteCache.cacheManager.diskStorePath", tempDirectory.toAbsolutePath().toString())
            .withProperty("siteCache.heavyContentPageCache.timeToIdle", "PT15M");

        applicationContext = new ClassPathXmlApplicationContext();
        applicationContext.setEnvironment(environment);
        applicationContext.setConfigLocations(
            "/test-data/api-specifications/CacheIntegratedTest/cache-test-app-context.xml", // enables property resolution in the rest of the app context files
            "/META-INF/hst-assembly/overrides/custom-page-cache.xml"
        );
        applicationContext.refresh();

        cache = (Cache<String, String>) applicationContext.getBean("heavyContentCache");
    }

    private void givenCachePopulated() {
        IntStream.rangeClosed(1, CACHED_ELEMENTS_COUNT)
            .forEach(i -> {
                final String key = String.valueOf(i);
                final String value = RANDOM_STRING_VALUE_SUPPLIER.get();

                elementsToCache.put(key, value); // populate reference map in memory

                cache.get(key, () -> value); // populate actual cache on disk
            });
    }

    static class RoundRobinIterableValueSupplier<V, I extends Iterable<V>> {


        private final I source;
        private Iterator<V> iterator;

        private RoundRobinIterableValueSupplier(final I source) {
            this.source = source;
            iterator = source.iterator();
        }

        public static <V, I extends Iterable<V>> RoundRobinIterableValueSupplier<V, I> valuesFrom(final I source) {
            return new RoundRobinIterableValueSupplier<>(source);
        }

        public V next() {
            if (!iterator.hasNext()) {
                iterator = source.iterator();
            }

            return iterator.next();
        }
    }
}
