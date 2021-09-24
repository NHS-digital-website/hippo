package uk.nhs.digital.apispecs;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static uk.nhs.digital.apispecs.ApiSpecificationPublicationService.PublicationResult.FAIL;
import static uk.nhs.digital.apispecs.ApiSpecificationPublicationService.PublicationResult.PASS;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;
import uk.nhs.digital.apispecs.model.SpecificationSyncData;
import uk.nhs.digital.apispecs.model.SyncResults;
import uk.nhs.digital.common.util.TimeProvider;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collector;


public class ApiSpecificationPublicationService {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecificationPublicationService.class);
    private static Pattern UUID_REGEX = Pattern.compile("[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}");

    private final OpenApiSpecificationJsonToHtmlConverter openApiSpecificationJsonToHtmlConverter;
    private final OpenApiSpecificationRepository remoteSpecRepository;
    private final ApiSpecificationDocumentRepository localSpecRepository;

    public ApiSpecificationPublicationService(final OpenApiSpecificationRepository remoteSpecRepository,
                                              final ApiSpecificationDocumentRepository localSpecRepository,
                                              final OpenApiSpecificationJsonToHtmlConverter openApiSpecificationJsonToHtmlConverter) {
        this.remoteSpecRepository = remoteSpecRepository;
        this.localSpecRepository = localSpecRepository;
        this.openApiSpecificationJsonToHtmlConverter = openApiSpecificationJsonToHtmlConverter;
    }

    public void syncEligibleSpecifications() {

        final List<ApiSpecificationDocument> localSpecs = findCmsApiSpecifications();

        final List<OpenApiSpecification> remoteSpecs = localSpecs.isEmpty()
            ? emptyList()
            : getRemoteSpecifications();

        final Map<String, OpenApiSpecification> remoteSpecsById = Maps.uniqueIndex(
            remoteSpecs, OpenApiSpecification::getId
        );

        final SyncResults syncResults = localSpecs.stream()
            .filter(specificationsPresentInBothSystems(remoteSpecsById))
            .map(toInitialSpecSyncData(remoteSpecsById))
            .peek(determineEligibility())
            .peek(renderHtmlIfContentActuallyChanged())
            .peek(setLastChangeCheckInstant())
            .peek(publishSpecIfActuallyChanged())
            .collect(syncResults());

        reportPublicationStatusFor(localSpecs.size(), remoteSpecs.size(), syncResults);
    }

    private Consumer<SpecificationSyncData> determineEligibility() {
        return specSyncData -> {
            if (specSyncData.failedEarlier()) {
                return;
            }

            try {
                specSyncData.setEligible(specSyncData.specContentChanged());
            } catch (final Exception e) {
                specSyncData.setError("Failed to determine whether the specification is eligible for update.", e);
            }
        };
    }

    private Function<ApiSpecificationDocument, SpecificationSyncData> toInitialSpecSyncData(
        final Map<String, OpenApiSpecification> remoteSpecsById) {
        return localSpec -> SpecificationSyncData.with(
            localSpec,
            remoteSpecsById.get(localSpec.getId())
        );
    }

    private Consumer<SpecificationSyncData> renderHtmlIfContentActuallyChanged() {
        return specSyncData -> {
            if (specSyncData.failedEarlier()) {
                return;
            }

            try {
                if (specSyncData.eligible()) {

                    specSyncData.remoteSpec()
                        .getSpecJson()
                        .map(openApiSpecificationJsonToHtmlConverter::htmlFrom)
                        .ifPresent(specSyncData::setHtml);

                } else {
                    if (specSyncData.specReportedAsUpdated()) {
                        specSyncData.markSkipped();
                    }
                }
            } catch (final Exception e) {
                specSyncData.setError("Failed to render OAS JSON into HTML.", e);
            }
        };
    }

    private Consumer<SpecificationSyncData> setLastChangeCheckInstant() {
        return specSyncData -> {
            if (specSyncData.failedEarlier()) {
                return;
            }

            try {
                specSyncData.localSpec().setLastChangeCheckInstantInPlace(TimeProvider.getNowInstant());
                specSyncData.localSpec().save();
            } catch (final Exception cause) {
                specSyncData.setError(
                    format("Failed to record time of last check on specification with id %s at %s.",
                        specSyncData.localSpec().getId(),
                        specSyncData.localSpec().path()
                    ),
                    cause
                );
            }
        };
    }

    private Consumer<SpecificationSyncData> publishSpecIfActuallyChanged() {
        return specSyncData -> {
            if (specSyncData.failedEarlier()) {
                return;
            }

            if (specSyncData.eligible()) {
                updateAndPublish(specSyncData);
            }
        };
    }

    private void reportPublicationStatusFor(final int localSpecsCount, final int remoteSpecsCount, final SyncResults syncResults) {

        log.info(
            "API Specifications found: in CMS: {}, in Apigee: {}, updated in Apigee and eligible to publish in CMS: {}, synced: {}, failed to sync: {}",
            localSpecsCount, remoteSpecsCount, syncResults.eligible().size(), syncResults.published().size(), syncResults.failed().size()
        );

        syncResults.published().forEach(specSyncResult ->
            log.info("Synchronised API Specification with id {} at {}", specSyncResult.specId(), specSyncResult.localSpecPath())
        );

        syncResults.failed().forEach(syncResult ->
            log.error("Failed to synchronise API Specification with id " + syncResult.specId()
                + " at " + syncResult.localSpecPath(), syncResult.error())
        );

        syncResults.skipped().forEach(syncResult ->
            log.debug("Modification timestamps indicate a change but the content has not changed; skipping spec with id {} at {}.",
                syncResult.specId(), syncResult.localSpecPath()
            )
        );
    }

    private Collector<SpecificationSyncData, SyncResults, SyncResults> syncResults() {
        return Collector.of(
            SyncResults::new,
            SyncResults::accumulate,
            SyncResults::combine,
            Collector.Characteristics.UNORDERED
        );
    }

    public void rerenderSpecifications() {
        log.info("Rerendering API Specifications.");

        final List<ApiSpecificationDocument> cmsApiSpecificationDocuments = findCmsApiSpecifications();

        log.debug("API Specifications found: {}", cmsApiSpecificationDocuments.size());

        final long failedSpecificationsCount = rerenderIfRenderingLogicChanged(cmsApiSpecificationDocuments);

        reportRerenderingStats(failedSpecificationsCount, cmsApiSpecificationDocuments.size());
    }

    private List<OpenApiSpecification> getRemoteSpecifications() {
        return remoteSpecRepository.apiSpecificationStatuses();
    }

    private List<ApiSpecificationDocument> findCmsApiSpecifications() {
        return localSpecRepository.findAllApiSpecifications();
    }

    private Predicate<ApiSpecificationDocument> specificationsPresentInBothSystems(
        final Map<String, OpenApiSpecification> remoteSpecsById) {
        return apiSpecification -> remoteSpecsById.containsKey(apiSpecification.getId());
    }

    private void updateAndPublish(final SpecificationSyncData specSyncData) {

        try {
            final ApiSpecificationDocument localSpec = specSyncData.localSpec();

            specSyncData.remoteSpec().getSpecJson()
                .ifPresent(localSpec::setJsonForPublishing);

            localSpec.setHtmlForPublishing(specSyncData.html());

            localSpec.saveAndPublish();

            specSyncData.markPublished();

        } catch (final Exception e) {
            specSyncData.setError("Failed to publish.", e);
        }
    }

    private long rerenderIfRenderingLogicChanged(final List<ApiSpecificationDocument> specsToPublish) {

        final long failedSpecificationsCount = specsToPublish.stream()
            .map(this::rerenderIfRenderingLogicChanged)
            .filter(PublicationResult::failed)
            .count();

        return failedSpecificationsCount;
    }

    private PublicationResult rerenderIfRenderingLogicChanged(final ApiSpecificationDocument apiSpecificationDocument) {
        try {
            log.debug("Rerendering API Specification: {}", apiSpecificationDocument);

            final String publishedHtml = apiSpecificationDocument.html().orElse("");
            final String candidateHtml = apiSpecificationDocument.json().map(this::specHtmlFrom).orElse("");

            if (renderingNotChangedIgnoringKnownVolatiles(candidateHtml, publishedHtml)) {
                log.debug("No changes to API Specification, skipped: {}", apiSpecificationDocument.getId());
                return PASS;
            }

            apiSpecificationDocument.setHtmlInPlace(candidateHtml);

            apiSpecificationDocument.save();

            log.info("API Specification has been rerendered: id: {}, path: {}", apiSpecificationDocument.getId(), apiSpecificationDocument.path());
            return PASS;

        } catch (final Exception e) {
            log.error("Failed to rerender API Specification: " + apiSpecificationDocument, e);

            return FAIL;
        }
    }

    /**
     * API Specification's rendered HTML contains a number of UUID
     * values which change with every rendering. If those values
     * are the only thing that changes across renderings, the rendered
     * content is deemed unchanged.
     */
    private boolean renderingNotChangedIgnoringKnownVolatiles(final String candidateHtml, final String publishedHtml) {

        final String candidateHtmlWithNoVolatiles = UUID_REGEX.matcher(candidateHtml).replaceAll("");
        final String publishedHtmlWithNoVolatiles = UUID_REGEX.matcher(publishedHtml).replaceAll("");

        return candidateHtmlWithNoVolatiles.equals(publishedHtmlWithNoVolatiles);
    }

    private String specHtmlFrom(final String openApiSpecJson) {
        return openApiSpecificationJsonToHtmlConverter.htmlFrom(openApiSpecJson);
    }

    private void reportRerenderingStats(final long failedSpecificationsCount,
                                        final long specificationsToPublishCount
    ) {
        log.info("Finished rerendering {} API specifications; successful: {}, failed: {}",
            specificationsToPublishCount,
            specificationsToPublishCount - failedSpecificationsCount,
            failedSpecificationsCount
        );

        if (failedSpecificationsCount > 0) {
            throw new RuntimeException(
                format("Failed to rerender %d out of %d eligible specifications; see preceding logs for details.",
                    failedSpecificationsCount,
                    specificationsToPublishCount
                ));
        }
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

