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
import static uk.nhs.digital.apispecs.ApiSpecificationPublicationServiceTest.ApiSpecDocMockBuilder.localSpec;
import static uk.nhs.digital.test.TestLogger.LogAssertor.*;
import static uk.nhs.digital.test.util.TimeProviderTestUtils.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;
import uk.nhs.digital.test.TestLoggerRule;

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

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApiSpecificationPublicationService.class);

    private ApiSpecificationPublicationService apiSpecificationPublicationService;

    @Before
    public void setUp() {
        initMocks(this);

        apiSpecificationPublicationService = new ApiSpecificationPublicationService(
            apigeeService,
            apiSpecDocumentRepo,
            apiSpecHtmlProvider
        );
    }

    @After
    public void tearDown() {
        resetTimeProvider();
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

        final OpenApiSpecification remoteSpec = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willReturn(newSpecificationHtml);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecNeverPublished).should().setLastChangeCheckInstantInPlace(newCheckTime);
        then(localSpecNeverPublished).should().save();

        then(localSpecNeverPublished).should().setJsonForPublishing(remoteSpecificationJson);
        then(localSpecNeverPublished).should().setHtmlForPublishing(newSpecificationHtml);
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

        final OpenApiSpecification remoteSpecUpdatedSincePublishedLocally = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUpdatedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willReturn(newSpecificationHtml);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should().setLastChangeCheckInstantInPlace(newCheckTime);
        then(localSpecPublishedBefore).should().save();

        then(localSpecPublishedBefore).should().setJsonForPublishing(remoteSpecificationJson);
        then(localSpecPublishedBefore).should().setHtmlForPublishing(newSpecificationHtml);
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

        final OpenApiSpecification remoteSpecWithContentUnchangedSincePublishedLocally = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecWithContentUnchangedSincePublishedLocally));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should().setLastChangeCheckInstantInPlace(newCheckTime);
        then(localSpecPublishedBefore).should().save();

        then(localSpecPublishedBefore).should(never()).setJsonForPublishing(any());
        then(localSpecPublishedBefore).should(never()).setHtmlForPublishing(any());
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

        final OpenApiSpecification remoteSpecUnchangedSincePublishedLocally = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpecUnchangedSincePublishedLocally));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should().setLastChangeCheckInstantInPlace(newCheckTime);
        then(localSpecPublishedBefore).should().save();

        then(localSpecPublishedBefore).should(never()).setJsonForPublishing(any());
        then(localSpecPublishedBefore).should(never()).setHtmlForPublishing(any());
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

        final OpenApiSpecification remoteSpec = remoteSpecWith(remoteSpecificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpecPublishedBefore).should(never()).setJsonForPublishing(any());
        then(localSpecPublishedBefore).should(never()).setHtmlForPublishing(any());
        then(localSpecPublishedBefore).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 1, in Apigee: 1, updated in Apigee and eligible to publish in CMS: 0, synced: 0, failed to sync: 0")
        );
    }

    @Test
    public void sync_handlesMultipleSpecifications() {

        // given
        // @formatter:off

        // never published but has counterpart in Apigee - expect to be updated and published
        final String neverPublishedEligible_id            = "neverPublishedEligible";
        final String neverPublishedEligible_remoteModTime =               "2020-05-10T10:30:00.001Z";
        final Instant neverPublishedEligible_newCheckTime = Instant.parse("2020-05-10T10:30:00.007Z");
        final String neverPublishedEligible_newJson       = "{ \"json\": \"neverPublishedEligible\" }";
        final String neverPublishedEligible_newHtml       = "<html><body>neverPublishedEligible</body></html>";
        final ApiSpecificationDocument neverPublishedEligible_localSpec = localSpec()
            .withId(neverPublishedEligible_id)
            .mock();
        final OpenApiSpecification neverPublishedEligible_remoteSpec = remoteSpecWith(neverPublishedEligible_id, neverPublishedEligible_remoteModTime);
        given(apigeeService.apiSpecificationJsonForSpecId(neverPublishedEligible_id)).willReturn(neverPublishedEligible_newJson);
        given(apiSpecHtmlProvider.htmlFrom(neverPublishedEligible_newJson)).willReturn(neverPublishedEligible_newHtml);

        // published before but changed in Apigee - expect to be updated and published
        final String publishedChanged_id                   = "publishedChanged";
        final String publishedChangedRemote_modTime        =               "2020-05-10T10:30:00.002Z";
        final String publishedChanged_oldCheckTime         =               "2020-05-10T10:30:00.001Z";
        final Instant publishedChanged_newCheckTime        = Instant.parse("2020-05-10T10:30:00.003Z");
        final String publishedChanged_newJson              = "{ \"json\": \"publishedChanged\" }";
        final String publishedChanged_newHtml              = "<html><body>publishedChanged</body></html>";
        final ApiSpecificationDocument publishedChanged_localSpec = localSpec()
            .withId(publishedChanged_id)
            .withLastCheckedInstant(publishedChanged_oldCheckTime)
            .mock();
        final OpenApiSpecification publishedChanged_remoteSpec = remoteSpecWith(publishedChanged_id, publishedChangedRemote_modTime);
        given(apigeeService.apiSpecificationJsonForSpecId(publishedChanged_id)).willReturn(publishedChanged_newJson);
        given(apiSpecHtmlProvider.htmlFrom(publishedChanged_newJson)).willReturn(publishedChanged_newHtml);

        // has a changed counterpart in Apigee but programmed to fail - expect to not be updated nor published
        final String eligibleFailing_id                    = "eligibleFailing";
        final String eligibleFailingRemote_modTime         = "2020-05-10T10:30:00.003Z";
        final String eligibleFailingOld_checkTime          = "2020-05-10T10:30:00.002Z";
        final String eligibleFailing_newJson               = "{ \"json\": \"eligibleFailing new\" }";
        final String eligibleFailing_oldJson               = "{ \"json\": \"eligibleFailing old\" }";
        final ApiSpecificationDocument eligibleFailing_localSpec = localSpec()
            .withId(eligibleFailing_id)
            .withLastCheckedInstant(eligibleFailingOld_checkTime)
            .withJson(eligibleFailing_oldJson)
            .mock();
        final OpenApiSpecification eligibleFailing_remoteSpec = remoteSpecWith(eligibleFailing_id, eligibleFailingRemote_modTime);
        given(apigeeService.apiSpecificationJsonForSpecId(eligibleFailing_id)).willReturn(eligibleFailing_newJson);
        given(apiSpecHtmlProvider.htmlFrom(eligibleFailing_newJson)).willThrow(new RuntimeException("Invalid spec JSON."));

        // published before but not changed in Apigee - expect to not be updated nor published
        final String publishedNotChanged_id                = "publishedNotChanged";
        final String publishedNotChangedRemote_modTime     =               "2020-05-10T10:30:00.001Z";
        final String publishedNotChangedOld_checkTime      =               "2020-05-10T10:30:00.002Z";
        final Instant publishedNotChanged_newCheckTime     = Instant.parse("2020-05-10T10:30:00.002Z");
        final ApiSpecificationDocument publishedNotChanged_localSpec = localSpec()
            .withId(publishedNotChanged_id)
            .withLastCheckedInstant(publishedNotChangedOld_checkTime)
            .mock();
        final OpenApiSpecification publishedNotChanged_remoteSpec = remoteSpecWith(publishedNotChanged_id, publishedNotChangedRemote_modTime);

        // has no counterpart in Apigee - expect to be ignored
        final String noCounterpartInApigee_id              = "noCounterpartInApigee";
        final ApiSpecificationDocument noCounterpartInApigee_localSpec = localSpec().withId(noCounterpartInApigee_id).mock();
        // @formatter:on

        // keep the order of 'next nows' matching that of the results returned by the mocked call to apiSpecDocumentRepo.findAllApiSpecifications() below
        nextNowsAre(
            neverPublishedEligible_newCheckTime,
            // nothing for eligibleFailing - because of the programmed failure, we won't be asking for new check time
            publishedChanged_newCheckTime,
            publishedNotChanged_newCheckTime
        );

        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(asList(
            neverPublishedEligible_localSpec,
            publishedChanged_localSpec,
            eligibleFailing_localSpec,
            noCounterpartInApigee_localSpec,
            publishedNotChanged_localSpec
        ));

        given(apigeeService.apiSpecificationStatuses()).willReturn(asList(
            neverPublishedEligible_remoteSpec,
            publishedChanged_remoteSpec,
            publishedNotChanged_remoteSpec,
            eligibleFailing_remoteSpec
        ));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(neverPublishedEligible_localSpec).should().setLastChangeCheckInstantInPlace(neverPublishedEligible_newCheckTime);
        then(neverPublishedEligible_localSpec).should().save();
        then(neverPublishedEligible_localSpec).should().setJsonForPublishing(neverPublishedEligible_newJson);
        then(neverPublishedEligible_localSpec).should().setHtmlForPublishing(neverPublishedEligible_newHtml);
        then(neverPublishedEligible_localSpec).should().saveAndPublish();

        then(publishedChanged_localSpec).should().setLastChangeCheckInstantInPlace(publishedChanged_newCheckTime);
        then(publishedChanged_localSpec).should().save();
        then(publishedChanged_localSpec).should().setJsonForPublishing(publishedChanged_newJson);
        then(publishedChanged_localSpec).should().setHtmlForPublishing(publishedChanged_newHtml);
        then(publishedChanged_localSpec).should().saveAndPublish();

        then(eligibleFailing_localSpec).should(never()).setLastChangeCheckInstantInPlace(any());
        then(eligibleFailing_localSpec).should(never()).save();
        then(eligibleFailing_localSpec).should(never()).setJsonForPublishing(any());
        then(eligibleFailing_localSpec).should(never()).setHtmlForPublishing(any());
        then(eligibleFailing_localSpec).should(never()).saveAndPublish();

        then(publishedNotChanged_localSpec).should().setLastChangeCheckInstantInPlace(publishedNotChanged_newCheckTime);
        then(publishedNotChanged_localSpec).should().save();
        then(publishedNotChanged_localSpec).should(never()).setJsonForPublishing(any());
        then(publishedNotChanged_localSpec).should(never()).setHtmlForPublishing(any());
        then(publishedNotChanged_localSpec).should(never()).saveAndPublish();

        then(noCounterpartInApigee_localSpec).should(never()).setLastChangeCheckInstantInPlace(any());
        then(noCounterpartInApigee_localSpec).should(never()).save();
        then(noCounterpartInApigee_localSpec).should(never()).setJsonForPublishing(any());
        then(noCounterpartInApigee_localSpec).should(never()).setHtmlForPublishing(any());
        then(noCounterpartInApigee_localSpec).should(never()).saveAndPublish();

        logger.shouldReceive(
            info("API Specifications found: in CMS: 5, in Apigee: 4, updated in Apigee and eligible to publish in CMS: 3, synced: 2, failed to sync: 1"),
            info("Synchronised API Specification with id neverPublishedEligible at /content/docs/neverPublishedEligible"),
            info("Synchronised API Specification with id publishedChanged at /content/docs/publishedChanged"),
            error("Failed to synchronise API Specification with id eligibleFailing at /content/docs/eligibleFailing")
                .withException("Failed to render OAS JSON into HTML.")
                .withCause("Invalid spec JSON.")
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

        final OpenApiSpecification remoteSpec = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willThrow(new RuntimeException("Failed to retrieve remote spec."));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpec).should(never()).setLastChangeCheckInstantInPlace(any());
        then(localSpec).should(never()).save();

        then(localSpec).should(never()).setJsonForPublishing(any());
        then(localSpec).should(never()).setHtmlForPublishing(any());
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

        final OpenApiSpecification remoteSpec = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willThrow(new RuntimeException("Invalid spec."));

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpec).should(never()).setLastChangeCheckInstantInPlace(any());
        then(localSpec).should(never()).save();

        then(localSpec).should(never()).setJsonForPublishing(any());
        then(localSpec).should(never()).setHtmlForPublishing(any());
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

        final OpenApiSpecification remoteSpec = remoteSpecWith(specificationId, remoteSpecModificationTime);
        given(apigeeService.apiSpecificationStatuses()).willReturn(singletonList(remoteSpec));

        given(apigeeService.apiSpecificationJsonForSpecId(specificationId)).willReturn(remoteSpecificationJson);

        given(apiSpecHtmlProvider.htmlFrom(remoteSpecificationJson)).willReturn(newSpecificationHtml);

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(localSpec).should().setLastChangeCheckInstantInPlace(newCheckTime);
        then(localSpec).should().save();

        then(localSpec).should(never()).setJsonForPublishing(any());
        then(localSpec).should(never()).setHtmlForPublishing(any());
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

        final OpenApiSpecification remoteSpecA = remoteSpecWith(specificationIdA, remoteSpecModificationTimeA);
        final OpenApiSpecification remoteSpecB = remoteSpecWith(specificationIdB, remoteSpecModificationTimeB);

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
        then(localSpecNeverPublishedA).should(never()).setLastChangeCheckInstantInPlace(any());
        then(localSpecNeverPublishedA).should(never()).save();

        then(localSpecNeverPublishedA).should(never()).setJsonForPublishing(any());
        then(localSpecNeverPublishedA).should(never()).setHtmlForPublishing(any());
        then(localSpecNeverPublishedA).should(never()).saveAndPublish();

        then(localSpecNeverPublishedB).should().setLastChangeCheckInstantInPlace(newCheckTimeB);
        then(localSpecNeverPublishedB).should().save();

        then(localSpecNeverPublishedB).should().setJsonForPublishing(remoteSpecificationJsonB);
        then(localSpecNeverPublishedB).should().setHtmlForPublishing(newSpecificationHtmlB);
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
        then(localSpecPublishedBefore).should().setHtmlInPlace(newSpecificationHtml);
        then(localSpecPublishedBefore).should().save();
        then(localSpecPublishedBefore).should().getId(); // this call is made for the sake of logging
        then(localSpecPublishedBefore).should().path();  // this call is made for the sake of logging
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
    public void rerender_skipsSpecification_whenUnchanged_ignoringUuids() {

        // given
        // @formatter:off
        final String specificationId        = "248569";

        final String localSpecificationJson = "{ \"json\": \"payload\" }";
        final String newSpecificationHtml   = "<html><body><div data-schema-uuid=\"df7a85bf-8dcf-4b7d-9bbb-1d387c638091\">spec html different only in UUID</div></body></html>";
        final String oldSpecificationHtml   = "<html><body><div data-schema-uuid=\"ea5cd2c0-37eb-4e7c-af44-112f574b5c51\">spec html different only in UUID</div></body></html>";
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

    private OpenApiSpecification remoteSpecWith(final String specificationId, final String lastModifiedInstant) {

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
