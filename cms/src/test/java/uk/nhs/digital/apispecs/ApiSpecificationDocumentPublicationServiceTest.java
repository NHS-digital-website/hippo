package uk.nhs.digital.apispecs;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.OpenApiSpecificationStatus;

import java.time.Instant;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ApiSpecificationDocumentPublicationServiceTest {

    @Mock
    private OpenApiSpecificationRepository apigeeService;

    @Mock
    private ApiSpecificationDocumentJcrRepository apiSpecDocumentRepo;

    @Mock
    private ApiSpecificationHtmlProvider apiSpecHtmlProvider;

    private ApiSpecificationDocumentPublicationService apiSpecificationDocumentPublicationService;

    @Before
    public void setUp() {
        initMocks(this);

        apiSpecificationDocumentPublicationService = new ApiSpecificationDocumentPublicationService(
            apigeeService,
            apiSpecDocumentRepo,
            apiSpecHtmlProvider
        );
    }

    @Test
    public void publish_publishesSpecification_thatChangedInApigeeAfterItWasPublishedInCms() {

        // given
        final String specificationId = "248569";

        // @formatter:off
        final String lastCmsPublicationTimestamp     = "2020-05-10T10:30:00.000Z";
        final String lastApigeeModificationTimestamp = "2020-05-20T10:30:00.000Z";
        // @formatter:on

        final ApiSpecificationDocument cmsSpecPublished = apiSpecDoc(specificationId, lastCmsPublicationTimestamp);
        when(apiSpecDocumentRepo.findAllApiSpecifications()).thenReturn(singletonList(cmsSpecPublished));

        final OpenApiSpecificationStatus apigeeSpecUpdateSincePublished = remoteSpecStatus(specificationId, lastApigeeModificationTimestamp);
        when(apigeeService.apiSpecificationStatuses()).thenReturn(singletonList(apigeeSpecUpdateSincePublished));

        final String specificationHtml = "<html><body> Some spec content </body></html>";
        when(apiSpecHtmlProvider.getHtmlForSpec(any())).thenReturn(specificationHtml);

        // when
        apiSpecificationDocumentPublicationService.updateAndPublishEligibleSpecifications();

        // then
        then(cmsSpecPublished).should().setHtml(specificationHtml);
        then(cmsSpecPublished).should().saveAndPublish();
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_ifTheyHaveNotChangedInApigeeAfterTheyWerePublishedInCms() {

        // given
        final String specificationId = "248569";

        // @formatter:off
        final String lastCmsPublicationTimestamp     = "2020-05-10T10:30:00.000Z";
        final String lastApigeeModificationTimestamp = "2020-05-10T10:30:00.000Z";
        // @formatter:on

        final ApiSpecificationDocument cmsSpecPublished = apiSpecDoc(specificationId, lastCmsPublicationTimestamp);
        when(apiSpecDocumentRepo.findAllApiSpecifications()).thenReturn(singletonList(cmsSpecPublished));

        final OpenApiSpecificationStatus apigeeSpecNotUpdatedSincePublished = remoteSpecStatus(specificationId, lastApigeeModificationTimestamp);
        when(apigeeService.apiSpecificationStatuses()).thenReturn(singletonList(apigeeSpecNotUpdatedSincePublished));

        // when
        apiSpecificationDocumentPublicationService.updateAndPublishEligibleSpecifications();

        // then
        then(cmsSpecPublished).should(never()).setHtml(any());
        then(cmsSpecPublished).should(never()).saveAndPublish();
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_ifTheyDoNotHaveMatchingCounterpartsInRemoteSystem() {

        // given
        // @formatter:off
        final String cmsSpecificationId    = "965842";
        final String remoteSpecificationId = "248569";

        final String lastCmsPublicationTimestamp     = "2020-05-10T10:30:00.000Z";
        final String lastApigeeModificationTimestamp = "2020-05-20T10:30:00.000Z";
        // @formatter:on

        final ApiSpecificationDocument cmsSpecPublished = apiSpecDoc(cmsSpecificationId, lastCmsPublicationTimestamp);
        when(apiSpecDocumentRepo.findAllApiSpecifications()).thenReturn(singletonList(cmsSpecPublished));

        final OpenApiSpecificationStatus apigeeSpecUpdateSincePublished = remoteSpecStatus(remoteSpecificationId, lastApigeeModificationTimestamp);
        when(apigeeService.apiSpecificationStatuses()).thenReturn(singletonList(apigeeSpecUpdateSincePublished));

        // when
        apiSpecificationDocumentPublicationService.updateAndPublishEligibleSpecifications();

        // then
        then(cmsSpecPublished).should(never()).setHtml(any());
        then(cmsSpecPublished).should(never()).saveAndPublish();
    }

    @Test
    public void publish_canHandleMultipleSpecifications() {

        // given
        final String specificationAId = "248569";
        final String specificationBId = "965842";

        final ApiSpecificationDocument cmsSpecAPublished = apiSpecDoc(specificationAId, "2020-05-11T10:30:00.000Z");
        final ApiSpecificationDocument cmsSpecBPublished = apiSpecDoc(specificationBId, "2020-05-10T10:30:00.000Z");
        when(apiSpecDocumentRepo.findAllApiSpecifications()).thenReturn(asList(
            cmsSpecAPublished,
            cmsSpecBPublished
        ));

        final OpenApiSpecificationStatus apigeeSpecAUpdateSincePublished = remoteSpecStatus(specificationAId, "2020-05-20T10:30:00.000Z");
        final OpenApiSpecificationStatus apigeeSpecBUpdateSincePublished = remoteSpecStatus(specificationBId, "2020-05-21T10:30:00.000Z");
        when(apigeeService.apiSpecificationStatuses()).thenReturn(asList(
            apigeeSpecAUpdateSincePublished,
            apigeeSpecBUpdateSincePublished
        ));

        final String specificationAHtml = "<html><body> spec A content </body></html>";
        final String specificationBHtml = "<html><body> spec B content </body></html>";
        when(apiSpecHtmlProvider.getHtmlForSpec(cmsSpecAPublished)).thenReturn(specificationAHtml);
        when(apiSpecHtmlProvider.getHtmlForSpec(cmsSpecBPublished)).thenReturn(specificationBHtml);

        // when
        apiSpecificationDocumentPublicationService.updateAndPublishEligibleSpecifications();

        // then
        then(cmsSpecAPublished).should().setHtml(specificationAHtml);
        then(cmsSpecAPublished).should().saveAndPublish();

        then(cmsSpecBPublished).should().setHtml(specificationBHtml);
        then(cmsSpecBPublished).should().saveAndPublish();
    }

    private ApiSpecificationDocument apiSpecDoc(final String specificationId, final String lastModTimestamp) {

        final ApiSpecificationDocument apiSpecificationDocument = mock(ApiSpecificationDocument.class);

        given(apiSpecificationDocument.getId()).willReturn(specificationId);
        given(apiSpecificationDocument.getLastPublicationInstant()).willReturn(Optional.of(Instant.parse(lastModTimestamp)));

        return apiSpecificationDocument;
    }

    private OpenApiSpecificationStatus remoteSpecStatus(final String specificationId, final String lastModifiedInstant) {
        return new OpenApiSpecificationStatus(specificationId, lastModifiedInstant);
    }
}
