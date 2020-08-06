package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;

public class PlatformExploreComponent extends CommonComponent {

    public static final String PLATFORM_RESOURCE_RESOLVER = "platformResourceResolver";

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        //EssentialsBannerComponentInfo componentInfo = getComponentParametersInfo(request);
        // HippoBean document = getHippoBeanForPath(componentInfo.getDocument(), HippoBean.class);

        //ResourceServiceBroker broker =  CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        //Resource r = broker.resolve(PLATFORM_RESOURCE_RESOLVER, "/Website%20Collection/Website%20Dev%20POC?
        //$expand=Items(%24expand%3DRelatedItems(%24levels%3D1%3B%20%24expand%3DAttributes))");
        //return broker.getResourceBeanMapper("platformResourceResolver").map(r, "A");

        //request.setAttribute("document", document);
        request.setAttribute("hello", "Hello World");

    }

}
