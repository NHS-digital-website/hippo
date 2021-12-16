package uk.nhs.digital.apispecs;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.nhs.digital.apispecs.ApiSpecificationImportMetadata.Item.metadataItem;
import static uk.nhs.digital.apispecs.ApiSpecificationImportMetadata.metadataWith;

import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApiSpecificationImportMetadataTest {

    public static final Comparator<ApiSpecificationImportMetadata.Item> BY_JCR_ID = Comparator.comparing(ApiSpecificationImportMetadata.Item::apiSpecJcrId);

    @Test
    public void with_createsNewInstancePopulatedWithGivenArguments() {

        // given
        final List<ApiSpecificationImportMetadata.Item> expectedItems = asList(
            metadataItem("6c684d98-61e9-4ff6-bf40-5e288bd692f8", Instant.parse("2021-11-26T12:30:01Z")),
            metadataItem("06bf9af3-e63d-42cf-a60a-b80063129638", Instant.parse("2021-11-26T12:30:02Z")),
            metadataItem("b214ce0f-8202-46b8-895b-24129a2dfd9c", Instant.parse("2021-11-26T12:30:03Z"))
        );

        // when
        final ApiSpecificationImportMetadata actualApiSpecificationImportMetadata = metadataWith(expectedItems);

        // then
        final List<ApiSpecificationImportMetadata.Item> actualItems = new ArrayList<>(actualApiSpecificationImportMetadata.items());

        // we don't care about the ordering
        expectedItems.sort(BY_JCR_ID);
        actualItems.sort(BY_JCR_ID);

        assertThat(
            "Metadata contains all the items used for its creation.",
            actualItems,
            is(expectedItems)
        );
    }

    @Test
    public void with_createsEmptyInstanceWhenNoMetadataItemsGiven() {

        // given
        final List<ApiSpecificationImportMetadata.Item> expectedItems = emptyList();

        // when
        final ApiSpecificationImportMetadata actualApiSpecificationImportMetadata = metadataWith(expectedItems);

        // then
        final List<ApiSpecificationImportMetadata.Item> actualItems = new ArrayList<>(actualApiSpecificationImportMetadata.items());

        assertThat(
            "Metadata created as empty returns empty collection of items.",
            actualItems,
            is(emptyList())
        );
    }

    @Test
    public void getBySpecJcrId_returnsExistingInstanceWhenAvailable() {

        // given
        final ApiSpecificationImportMetadata.Item expectedMetadataItem = metadataItem("06bf9af3-e63d-42cf-a60a-b80063129638", Instant.parse("2021-11-26T12:30:02Z"));

        final List<ApiSpecificationImportMetadata.Item> expectedItems = asList(
            metadataItem("6c684d98-61e9-4ff6-bf40-5e288bd692f8", Instant.parse("2021-11-26T12:30:01Z")),
            expectedMetadataItem,
            metadataItem("b214ce0f-8202-46b8-895b-24129a2dfd9c", Instant.parse("2021-11-26T12:30:03Z"))
        );
        final ApiSpecificationImportMetadata metadata = metadataWith(expectedItems);

        // when
        final ApiSpecificationImportMetadata.Item actualMetadataItem = metadata.getBySpecJcrId("06bf9af3-e63d-42cf-a60a-b80063129638");

        // then

        assertThat(
            "Metadata return existing item with matching JCR id.",
            actualMetadataItem,
            is(expectedMetadataItem)
        );
    }

    @Test
    public void getBySpecJcrId_returnsNewInstanceWhenAvailable() {

        // given
        final List<ApiSpecificationImportMetadata.Item> existingItems = asList(
            metadataItem("6c684d98-61e9-4ff6-bf40-5e288bd692f8", Instant.parse("2021-11-26T12:30:01Z")),
            metadataItem("06bf9af3-e63d-42cf-a60a-b80063129638", Instant.parse("2021-11-26T12:30:02Z")),
            metadataItem("b214ce0f-8202-46b8-895b-24129a2dfd9c", Instant.parse("2021-11-26T12:30:03Z"))
        );
        final ApiSpecificationImportMetadata metadata = metadataWith(existingItems);

        final ApiSpecificationImportMetadata.Item newMetadataItem = metadataItem("f970a41d-baed-4777-8db0-607532e8f1a1", Instant.EPOCH);

        // when
        final ApiSpecificationImportMetadata.Item actualMetadataItem = metadata.getBySpecJcrId(newMetadataItem.apiSpecJcrId());

        // then
        assertThat(
            "Metadata return existing item with matching JCR id.",
            actualMetadataItem,
            is(newMetadataItem)
        );
    }
}
