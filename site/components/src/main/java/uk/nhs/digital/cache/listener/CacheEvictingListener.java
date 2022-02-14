package uk.nhs.digital.cache.listener;

import org.onehippo.cms7.event.HippoEvent;
import org.onehippo.cms7.event.HippoEventConstants;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.events.PersistedHippoEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.cache.Cache;

import java.util.Set;

public class CacheEvictingListener implements PersistedHippoEventListener {

    private final Logger log = LoggerFactory.getLogger(CacheEvictingListener.class);

    private final Cache<String, String> cache;
    private Set<String> eligibleDoctypes;

    public CacheEvictingListener(final Cache<String, String> cache, final Set<String> eligibleDoctypes) {
        this.cache = cache;
        this.eligibleDoctypes = eligibleDoctypes;
    }

    @Override public String getEventCategory() {
        return HippoEventConstants.CATEGORY_WORKFLOW;
    }

    @Override public String getChannelName() {
        return "cache-evicting-listener";
    }

    @Override public boolean onlyNewEvents() {
        return false;
    }

    @Override public void onHippoEvent(final HippoEvent event) {
        @SuppressWarnings("rawtypes") final HippoWorkflowEvent workflowEvent = new HippoWorkflowEvent(event);

        if (notEligible(workflowEvent)) {
            log.debug("Ignoring ineligible event '{}' for document {}", workflowEvent.action(), workflowEvent.subjectPath());
            return;
        }

        final String documentHandleUuid = workflowEvent.subjectId();
        final String documentPath = workflowEvent.subjectPath();

        log.info("Document has been published: evicting previous version from cache: {} ({})", documentPath, documentHandleUuid);
        cache.remove(documentHandleUuid);
    }

    @SuppressWarnings("rawtypes")
    private boolean notEligible(final HippoWorkflowEvent workflowEvent) {
        return notForEligibleDoctype(workflowEvent) || notPublicationEvent(workflowEvent) || publicationFailed(workflowEvent);
    }

    @SuppressWarnings("rawtypes")
    private boolean publicationFailed(final HippoWorkflowEvent workflowEvent) {
        return !workflowEvent.success();
    }

    @SuppressWarnings("rawtypes")
    private boolean notPublicationEvent(final HippoWorkflowEvent workflowEvent) {
        return !"publish".equals(workflowEvent.action());
    }

    @SuppressWarnings("rawtypes")
    private boolean notForEligibleDoctype(final HippoWorkflowEvent workflowEvent) {
        return !eligibleDoctypes.contains(workflowEvent.documentType());
    }
}
