package uk.nhs.digital.apispecs.model;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class SyncResults {

    private final List<SyncResult> results = new ArrayList<>();

    static SyncResults.SyncResult from(final SpecificationSyncData specificationSyncData) {
        return new SyncResults.SyncResult(
            specificationSyncData.localSpec().specificationId(),
            specificationSyncData.localSpec().path(),
            specificationSyncData.error(),
            specificationSyncData.eligible(),
            specificationSyncData.published(),
            specificationSyncData.skipped()
        );
    }

    public void accumulate(final SpecificationSyncData specificationSyncData) {
        results.add(from(specificationSyncData));
    }

    public SyncResults combine(final SyncResults syncResults) {
        results.addAll(syncResults.results());

        return this;
    }

    public List<SyncResult> eligible() {
        return results.stream().filter(SyncResults.SyncResult::eligible).collect(toList());
    }

    public List<SyncResult> published() {
        return results.stream().filter(SyncResults.SyncResult::published).collect(toList());
    }

    public List<SyncResult> failed() {
        return results.stream().filter(SyncResults.SyncResult::failed).collect(toList());
    }

    public List<SyncResult> skipped() {
        return results.stream().filter(SyncResult::skipped).collect(toList());
    }

    private List<SyncResults.SyncResult> results() {
        return ImmutableList.copyOf(results);
    }

    public static class SyncResult {
        private final String id;
        private final String localSpecPath;
        private final Exception error;
        private final boolean eligible;
        private final boolean published;
        private final boolean skipped;

        private SyncResult(
            final String id,
            final String localSpecPath,
            final Exception error,
            final boolean eligible,
            final boolean published,
            final boolean skipped) {
            this.id = id;
            this.localSpecPath = localSpecPath;
            this.error = error;
            this.eligible = eligible;
            this.published = published;
            this.skipped = skipped;
        }

        public String specId() {
            return id;
        }

        public String localSpecPath() {
            return localSpecPath;
        }

        public Exception error() {
            return error;
        }

        boolean failed() {
            return error != null;
        }

        boolean published() {
            return published;
        }

        private boolean eligible() {
            return eligible;
        }

        private boolean skipped() {
            return skipped;
        }
    }
}
