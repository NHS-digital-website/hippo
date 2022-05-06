package uk.nhs.digital.ps.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.LegacyPublication;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.PublicationPage;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.ps.beans.navigation.CiBreadcrumb;
import uk.nhs.digital.ps.beans.navigation.CiBreadcrumbProvider;

import java.util.Arrays;
import java.util.List;

public class CiBreadcrumbComponent extends BaseHstComponent {
    private final List<Class<?>> publicationTypes = Arrays.asList(
        Publication.class,
        PublicationPage.class,
        LegacyPublication.class,
        Series.class,
        Dataset.class
    );

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        CiBreadcrumbProvider ciBreadcrumbProvider = new CiBreadcrumbProvider(request);
        CiBreadcrumb ciBreadcrumb = ciBreadcrumbProvider.getBreadcrumb();

        request.setAttribute("ciBreadcrumb", ciBreadcrumb);

        request.setAttribute("isStatisticalPublication", isStatisticalPublication(request));
    }

    private boolean isStatisticalPublication(HstRequest request) {
        if (!request.getRequestContext().getContentBean().getPath().startsWith("/content/documents/corporate-website/publication-system/statistical/")) {
            return false;
        }

        HippoBean currentDocumentBean = request.getRequestContext().getContentBean();

        for (Class<?> publicationType : publicationTypes) {
            if (publicationType.isInstance(currentDocumentBean)) {
                return true;
            }
        }
        return false;
    }
}
