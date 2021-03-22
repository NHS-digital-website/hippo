package uk.nhs.digital.apispecs;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.TestLogger.LogAssertionBuilder.*;
import static uk.nhs.digital.apispecs.ApiSpecificationPublicationServiceTest.ApiSpecDocMockBuilder.localSpec;
import static uk.nhs.digital.test.util.TimeProviderTestUtils.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.TestLogger;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;

import java.time.Instant;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ApiSpecificationPublicationServiceTest {

    @Mock
    private OpenApiSpecificationRepository apigeeService;

    @Mock
    private ApiSpecificationDocumentJcrRepository apiSpecDocumentRepo;

    @Mock
    private OpenApiSpecificationJsonToHtmlConverter apiSpecHtmlProvider;

    private TestLogger logger;

    private ApiSpecificationPublicationService apiSpecificationPublicationService;

    @Before
    public void setUp() {
        initMocks(this);

        apiSpecificationPublicationService = new ApiSpecificationPublicationService(
            apigeeService,
            apiSpecDocumentRepo,
            apiSpecHtmlProvider
        );

        logger = TestLogger.initialiseFor(ApiSpecificationPublicationService.class);
    }

    @After
    public void tearDown() {
        resetTimeProvider();

        logger.reset();
    }

    @Test
    public void sync_publishesSpecifications_existingInCmsButNeverPublished() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.000Z";
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z");

        final String remoteSpecificationJson    = "{ \"new-spec\": \"json\" }";
        final String newSpecificationHtml       = "<html><body> new spec html </body></html>";
        // @formatter:on

        final ApiSpecificationDocument localSpecNeverPublished = localSpec()
            .withId(specificationId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecNeverPublished));

        final OpenApiSpecification remoteSpec = remoteSpec(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willReturn(newSpecificationHtml);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecNeverPublished).should().setLastChangeCheckInstant(newCheckTime);
        then(localSpecNeverPublished).should().save();

        then(localSpecNeverPublished).should().setJson(remoteSpecificationJson);
        then(localSpecNeverPublished).should().setHtml(newSpecificationHtml);
        then(localSpecNeverPublished).should().saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 1, synced: 1, failed to sync: 0"),
            info("Synchronised API Specification with id 248569 at /content/docs/248569")
        );
    }

    @Test
    public void sync_publishesSpecifications_thatChangedInApigeeAfterItWasPublishedInCms() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.001Z";
        final String oldLocalCheckTime          =           "2020-05-10T10:30:00.000Z";
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.002Z");

        final String remoteSpecificationJson    = "{ \"new-spec\": \"json\" }";
        final String oldLocalSpecificationJson  = "{ \"old-spec\": \"json\" }";
        final String newSpecificationHtml       = "<html><body> new spec html </body></html>";
        // @formatter:on

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withId(specificationId)
            .withJson(oldLocalSpecificationJson)
            .withHtml(newSpecificationHtml)
            .withLastCheckedInstant(oldLocalCheckTime)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpecUpdatedSincePublishedLocally = remoteSpec(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUpdatedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willReturn(newSpecificationHtml);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should().setLastChangeCheckInstant(newCheckTime);
        then(localSpecPublishedBefore).should().save();

        then(localSpecPublishedBefore).should().setJson(remoteSpecificationJson);
        then(localSpecPublishedBefore).should().setHtml(newSpecificationHtml);
        then(localSpecPublishedBefore).should().saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 1, synced: 1, failed to sync: 0"),
            info("Synchronised API Specification with id 248569 at /content/docs/248569")
        );
    }

    @Test
    public void sync_doesNotChangeNorPublishSpecifications_whereApigeeReportsChangeAfterLastCheckInCms_butApigeeContentHasNotActuallyChanged() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String oldLocalCheckTime          =           "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.001Z";
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z");

        // Specs differing only in version; this field is ignored when comparing specs' JSON for changes:
        final String oldLocalSpecificationJson  = "{ \"info\": { \"version\": \"v1.2.525-beta\" }, \"json\": \"payload\" }";
        final String remoteSpecificationJson    = "{ \"info\": { \"version\": \"v1.2.526-beta\" }, \"json\": \"payload\" }";
        // @formatter:on

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withId(specificationId)
            .withLastCheckedInstant(oldLocalCheckTime)
            .withJson(oldLocalSpecificationJson)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpecWithContentUnchangedSincePublishedLocally = remoteSpec(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecWithContentUnchangedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should().setLastChangeCheckInstant(newCheckTime);
        then(localSpecPublishedBefore).should().save();

        then(localSpecPublishedBefore).should(never()).setJson(any());
        then(localSpecPublishedBefore).should(never()).setHtml(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 0, synced: 0, failed to sync: 0"),
            debug("Modification timestamps indicate a change but the content has not changed; skipping spec with id 248569 at /content/docs/248569.")
        );
    }

    @Test
    public void sync_doesNotChangeNorPublishSpecifications_ifApigeeReportsNoChangeAfterLastCheckInCms() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String oldLocalCheckTime          =           "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.000Z";
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.002Z");
        // @formatter:on

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withId(specificationId)
            .withLastCheckedInstant(oldLocalCheckTime)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpecUnchangedSincePublishedLocally = remoteSpec(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUnchangedSincePublishedLocally));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should().setLastChangeCheckInstant(newCheckTime);
        then(localSpecPublishedBefore).should().save();

        then(localSpecPublishedBefore).should(never()).setJson(any());
        then(localSpecPublishedBefore).should(never()).setHtml(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 0, synced: 0, failed to sync: 0")
        );
    }

    @Test
    public void sync_doesNotChangeNorPublishSpecifications_whichDoNotHaveMatchingCounterpartsInRemoteSystem() {

        // given
        // @formatter:off
        final String localSpecificationId       = "965842";
        final String remoteSpecificationId      = "248569";

        final String localCheckTime             = "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime = "2020-05-10T10:30:00.001Z";
        // @formatter:on

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withId(localSpecificationId)
            .withLastCheckedInstant(localCheckTime)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        final OpenApiSpecification remoteSpec = remoteSpec(remoteSpecificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should(never()).setJson(any());
        then(localSpecPublishedBefore).should(never()).setHtml(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 0, synced: 0, failed to sync: 0")
        );
    }

    @Test
    public void sync_handlesMultipleSpecifications() {

        // given
        // @formatter:off
        final String specificationIdA            = "248569";
        final String specificationIdB            = "965842";

        final String remoteSpecModificationTimeA =               "2020-05-10T10:30:00.005Z";
        final Instant newCheckTimeA              = Instant.parse("2020-05-10T10:30:00.007Z");

        final String remoteSpecificationJsonA    = "{ \"new-spec\": \"json-a\" }";
        final String newSpecificationHtmlA       = "<html><body> new spec html A</body></html>";

        final String remoteSpecModificationTimeB =               "2020-05-10T10:30:00.006Z";
        final Instant newCheckTimeB              = Instant.parse("2020-05-10T10:30:00.008Z");

        final String remoteSpecificationJsonB    = "{ \"new-spec\": \"json-b\" }";
        final String newSpecificationHtmlB       = "<html><body> new spec html B</body></html>";
        // @formatter:on

        nextNowsAre(newCheckTimeA, newCheckTimeB);

        final ApiSpecificationDocument localSpecNeverPublishedA = localSpec()
            .withId(specificationIdA)
            .mock();

        final ApiSpecificationDocument localSpecNeverPublishedB = localSpec()
            .withId(specificationIdB)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(asList(
            localSpecNeverPublishedA, localSpecNeverPublishedB
        ));

        final OpenApiSpecification remoteSpecA = remoteSpec(specificationIdA, remoteSpecModificationTimeA);
        final OpenApiSpecification remoteSpecB = remoteSpec(specificationIdB, remoteSpecModificationTimeB);

        given(apigeeService.apiSpecificationStatuses()).willReturn(asList(
            remoteSpecA, remoteSpecB
        ));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationIdA)).willReturn(remoteSpecificationJsonA);
        given(apigeeService.apiSpecificationJsonForSpecId(specificationIdB)).willReturn(remoteSpecificationJsonB);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJsonA)).willReturn(newSpecificationHtmlA);
        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJsonB)).willReturn(newSpecificationHtmlB);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecNeverPublishedA).should().setLastChangeCheckInstant(newCheckTimeA);
        then(localSpecNeverPublishedA).should().save();

        then(localSpecNeverPublishedA).should().setJson(remoteSpecificationJsonA);
        then(localSpecNeverPublishedA).should().setHtml(newSpecificationHtmlA);
        then(localSpecNeverPublishedA).should().saveAndPublish();

        then(localSpecNeverPublishedB).should().setLastChangeCheckInstant(newCheckTimeB);
        then(localSpecNeverPublishedB).should().save();

        then(localSpecNeverPublishedB).should().setJson(remoteSpecificationJsonB);
        then(localSpecNeverPublishedB).should().setHtml(newSpecificationHtmlB);
        then(localSpecNeverPublishedB).should().saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 2, in Apigee: 2, updated in Apigee and eligible to publish in CMS: 2, synced: 2, failed to sync: 0"),
            info("Synchronised API Specification with id 248569 at /content/docs/248569"),
            info("Synchronised API Specification with id 965842 at /content/docs/965842")
        );
    }

    @Test
    public void sync_doesNotChangeOrPublishSpec_onFailureTo_determineEligibilityForUpdate() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String remoteSpecModificationTime = "2020-05-10T10:30:00.000Z";
        // @formatter:on

        final ApiSpecificationDocument localSpec = localSpec()
            .withId(specificationId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpec));

        final OpenApiSpecification remoteSpec = remoteSpec(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willThrow(new RuntimeException("Failed to retrieve remote spec."));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpec).should(never()).setLastChangeCheckInstant(any());
        then(localSpec).should(never()).save();

        then(localSpec).should(never()).setJson(any());
        then(localSpec).should(never()).setHtml(any());
        then(localSpec).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 0, synced: 0, failed to sync: 1"),
            error("Failed to synchronise API Specification with id 248569 at /content/docs/248569")
                .withException("Failed to determine whether the specification is eligible for update.")
                .withCause("Failed to retrieve remote spec.")
        );
    }

    @Test
    public void sync_doesNotChangeOrPublishSpec_onFailureTo_renderJsonToHtml() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String remoteSpecModificationTime = "2020-05-10T10:30:00.000Z";

        final String remoteSpecificationJson    = "{ \"new-spec\": \"json\" }";
        // @formatter:on

        final ApiSpecificationDocument localSpec = localSpec()
            .withId(specificationId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpec));

        final OpenApiSpecification remoteSpec = remoteSpec(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willThrow(new RuntimeException("Invalid spec."));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpec).should(never()).setLastChangeCheckInstant(any());
        then(localSpec).should(never()).save();

        then(localSpec).should(never()).setJson(any());
        then(localSpec).should(never()).setHtml(any());
        then(localSpec).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 1, synced: 0, failed to sync: 1"),
            error("Failed to synchronise API Specification with id 248569 at /content/docs/248569")
                .withException("Failed to render OAS JSON into HTML.")
                    .withCause("Invalid spec.")
        );
    }

    @Test
    public void sync_doesNotChangeOrPublishSpec_onFailureTo_saveLastCheckTime() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String remoteSpecModificationTime =           "2020-05-10T10:30:00.000Z";
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z");

        final String remoteSpecificationJson    = "{ \"new-spec\": \"json\" }";
        final String newSpecificationHtml       = "<html><body> new spec html </body></html>";
        // @formatter:on

        final ApiSpecificationDocument localSpec = localSpec()
            .withId(specificationId)
            .withBrokenSave()
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpec));

        final OpenApiSpecification remoteSpec = remoteSpec(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willReturn(newSpecificationHtml);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpec).should().setLastChangeCheckInstant(newCheckTime);
        then(localSpec).should().save();

        then(localSpec).should(never()).setJson(any());
        then(localSpec).should(never()).setHtml(any());
        then(localSpec).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 1, synced: 0, failed to sync: 1"),
            error("Failed to synchronise API Specification with id 248569 at /content/docs/248569")
                .withException("Failed to record time of last check on specification with id 248569 at /content/docs/248569.")
                .withCause("Failed to save.")
        );
    }

    @Test
    public void sync_syncsEligibleSpecification_evenWhenPrecedingOneFailed() {

        // given
        // @formatter:off
        final String specificationIdA            = "248569";
        final String specificationIdB            = "965842";

        final String remoteSpecModificationTimeA =               "2020-05-10T10:30:00.000Z";
        final String remoteSpecificationJsonA    = "{ \"new-spec\": \"json-a\" }";

        final String remoteSpecModificationTimeB =               "2020-05-10T10:30:00.000Z";
        final Instant newCheckTimeB              = Instant.parse("2020-05-10T10:30:00.001Z");

        final String remoteSpecificationJsonB    = "{ \"new-spec\": \"json-b\" }";
        final String newSpecificationHtmlB       = "<html><body> new spec html B</body></html>";
        // @formatter:on

        nextNowsAre(newCheckTimeB); // expecting only B to be read - we're breaking A before it can read the time

        final ApiSpecificationDocument localSpecNeverPublishedA = localSpec()
            .withId(specificationIdA)
            .mock();

        final ApiSpecificationDocument localSpecNeverPublishedB = localSpec()
            .withId(specificationIdB)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(asList(
            localSpecNeverPublishedA, localSpecNeverPublishedB
        ));

        final OpenApiSpecification remoteSpecA = remoteSpec(specificationIdA, remoteSpecModificationTimeA);
        final OpenApiSpecification remoteSpecB = remoteSpec(specificationIdB, remoteSpecModificationTimeB);

        given(apigeeService.apiSpecificationStatuses()).willReturn(asList(
            remoteSpecA, remoteSpecB
        ));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationIdA)).willReturn(remoteSpecificationJsonA);
        given(apigeeService.apiSpecificationJsonForSpecId(specificationIdB)).willReturn(remoteSpecificationJsonB);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJsonA)).willThrow(new RuntimeException("Invalid spec."));
        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJsonB)).willReturn(newSpecificationHtmlB);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecNeverPublishedA).should(never()).setLastChangeCheckInstant(any());
        then(localSpecNeverPublishedA).should(never()).save();

        then(localSpecNeverPublishedA).should(never()).setJson(any());
        then(localSpecNeverPublishedA).should(never()).setHtml(any());
        then(localSpecNeverPublishedA).should(never()).saveAndPublish();

        then(localSpecNeverPublishedB).should().setLastChangeCheckInstant(newCheckTimeB);
        then(localSpecNeverPublishedB).should().save();

        then(localSpecNeverPublishedB).should().setJson(remoteSpecificationJsonB);
        then(localSpecNeverPublishedB).should().setHtml(newSpecificationHtmlB);
        then(localSpecNeverPublishedB).should().saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 2, in Apigee: 2, updated in Apigee and eligible to publish in CMS: 2, synced: 1, failed to sync: 1"),
            info("Synchronised API Specification with id 965842 at /content/docs/965842"),
            error("Failed to synchronise API Specification with id 248569 at /content/docs/248569")
                .withException("Failed to render OAS JSON into HTML.")
                .withCause("Invalid spec.")
        );
    }

    @Test
    public void sync_doesNotMakeAnyRequestToRemoteSystem_ifThereAreNoLocalApiSpecDocuments() {

        // given
        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(emptyList());

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apigeeService).shouldHaveZeroInteractions();
    }

    @Test
    public void rerender_updatesAndPublishesSpecificationWhenChanged() {

        // @formatter:off
        final String specificationId            = "248569";

        final String localSpecificationJson     = "{ \"old-spec\": \"json\" }";
        final String newSpecificationHtml       = "<html><body> new spec html </body></html>";
        final String oldSpecificationHtml       = "<html><body> old spec html </body></html>";
        // @formatter:on

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withId(specificationId)
            .withJson(localSpecificationJson)
            .withHtml(oldSpecificationHtml)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        given(apiSpecHtmlProvider.htmlFrom(localSpecificationJson)).willReturn(newSpecificationHtml);

        // when
        apiSpecificationPublicationService.rerenderSpecifications();

        // then
        then(localSpecPublishedBefore).should().json();
        then(localSpecPublishedBefore).should().html();
        then(localSpecPublishedBefore).should().setHtml(newSpecificationHtml);
        then(localSpecPublishedBefore).should().saveAndPublish();
        then(localSpecPublishedBefore).should().getId(); // this call is made for the sake of logging
        then(localSpecPublishedBefore).shouldHaveNoMoreInteractions();
    }

    @Test
    public void rerender_skipsSpecification_whenUnchanged() {
        // given
        // @formatter:off
        final String specificationId        = "248569";

        final String localSpecificationJson = "{ \"json\": \"payload\" }";
        final String localSpecificationHtml = "<html><body>existing spec content</body></html>";
        // @formatter:on

        final ApiSpecificationDocument localSpecPublishedBefore = localSpec()
            .withId(specificationId)
            .withJson(localSpecificationJson)
            .withHtml(localSpecificationHtml)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecPublishedBefore));

        given(apiSpecHtmlProvider.htmlFrom(localSpecificationJson)).willReturn(localSpecificationHtml);

        // when
        apiSpecificationPublicationService.rerenderSpecifications();

        // then
        then(localSpecPublishedBefore).should().json();
        then(localSpecPublishedBefore).should().html();
        then(localSpecPublishedBefore).should().getId(); // this call is made for the sake of logging
        then(localSpecPublishedBefore).shouldHaveNoMoreInteractions();
    }

    @Test
    public void rerender_skipsSpecification_whenNeverPublished() {
        // given
        final String specificationId = "248569";

        final ApiSpecificationDocument localSpecNeverPublished = localSpec()
            .withId(specificationId)
            .mock();

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(singletonList(localSpecNeverPublished));

        // when
        apiSpecificationPublicationService.rerenderSpecifications();

        // then
        then(localSpecNeverPublished).should().json();
        then(localSpecNeverPublished).should().html();
        then(localSpecNeverPublished).should().getId(); // this call is made for the sake of logging
        then(localSpecNeverPublished).shouldHaveNoMoreInteractions();
    }

    private OpenApiSpecification remoteSpec(final String specificationId, final String lastModifiedInstant) {

        final OpenApiSpecification remoteApiSpecification = new OpenApiSpecification(specificationId, lastModifiedInstant);
        remoteApiSpecification.setApigeeService(apigeeService);

        return remoteApiSpecification;
    }


    static class ApiSpecDocMockBuilder {

        private final ApiSpecificationDocument spec = Mockito.mock(ApiSpecificationDocument.class);

        public static ApiSpecDocMockBuilder localSpec() {
            return new ApiSpecDocMockBuilder();
        }

        public ApiSpecDocMockBuilder withId(final String specificationId) {
            given(spec.getId()).willReturn(specificationId);
            given(spec.path()).willReturn("/content/docs/" + specificationId);

            return this;
        }

        public ApiSpecDocMockBuilder withJson(final String json) {
            given(spec.json()).willReturn(Optional.of(json));

            return this;
        }

        public ApiSpecDocMockBuilder withHtml(final String html) {
            given(spec.html()).willReturn(Optional.of(html));

            return this;
        }

        public ApiSpecDocMockBuilder withLastCheckedInstant(final String instant) {
            given(spec.lastChangeCheckInstant()).willReturn(Optional.of(instant).map(Instant::parse));

            return this;
        }

        public ApiSpecDocMockBuilder withBrokenSave() {
            doThrow(new RuntimeException("Failed to save.")).when(spec).save();

            return this;
        }

        public ApiSpecificationDocument mock() {
            return spec;
        }
    }
}
