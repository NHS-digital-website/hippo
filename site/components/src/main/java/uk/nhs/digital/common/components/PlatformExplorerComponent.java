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
import uk.nhs.digital.platformexplorer.response.Item;

import java.util.List;

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
            final Resource resource = resourceServiceBroker.resolve(PLATFORM_RESOURCE_RESOLVER, ALTO_POC_VIEW_REQUEST_PATH);
            getAllCapabilities(request, resourceServiceBroker, resource);
        } catch (Exception exception) {
            log.error("Exception while fetching alto data", exception);
        }
    }

    private void getAllCapabilities(HstRequest request, ResourceServiceBroker resourceServiceBroker, Resource resource) {
        final ResourceBeanMapper resourceBeanMapper = resourceServiceBroker.getResourceBeanMapper(PLATFORM_RESOURCE_RESOLVER);
        AaltoView aaltoPayload = resourceBeanMapper.map(resource, AaltoView.class);
        final List<Item> capabilities = aaltoPayload.getItems();
        log.info(capabilities.toString());
        request.setAttribute("capabilities", capabilities);
    }

}
