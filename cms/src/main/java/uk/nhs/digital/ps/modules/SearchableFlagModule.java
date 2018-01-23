package uk.nhs.digital.ps.modules;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import org.onehippo.repository.events.HippoWorkflowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.modules.searchableFlagModule.DatasetProcessor;
import uk.nhs.digital.ps.modules.searchableFlagModule.PublicationDepublishProcessor;
import uk.nhs.digital.ps.modules.searchableFlagModule.PublicationProcessor;

import java.util.List;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class SearchableFlagModule extends AbstractDaemonModule {

    private static final Logger log = LoggerFactory.getLogger(SearchableFlagModule.class);

    private final PublicationProcessor publicationUpdater = new PublicationProcessor();
    private final DatasetProcessor datasetUpdater = new DatasetProcessor();
    private final PublicationDepublishProcessor publicationDepublishProcessor = new PublicationDepublishProcessor();

    @Override
    protected void handleCommitEvent(HippoWorkflowEvent event) {
        updateSearchableFlag(event, asList(STATE_UNPUBLISHED, STATE_DRAFT));
    }

    @Override
    protected void handlePublishEvent(HippoWorkflowEvent event) {
        updateSearchableFlag(event, singletonList(STATE_PUBLISHED));
    }

    @Override
    protected void handleDepublishEvent(HippoWorkflowEvent event) {
        forceSearchableFlagOnDataset(event, singletonList(STATE_PUBLISHED));
    }

    private void updateSearchableFlag(final HippoWorkflowEvent event, final List<String> acceptedStates) {
        try {
            Node node = getSession().getNode(event.subjectPath());

            publicationUpdater.processNode(node, acceptedStates);
            datasetUpdater.processNode(node, acceptedStates);

            getSession().save();
        } catch (RepositoryException repositoryException) {
            log.error("Something's very wrong: unexpected exception while doing simple JCR read operations", repositoryException);
        }
    }

    private void forceSearchableFlagOnDataset(final HippoWorkflowEvent event, final List<String> acceptedStates) {
        try {
            Node node = getSession().getNode(event.subjectPath());

            publicationDepublishProcessor.processNode(node, acceptedStates);

            getSession().save();
        } catch (RepositoryException repositoryException) {
            log.error("Something's very wrong: unexpected exception while doing simple JCR read operations", repositoryException);
        }
    }
}
