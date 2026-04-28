package uk.nhs.digital.common.listeners;

import org.onehippo.cms7.services.eventbus.HippoEventListenerRegistry;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.earlyaccesskey.ProcessSearch;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

/**
 * Listener for pre release key document types.
 */
public class PreReleaseKeyListener implements DaemonModule {

    private static final Logger log = LoggerFactory.getLogger(PreReleaseKeyListener.class);

    private Session session;
    private ProcessSearch processSearch = new ProcessSearch();

    @Subscribe
    public void handleEvent(HippoWorkflowEvent event) {
        if (event.success() && "publish".equals(event.get("methodName"))) {
            Session impersonatedSession = null;
            try {
                impersonatedSession = session.impersonate(
                    new SimpleCredentials(session.getUserID(), new char[0])
                );
                processSearch.processPreReleaseContentSearch(event, impersonatedSession);
            } catch (RepositoryException e) {
                log.warn("Failed to impersonate session for pre-release key listener.", e);
            } catch (RuntimeException e) {
                log.warn("Failed to process pre-release key event.", e);
            } finally {
                if (impersonatedSession != null) {
                    impersonatedSession.logout();
                }
            }
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
