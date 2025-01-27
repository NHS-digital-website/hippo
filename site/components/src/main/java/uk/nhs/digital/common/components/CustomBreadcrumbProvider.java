package uk.nhs.digital.common.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.facetnavigation.AbstractHippoFacetChildNavigation;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.onehippo.forge.breadcrumb.components.BreadcrumbProvider;
import org.onehippo.forge.breadcrumb.om.BreadcrumbItem;

import java.util.List;

public class CustomBreadcrumbProvider  extends BreadcrumbProvider {

    public CustomBreadcrumbProvider(BaseHstComponent component) {
        super(component);
    }

    public CustomBreadcrumbProvider(BaseHstComponent component, boolean addTrailingDocumentOnly) {
        super(component, addTrailingDocumentOnly);
    }

    @Override
    protected void addURLBasedParentItems(final List<BreadcrumbItem> items, final HippoBean currentBean,
                                          final ResolvedSiteMapItem currentSmi,
                                          final ResolvedSiteMapItem deepestExpandedmenuItemSmi,
                                          final HstRequest request) {
        final String ancestorPath = deepestExpandedmenuItemSmi.getPathInfo();
        final String currentPath = currentSmi.getPathInfo();

        if (currentPath.startsWith(ancestorPath)) {
            String trailingPath = currentPath.substring(ancestorPath.length());

            if (trailingPath.startsWith("/")) {
                trailingPath = trailingPath.substring(1);
            }

            if (trailingPath.endsWith("/_index_")) {
                trailingPath = trailingPath.substring(0,trailingPath.indexOf("/_index_"));
            }

            int steps = trailingPath.split("/").length;

            HippoBean currentItemBean = currentBean;
            for (int i = 0; i < steps; i++) {
                addBreadcrumbItem(items, currentItemBean, request);
                currentItemBean = currentItemBean.getParentBean();
            }
        }
    }

    @Override
    protected void addAncestorBasedParentItems(final List<BreadcrumbItem> items, final HippoBean currentBean,
                                               final HippoBean ancestorBean, final HstRequest request) {

        HippoBean currentItemBean = currentBean;
        while (!currentItemBean.isSelf(ancestorBean)) {
            addBreadcrumbItem(items, currentItemBean, request);
            currentItemBean = currentItemBean.getParentBean();
        }
    }

    @Override
    protected void addContentBasedItems(final List<BreadcrumbItem> items, HippoBean currentBean,
                                        final ResolvedSiteMapItem currentSmi,
                                        final HstRequest request) {

        final HippoBean siteContentBean = request.getRequestContext().getSiteContentBaseBean();

        HippoBean bean = currentBean;

        // go up to until site content base bean
        while (!bean.isSelf(siteContentBean)) {
            addBreadcrumbItem(items, bean, request);
            bean = bean.getParentBean();
        }
    }

    private void addBreadcrumbItem(final List<BreadcrumbItem> items, final HippoBean currentBean,
                                   final HstRequest request) {
        //ignore any child facets, we only want to show the root facet
        if (!(currentBean instanceof AbstractHippoFacetChildNavigation)) {
            final BreadcrumbItem item = getBreadcrumbItem(request, currentBean);
            if (item != null && item.getLink() != null && !item.getLink().isNotFound()) {
                items.add(item);
            }
        }
    }

}