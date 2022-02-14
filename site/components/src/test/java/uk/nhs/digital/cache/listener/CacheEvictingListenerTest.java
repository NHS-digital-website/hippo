package uk.nhs.digital.cache.listener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.then;
import static uk.nhs.digital.test.TestLogger.LogAssertor.info;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import com.google.common.collect.ImmutableSet;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.cms7.event.HippoEvent;
import org.onehippo.cms7.event.HippoEventConstants;
import uk.nhs.digital.cache.Cache;
import uk.nhs.digital.cache.listener.CacheEvictingListener;
import uk.nhs.digital.test.TestLoggerRule;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;

import java.util.Set;

@RunWith(DataProviderRunner.class)
public class CacheEvictingListenerTest extends MockitoSessionTestBase {

    @Rule public TestLoggerRule logger = TestLoggerRule.targeting(CacheEvictingListener.class);

    @Mock Cache<String, String> cache;

    private Set<String> eligibleDoctypes = ImmutableSet.of("supported:doctype");

    private CacheEvictingListener listener;

    @Test
    public void newInstanceIsConfiguredWithCorrectInitialValues() {

        // given
        // setUp()

        // when
        listener = new CacheEvictingListener(cache, eligibleDoctypes);

        // then
        assertThat(
            "Event category makes the listener respond only to document workflow events.",
            listener.getEventCategory(),
            is(HippoEventConstants.CATEGORY_WORKFLOW)
        );

        assertThat(
            "Listener listens on event channel with a node-unique name.",
            listener.getChannelName(),
            is("cache-evicting-listener")
        );

        assertThat(
            "Listeners consumes event emitted before and after its registration.",
            listener.onlyNewEvents(),
            is(false)
        );
    }

    @Test
    public void evictsEntryWithGivenIdFromCache_forEligibleEvents() {

        // given
        listener = new CacheEvictingListener(cache, eligibleDoctypes);

        final String documentId = randomString();
        final String documentPath = randomString();

        @SuppressWarnings("rawtypes") final HippoEvent eligibleEvent = new HippoEvent("cms")
            .set("documentType", "supported:doctype")
            .set("action", "publish")
            .set("exception", null)
            .set("subjectId", documentId)
            .set("subjectPath", documentPath);

        // when
        listener.onHippoEvent(eligibleEvent);

        // then
        then(cache).should().remove(documentId);

        logger.shouldReceive(
            info("Document has been published: evicting previous version from cache: " + documentPath + " (" + documentId + ")")
        );
    }

    @Test
    @UseDataProvider("ineligibleEvents")
    public void ignoresIneligibleEventsWith(final String documentType, final String action, final Exception exception) {

        // given
        listener = new CacheEvictingListener(cache, eligibleDoctypes);

        final String documentId = randomString();
        final String documentPath = randomString();

        @SuppressWarnings("rawtypes")
        final HippoEvent nonEligibleEvent = new HippoEvent("cms")
            .set("documentType", documentType)
            .set("action", action)
            .set("exception", exception)
            .set("subjectId", documentId)
            .set("subjectPath", documentPath);

        // when
        listener.onHippoEvent(nonEligibleEvent);

        // then
        then(cache).shouldHaveNoInteractions();
    }

    @DataProvider
    public static Object[][] ineligibleEvents() {
        // @formatter:off
        return new Object[][]{
            // documentType           action                  exception
            {"unsupported:doctype",   "publish",              null},
            {"supported:doctype",     "unsupportedaction",    null},
            {"supported:doctype",     "publish",              new Exception()}
        };
        // @formatter:on
    }
}