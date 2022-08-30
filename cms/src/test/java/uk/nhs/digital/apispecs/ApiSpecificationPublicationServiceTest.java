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
import uk.nhs.digital.apispecs.model.SpecificationSyncData;
import uk.nhs.digital.test.TestLoggerRule;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ApiSpecificationPublicationServiceTest {
    private static final String SPEC_ID_A = "248569";
    private static final String SPEC_HANDLE_NODE_ID_A = "30f1cc21-77fb-49a2-bf93-5c6d9191a636";
    private static final String DRAFT_SPEC_PATH_A = "/content/documents/corporate-website/api-specifications-location/api-spec-a/api-spec-a";
    private static final String SPEC_HANDLE_NODE_PATH_A = "/content/documents/corporate-website/api-specifications-location/api-spec-a";
    private static final String REMOTE_SPEC_NEW_JSON = "{ \"new-spec\": \"json\" }";
    private static final String LOCAL_SPEC_OLD_JSON = "{ \"old-spec\": \"json\" }";
    private static final String SPEC_ID_B = "965842";
    private static final String SPEC_HANDLE_NODE_ID_B = "d474c21e-3f5f-4db7-89e6-2f5e2ceee69a";
    private static final String DRAFT_SPEC_PATH_B = "/content/documents/corporate-website/api-specifications-location/api-spec-b/api-spec-b";
    private static final String SPEC_HANDLE_NODE_PATH_B = "/content/documents/corporate-website/api-specifications-location/api-spec-b";

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
        final String remoteSpecModificationTime = "2020-05-10T10:30:00.000Z";
        final String oldLocalCheckTime          = Instant.EPOCH.toString(); // older than remoteSpecModificationTime, returned when no metadata entry present
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z"); // younger than either of the above
        // @formatter:on

        metadataExistsForSpecWith(oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(SPEC_HANDLE_NODE_ID_A, newCheckTime) // last change check time expected to be updated
        );

        final SpecificationSyncData initialLocalSpecSyncData = SpecificationSyncData.with(
            SPEC_ID_A,
            SPEC_HANDLE_NODE_ID_A,
            DRAFT_SPEC_PATH_A
        );
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(singletonList(initialLocalSpecSyncData));

        final ApiSpecificationDocument localSpecNeverPublished = localSpec()
            .withPath(SPEC_HANDLE_NODE_PATH_A)
            .mock();
        given(apiSpecDocumentRepo.findApiSpecification(SPEC_HANDLE_NODE_ID_A)).willReturn(localSpecNeverPublished);

        final OpenApiSpecification remoteSpec = remoteSpecWith(SPEC_ID_A, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(SPEC_ID_A)).willReturn(REMOTE_SPEC_NEW_JSON);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apiSpecDocumentRepo).should().findApiSpecification(SPEC_HANDLE_NODE_ID_A);
        then(localSpecNeverPublished).should(never()).save();

        then(localSpecNeverPublished).should().setJsonForPublishing(REMOTE_SPEC_NEW_JSON);
        then(localSpecNeverPublished).should().saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 1, synced: 1, failed to sync: 0",
                    apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            info(format("Synchronised API Specification with id %s at %s", SPEC_ID_A, SPEC_HANDLE_NODE_PATH_A))
        );
    }

    @Test
    public void sync_publishesSpecifications_thatChangedInApigeeAfterItWasPublishedInCms() {

        // given
        // @formatter:off
        final String remoteSpecModificationTime = "2020-05-10T10:30:00.001Z";
        final String oldLocalCheckTime          = "2020-05-10T10:30:00.000Z"; // older than remoteSpecModificationTime
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.002Z"); // younger than either of the above
        // @formatter:on

        metadataExistsForSpecWith(oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(SPEC_HANDLE_NODE_ID_A, newCheckTime) // last change check time expected to be updated
        );

        final SpecificationSyncData initialLocalSpecSyncData = SpecificationSyncData.with(
            SPEC_ID_A,
            SPEC_HANDLE_NODE_ID_A,
            DRAFT_SPEC_PATH_A
        );
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(singletonList(initialLocalSpecSyncData));

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withPath(SPEC_HANDLE_NODE_PATH_A)
            .withJson(LOCAL_SPEC_OLD_JSON)
            .mock();
        given(apiSpecDocumentRepo.findApiSpecification(SPEC_HANDLE_NODE_ID_A)).willReturn(localSpecPublishedBefore);

        final OpenApiSpecification remoteSpecUpdatedSincePublishedLocally = remoteSpecWith(SPEC_ID_A, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUpdatedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(SPEC_ID_A)).willReturn(REMOTE_SPEC_NEW_JSON);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apiSpecDocumentRepo).should().findApiSpecification(SPEC_HANDLE_NODE_ID_A);
        then(localSpecPublishedBefore).should(never()).save();

        then(localSpecPublishedBefore).should().setJsonForPublishing(REMOTE_SPEC_NEW_JSON);
        then(localSpecPublishedBefore).should().saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 1, synced: 1, failed to sync: 0",
                apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            info(format("Synchronised API Specification with id %s at %s", SPEC_ID_A, SPEC_HANDLE_NODE_PATH_A))
        );
    }

    @Test
    public void sync_doesNotChangeNorPublishSpecifications_whereApigeeReportsChangeAfterLastCheckInCms_butApigeeContentHasNotActuallyChanged() {

        // given
        // @formatter:off
        final String oldLocalCheckTime          = "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime = "2020-05-10T10:30:00.001Z"; // younger than remoteSpecModificationTime
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z"); // younger than either of the above

        // Specs differing only in version; this field is ignored when comparing specs' JSON for changes:
        final String oldLocalSpecificationJson  = "{ \"info\": { \"version\": \"v1.2.525-beta\" }, \"json\": \"payload\" }";
        final String remoteSpecificationJson    = "{ \"info\": { \"version\": \"v1.2.526-beta\" }, \"json\": \"payload\" }";
        // @formatter:on

        metadataExistsForSpecWith(oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(SPEC_HANDLE_NODE_ID_A, newCheckTime) // last change check time expected to be updated
        );

        final SpecificationSyncData initialLocalSpecSyncData = SpecificationSyncData.with(
            SPEC_ID_A,
            SPEC_HANDLE_NODE_ID_A,
            DRAFT_SPEC_PATH_A
        );
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(singletonList(initialLocalSpecSyncData));

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withPath(SPEC_HANDLE_NODE_PATH_A)
            .withJson(oldLocalSpecificationJson)
            .mock();
        given(apiSpecDocumentRepo.findApiSpecification(SPEC_HANDLE_NODE_ID_A)).willReturn(localSpecPublishedBefore);

        final OpenApiSpecification remoteSpecWithContentUnchangedSincePublishedLocally = remoteSpecWith(SPEC_ID_A, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecWithContentUnchangedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(SPEC_ID_A)).willReturn(remoteSpecificationJson);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apiSpecDocumentRepo).should().findApiSpecification(SPEC_HANDLE_NODE_ID_A);
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
        final String remoteSpecModificationTime = "2020-05-10T10:30:00.000Z";
        final String oldLocalCheckTime          = "2020-05-10T10:30:00.000Z";  // not older than remoteSpecModificationTime
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.002Z"); // younger than either of the above
        // @formatter:on

        metadataExistsForSpecWith(oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(SPEC_HANDLE_NODE_ID_A, newCheckTime) // last change check time expected to be updated
        );

        final SpecificationSyncData initialLocalSpecSyncData = SpecificationSyncData.with(
            SPEC_ID_A,
            SPEC_HANDLE_NODE_ID_A,
            DRAFT_SPEC_PATH_A
        );
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(singletonList(initialLocalSpecSyncData));

        final ApiSpecificationDocument localSpec = localSpec().mock();

        final OpenApiSpecification remoteSpecUnchangedSincePublishedLocally = remoteSpecWith(SPEC_ID_A, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUnchangedSincePublishedLocally));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apiSpecDocumentRepo).should(never()).findApiSpecification(SPEC_HANDLE_NODE_ID_A);
        then(localSpec).should(never()).save();

        then(localSpec).should(never()).setJsonForPublishing(any());
        then(localSpec).should(never()).saveAndPublish();

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
        final String remoteSpecModificationTime = "2020-05-10T10:30:00.001Z";
        final String oldLocalCheckTime          = "2020-05-10T10:30:00.000Z";
        // @formatter:on

        metadataExistsForSpecWith(oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            metadataItem(SPEC_HANDLE_NODE_ID_A,  Instant.parse(oldLocalCheckTime)) // last change check time expected to NOT be updated
        );

        final SpecificationSyncData initialLocalSpecSyncData = SpecificationSyncData.with(
            SPEC_ID_A,
            SPEC_HANDLE_NODE_ID_A,
            DRAFT_SPEC_PATH_A
        );
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(singletonList(initialLocalSpecSyncData));

        final ApiSpecificationDocument localSpec = localSpec().mock();

        final OpenApiSpecification remoteSpec = remoteSpecWith(SPEC_ID_B, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apiSpecDocumentRepo).should(never()).findApiSpecification(SPEC_HANDLE_NODE_ID_A);
        then(localSpec).should(never()).setJsonForPublishing(any());
        then(localSpec).should(never()).saveAndPublish();

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
        final String remoteSpecModificationTime = "2020-05-10T10:30:00.001Z";
        final String oldLocalCheckTime          = "2020-05-10T10:30:00.000Z";  // older than remoteSpecModificationTime
        // @formatter:on

        metadataExistsForSpecWith(oldLocalCheckTime);

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem()  // last change check time expected to NOT be updated
        );

        final SpecificationSyncData initialLocalSpecSyncData = SpecificationSyncData.with(
            SPEC_ID_A,
            SPEC_HANDLE_NODE_ID_A,
            DRAFT_SPEC_PATH_A
        );
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(singletonList(initialLocalSpecSyncData));

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withPath(SPEC_HANDLE_NODE_PATH_A)
            .withJson(LOCAL_SPEC_OLD_JSON)
            .mock();
        given(apiSpecDocumentRepo.findApiSpecification(SPEC_HANDLE_NODE_ID_A)).willReturn(localSpecPublishedBefore);

        final OpenApiSpecification remoteSpec = remoteSpecWith(SPEC_ID_A, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(SPEC_ID_A)).willThrow(new RuntimeException("Failed to retrieve remote spec."));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apiSpecDocumentRepo).should().findApiSpecification(SPEC_HANDLE_NODE_ID_A);
        then(localSpecPublishedBefore).should(never()).save();

        then(localSpecPublishedBefore).should(never()).setJsonForPublishing(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 1, in %s: 1, updated in %s and eligible to publish in CMS: 0, synced: 0, failed to sync: 1",
                apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            error(format("Failed to synchronise API Specification with id %s at %s", SPEC_ID_A, SPEC_HANDLE_NODE_PATH_A))
                .withException("Failed to determine whether the specification is eligible for update.")
                .withCause("Failed to retrieve remote spec.")
        );
    }

    @Test
    public void sync_syncsEligibleSpecification_evenWhenPrecedingOneFailed() {

        // given
        // @formatter:off
        final String remoteSpecModificationTimeA = "2020-05-10T10:30:00.000Z";
        final Instant newCheckTimeA              = Instant.EPOCH;

        final String remoteSpecModificationTimeB = "2020-05-10T10:30:00.000Z";
        final Instant newCheckTimeB              = nextNowIs("2020-05-10T10:30:00.002Z");
        // expecting only B to be read - we're breaking A before it can read the time
        // @formatter:on

        metadataExistsWithItems(emptyList());

        final ApiSpecificationImportMetadata newApiSpecificationImportMetadata = metadataWith(
            newMetadataItem(SPEC_HANDLE_NODE_ID_A, newCheckTimeA), // last change check time expected to be updated; stops re-trying failed spec until it's fixed in Apigee
            newMetadataItem(SPEC_HANDLE_NODE_ID_B, newCheckTimeB)  // last change check time expected to be updated
        );

        final SpecificationSyncData initialLocalSpecSyncDataA = SpecificationSyncData.with(
            SPEC_ID_A,
            SPEC_HANDLE_NODE_ID_A,
            DRAFT_SPEC_PATH_A
        );

        final SpecificationSyncData initialLocalSpecSyncDataB = SpecificationSyncData.with(
            SPEC_ID_B,
            SPEC_HANDLE_NODE_ID_B,
            DRAFT_SPEC_PATH_B
        );
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(asList(initialLocalSpecSyncDataA, initialLocalSpecSyncDataB));

        final ApiSpecificationDocument localSpecNeverPublishedA = localSpec()
            .withPath(SPEC_HANDLE_NODE_PATH_A)
            .withJson(LOCAL_SPEC_OLD_JSON)
            .mock();

        final ApiSpecificationDocument localSpecNeverPublishedB = localSpec()
            .withPath(SPEC_HANDLE_NODE_PATH_B)
            .withJson(LOCAL_SPEC_OLD_JSON)
            .mock();

        given(apiSpecDocumentRepo.findApiSpecification(SPEC_HANDLE_NODE_ID_A)).willReturn(localSpecNeverPublishedA);
        given(apiSpecDocumentRepo.findApiSpecification(SPEC_HANDLE_NODE_ID_B)).willReturn(localSpecNeverPublishedB);

        final OpenApiSpecification remoteSpecA = remoteSpecWith(SPEC_ID_A, remoteSpecModificationTimeA);
        final OpenApiSpecification remoteSpecB = remoteSpecWith(SPEC_ID_B, remoteSpecModificationTimeB);

        given(apigeeService.apiSpecificationStatuses()).willReturn(asList(
            remoteSpecA, remoteSpecB
        ));

        given(apigeeService.apiSpecificationJsonForSpecId(SPEC_ID_A)).willThrow(new RuntimeException("Failed to retrieve remote spec."));
        given(apigeeService.apiSpecificationJsonForSpecId(SPEC_ID_B)).willReturn(REMOTE_SPEC_NEW_JSON);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apiSpecDocumentRepo).should().findApiSpecification(SPEC_HANDLE_NODE_ID_A);
        then(localSpecNeverPublishedA).should(never()).save();

        then(localSpecNeverPublishedA).should(never()).setJsonForPublishing(any());
        then(localSpecNeverPublishedA).should(never()).saveAndPublish();

        then(apiSpecDocumentRepo).should().findApiSpecification(SPEC_HANDLE_NODE_ID_B);
        then(localSpecNeverPublishedB).should(never()).save();

        then(localSpecNeverPublishedB).should().setJsonForPublishing(REMOTE_SPEC_NEW_JSON);
        then(localSpecNeverPublishedB).should().saveAndPublish();

        metadataForAllProcessedApiSpecsAreUpdatedAndSaved(newApiSpecificationImportMetadata);

        logger.shouldReceive(
            info(format("API Specifications found: in CMS: 2, in %s: 2, updated in %s and eligible to publish in CMS: 1, synced: 1, failed to sync: 1",
                    apigeeService.getClass().getSimpleName(), apigeeService.getClass().getSimpleName())),
            info(format("Synchronised API Specification with id %s at %s", SPEC_ID_B, SPEC_HANDLE_NODE_PATH_B)),
            error(format("Failed to synchronise API Specification with id %s at %s", SPEC_ID_A, SPEC_HANDLE_NODE_PATH_A))
                .withException("Failed to determine whether the specification is eligible for update.")
                .withCause("Failed to retrieve remote spec.")
        );
    }

    @Test
    public void sync_doesNotMakeAnyRequestToRemoteSystem_ifThereAreNoLocalApiSpecDocuments() {

        // given
        given(apiSpecDocumentRepo.findInitialSyncDataForAllApiSpecifications()).willReturn(emptyList());

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

    private ApiSpecificationImportMetadata.Item newMetadataItem() {
        return newMetadataItem(SPEC_HANDLE_NODE_ID_A, Instant.parse("2020-05-10T10:30:00.000Z"));
    }

    private void metadataExistsForSpecWith(final String lastChangeCheckInstant) {
        metadataExistsWithItems(singletonList(
            metadataItem(SPEC_HANDLE_NODE_ID_A, Instant.parse(lastChangeCheckInstant)))
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

        public ApiSpecDocMockBuilder withJson(final String json) {
            given(spec.json()).willReturn(Optional.of(json));

            return this;
        }

        public ApiSpecDocMockBuilder withPath(final String path) {
            given(spec.path()).willReturn(path);

            return this;
        }

        public ApiSpecificationDocument mock() {
            return spec;
        }
    }
}
