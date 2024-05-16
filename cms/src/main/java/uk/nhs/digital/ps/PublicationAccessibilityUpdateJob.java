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

import java.time.LocalDate;
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
        Session session = context.createSystemSession();
        DocumentManager documentManager = new WorkflowDocumentManagerImpl(session);
        LOGGER.info("Running the publication Accessibility Update Job");
        try {
            NodeIterator nodeItr = session
                .getWorkspace()
                .getQueryManager()
                .createQuery("/jcr:root/content/documents/corporate-website/publication-system//*[@jcr:primaryType='publicationsystem:publication' "
                    + " and (@publicationsystem:PubliclyAccessible='false')"
                    + " and ( hippostd:state  = 'published') ]", Query.XPATH)
                .execute()
                .getNodes();
            while (nodeItr.hasNext()) {
                Node pubNode = nodeItr.nextNode();

                Calendar nominalDate = pubNode.getProperty("publicationsystem:NominalDate").getDate();

                if (isTodayDate(nominalDate) && isPastNineThirty(nominalDate)) {
                    Node documentNode = documentManager.obtainEditableDocument(pubNode.getParent().getPath()).getNode(session);
                    JcrUtils.ensureIsCheckedOut(documentNode);
                    LOGGER.debug(" Updating the Accessibility for  " + pubNode.getPath());

                    documentNode.setProperty("publicationsystem:PubliclyAccessible", true);
                    documentNode.setProperty(PublicationSystemConstants.PROPERTY_EARLY_ACCESS_KEY, "");
                    documentNode.setProperty("hippostdpubwf:lastModificationDate", Calendar.getInstance());
                    session.save();

                    documentManager.commitEditableDocument(pubNode.getParent().getPath());
                    documentManager.publishDocument(pubNode.getParent().getPath());
                }
            }
        } catch (Exception e) {
            LOGGER.error(" Got exception whilst running Job {} ", e.getMessage());
        } finally {
            session.logout();
        }
    }

    public boolean isTodayDate(Calendar publicationDate) {
        LocalDate nominalDate = publicationDate.toInstant().atZone(LONDON_ZONE_ID).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return currentDate.isEqual(nominalDate);
    }


    private boolean isPastNineThirty(Calendar publicationDate) {
        LocalDateTime publicationDateTime = publicationDate.toInstant()
            .atZone(LONDON_ZONE_ID).toLocalDateTime()
            .withHour(HOUR_OF_PUBLICATION_RELEASE)
            .withMinute(MINUTE_OF_PUBLICATION_RELEASE)
            .withSecond(0);

        LocalDateTime currentDateTime = LocalDateTime.now(LONDON_ZONE_ID);

        return currentDateTime.isAfter(publicationDateTime);
    }
}
