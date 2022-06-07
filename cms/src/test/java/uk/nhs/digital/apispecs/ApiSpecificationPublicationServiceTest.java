package uk.nhs.digital.apispecs;

import static ch.qos.logback.classic.Level.INFO;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.nhs.digital.apispecs.ApiSpecificationImportMetadata.Item.metadataItem;
import static uk.nhs.digital.apispecs.ApiSpecificationPublicationServiceTest.ApiSpecDocMockBuilder.localSpec;
import static uk.nhs.digital.test.TestLogger.LogAssertor.error;
import static uk.nhs.digital.test.TestLogger.LogAssertor.info;
import static uk.nhs.digital.test.util.TimeProviderTestUtils.nextNowIs;
import static uk.nhs.digital.test.util.TimeProviderTestUtils.resetTimeProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;
import uk.nhs.digital.test.TestLoggerRule;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ApiSpecificationPublicationServiceTest {

    private AutoCloseable mocks;

    @Mock
    private OpenApiSpecificationRepository apigeeService;

    @Mock
    private ApiSpecificationDocumentJcrRepository apiSpecDocumentRepo;

    @Mock
    private ApiSpecificationImportMetadataRepository apiSpecMetadataRepo;

    private final ArgumentCaptor<ApiSpecificationImportMetadata> apiSpecsImportMetadataArgumentCaptor =
        ArgumentCaptor.forClass(ApiSpecificationImportMetadata.class);

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApiSpecificationPublicationService.class, INFO);

    private ApiSpecificationPublicationService apiSpecificationPublicationService;

    @Before
    public void setUp() {
        mocks = openMocks(this);

        apiSpecificationPublicationService = new ApiSpecificationPublicationService(
            apigeeService,
            apiSpecDocumentRepo,
            apiSpecMetadataRepo
        );
    }

    @After
    public void tearDown() throws Exception {
        mocks.close();

        resetTimeProvider();
    }

    @Test
    public void sync_publishesSpecifications_existingInCmsButNeverPublished() {

        // given
        // @formatter:off
        final String specificationId            = "248569";
        final String apiSpecJcrId               = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";

        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.000Z";
        final String oldLocalCheckTime          = Instant.EPOCH.toString();              // older than remoteSpecModificationTime, returned when no metadata entry present
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z"); // younger than either of the above

        final String remoteSpecificationJson    = "{ \"new-spec\": \"json\" }";
        // @formatter:on

        metadataExistsForSpecWith(apiSpecJcrId, oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(apiSpecJcrId, newCheckTime) // last change check time expected to be updated
        );

        final ApiSpecificationDocument localSpecNeverPublished = localSpec()
            .withSpecId(specificationId)
            .withJcrId(apiSpecJcrId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecNeverPublished));

        final OpenApiSpecification remoteSpec = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecNeverPublished).should(never()).save();

        then(localSpecNeverPublished).should().setJsonForPublishing(remoteSpecificationJson);
        then(localSpecNeverPublished).should().saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 1, synced: 1, failed to sync: 0",
                    apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            info("Synchronised API Specification with id 248569 at /content/docs/248569")
        );
    }

    @Test
    public void sync_publishesSpecifications_thatChangedInApigeeAfterItWasPublishedInCms() {

        // given
        // @formatter:off
        final String specificationId            = "248569";
        final String apiSpecJcrId               = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";

        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.001Z";
        final String oldLocalCheckTime          =           "2020-05-10T10:30:00.000Z";  // older than remoteSpecModificationTime
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.002Z"); // younger than either of the above

        final String remoteSpecificationJson    = "{ \"new-spec\": \"json\" }";
        final String oldLocalSpecificationJson  = "{ \"old-spec\": \"json\" }";
        // @formatter:on

        metadataExistsForSpecWith(apiSpecJcrId, oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(apiSpecJcrId, newCheckTime) // last change check time expected to be updated
        );

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withSpecId(specificationId)
            .withJcrId(apiSpecJcrId)
            .withJson(oldLocalSpecificationJson)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpecUpdatedSincePublishedLocally = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUpdatedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should(never()).save();

        then(localSpecPublishedBefore).should().setJsonForPublishing(remoteSpecificationJson);
        then(localSpecPublishedBefore).should().saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 1, synced: 1, failed to sync: 0",
                apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            info("Synchronised API Specification with id 248569 at /content/docs/248569")
        );
    }

    @Test
    public void sync_doesNotChangeNorPublishSpecifications_whereApigeeReportsChangeAfterLastCheckInCms_butApigeeContentHasNotActuallyChanged() {

        // given
        // @formatter:off
        final String specificationId            = "248569";
        final String apiSpecJcrId               = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";

        final String oldLocalCheckTime          =           "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.001Z";  // younger than remoteSpecModificationTime
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z"); // younger than either of the above

        // Specs differing only in version; this field is ignored when comparing specs' JSON for changes:
        final String oldLocalSpecificationJson  = "{ \"info\": { \"version\": \"v1.2.525-beta\" }, \"json\": \"payload\" }";
        final String remoteSpecificationJson    = "{ \"info\": { \"version\": \"v1.2.526-beta\" }, \"json\": \"payload\" }";
        // @formatter:on

        metadataExistsForSpecWith(apiSpecJcrId, oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(apiSpecJcrId, newCheckTime) // last change check time expected to be updated
        );

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withSpecId(specificationId)
            .withJcrId(apiSpecJcrId)
            .withJson(oldLocalSpecificationJson)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpecWithContentUnchangedSincePublishedLocally = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecWithContentUnchangedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should(never()).save();

        then(localSpecPublishedBefore).should(never()).setJsonForPublishing(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 0, synced: 0, failed to sync: 0",
                apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName()))
        );
    }

    @Test
    public void sync_doesNotChangeNorPublishSpecifications_ifApigeeReportsNoChangeAfterLastCheckInCms() {

        // given
        // @formatter:off
        final String specificationId            = "248569";
        final String apiSpecJcrId               = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";

        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.000Z";
        final String oldLocalCheckTime          =           "2020-05-10T10:30:00.000Z";  // not older than remoteSpecModificationTime
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.002Z"); // younger than either of the above
        // @formatter:on

        metadataExistsForSpecWith(apiSpecJcrId, oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(apiSpecJcrId, newCheckTime) // last change check time expected to be updated
        );

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withSpecId(specificationId)
            .withJcrId(apiSpecJcrId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpecUnchangedSincePublishedLocally = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUnchangedSincePublishedLocally));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should(never()).save();

        then(localSpecPublishedBefore).should(never()).setJsonForPublishing(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 0, synced: 0, failed to sync: 0",
                apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName()))
        );
    }

    @Test
    public void sync_doesNotChangeNorPublishSpecifications_whichDoNotHaveMatchingCounterpartsInRemoteSystem() {

        // given
        // @formatter:off
        final String apiSpecJcrId               = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";
        final String localSpecificationId       = "965842";
        final String remoteSpecificationId      = "248569";

        final String remoteSpecModificationTime = "2020-05-10T10:30:00.001Z";
        final String oldLocalCheckTime          = "2020-05-10T10:30:00.000Z";
        // @formatter:on

        metadataExistsForSpecWith(apiSpecJcrId, oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            metadataItem(apiSpecJcrId,  Instant.parse(oldLocalCheckTime)) // last change check time expected to NOT be updated
        );

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withSpecId(localSpecificationId)
            .withSpecId(apiSpecJcrId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpec = remoteSpecWith(remoteSpecificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should(never()).setJsonForPublishing(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 0, synced: 0, failed to sync: 0",
                apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName()))
        );
    }

    @Test
    public void sync_doesNotChangeOrPublishSpec_onFailureTo_determineEligibilityForUpdate() {

        // given
        // @formatter:off
        final String specificationId            = "248569";
        final String apiSpecJcrId               = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";

        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.001Z";
        final String oldLocalCheckTime          =           "2020-05-10T10:30:00.000Z";  // older than remoteSpecModificationTime
        // @formatter:on

        metadataExistsForSpecWith(apiSpecJcrId, oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(apiSpecJcrId, oldLocalCheckTime)  // last change check time expected to NOT be updated
        );

        final ApiSpecificationDocument localSpec = localSpec()
            .withSpecId(specificationId)
            .withJcrId(apiSpecJcrId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpec));

        final OpenApiSpecification remoteSpec = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willThrow(new RuntimeException("Failed to retrieve remote spec."));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpec).should(never()).save();

        then(localSpec).should(never()).setJsonForPublishing(any());
        then(localSpec).should(never()).saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 0, synced: 0, failed to sync: 1",
                apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            error("Failed to synchronise API Specification with id 248569 at /content/docs/248569")
                .withException("Failed to determine whether the specification is eligible for update.")
                .withCause("Failed to retrieve remote spec.")
        );
    }

    @Test
    public void sync_syncsEligibleSpecification_evenWhenPrecedingOneFailed() {

        // given
        // @formatter:off
        final String specificationIdA            = "248569";
        final String apiSpecJcrIdA               = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";

        final String specificationIdB            = "965842";
        final String apiSpecJcrIdB               = "d474c21e-3f5f-4db7-89e6-2f5e2ceee69a";

        final String remoteSpecModificationTimeA =           "2020-05-10T10:30:00.000Z";
        final Instant newCheckTimeA              = Instant.EPOCH;

        final String remoteSpecModificationTimeB =           "2020-05-10T10:30:00.000Z";
        final Instant newCheckTimeB              = nextNowIs("2020-05-10T10:30:00.002Z");
        // expecting only B to be read - we're breaking A before it can read the time

        final String remoteSpecificationJsonB    = "{ \"new-spec\": \"json-b\" }";
        // @formatter:on

        metadataExistsWithItems(emptyList());

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(apiSpecJcrIdA, newCheckTimeA), // last change check time expected to be updated; stops re-trying failed spec until it's fixed in Apigee
            newMetadataItem(apiSpecJcrIdB, newCheckTimeB)  // last change check time expected to be updated
        );

        final ApiSpecificationDocument localSpecNeverPublishedA = localSpec()
            .withSpecId(specificationIdA)
            .withJcrId(apiSpecJcrIdA)
            .mock();

        final ApiSpecificationDocument localSpecNeverPublishedB = localSpec()
            .withSpecId(specificationIdB)
            .withJcrId(apiSpecJcrIdB)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(asList(
            localSpecNeverPublishedA, localSpecNeverPublishedB
        ));

        final OpenApiSpecification remoteSpecA = remoteSpecWith(specificationIdA, remoteSpecModificationTimeA);
        final OpenApiSpecification remoteSpecB = remoteSpecWith(specificationIdB, remoteSpecModificationTimeB);

        given(apigeeService.apiSpecificationStatuses()).willReturn(asList(
            remoteSpecA, remoteSpecB
        ));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationIdA)).willThrow(new RuntimeException("Failed to retrieve remote spec."));
        given(apigeeService.apiSpecificationJsonForSpecId(specificationIdB)).willReturn(remoteSpecificationJsonB);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecNeverPublishedA).should(never()).save();

        then(localSpecNeverPublishedA).should(never()).setJsonForPublishing(any());
        then(localSpecNeverPublishedA).should(never()).saveAndPublish();

        then(localSpecNeverPublishedB).should(never()).save();

        then(localSpecNeverPublishedB).should().setJsonForPublishing(remoteSpecificationJsonB);
        then(localSpecNeverPublishedB).should().saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 2, in %s: 2, updated in %s and eligible to publish in CMS: 1, synced: 1, failed to sync: 1",
                    apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            info("Synchronised API Specification with id 965842 at /content/docs/965842"),
            error("Failed to synchronise API Specification with id 248569 at /content/docs/248569")
                .withException("Failed to determine whether the specification is eligible for update.")
                .withCause("Failed to retrieve remote spec.")
        );
    }

    @Test
    public void sync_doesNotMakeAnyRequestToRemoteSystem_ifThereAreNoLocalApiSpecDocuments() {

        // given
        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(emptyList());

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apigeeService).shouldHaveNoInteractions();
    }

    private void metadataForAllProcessedApiSpecsAreUpdatedAndSaved(final ApiSpecificationImportMetadata newApiSpecificationImportMetadata) {
        then(apiSpecMetadataRepo).should().save(apiSpecsImportMetadataArgumentCaptor.capture());
        assertThat("Metadata for all processed API Specifications are updated and saved.",
            apiSpecsImportMetadataArgumentCaptor.getValue(),
            is(newApiSpecificationImportMetadata)
        );
    }

    private ApiSpecificationImportMetadata metadataWith(final ApiSpecificationImportMetadata.Item... metadataItem) {
        return ApiSpecificationImportMetadata.metadataWith(asList(metadataItem));
    }

    private ApiSpecificationImportMetadata.Item newMetadataItem(final String apiSpecJcrId, final Instant newCheckTime) {

        final ApiSpecificationImportMetadata.Item expectedItem = metadataItem(apiSpecJcrId, newCheckTime);
        expectedItem.setSpecExists();

        return expectedItem;
    }

    private ApiSpecificationImportMetadata.Item newMetadataItem(final String apiSpecJcrId, final String oldLocalCheckTime) {
        return newMetadataItem(apiSpecJcrId, Instant.parse(oldLocalCheckTime));
    }

    private void metadataExistsForSpecWith(final String apiSpecJcrId, final String lastChangeCheckInstant) {
        metadataExistsWithItems(singletonList(
            metadataItem(apiSpecJcrId, Instant.parse(lastChangeCheckInstant)))
        );
    }

    private void metadataExistsWithItems(final List<ApiSpecificationImportMetadata.Item> apiSpecsMetadata) {
        final ApiSpecificationImportMetadata initialApiSpecificationImportMetadata = ApiSpecificationImportMetadata.metadataWith(apiSpecsMetadata);

        given(apiSpecMetadataRepo.findApiSpecificationImportMetadata()).willReturn(initialApiSpecificationImportMetadata);
    }

    private OpenApiSpecification remoteSpecWith(final String specificationId, final String lastModifiedInstant) {

        final OpenApiSpecification remoteApiSpecification = new OpenApiSpecification(specificationId, lastModifiedInstant);
        remoteApiSpecification.setService(apigeeService);

        return remoteApiSpecification;
    }

    static class ApiSpecDocMockBuilder {


        private final ApiSpecificationDocument spec = Mockito.mock(ApiSpecificationDocument.class);

        public static ApiSpecDocMockBuilder localSpec() {
            return new ApiSpecDocMockBuilder();
        }

        public ApiSpecDocMockBuilder withSpecId(final String specificationId) {
            given(spec.specificationId()).willReturn(specificationId);
            given(spec.path()).willReturn("/content/docs/" + specificationId);

            return this;
        }

        private ApiSpecDocMockBuilder withJcrId(final String specJcrId) {
            given(spec.jcrId()).willReturn(specJcrId);

            return this;
        }

        public ApiSpecDocMockBuilder withJson(final String json) {
            given(spec.json()).willReturn(Optional.of(json));

            return this;
        }

        public ApiSpecificationDocument mock() {
            return spec;
        }
    }

}
