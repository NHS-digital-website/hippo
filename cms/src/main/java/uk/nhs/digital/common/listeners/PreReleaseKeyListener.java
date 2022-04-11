package uk.nhs.digital.common.listeners;

import org.onehippo.cms7.services.eventbus.HippoEventListenerRegistry;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;
import uk.nhs.digital.common.earlyaccesskey.ProcessSearch;

import javax.jcr.Session;

/**
 * Listener for pre release key document types.
 */
public class PreReleaseKeyListener implements DaemonModule {

    private Session session;
    private ProcessSearch processSearch = new ProcessSearch();

    @Subscribe
    public void handleEvent(HippoWorkflowEvent event) {
        if (event.success() && event.get("methodName").equals("publish")) {
            processSearch.processPreReleaseContentSearch(event, session);
        }
    }

    @Override
    public void initialize(Session session) {
        this.session = session;
        HippoEventListenerRegistry.get().register(this);
    }

    @Override
    public void shutdown() {
        HippoEventListenerRegistry.get().unregister(this);
    }

}
