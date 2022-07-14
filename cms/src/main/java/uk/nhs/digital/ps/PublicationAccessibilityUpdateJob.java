package uk.nhs.digital.ps;

import static uk.nhs.digital.ps.PublicationSystemConstants.LONDON_ZONE_ID;

import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Calendar;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;

public class PublicationAccessibilityUpdateJob implements RepositoryJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationAccessibilityUpdateJob.class);

    @Override
    public void execute(RepositoryJobExecutionContext context) throws RepositoryException {
        Session session = context.createSystemSession();
        LOGGER.info("Running the publication Accessibility Update Job");

        try {
            NodeIterator nodeItr = session
                .getWorkspace()
                .getQueryManager()
                .createQuery("/jcr:root/content/documents/corporate-website/publication-system//*[@jcr:primaryType='publicationsystem:publication' "
                    + " and (@publicationsystem:PubliclyAccessible='false') and (hippostd:state='published') ]", Query.XPATH)
                .execute()
                .getNodes();
            while (nodeItr.hasNext()) {
                Node pubNode = nodeItr.nextNode();
                Calendar nominalDate = pubNode.getProperty("publicationsystem:NominalDate").getDate();
                if (isTodayDate(nominalDate)) {
                    LOGGER.debug(" Updating the Accessibility for  " + pubNode.getPath());
                    pubNode.setProperty("publicationsystem:PubliclyAccessible", true);
                    session.save();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.logout();
        }
    }

    public boolean isTodayDate(Calendar publicationDate) {
        LocalDate nominalDate = publicationDate.toInstant().atZone(LONDON_ZONE_ID).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return currentDate.isEqual(nominalDate);
    }
}
