package uk.nhs.digital.apispecs.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.ApiSpecificationImportMetadata;

import java.time.Instant;
import java.util.regex.Pattern;

public class SpecificationSyncData {

    private static final Logger log = LoggerFactory.getLogger(SpecificationSyncData.class);

    private static final Pattern VERSION_FIELD_PATTERN = Pattern.compile("\"version\"\\s*:\\s*\"[^\"]+\"");

    private final String specificationId;
    private final String apiSpecJcrHandleNodeId;
    private final String draftSpecJcrPath;
    private ApiSpecificationDocument localSpec;
    private OpenApiSpecification remoteSpec;
    private ApiSpecificationImportMetadata.Item localSpecMetadataItem;
    private Exception error;
    private boolean published;
    private boolean eligible;
    private boolean skipped;

    private SpecificationSyncData(
        final String specificationId,
        final String apiSpecJcrHandleNodeId,
        final String draftSpecJcrPath
    ) {
        this.specificationId = specificationId;
        this.apiSpecJcrHandleNodeId = apiSpecJcrHandleNodeId;
        this.draftSpecJcrPath = draftSpecJcrPath;
    }

    public static SpecificationSyncData with(
        final String specificationId,
        final String apiSpecJcrHandleNodeId,
        final String draftSpecJcrPath
    ) {
        return new SpecificationSyncData(
            specificationId,
            apiSpecJcrHandleNodeId,
            draftSpecJcrPath
        );
    }

    public boolean specContentDiffersIgnoringVersion() {

        final String remoteSpecJson = remoteSpec.getSpecJson().orElse("");
        final String localSpecJson = localSpec.json().orElse("");

        // We're ignoring version field because it is often the only piece of spec's content that actually changes.
        // It is calculated from git tags which are incremented on each merge to master (of the API codebase)
        // and that incrementation often takes place as a result of the API proxy definition being updated,
        // with no change to the spec itself.
        final String remoteSpecJsonNoVersion = VERSION_FIELD_PATTERN.matcher(remoteSpecJson).replaceFirst("");
        final String localSpecJsonNoVersion = VERSION_FIELD_PATTERN.matcher(localSpecJson).replaceFirst("");

        final boolean jsonDiffers = !remoteSpecJsonNoVersion.equals(localSpecJsonNoVersion);

        log.debug("{} Json differs: {}.", apiSpecJcrHandleNodeId, jsonDiffers);

        return jsonDiffers;
    }

    public boolean remoteSpecReportedAsUpdated() {

        final boolean specReportedAsUpdated = remoteSpec.getModified().isAfter(localSpecLastCheckTime());

        log.debug(
            "{} Remote spec reported as modified after last check: {}; local: {}, remote: {} ",
            apiSpecJcrHandleNodeId,
            specReportedAsUpdated,
            localSpecLastCheckTime(),
            remoteSpec.getModified()
        );

        return specReportedAsUpdated;
    }

    private Instant localSpecLastCheckTime() {
        return localSpecMetadataItem.lastChangeCheckInstant();
    }

    public void setLocalSpec(final ApiSpecificationDocument localSpec) {
        this.localSpec = localSpec;
    }

    public void setRemoteSpec(final OpenApiSpecification remoteSpec) {
        this.remoteSpec = remoteSpec;
    }

    public void setLocalSpecMetadataItem(final ApiSpecificationImportMetadata.Item localSpecMetadataItem) {
        this.localSpecMetadataItem = localSpecMetadataItem;
    }

    public void setError(final String message, final Exception cause) {
        this.error = new RuntimeException(message, cause);
    }

    public void setEligible(final boolean eligible) {
        this.eligible = eligible;
    }

    public void markPublished() {
        published = true;
    }

    public boolean noFailure() {
        return error == null;
    }

    public boolean failedEarlier() {
        return !noFailure();
    }

    public ApiSpecificationDocument localSpec() {
        return localSpec;
    }

    public OpenApiSpecification remoteSpec() {
        return remoteSpec;
    }

    public ApiSpecificationImportMetadata.Item localMetadata() {
        return localSpecMetadataItem;
    }

    public boolean eligible() {
        return eligible;
    }

    Exception error() {
        return error;
    }

    boolean published() {
        return published;
    }

    public void markSkipped() {
        skipped = true;
    }

    public boolean skipped() {
        return skipped;
    }

    public String specJcrHandleNodeId() {
        return apiSpecJcrHandleNodeId;
    }

    public String specificationId() {
        return specificationId;
    }

    public String specJcrPath() {
        return (localSpec != null) ? localSpec.path() : draftSpecJcrPath;
    }
}
