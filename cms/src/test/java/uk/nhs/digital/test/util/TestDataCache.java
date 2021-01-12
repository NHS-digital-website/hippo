package uk.nhs.digital.test.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Simple in-memory cache for test data, that helps reducing the number of reads
 * from disk when the same test data file is used repeatedly by a number of test
 * cases in the same class.
 * <p>
 * Such pattern is often the case when the file defines a template that needs its
 * placeholders resolved with values sourced from a set in a 'data driven' tests.
 * <p>
 * See existing tests where this class is used for examples; note that they will
 * often use <a href="https://github.com/TNG/junit-dataprovider">junit-dataprovider</a>
 * library (look for methods annotated with {@code @DataProvider/@UseDataProvider}
 * annotations).
 */
public class TestDataCache {

    private final Map<String, String> cache = new HashMap<>();

    public static TestDataCache create() {
        return new TestDataCache();
    }

    public String get(final String cacheKey, final TestDataProvider testDataProvider) {

        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        final String testData = testDataProvider.provide();

        cache.put(cacheKey, testData);

        return testData;
    }

    @FunctionalInterface
    public interface TestDataProvider {
        String provide();
    }
}
