package uk.nhs.digital.ps.components;

import static uk.nhs.digital.ps.components.HippoComponentHelper.*;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Archive;
import uk.nhs.digital.ps.beans.LegacyPublication;
import uk.nhs.digital.ps.beans.Publication;

import java.util.List;

public class ArchiveComponent extends EssentialsContentComponent {

    private static final Logger log = LoggerFactory.getLogger(ArchiveComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);

        final HstRequestContext requestContext = request.getRequestContext();
        final HippoBean contentBean = requestContext.getContentBean();

        final Archive archiveIndexDocument;
        if (contentBean.isHippoFolderBean()) {
            final List<Archive> archiveIndexDocuments = contentBean.getChildBeans(Archive.class);
            if (archiveIndexDocuments.size() != 1) {
                reportInvalidTarget(request, contentBean, archiveIndexDocuments.size());
                return;
            }

            archiveIndexDocument = archiveIndexDocuments.get(0);
        } else if (contentBean instanceof Archive) {
            archiveIndexDocument = (Archive) contentBean;
        } else {
            reportInvalidInvocation(request, contentBean);
            return;
        }

        request.setAttribute("archive", archiveIndexDocument);

        try {
            final HstQuery query = requestContext.getQueryManager().createQuery(archiveIndexDocument.getParentBean(), Publication.class, LegacyPublication.class);

            query.addOrderByDescending("publicationsystem:NominalDate");

            final HstQueryResult hstQueryResult = query.execute();

            request.setAttribute("publications", hstQueryResult.getHippoBeans());

        } catch (QueryException queryException) {
            log.error("Failed to find publications for archive " + archiveIndexDocument.getTitle(), queryException);

            reportDisplayError(request, archiveIndexDocument.getTitle());
        }
    }
}
