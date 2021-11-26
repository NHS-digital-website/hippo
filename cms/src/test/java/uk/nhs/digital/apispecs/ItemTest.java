package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

public class ItemTest {

    @Test
    public void with_createsNewInstancePopulatedWithGivenArguments() {
        // given
        final String apiSpecJcrId = UUID.randomUUID().toString();
        final Instant lastChangeCheckInstant = Instant.now();

        // when
        final ApiSpecificationImportMetadata.Item actual = ApiSpecificationImportMetadata.Item.metadataItem(apiSpecJcrId, lastChangeCheckInstant);

        assertThat(
            "Sets API Specification JCR id.",
            actual.apiSpecJcrId(),
            is(apiSpecJcrId)
        );

        assertThat(
            "Sets API Specification last change check instant.",
            actual.lastChangeCheckInstant(),
            is(lastChangeCheckInstant)
        );

        assertThat(
            "Keeps default value of 'specExists' flag.",
            actual.specExists(),
            is(false)
        );
    }
}