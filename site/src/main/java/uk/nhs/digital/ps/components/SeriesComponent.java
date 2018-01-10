package uk.nhs.digital.ps.components;

import static java.text.MessageFormat.format;

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
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;

import java.util.List;

public class SeriesComponent extends EssentialsContentComponent {

    private static final Logger log = LoggerFactory.getLogger(SeriesComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);

        final HstRequestContext requestContext = request.getRequestContext();
        final HippoBean contentBean = requestContext.getContentBean();

        if (!contentBean.isHippoFolderBean()) {
            reportInvalidInvocation(request, contentBean);
            return;
        }

        final List<Series> seriesIndexDocuments = contentBean.getChildBeans(Series.class);

        if (seriesIndexDocuments.size() != 1) {
            reportInvalidTarget(request, contentBean, seriesIndexDocuments.size());
            return;
        }

        final Series seriesIndexDocument = seriesIndexDocuments.get(0);

        request.setAttribute("series" , seriesIndexDocument);

        try {
            final HstQuery query = requestContext.getQueryManager().createQuery(contentBean, Publication.class);

            query.addOrderByDescending("publicationsystem:NominalDate");

            final HstQueryResult hstQueryResult = query.execute();

            request.setAttribute("publications", hstQueryResult.getHippoBeans());

        } catch (QueryException queryException) {
            log.error("Failed to find publications for series " + seriesIndexDocument.getTitle(), queryException);

            reportDisplayError(request, seriesIndexDocument.getTitle());
        }
    }

    private void reportInvalidTarget(final HstRequest request, final HippoBean contentBean, final int indexDocumentsCount) {
        log.error("Expected exactly one published publication series index documents but found {} in folder {}.",
            indexDocumentsCount, contentBean.getPath());

        reportDisplayError(request, contentBean.getDisplayName());
    }

    private void reportInvalidInvocation(final HstRequest request, final HippoBean contentBean) {
        log.error("{} invoked for a node ''{}'' that is not a folder. This is likely a result of the site map"
            + " being misconfigured. Node path: {}",
            getClass().getSimpleName(), contentBean.getDisplayName(), contentBean.getPath());

        reportDisplayError(request, contentBean.getDisplayName());
    }

    private void reportDisplayError(final HstRequest request, final String name) {
        request.setAttribute("error", format("Cannot display ''{0}''. The error has been logged.", name));
    }
}
