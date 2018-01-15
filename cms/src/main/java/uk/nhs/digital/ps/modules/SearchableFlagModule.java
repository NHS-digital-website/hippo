package uk.nhs.digital.ps.modules;

import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.eventbus.HippoEventBus;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.modules.searchableFlagModule.DatasetProcessor;
import uk.nhs.digital.ps.modules.searchableFlagModule.PublicationDepublishProcessor;
import uk.nhs.digital.ps.modules.searchableFlagModule.PublicationProcessor;

import java.util.Arrays;
import java.util.List;
import javax.jcr.*;

public class SearchableFlagModule implements DaemonModule {

    private static final Logger log = LoggerFactory.getLogger(SearchableFlagModule.class);

    private static final String ACTION_COMMIT = "commitEditableInstance";
    private static final String ACTION_PUBLISH = "publish";
    private static final String ACTION_DEPUBLISH = "depublish";
    private static final String STATE_PUBLISHED = "published";
    private static final String STATE_DRAFT = "draft";
    private static final String STATE_UNPUBLISHED = "unpublished";

    private Session session;
    private final PublicationProcessor publicationUpdater = new PublicationProcessor();
    private final DatasetProcessor datasetUpdater = new DatasetProcessor();
    private final PublicationDepublishProcessor publicationDepublishProcessor = new PublicationDepublishProcessor();


    @Override
    public void initialize(final Session session) throws RepositoryException {
        this.session = session;
        HippoServiceRegistry.registerService(this, HippoEventBus.class);
    }

    @Override
    public void shutdown() {
        HippoServiceRegistry.unregisterService(this, HippoEventBus.class);
    }

    @Subscribe
    public void handleEvent(final HippoWorkflowEvent event) {
        if (event.success() && ACTION_COMMIT.equals(event.action())) {
            updateSearchableFlag(event, Arrays.asList(STATE_UNPUBLISHED, STATE_DRAFT));
        }

        if (event.success() && ACTION_PUBLISH.equals(event.action())) {
            updateSearchableFlag(event, Arrays.asList(STATE_PUBLISHED));
        }

        if (event.success() && ACTION_DEPUBLISH.equals(event.action())) {
            forceSearchableFlagOnDataset(event, Arrays.asList(STATE_PUBLISHED));
        }
    }

    private void updateSearchableFlag(final HippoWorkflowEvent event, final List acceptedStates) {
        try {
            Node node = session.getNode(event.subjectPath());

            publicationUpdater.processNode(node, acceptedStates);
            datasetUpdater.processNode(node, acceptedStates);

            session.save();
        } catch (RepositoryException repositoryException) {
            log.error("Something's very wrong: unexpected exception while doing simple JCR read operations", repositoryException);
        }
    }

    private void forceSearchableFlagOnDataset(final HippoWorkflowEvent event, final List acceptedStates) {
        try {
            Node node = session.getNode(event.subjectPath());

            publicationDepublishProcessor.processNode(node, acceptedStates);

            session.save();
        } catch (RepositoryException repositoryException) {
            log.error("Something's very wrong: unexpected exception while doing simple JCR read operations", repositoryException);
        }
    }
}
