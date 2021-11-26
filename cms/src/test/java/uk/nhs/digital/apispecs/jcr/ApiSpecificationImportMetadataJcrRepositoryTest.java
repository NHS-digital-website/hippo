package uk.nhs.digital.apispecs.jcr;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static uk.nhs.digital.apispecs.ApiSpecificationImportMetadata.metadataWith;
import static uk.nhs.digital.apispecs.jcr.ApiSpecificationImportMetadataJcrRepositoryTest.MetatadaItemPersistentProperties.BY_JCR_ID;
import static uk.nhs.digital.test.util.ExceptionTestUtils.wrapCheckedException;
import static uk.nhs.digital.test.util.JcrTestUtils.*;
import static uk.nhs.digital.test.util.MockJcrRepoProvider.initJcrRepoFromYaml;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.junit.Before;
import org.junit.Test;
import uk.nhs.digital.apispecs.ApiSpecificationImportMetadata;
import uk.nhs.digital.test.util.JcrTestUtils;

import java.time.Instant;
import java.util.*;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class ApiSpecificationImportMetadataJcrRepositoryTest {

    private Session session;

    private ApiSpecificationImportImportMetadataJcrRepository apiSpecificationImportMetadataJcrRepository;

    @Before
    public void setUp() throws Exception {

        session = MockJcr.newSession();

        apiSpecificationImportMetadataJcrRepository = new ApiSpecificationImportImportMetadataJcrRepository(session);
    }

    @Test
    public void findApiSpecificationMetadata_returnsExistingMetadata() {

        // given
        jcrRepositoryWith("apiSpecMetadataRoot_withActualMetadata.yml");

        final ApiSpecificationImportMetadata expectedApiSpecificationImportMetadata = metadataWith(
            asList(
                metadataItem("api-spec-a-6c684d98-61e9-4ff6-bf40-5e288bd692f8", "2021-11-26T12:30:00.001Z"),
                metadataItem("api-spec-b-06bf9af3-e63d-42cf-a60a-b80063129638", "2021-11-26T12:30:00.002Z"),
                metadataItem("api-spec-c-b214ce0f-8202-46b8-895b-24129a2dfd9c", "2021-11-26T12:30:00.003Z")
            )
        );

        // when
        final ApiSpecificationImportMetadata actualApiSpecificationImportMetadata = apiSpecificationImportMetadataJcrRepository.findApiSpecificationImportMetadata();

        // then
        assertThat(
            "Returns API Specifications' metadata ",
            actualApiSpecificationImportMetadata,
            is(expectedApiSpecificationImportMetadata)
        );
    }

    @Test
    public void findApiSpecificationMetadata_returnsEmptyApiSpecificationMetadata_whenNoMetadataOfIndividualSpecsFound() {

        // given
        jcrRepositoryWith("apiSpecMetadataRoot_withNoActualMetadata.yml");

        final ApiSpecificationImportMetadata expectedApiSpecificationImportMetadata = metadataWith(emptyList());

        // when
        final ApiSpecificationImportMetadata actualApiSpecificationImportMetadata = apiSpecificationImportMetadataJcrRepository.findApiSpecificationImportMetadata();

        // then
        assertThat(
            "Returns API Specifications' metadata ",
            actualApiSpecificationImportMetadata,
            is(expectedApiSpecificationImportMetadata)
        );
    }

    @Test
    public void findApiSpecificationMetadata_throwsException_whenMetadataNodeNotAvailable() {

        // given
        // repository with no API Specification Metadata Root

        // when
        final RuntimeException actualException = assertThrows(
            RuntimeException.class,
            () -> apiSpecificationImportMetadataJcrRepository.findApiSpecificationImportMetadata()
        );

        // then
        assertThat(
            "Exception contains failure description.",
            actualException.getMessage(),
            is("Failed to retrieve API Specification Metadata node at"
                + " /jcr:root/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata")
        );
    }

    @Test
    public void save_replacesOldMetadataWithNew() throws RepositoryException {

        // given
        jcrRepositoryWith("apiSpecMetadataRoot_withActualMetadata.yml");

        final ApiSpecificationImportMetadata.Item metadataOfAnExistingSpec_unchanged =
            metadataItem("api-spec-a-6c684d98-61e9-4ff6-bf40-5e288bd692f8", "2021-11-26T12:30:00.001Z");
        metadataOfAnExistingSpec_unchanged.setSpecExists();

        final ApiSpecificationImportMetadata.Item metadataOfAnExistingSpec_changed =
            metadataItem("api-spec-b-06bf9af3-e63d-42cf-a60a-b80063129638", "2021-11-26T12:30:00.003Z");
        metadataOfAnExistingSpec_changed.setSpecExists();

        final ApiSpecificationImportMetadata.Item metadataOfANewSpec =
            metadataItem("api-spec-d-f1fc0b78-cf4e-4683-bd79-ad835dc41944", "2021-11-26T12:30:00.004Z");
        metadataOfANewSpec.setSpecExists();

        final List<ApiSpecificationImportMetadata.Item> newApiSpecsMetadataItems = asList(
            metadataOfAnExistingSpec_unchanged,
            metadataOfAnExistingSpec_changed,
            metadataOfANewSpec
        );
        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(newApiSpecsMetadataItems);

        final List<MetatadaItemPersistentProperties> expectedMetadataItems = asList(
            MetatadaItemPersistentProperties.from(metadataOfAnExistingSpec_unchanged),
            MetatadaItemPersistentProperties.from(metadataOfAnExistingSpec_changed),
            // Metadata for spec api-spec-c in the test file
            // is expected to be removed and be absent from the list of eventually saved items.
            MetatadaItemPersistentProperties.from(metadataOfANewSpec)
        );

        // when
        apiSpecificationImportMetadataJcrRepository.save(newApiSpecificationImportMetadata);

        // then
        final List<MetatadaItemPersistentProperties> actualMetadataItems = specMetadataItemsFromRepo();

        assertThat(
            "Saves API Specifications' metadata.",
            actualMetadataItems,
            is(expectedMetadataItems)
        );
    }

    @Test
    public void save_removesOldMetadata_whenSavingEmptyNewMetadata() throws RepositoryException {

        // given
        jcrRepositoryWith("apiSpecMetadataRoot_withActualMetadata.yml");

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(emptyList());

        // when
        apiSpecificationImportMetadataJcrRepository.save(newApiSpecificationImportMetadata);

        // then
        final List<MetatadaItemPersistentProperties> actualApiSpecMetadata = specMetadataItemsFromRepo();

        assertThat(
            "Removes all pre-existing API Specifications' metadata.",
            actualApiSpecMetadata,
            is(emptyList())
        );
    }

    @Test
    public void save_throwsException_onFailure() throws RepositoryException {

        // given
        // repository with no API Specification Metadata Root

        final ApiSpecificationImportMetadata anyApiSpecificationImportMetadata = metadataWith(emptyList());

        // when
        final RuntimeException actualException = assertThrows(
            RuntimeException.class,
            () -> apiSpecificationImportMetadataJcrRepository.save(anyApiSpecificationImportMetadata)
        );

        // then
        assertThat(
            "Exception contains failure description.",
            actualException.getMessage(),
            is("Failed to save API Specification Metadata at"
                + " /jcr:root/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata")
        );
    }

    private static ApiSpecificationImportMetadata.Item metadataItem(final String apiSpecJcrId, final String lastChangeCheckInstant) {
        return ApiSpecificationImportMetadata.Item.metadataItem(apiSpecJcrId, Instant.parse(lastChangeCheckInstant));
    }

    private List<MetatadaItemPersistentProperties> specMetadataItemsFromRepo() throws RepositoryException {

        final Node metadataNode = session.getRootNode()
            .getNode("/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata");

        final List<Instant> timestamps = Arrays.stream(metadataNode.getProperty("website:lastChangeCheckInstants").getValues())
            .map(value -> wrapCheckedException(value::getDate))
            .map(Calendar::toInstant)
            .collect(toList());

        final List<String> ids = Arrays.stream(metadataNode.getProperty("website:apiSpecHandleNodeIds").getValues())
            .map(value -> wrapCheckedException(value::getString))
            .collect(toList());

        final List<MetatadaItemPersistentProperties> items = new ArrayList<>(ids.size());
        for (int i = 0; i < ids.size(); i++) {
            items.add(MetatadaItemPersistentProperties.from(
                ids.get(i),
                timestamps.get(i)
            ));
        }

        return items.stream().sorted(BY_JCR_ID).collect(toList());
    }

    private void jcrRepositoryWith(final String mockRepositoryYamlFile) {

        initJcrRepoFromYaml(session, "/test-data/api-specifications/ApiSpecificationMetadataJcrRepositoryTest/" + mockRepositoryYamlFile);

        final Node rootNode = JcrTestUtils.getRootNode(session);

        MockJcr.setQueryResult(session, asList(
            JcrTestUtils.getRelativeNode(rootNode, "/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata")
        ));
    }

    static class MetatadaItemPersistentProperties {

        final String apiSpecJcrId;
        final Instant lastChangeCheckInstant;

        public static final Comparator<MetatadaItemPersistentProperties> BY_JCR_ID = Comparator.comparing(o -> o.apiSpecJcrId);

        static MetatadaItemPersistentProperties from(final Node node) {
            return new MetatadaItemPersistentProperties(
                getNameQuietly(node),
                getInstantProperty(node, "website:lastChangeCheckInstant"));
        }

        static MetatadaItemPersistentProperties from(final ApiSpecificationImportMetadata.Item item) {
            return new MetatadaItemPersistentProperties(
                item.apiSpecJcrId(),
                item.lastChangeCheckInstant()
            );
        }

        static MetatadaItemPersistentProperties from(final String apiSpecJcrId, final Instant lastChangeCheckInstant) {
            return new MetatadaItemPersistentProperties(apiSpecJcrId, lastChangeCheckInstant);
        }

        private MetatadaItemPersistentProperties(final String apiSpecJcrId, final Instant lastChangeCheckInstant) {
            this.apiSpecJcrId = apiSpecJcrId;
            this.lastChangeCheckInstant = lastChangeCheckInstant;
        }

        @Override public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final MetatadaItemPersistentProperties metatadaItemPersistentProperties = (MetatadaItemPersistentProperties) o;

            return new EqualsBuilder()
                .append(apiSpecJcrId, metatadaItemPersistentProperties.apiSpecJcrId)
                .append(lastChangeCheckInstant, metatadaItemPersistentProperties.lastChangeCheckInstant)
                .isEquals();
        }

        @Override public int hashCode() {
            return new HashCodeBuilder(17, 37)
                .append(apiSpecJcrId)
                .append(lastChangeCheckInstant)
                .toHashCode();
        }
    }
}