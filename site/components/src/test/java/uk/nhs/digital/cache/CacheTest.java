package uk.nhs.digital.cache;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import com.google.common.collect.Streams;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class CacheTest extends MockitoSessionTestBase {

    @Mock private org.ehcache.Cache<String, String> ehcache;

    @Mock private Supplier<String> valueFactory;

    private String key = randomString();

    Cache<String, String> cache;

    @Before
    public void setUp() throws Exception {
        cache = new Cache<>(ehcache);
        cache.setBeanName("testCache");
    }

    @Test
    public void get_retrievesCachedValue_whenEntryPresentInCache() {

        // given
        final String cachedValue = randomString();

        given(ehcache.containsKey(key)).willReturn(true);
        given(ehcache.get(key)).willReturn(cachedValue);

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

        given(ehcache.get(key)).willReturn(null);
        given(valueFactory.get()).willReturn(generatedValue);

        // when
        final String actualValue = cache.get(key, valueFactory);

        // then
        then(ehcache).should().put(key, generatedValue);

        assertThat(
            "Returned value is that returned from value factory.",
            actualValue,
            is(generatedValue)
        );
    }

    @Test
    public void remove_removesEntryWithGivenKey() {

        // given
        // setUp()

        // when
        cache.remove(key);

        // then
        then(ehcache).should().remove(key);
    }

    @Test
    public void purge_removesAllEntriesFromCache() {

        // given
        final List<CacheEntry> cachedEntriesBatchA = cachedEntriesBatchOf(100);
        final List<CacheEntry> cachedEntriesBatchB = cachedEntriesBatchOf(1);

        final Set<String> keysBatchA = keysOf(cachedEntriesBatchA);
        final Set<String> keysBatchB = keysOf(cachedEntriesBatchB);

        final List<org.ehcache.Cache.Entry<String, String>> cachedEntriesAll =
            from(cachedEntriesBatchA, cachedEntriesBatchB);

        given(ehcache.iterator()).will(invocation -> cachedEntriesAll.iterator());
        given(ehcache.spliterator()).will(invocation -> cachedEntriesAll.spliterator());

        givenRemoveAllCalledOnEhcacheForBatch_matchingEntriesAreRemovedFromUnderlyingStore(keysBatchA, cachedEntriesAll);
        givenRemoveAllCalledOnEhcacheForBatch_matchingEntriesAreRemovedFromUnderlyingStore(keysBatchB, cachedEntriesAll);

        // when
        cache.purge();

        // then
        final InOrder inOrder = inOrder(ehcache);
        inOrder.verify(ehcache).removeAll(keysBatchA);
        inOrder.verify(ehcache).removeAll(keysBatchB);
    }

    private ArrayList<org.ehcache.Cache.Entry<String, String>> from(
        final List<CacheEntry> cachedEntriesBatchA,
        final List<CacheEntry> cachedEntriesBatchB
    ) {
        return Streams
            .concat(cachedEntriesBatchA.stream(), cachedEntriesBatchB.stream())
            .collect(toCollection(ArrayList::new));
    }

    private Set<String> keysOf(final List<CacheEntry> batch) {
        return batch.stream().map(cacheEntry -> cacheEntry.key).collect(toSet());
    }

    private void givenRemoveAllCalledOnEhcacheForBatch_matchingEntriesAreRemovedFromUnderlyingStore(
        final Set<String> keysBatch,
        final List<org.ehcache.Cache.Entry<String, String>> cachedEntries
    ) {
        final Consumer<String> evictCorrespondingEntryFromCachedEntries =
            key -> cachedEntries.stream().filter(entry -> entry.getKey().equals(key)).findAny().ifPresent(cachedEntries::remove);

        doAnswer(invocation -> {
            keysBatch.forEach(evictCorrespondingEntryFromCachedEntries);
            return "null";
        })
            .when(ehcache)
            .removeAll(keysBatch);
    }

    private List<CacheEntry> cachedEntriesBatchOf(final int count) {
        return IntStream.rangeClosed(1, count)
            .mapToObj(index -> CacheEntry.of("key_" + index, "value_" + index))
            .collect(toCollection(ArrayList::new));
    }

    private static class CacheEntry implements org.ehcache.Cache.Entry<String, String> {

        private String key;
        private String value;

        private CacheEntry(final String key, final String value) {
            this.key = key;
            this.value = value;
        }

        public static CacheEntry of(final String key, final String value) {
            return new CacheEntry(key, value);
        }

        @Override public String getKey() {
            return key;
        }

        @Override public String getValue() {
            return value;
        }

        @Override public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final CacheEntry that = (CacheEntry) o;

            return new EqualsBuilder().append(key, that.key).append(value, that.value).isEquals();
        }

        @Override public int hashCode() {
            return new HashCodeBuilder(17, 37).append(key).append(value).toHashCode();
        }
    }
}