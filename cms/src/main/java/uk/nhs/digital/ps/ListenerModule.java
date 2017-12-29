package uk.nhs.digital.ps;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.eventbus.HippoEventBus;
import org.onehippo.repository.modules.DaemonModule;

public class ListenerModule implements DaemonModule {

    private AuditEventListener listener;

    @Override
    public void initialize(Session session) throws RepositoryException {
        listener = new AuditEventListener();
        HippoServiceRegistry.registerService(listener, HippoEventBus.class);
    }

    @Override
    public void shutdown() {
        HippoServiceRegistry.unregisterService(listener, HippoEventBus.class);
    }

}
