package uk.nhs.digital.ps.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.Publication;

import java.io.IOException;

public class DatasetComponent extends BaseHstComponent {

    private static final Logger log = LoggerFactory.getLogger(PublicationComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final HstRequestContext ctx = request.getRequestContext();
        Dataset dataset = (Dataset) ctx.getContentBean();
        Publication publication = getParentPublication(ctx, dataset);

        if (!publication.isPubliclyAccessible()) {
            try {
                response.forward("/error/404");
            } catch (IOException e) {
                throw new HstComponentException("forward failed", e);
            }

            return;
        }

        request.setAttribute("dataset", dataset);
        request.setAttribute("parentPublication", publication);

        if (publication == null) {
            log.warn("Cannot find parent publication for Dataset document {}", dataset.getPath());
        }
    }

    private Publication getParentPublication(HstRequestContext ctx, Dataset dataset) {
        Publication publicationBean = null;

        HippoFolder folder = (HippoFolder) dataset.getParentBean();
        while (!isRootFolder(folder, ctx)) {
            publicationBean = Publication.getPublicationInFolder(folder);

            if (publicationBean != null) {
                break;
            } else {
                folder = (HippoFolder) folder.getParentBean();
            }
        }

        return publicationBean;
    }

    private boolean isRootFolder(HippoFolder folder, HstRequestContext ctx) {
        return folder.isSelf(ctx.getSiteContentBaseBean());
    }
}
