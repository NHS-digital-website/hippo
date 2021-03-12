package uk.nhs.digital.apispecs.model;

import static java.util.stream.Collectors.toSet;

import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.Set;

public class SyncResults {

    private final Set<SyncResults.SyncResult> results = new HashSet<>();

    static SyncResults.SyncResult from(final SpecificationSyncData specificationSyncData) {
        return new SyncResults.SyncResult(
            specificationSyncData.localSpec().getId(),
            specificationSyncData.localSpec().path(),
            specificationSyncData.error(),
            specificationSyncData.specContentChanged(),
            specificationSyncData.published());
    }

    public void accumulate(final SpecificationSyncData specificationSyncData) {
        results.add(from(specificationSyncData));
    }

    public SyncResults combine(final SyncResults syncResults) {
        results.addAll(syncResults.results());

        return this;
    }

    public Set<SyncResults.SyncResult> eligible() {
        return results.stream().filter(SyncResults.SyncResult::eligible).collect(toSet());
    }

    public Set<SyncResults.SyncResult> published() {
        return results.stream().filter(SyncResults.SyncResult::published).collect(toSet());
    }

    public Set<SyncResults.SyncResult> failed() {
        return results.stream().filter(SyncResults.SyncResult::failed).collect(toSet());
    }

    private Set<SyncResults.SyncResult> results() {
        return ImmutableSet.copyOf(results);
    }

    public static class SyncResult {
        private final String id;
        private final String localSpecPath;
        private final Exception error;
        private final boolean eligible;
        private final boolean published;

        private SyncResult(
            final String id,
            final String localSpecPath,
            final Exception error,
            final boolean eligible,
            final boolean published
        ) {
            this.id = id;
            this.localSpecPath = localSpecPath;
            this.error = error;
            this.eligible = eligible;
            this.published = published;
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
    }
}
