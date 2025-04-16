package uk.nhs.digital.ps;

import static uk.nhs.digital.ps.PublicationSystemConstants.HOUR_OF_PUBLICATION_RELEASE;
import static uk.nhs.digital.ps.PublicationSystemConstants.LONDON_ZONE_ID;
import static uk.nhs.digital.ps.PublicationSystemConstants.MINUTE_OF_PUBLICATION_RELEASE;

import org.hippoecm.repository.util.JcrUtils;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Calendar;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;

/**
 * A scheduled job that updates 'publication' documents in the repository
 * so that they become publicly accessible once their nominal release date/time (09:30) has passed.
 *
 * <p>It runs as a repository (system) job, finds all 'publicationsystem:publication'
 * nodes that are:
 * <ul>
 *   <li>publiclyAccessible = false</li>
 *   <li>published (hippostd:state = 'published')</li>
 * </ul>
 * and checks if their NominalDate is past today's 09:30. If so, sets them to publicly accessible
 * and publishes them (via the Bloomreach workflow).
 */
@SuppressWarnings("PMD.TooManyStaticImports")
public class PublicationAccessibilityUpdateJob implements RepositoryJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationAccessibilityUpdateJob.class);

    /**
     * Called by the Bloomreach repository scheduler. Creates a system session,
     * queries for relevant documents, updates each one if needed, and publishes it
     * through the workflow when the nominal release time has passed.
     */
    @Override
    public void execute(RepositoryJobExecutionContext context) throws RepositoryException {
        LOGGER.info("Starting the publication Accessibility Update Job");

        // Create a system session for making repository changes
        Session session = context.createSystemSession();
        DocumentManager documentManager = new WorkflowDocumentManagerImpl(session);

        try {
            // Query to find all publications that are not yet publicly accessible, but are published
            String xpath = "/jcr:root/content/documents/corporate-website/publication-system"
                + "//*[@jcr:primaryType='publicationsystem:publication' "
                    + " and (@publicationsystem:PubliclyAccessible='false')"
                + " and (hippostd:state = 'published')]";

            Query query = session.getWorkspace().getQueryManager().createQuery(xpath, Query.XPATH);
            NodeIterator nodeItr = query.execute().getNodes();

            // Count how many nodes we found
            long totalFound = nodeItr.getSize();
            LOGGER.info("Found {} publication nodes matching the query.",
                totalFound == -1 ? "an unknown number of" : totalFound);

            // Process each publication node
            while (nodeItr.hasNext()) {
                Node pubNode = nodeItr.nextNode();
                LOGGER.debug("Checking publication node at path: {}", pubNode.getPath());

                // Ensure it has the NominalDate property
                if (!pubNode.hasProperty("publicationsystem:NominalDate")) {
                    LOGGER.warn("Skipping node {} as it has no publicationsystem:NominalDate property.", pubNode.getPath());
                    continue;
                }

                Calendar nominalDate = pubNode.getProperty("publicationsystem:NominalDate").getDate();
                boolean hasReleaseTimePassed = hasReleaseTimePassed(nominalDate);

                LOGGER.debug("NominalDate = {}. hasReleaseTimePassed = {}",
                    nominalDate.getTime(), hasReleaseTimePassed);

                // If the release time for this nominal date has passed, we make it publicly accessible
                if (hasReleaseTimePassed) {
                    LOGGER.info("Updating accessibility for publication node: {}", pubNode.getPath());

                    try {
                        // Obtain an editable document handle (preview variant) via the Bloomreach workflow
                        Node documentNode = documentManager.obtainEditableDocument(pubNode.getParent().getPath()).getNode(session);

                        // Ensure the node is checked out to allow property changes
                        JcrUtils.ensureIsCheckedOut(documentNode);
                        LOGGER.debug("Marking publication as publicly accessible at: {}", pubNode.getPath());

                        // Update properties to reflect the new publicly accessible state
                        documentNode.setProperty("publicationsystem:PubliclyAccessible", true);
                        documentNode.setProperty(PublicationSystemConstants.PROPERTY_EARLY_ACCESS_KEY, "");
                        documentNode.setProperty("hippostdpubwf:lastModificationDate", Calendar.getInstance());

                        // Save changes to the repository
                        session.save();

                        // Publish the document changes via Bloomreach workflow
                        documentManager.commitEditableDocument(pubNode.getParent().getPath());
                        documentManager.publishDocument(pubNode.getParent().getPath());
                        LOGGER.info("Successfully published node: {}", pubNode.getPath());

                    } catch (Exception publishEx) {
                        LOGGER.error("Failed to publish node {} due to: {}",
                            pubNode.getPath(), publishEx.getMessage(), publishEx);
                    }
                } else {
                    LOGGER.debug("No update required for node {}. Either the date is in the future or it's before release time.",
                        pubNode.getPath());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while running PublicationAccessibilityUpdateJob: {}", e.getMessage(), e);
        } finally {
            // Always close the session
            LOGGER.info("PublicationAccessibilityUpdateJob finishing, logging out session.");
            session.logout();
        }
    }

    /**
     * Determines if we have passed 9:30 on the given nominal date.
     * If 'now' in London is after nominalDate's 09:30, return true.
     */
    boolean hasReleaseTimePassed(Calendar publicationDate) {
        // Convert the nominal date to LocalDateTime at 9:30 (London time)
        LocalDateTime nominalReleaseTime = publicationDate
            .toInstant()
            .atZone(LONDON_ZONE_ID)
            .toLocalDateTime()
            .withHour(HOUR_OF_PUBLICATION_RELEASE)   // e.g. 9
            .withMinute(MINUTE_OF_PUBLICATION_RELEASE) // e.g. 30
            .withSecond(0);

        // Compare to the current time in London
        return LocalDateTime.now(LONDON_ZONE_ID).isAfter(nominalReleaseTime);
    }
}
