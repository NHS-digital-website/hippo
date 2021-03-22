package uk.nhs.digital.apispecs;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.apispecs.ApiSpecificationPublicationServiceTest.ApiSpecDocMocker.localSpec;
import static uk.nhs.digital.test.util.TimeProviderTestUtils.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
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
    public void publish_publishesSpecifications_existingInCmsButNeverPublished() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String remoteSpecModificationTime =                     "2020-05-20T10:30:00.000Z";
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
    }

    @Test
    public void publish_publishesSpecifications_thatChangedInApigeeAfterItWasPublishedInCms() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String remoteSpecModificationTime =                     "2020-05-20T10:30:00.000Z";
        final String oldLocalCheckTime          =                     "2020-05-10T10:30:00.000Z";
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z");

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
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_whereApigeeReportsChangeAfterLastCheckInCms_butApigeeContentHasNotActuallyChanged() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String oldLocalCheckTime          =                     "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime =                     "2020-05-20T10:30:00.000Z";
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
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_ifApigeeReportsNoChangeAfterLastCheckInCms() {

        // given
        // @formatter:off
        final String specificationId            = "248569";

        final String oldLocalCheckTime          =                     "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime =                     "2020-05-20T10:30:00.000Z";
        final Instant newCheckTime              = nextNowIs("2020-05-10T10:30:00.001Z");
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
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_whichDoNotHaveMatchingCounterpartsInRemoteSystem() {

        // given
        // @formatter:off
        final String localSpecificationId       = "965842";
        final String remoteSpecificationId      = "248569";

        final String localCheckTime             = "2020-05-10T10:30:00.000Z";
        final String remoteSpecModificationTime = "2020-05-20T10:30:00.000Z";
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
    }

    @Test
    public void publish_doesNotMakeAnyRequestToRemoteSystem_ifThereAreNoLocalApiSpecDocuments() {

        // given
        given(apiSpecDocumentRepo.findAllApiSpecifications()).willReturn(emptyList());

        // when
        apiSpecificationPublicationService.syncEligibleSpecifications();

        // then
        then(apigeeService).shouldHaveZeroInteractions();
    }

    @Test
    public void publish_handlesMultipleSpecifications() {

        // given
        // @formatter:off
        final String specificationIdA            = "248569";
        final String specificationIdB            = "965842";

        final String remoteSpecModificationTimeA =               "2020-05-20T10:30:00.000Z";
        final Instant newCheckTimeA              = Instant.parse("2020-05-10T10:30:02.001Z");

        final String remoteSpecificationJsonA    = "{ \"new-spec\": \"json-a\" }";
        final String newSpecificationHtmlA       = "<html><body> new spec html A</body></html>";

        final String remoteSpecModificationTimeB =               "2020-05-20T10:30:01.000Z";
        final Instant newCheckTimeB              = Instant.parse("2020-05-10T10:30:03.001Z");

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

    static class ApiSpecDocMocker {

        private final ApiSpecificationDocument spec = Mockito.mock(ApiSpecificationDocument.class);

        public static ApiSpecDocMocker localSpec() {
            return new ApiSpecDocMocker();
        }

        public ApiSpecDocMocker withId(final String specificationId) {
            given(spec.getId()).willReturn(specificationId);
            given(spec.path()).willReturn("/content/docs/" + specificationId);

            return this;
        }

        public ApiSpecDocMocker withJson(final String json) {
            given(spec.json()).willReturn(Optional.of(json));

            return this;
        }

        public ApiSpecDocMocker withHtml(final String html) {
            given(spec.html()).willReturn(Optional.of(html));

            return this;
        }

        public ApiSpecDocMocker withLastCheckedInstant(final String instant) {
            given(spec.lastChangeCheckInstant()).willReturn(Optional.of(instant).map(Instant::parse));

            return this;
        }

        public ApiSpecificationDocument mock() {
            return spec;
        }
    }
}
