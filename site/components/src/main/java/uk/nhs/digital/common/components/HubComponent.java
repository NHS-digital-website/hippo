package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import uk.nhs.digital.website.beans.*;

import java.util.*;

public class HubComponent extends DocumentChildComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        Hub hubDocument = request.getRequestContext().getContentBean(Hub.class);
        //creating a map where key is the component and value is a list of its children
        Map<HippoBean, List<HippoDocumentBean>> componentsMap = new LinkedHashMap<>();
        for (HippoBean component : hubDocument.getComponentlist()) {
            componentsMap.put(component, getRelatedDocuments(component));
        }
        request.setAttribute("components", componentsMap);
    }
}
