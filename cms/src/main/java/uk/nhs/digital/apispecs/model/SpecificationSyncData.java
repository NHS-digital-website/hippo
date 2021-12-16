package uk.nhs.digital.apispecs.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.ApiSpecificationImportMetadata;

import java.time.Instant;
import java.util.regex.Pattern;

public class SpecificationSyncData {

    private static final Logger log = LoggerFactory.getLogger(SpecificationSyncData.class);

    private static final Pattern VERSION_FIELD_PATTERN = Pattern.compile("\"version\"\\s*:\\s*\"[^\"]+\"");

    private final ApiSpecificationDocument localSpec;
    private final OpenApiSpecification remoteSpec;
    private final ApiSpecificationImportMetadata.Item localSpecMetadataItem;
    private String html;
    private Exception error;
    private boolean published;
    private boolean eligible;
    private boolean skipped;

    private SpecificationSyncData(
        final ApiSpecificationDocument localSpec,
        final OpenApiSpecification remoteSpec,
        final ApiSpecificationImportMetadata.Item localSpecMetadataItem
    ) {
        this.localSpec = localSpec;
        this.remoteSpec = remoteSpec;
        this.localSpecMetadataItem = localSpecMetadataItem;
    }

    public static SpecificationSyncData with(
        final ApiSpecificationDocument localSpec,
        final OpenApiSpecification remoteSpec,
        final ApiSpecificationImportMetadata.Item localSpecMetadataItem
    ) {
        return new SpecificationSyncData(
            localSpec,
            remoteSpec,
            localSpecMetadataItem
        );
    }

    private boolean specContentDiffersIgnoringVersion(final String left, final String right) {

        // We're ignoring version field because it is often the only piece of spec's content that actually changes.
        // It is calculated from git tags which are incremented on each merge to master (of the API codebase)
        // and that incrementation often takes place as a result of the API proxy definition being updated,
        // with no change to the spec itself.

        final String leftSpecJsonNoVersion = VERSION_FIELD_PATTERN.matcher(left).replaceFirst("");
        final String rightSpecJsonNoVersion = VERSION_FIELD_PATTERN.matcher(right).replaceFirst("");

        final boolean jsonDiffers = !leftSpecJsonNoVersion.equals(rightSpecJsonNoVersion);

        log.debug("{} Json differs: {}.", specJcrId(), jsonDiffers);

        return jsonDiffers;
    }

    public boolean specContentChanged() {

        return remoteSpecReportedAsUpdated() && specContentDiffersIgnoringVersion(
            remoteSpec.getSpecJson().orElse(""),
            localSpec.json().orElse("")
        );

    }

    public boolean remoteSpecReportedAsUpdated() {

        final boolean specReportedAsUpdated = remoteSpec.getModified().isAfter(localSpecLastCheckTime());

        log.debug(
            "{} Remote spec reported as modified after last check: {}; local: {}, remote: {} ",
            specJcrId(),
            specReportedAsUpdated,
            localSpecLastCheckTime(),
            remoteSpec.getModified()
        );

        return specReportedAsUpdated;
    }

    private Instant localSpecLastCheckTime() {
        return localMetadata().lastChangeCheckInstant();
    }

    public void setHtml(final String html) {
        this.html = html;
    }

    public void setError(final String message, final Exception cause) {
        this.error = new RuntimeException(message, cause);
    }

    public boolean noFailure() {
        return error == null;
    }

    public boolean failedEarlier() {
        return !noFailure();
    }

    public void markPublished() {
        published = true;
    }

    public ApiSpecificationDocument localSpec() {
        return localSpec;
    }

    public OpenApiSpecification remoteSpec() {
        return remoteSpec;
    }

    public String html() {
        return html;
    }

    public void setEligible(final boolean eligible) {
        this.eligible = eligible;
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

    public ApiSpecificationImportMetadata.Item localMetadata() {
        return localSpecMetadataItem;
    }

    public String specJcrId() {
        return localSpecMetadataItem.apiSpecJcrId();
    }
}
