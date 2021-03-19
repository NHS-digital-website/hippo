package uk.nhs.digital.apispecs.model;

import java.time.Instant;
import java.util.regex.Pattern;

public class SpecificationSyncData {

    private static final Pattern VERSION_FIELD_PATTERN = Pattern.compile("\"version\"\\s*:\\s*\"[^\"]+\"");

    private final ApiSpecificationDocument localSpec;
    private final OpenApiSpecification remoteSpec;
    private String html;
    private Exception error;
    private boolean published;
    private boolean eligible;
    private boolean skipped;

    private SpecificationSyncData(
        final ApiSpecificationDocument localSpec,
        final OpenApiSpecification remoteSpec
    ) {
        this.localSpec = localSpec;
        this.remoteSpec = remoteSpec;
    }

    public static SpecificationSyncData with(
        final ApiSpecificationDocument localSpec,
        final OpenApiSpecification remoteSpec
    ) {
        return new SpecificationSyncData(
            localSpec,
            remoteSpec
        );
    }

    private static boolean specContentDiffersIgnoringVersion(final String left, final String right) {

        // We're ignoring version field because it often is the only piece of spec's content that actually changes.
        // It is calculated from git tags which are incremented on each merge to master (of the API codebase)
        // and that incrementation often takes place as a result of the API proxy definition being updated,
        // with no change to the spec itself.

        final String leftSpecJsonNoVersion = VERSION_FIELD_PATTERN.matcher(left).replaceFirst("");
        final String rightSpecJsonNoVersion = VERSION_FIELD_PATTERN.matcher(right).replaceFirst("");

        return !leftSpecJsonNoVersion.equals(rightSpecJsonNoVersion);
    }

    public boolean specContentChanged() {

        return specReportedAsUpdated() && specContentDiffersIgnoringVersion(
            remoteSpec.getSpecJson().orElse(""),
            localSpec.json().orElse("")
        );

    }

    public boolean specReportedAsUpdated() {
        return remoteSpec.getModified().isAfter(localSpecLastCheckTime());
    }

    private Instant localSpecLastCheckTime() {

        // Once the spec is published for the very first time,
        // its 'published' variant won't have the 'last check time'
        // recorded yet, but the last publication time is a good
        // substitute in such instance and helps avoiding a redundant
        // call to Apigee in that scenario.

        return localSpec.lastChangeCheckInstant()
            .orElse(localSpec.lastPublicationInstant()
                .orElse(Instant.EPOCH));
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
}
