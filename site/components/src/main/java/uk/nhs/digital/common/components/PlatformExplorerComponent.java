package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceBeanMapper;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.platformexplorer.response.AaltoView;

public class PlatformExplorerComponent extends CommonComponent {
    private static Logger log = LoggerFactory.getLogger(PlatformExplorerComponent.class);
    public static final String PLATFORM_RESOURCE_RESOLVER = "platformResourceResolver";
    public static final String EXPAND_ITEMS_PARAM = "$expand=Items($expand=RelatedItems($levels=1;$expand=Attributes))";
    public static final String ALTO_POC_VIEW_REQUEST_PATH = "/api/views/Website Collection/Website Dev POC?" + EXPAND_ITEMS_PARAM;

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        try {
            ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
            log.info(resourceServiceBroker.toString());
            final Resource resource = resourceServiceBroker.resolve(PLATFORM_RESOURCE_RESOLVER, ALTO_POC_VIEW_REQUEST_PATH);
            log.info(resource.toString());
            final ResourceBeanMapper resourceBeanMapper = resourceServiceBroker.getResourceBeanMapper(PLATFORM_RESOURCE_RESOLVER);
            log.info(resourceBeanMapper.toString());
            AaltoView aaltoPayload = resourceBeanMapper.map(resource, AaltoView.class);
            request.setAttribute("aaltoPayload", aaltoPayload);
        } catch (Exception exception) {
            log.error("Exception while fetching alto data", exception);
        }

        request.setAttribute("hello", "Hello World");

    }

}
