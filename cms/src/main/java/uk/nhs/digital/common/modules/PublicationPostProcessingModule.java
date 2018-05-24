package uk.nhs.digital.common.modules;

import org.apache.commons.lang.*;
import org.hippoecm.hst.rest.*;
import org.onehippo.cms7.services.*;
import org.onehippo.cms7.services.eventbus.*;
import org.onehippo.forge.content.exim.core.impl.*;
import org.onehippo.repository.modules.*;

import javax.jcr.*;

public class PublicationPostProcessingModule extends AbstractReconfigurableDaemonModule {

    private FriendlyUrlManagerService friendlyUrlManagerService;

    private final Object configurationLock = new Object();

    @Override
    public void doInitialize(final Session session) throws RepositoryException {
        this.session = session;
        //registering the service during the startup
        HippoServiceRegistry.registerService(friendlyUrlManagerService, HippoEventBus.class);
    }

    @Override
    protected void doConfigure(final Node moduleConfig) throws RepositoryException {
        //configuring module during startup or during a property change
        synchronized (configurationLock) {
            Property restServiceProperty = moduleConfig.getProperty("restServiceUrl");
            if (restServiceProperty != null) {
                String restServiceUrl = StringUtils.defaultIfBlank(restServiceProperty.getString(), "");
                DocumentService documentService =
                    RestProxyServiceFactory.createRestProxy(DocumentService.class, moduleConfig.getSession(), restServiceUrl);
                //reloading the document service url
                getFriendlyUrlManagerService(moduleConfig.getSession()).setDocumentService(documentService);
            }
        }
    }

    @Override
    protected void doShutdown() {
        HippoServiceRegistry.unregisterService(friendlyUrlManagerService, HippoEventBus.class);
    }

    protected FriendlyUrlManagerService getFriendlyUrlManagerService(final Session session) {
        if (friendlyUrlManagerService == null) {
            //using the workflow document manager from exim to handle rewrite rules modification
            friendlyUrlManagerService =
                new FriendlyUrlManagerService(session, new WorkflowDocumentManagerImpl(session));
        }
        return friendlyUrlManagerService;
    }

}
