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

@SuppressWarnings("PMD.TooManyStaticImports")
public class PublicationAccessibilityUpdateJob implements RepositoryJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationAccessibilityUpdateJob.class);

    @Override
    public void execute(RepositoryJobExecutionContext context) throws RepositoryException {
        LOGGER.info("Starting the publication Accessibility Update Job");

        Session session = context.createSystemSession();
        DocumentManager documentManager = new WorkflowDocumentManagerImpl(session);

        try {
            // Query for relevant publications
            String xpath = "/jcr:root/content/documents/corporate-website/publication-system"
                + "//*[@jcr:primaryType='publicationsystem:publication' "
                    + " and (@publicationsystem:PubliclyAccessible='false')"
                + " and (hippostd:state = 'published')]";
            Query query = session.getWorkspace().getQueryManager().createQuery(xpath, Query.XPATH);

            NodeIterator nodeItr = query.execute().getNodes();

            // Count how many nodes we found
            long totalFound = nodeItr.getSize();
            LOGGER.info("Found {} publication nodes matching the query.", totalFound == -1 ? "an unknown number of" : totalFound);

            // Loop through the nodes
            while (nodeItr.hasNext()) {
                Node pubNode = nodeItr.nextNode();
                LOGGER.debug("Checking publication node at path: {}", pubNode.getPath());

                if (!pubNode.hasProperty("publicationsystem:NominalDate")) {
                    LOGGER.warn("Skipping node {} as it has no publicationsystem:NominalDate property.", pubNode.getPath());
                    continue;
                }

                Calendar nominalDate = pubNode.getProperty("publicationsystem:NominalDate").getDate();
                boolean hasReleaseTimePassed = hasReleaseTimePassed(nominalDate);

                LOGGER.debug("NominalDate={}. asReleaseTimePassed={}",
                    nominalDate.getTime(), hasReleaseTimePassed);

                // If the publication is for today and we've passed the release time
                if (hasReleaseTimePassed) {
                    LOGGER.info("Updating accessibility for publication node: {}", pubNode.getPath());

                    // Acquire an editable instance of the document
                    Node documentNode = documentManager.obtainEditableDocument(pubNode.getParent().getPath()).getNode(session);
                    JcrUtils.ensureIsCheckedOut(documentNode);
                    LOGGER.debug(" Updating the Accessibility for  " + pubNode.getPath());

                    // Update properties
                    documentNode.setProperty("publicationsystem:PubliclyAccessible", true);
                    documentNode.setProperty(PublicationSystemConstants.PROPERTY_EARLY_ACCESS_KEY, "");
                    documentNode.setProperty("hippostdpubwf:lastModificationDate", Calendar.getInstance());

                    // Persist changes
                    session.save();

                    // Attempt publish
                    try {
                        documentManager.commitEditableDocument(pubNode.getParent().getPath());
                        documentManager.publishDocument(pubNode.getParent().getPath());
                        LOGGER.info("Successfully published node: {}", pubNode.getPath());
                    } catch (Exception publishEx) {
                        LOGGER.error("Failed to publish node {} due to: {}", pubNode.getPath(), publishEx.getMessage(), publishEx);
                    }
                } else {
                    LOGGER.debug("No update required for node {}. Either not today or before the release time.", pubNode.getPath());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception while running PublicationAccessibilityUpdateJob: {}", e.getMessage(), e);
        } finally {
            LOGGER.info("PublicationAccessibilityUpdateJob finishing, logging out session.");
            session.logout();
        }
    }

    private boolean hasReleaseTimePassed(Calendar publicationDate) {
        // “Nominal date’s 09:30” in London time
        LocalDateTime nominalReleaseTime = publicationDate
            .toInstant()
            .atZone(LONDON_ZONE_ID)
            .toLocalDateTime()
            .withHour(HOUR_OF_PUBLICATION_RELEASE)   // 9
            .withMinute(MINUTE_OF_PUBLICATION_RELEASE)  // 30
            .withSecond(0);

        // true if we've passed that date and 09:30
        return LocalDateTime.now(LONDON_ZONE_ID).isAfter(nominalReleaseTime);
    }
}
