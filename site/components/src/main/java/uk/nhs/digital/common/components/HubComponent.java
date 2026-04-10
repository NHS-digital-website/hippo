package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.Hub;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HubComponent extends DocumentChildComponent {

    private static final Logger log = LoggerFactory.getLogger(HubComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        HstRequestContext requestContext = request.getRequestContext();
        Hub hubDocument = requestContext.getContentBean(Hub.class);
        if (hubDocument == null) {
            log.warn("HubComponent invoked without a Hub content bean at {}",
                requestContext.getResolvedSiteMapItem() != null
                    ? requestContext.getResolvedSiteMapItem().getPathInfo()
                    : "unknown path");
            return;
        }

        //creating a map where key is the component and value is a list of its children
        Map<HippoBean, List<HippoDocumentBean>> componentsMap = new LinkedHashMap<>();
        List<HippoBean> componentlist = hubDocument.getComponentlist();
        if (componentlist != null) {
            for (HippoBean component : componentlist) {
                componentsMap.put(component, getRelatedDocuments(component));
            }
        }
        request.setAttribute("components", componentsMap);
    }
}
