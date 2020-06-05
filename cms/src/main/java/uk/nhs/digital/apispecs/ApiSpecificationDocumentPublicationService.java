package uk.nhs.digital.apispecs;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.apispecs.ApiSpecificationDocumentPublicationService.PublicationResult.FAIL;
import static uk.nhs.digital.apispecs.ApiSpecificationDocumentPublicationService.PublicationResult.PASS;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.OpenApiSpecificationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ApiSpecificationDocumentPublicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecificationDocumentPublicationService.class);

    private ApiSpecificationHtmlProvider apiSpecificationHtmlProvider;
    private OpenApiSpecificationRepository openApiSpecificationRepository;
    private ApiSpecificationDocumentRepository apiSpecificationDocumentRepository;

    public ApiSpecificationDocumentPublicationService(final OpenApiSpecificationRepository openApiSpecificationRepository,
                                                      final ApiSpecificationDocumentRepository apiSpecificationDocumentRepository,
                                                      final ApiSpecificationHtmlProvider apiSpecificationHtmlProvider) {
        this.openApiSpecificationRepository = openApiSpecificationRepository;
        this.apiSpecificationDocumentRepository = apiSpecificationDocumentRepository;
        this.apiSpecificationHtmlProvider = apiSpecificationHtmlProvider;
    }

    public void updateAndPublishEligibleSpecifications() {

        final List<OpenApiSpecificationStatus> apigeeSpecsStatuses = getApigeeSpecStatuses();

        final List<ApiSpecificationDocument> cmsApiSpecificationDocuments = findCmsApiSpecifications();

        final List<ApiSpecificationDocument> specsEligibleToUpdateAndPublish = identifySpecsEligibleForUpdateAndPublication(
            cmsApiSpecificationDocuments,
            apigeeSpecsStatuses
        );

        if (!specsEligibleToUpdateAndPublish.isEmpty()) {
            LOGGER.info("API Specifications found: in CMS: {}, in Apigee: {}, updated in Apigee and eligible to publish in CMS: {}",
                cmsApiSpecificationDocuments.size(),
                apigeeSpecsStatuses.size(),
                specsEligibleToUpdateAndPublish.size());
        }

        updateAndPublish(specsEligibleToUpdateAndPublish);
    }

    private List<OpenApiSpecificationStatus> getApigeeSpecStatuses() {
        return openApiSpecificationRepository.apiSpecificationStatuses();
    }

    private List<ApiSpecificationDocument> findCmsApiSpecifications() {
        return apiSpecificationDocumentRepository.findAllApiSpecifications();
    }

    private List<ApiSpecificationDocument> identifySpecsEligibleForUpdateAndPublication(
        final List<ApiSpecificationDocument> cmsSpecs,
        final List<OpenApiSpecificationStatus> apigeeSpecStatuses
    ) {
        final Map<String, OpenApiSpecificationStatus> apigeeSpecsById = Maps.uniqueIndex(apigeeSpecStatuses, OpenApiSpecificationStatus::getId);

        return cmsSpecs.stream()
            .filter(specificationsPresentInBothSystems(apigeeSpecsById))
            .filter(specificationsChangedInApigeeAfterPublishedInCms(apigeeSpecsById))
            .collect(toList());
    }

    private Predicate<ApiSpecificationDocument> specificationsChangedInApigeeAfterPublishedInCms(
        final Map<String, OpenApiSpecificationStatus> apigeeSpecsById) {
        return apiSpecification -> {
            final OpenApiSpecificationStatus apigeeSpec = apigeeSpecsById.get(apiSpecification.getId());

            final Instant cmsSpecificationLastPublicationInstant =
                apiSpecification.getLastPublicationInstant().orElse(Instant.EPOCH);

            return apigeeSpec.getModified().isAfter(cmsSpecificationLastPublicationInstant);
        };
    }

    private Predicate<ApiSpecificationDocument> specificationsPresentInBothSystems(
        final Map<String, OpenApiSpecificationStatus> apigeeSpecsById) {
        return apiSpecification -> apigeeSpecsById.containsKey(apiSpecification.getId());
    }

    private void updateAndPublish(List<ApiSpecificationDocument> specsToPublish) {

        final long failedSpecificationsCount = specsToPublish.stream()
            .map(this::updateAndPublish)
            .filter(PublicationResult::failed)
            .count();

        reportErrorIfAnySpecificationsFailed(failedSpecificationsCount, specsToPublish.size());
    }

    private PublicationResult updateAndPublish(final ApiSpecificationDocument apiSpecificationDocument) {

        try {
            LOGGER.info("Publishing API Specification: {}", apiSpecificationDocument);

            final String specHtml = getHtmlForSpec(apiSpecificationDocument);

            apiSpecificationDocument.setHtml(specHtml);

            apiSpecificationDocument.saveAndPublish();

            LOGGER.info("API Specification has been published: {}", apiSpecificationDocument.getId());

            return PASS;

        } catch (final Exception e) {
            LOGGER.error("Failed to publish API Specification: " + apiSpecificationDocument, e);

            return FAIL;
        }
    }

    private void reportErrorIfAnySpecificationsFailed(final long failedSpecificationsCount,
                                                      final long specificationsToPublishCount
    ) {
        if (failedSpecificationsCount > 0) {
            throw new RuntimeException(
                format("Failed to publish %d out of %d specifications; see preceding logs for details.",
                    failedSpecificationsCount,
                    specificationsToPublishCount
                ));
        }
    }

    private String getHtmlForSpec(final ApiSpecificationDocument apiSpecificationDocument) {
        return apiSpecificationHtmlProvider.getHtmlForSpec(apiSpecificationDocument);
    }

    enum PublicationResult {
        PASS,
        FAIL;

        public boolean failed() {
            return this == FAIL;
        }

        public boolean passed() {
            return this == PASS;
        }
    }
}
