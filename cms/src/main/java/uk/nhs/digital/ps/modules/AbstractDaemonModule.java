package uk.nhs.digital.ps.modules;

import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.eventbus.HippoEventBus;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;

import javax.jcr.Session;

public abstract class AbstractDaemonModule implements DaemonModule {

    protected static final String STATE_PUBLISHED = "published";
    protected static final String STATE_DRAFT = "draft";
    protected static final String STATE_UNPUBLISHED = "unpublished";

    protected static final String DOCUMENT_WORKFLOW_NAME = "handle";
    protected static final String ACTION_COMMIT = "commitEditableInstance";
    protected static final String ACTION_PUBLISH = "publish";
    protected static final String ACTION_DEPUBLISH = "depublish";

    private Session session;

    @Override
    public void initialize(final Session session) {
        this.session = session;
        HippoServiceRegistry.registerService(this, HippoEventBus.class);
    }

    @Override
    public void shutdown() {
        HippoServiceRegistry.unregisterService(this, HippoEventBus.class);
    }

    @Subscribe
    public final void handleEvent(final HippoWorkflowEvent event) {
        if (event.success() && event.workflowName().equals(DOCUMENT_WORKFLOW_NAME)) {

            switch (event.action()) {
                case ACTION_COMMIT:
                    handleCommitEvent(event);
                    break;
                case ACTION_PUBLISH:
                    handlePublishEvent(event);
                    break;
                case ACTION_DEPUBLISH:
                    handleDepublishEvent(event);
                    break;
                default:
                    // ignore the event
            }
        }
    }

    public Session getSession() {
        return session;
    }

    /**
     * These are overridden when we are interested in each event
     */
    protected void handleCommitEvent(HippoWorkflowEvent event) {}

    protected void handlePublishEvent(HippoWorkflowEvent event) {}

    protected void handleDepublishEvent(HippoWorkflowEvent event) {}
}
