package uk.nhs.digital.apispecs;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.OpenApiSpecification;
import uk.nhs.digital.apispecs.model.SpecificationSyncData;
import uk.nhs.digital.apispecs.model.SyncResults;
import uk.nhs.digital.common.util.TimeProvider;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;


public class ApiSpecificationPublicationService {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecificationPublicationService.class);

    private final ApiSpecificationImportMetadataRepository apiSpecificationImportMetadataRepository;
    private final OpenApiSpecificationRepository remoteSpecRepository;
    private final ApiSpecificationDocumentRepository localSpecRepository;

    public ApiSpecificationPublicationService(final OpenApiSpecificationRepository remoteSpecRepository,
                                              final ApiSpecificationDocumentRepository localSpecRepository,
                                              final ApiSpecificationImportMetadataRepository apiSpecificationImportMetadataRepository) {
        this.remoteSpecRepository = remoteSpecRepository;
        this.localSpecRepository = localSpecRepository;
        this.apiSpecificationImportMetadataRepository = apiSpecificationImportMetadataRepository;
    }

    public void syncEligibleSpecifications() {

        final List<ApiSpecificationDocument> localSpecs = findCmsApiSpecifications();

        final List<OpenApiSpecification> remoteSpecs = localSpecs.isEmpty()
            ? emptyList()
            : getRemoteSpecifications();

        final ApiSpecificationImportMetadata apiSpecificationImportMetadata = apiSpecificationImportMetadata();

        final Map<String, OpenApiSpecification> remoteSpecsById = Maps.uniqueIndex(
            remoteSpecs, OpenApiSpecification::getId
        );

        final SyncResults syncResults = localSpecs.stream()
            .filter(specificationsPresentInBothSystems(remoteSpecsById))
            .map(toInitialSpecSyncData(remoteSpecsById, apiSpecificationImportMetadata))
            .peek(determineEligibility())
            .peek(setLastChangeCheckInstant())
            .peek(publishSpecIfActuallyChanged())
            .collect(syncResults());

        save(apiSpecificationImportMetadata);

        reportPublicationStatusFor(localSpecs.size(), remoteSpecs.size(), syncResults);
    }

    private void save(final ApiSpecificationImportMetadata localSpecsMetadata) {
        log.debug("Saving API Specification import metadata: start: {}", localSpecsMetadata);
        apiSpecificationImportMetadataRepository.save(localSpecsMetadata);
        log.debug("Saving API Specification import metadata: done.");
    }

    private ApiSpecificationImportMetadata apiSpecificationImportMetadata() {

        log.debug("API Specification import metadata: query start.");
        final ApiSpecificationImportMetadata apiSpecificationImportMetadata = apiSpecificationImportMetadataRepository.findApiSpecificationImportMetadata();
        log.debug("API Specification import metadata: found metadata: {}", apiSpecificationImportMetadata);

        return apiSpecificationImportMetadata;
    }

    private Function<ApiSpecificationDocument, SpecificationSyncData> toInitialSpecSyncData(
        final Map<String, OpenApiSpecification> remoteSpecsById,
        final ApiSpecificationImportMetadata apiSpecificationImportMetadata
    ) {
        return localSpec -> {
            final ApiSpecificationImportMetadata.Item specMetadata = apiSpecificationImportMetadata.getBySpecJcrId(localSpec.jcrId());
            specMetadata.setSpecExists();

            log.debug("{} Processing API Specification: id: {}; path: {}.", localSpec.jcrId(), localSpec.specificationId(), localSpec.path());

            return SpecificationSyncData.with(
                localSpec,
                remoteSpecsById.get(localSpec.specificationId()),
                specMetadata
            );
        };
    }

    private Consumer<SpecificationSyncData> determineEligibility() {
        return ifSuccessfulSoFar(specSyncData -> {
            try {
                log.debug("{} Determining eligibility for update: start.", specSyncData.specJcrId());
                final boolean eligible = specSyncData.specContentChanged();
                log.debug("{} Determining eligibility for update: done; eligible: {}.", specSyncData.specJcrId(), eligible);

                specSyncData.setEligible(eligible);
            } catch (final Exception e) {
                specSyncData.setError("Failed to determine whether the specification is eligible for update.", e);
            }
        });
    }

    private Consumer<SpecificationSyncData> setLastChangeCheckInstant() {
        return ifSuccessfulSoFar(specSyncData -> {
            try {
                final Instant nowInstant = TimeProvider.getNowInstant();

                log.debug("{} Setting lastChangeCheckInstant to {}", specSyncData.specJcrId(), nowInstant.toString());

                specSyncData.localMetadata().setLastChangeCheckInstant(nowInstant);
            } catch (final Exception cause) {
                specSyncData.setError(
                    format("Failed to record time of last check on specification with id %s at %s.",
                        specSyncData.localSpec().specificationId(),
                        specSyncData.localSpec().path()
                    ),
                    cause
                );
            }
        });
    }

    private Consumer<SpecificationSyncData> publishSpecIfActuallyChanged() {
        return ifSuccessfulSoFar(specSyncData -> {
            if (specSyncData.eligible()) {
                log.debug("{} Publishing spec: start.", specSyncData.specJcrId());
                updateAndPublish(specSyncData);
                log.debug("{} Publishing spec: done.", specSyncData.specJcrId());
            }
        });
    }

    private Consumer<SpecificationSyncData> ifSuccessfulSoFar(final Consumer<SpecificationSyncData> specificationSyncDataConsumer) {
        return specSyncData -> {
            if (specSyncData.failedEarlier()) {
                return;
            }

            specificationSyncDataConsumer.accept(specSyncData);
        };
    }

    private void reportPublicationStatusFor(final int localSpecsCount, final int remoteSpecsCount, final SyncResults syncResults) {

        log.info(
            "API Specifications found: in CMS: {}, in {}: {}, updated in {} and eligible to publish in CMS: {}, synced: {}, failed to sync: {}",
            localSpecsCount, remoteSpecRepository.getClass().getSimpleName(),
            remoteSpecsCount, remoteSpecRepository.getClass().getSimpleName(),
            syncResults.eligible().size(), syncResults.published().size(),
            syncResults.failed().size()
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

    private List<OpenApiSpecification> getRemoteSpecifications() {

        log.debug("Retrieving API Specification statuses from remote repository: start.");
        final List<OpenApiSpecification> openApiSpecifications = remoteSpecRepository.apiSpecificationStatuses();
        log.debug("Retrieving API Specification statuses from remote repository: found {} entries", openApiSpecifications.size());

        return openApiSpecifications;
    }

    private List<ApiSpecificationDocument> findCmsApiSpecifications() {

        log.debug("Local API Specifications query: start.");
        final List<ApiSpecificationDocument> allApiSpecifications = localSpecRepository.findAllApiSpecifications();
        log.debug("Local API Specifications query: found {} documents.", allApiSpecifications.size());

        return allApiSpecifications;
    }

    private Predicate<ApiSpecificationDocument> specificationsPresentInBothSystems(
        final Map<String, OpenApiSpecification> remoteSpecsById) {
        return apiSpecification -> remoteSpecsById.containsKey(apiSpecification.specificationId());
    }

    private void updateAndPublish(final SpecificationSyncData specSyncData) {

        try {
            final ApiSpecificationDocument localSpec = specSyncData.localSpec();

            specSyncData.remoteSpec().getSpecJson()
                .ifPresent(localSpec::setJsonForPublishing);

            localSpec.saveAndPublish();

            specSyncData.markPublished();

        } catch (final Exception e) {
            specSyncData.setError("Failed to publish.", e);
        }
    }
}

