package uk.nhs.digital.common.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
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
                BreadcrumbItem item = getBreadcrumbItem(request, currentItemBean);
                if (item != null) {
                    items.add(item);
                }
                currentItemBean = currentItemBean.getParentBean();
            }
        }
    }
}




