package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceBeanMapper;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.platformexplorer.response.AaltoView;
import uk.nhs.digital.platformexplorer.response.AltoDataType;
import uk.nhs.digital.platformexplorer.response.Item;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilityComponent
    extends CommonComponent {

    private static Logger log = LoggerFactory.getLogger( CapabilityComponent.class );

    public static final String HEADING_TEXT = "headingText";

    public static final String ITEMS_REQUEST_KEY = "items";

    public static final String PLATFORM_RESOURCE_RESOLVER = "platformResourceResolver";

    public static final String EXPAND_ITEMS_PARAM = "$expand=Items($expand=RelatedItems($levels=1;$expand=Attributes))";

    public static final String ALTO_POC_VIEW_REQUEST_PATH = "/api/views/Website Collection/Website Dev POC?" + EXPAND_ITEMS_PARAM;

    public static final String ITEM_REQUEST_PATH = "/api/item/";

    public static final String EXPAND_ATTRIBUTES_PARAM = "$expand=RelatedItems($expand=Attributes)";

    public static final Comparator<Item> ITEM_COMPARATOR
        = Comparator.comparing(Item::getName);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        try {
            ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
            final String id = getAnyParameter(request, "id");
            log.info("requested Item Id:" + id);
            if (id != null) {
                final Resource resource = resourceServiceBroker.resolve(PLATFORM_RESOURCE_RESOLVER, ITEM_REQUEST_PATH + id + "?" + EXPAND_ATTRIBUTES_PARAM);
                getItemById(request, resourceServiceBroker, resource);
            } else {
                final Resource resource = resourceServiceBroker.resolve(PLATFORM_RESOURCE_RESOLVER, ALTO_POC_VIEW_REQUEST_PATH);
                getAllRootItemsFromView(request, resourceServiceBroker, resource);
            }

        } catch (Exception exception) {
            log.error(((ResourceException) exception).getCause().toString());
            log.error("Exception while fetching alto data", exception);
        }
    }

    private void getItemById(HstRequest request, ResourceServiceBroker resourceServiceBroker, Resource resource) {
        log.info("Retrieving Item");
        final ResourceBeanMapper resourceBeanMapper = resourceServiceBroker.getResourceBeanMapper(PLATFORM_RESOURCE_RESOLVER);
        Item dataItem = resourceBeanMapper.map(resource, Item.class);
        String childType = AltoDataType.fromString(dataItem.getType()).getChildItemType();
        log.info("childType : " + childType);
        final List<Item> relatedItems = dataItem.getRelatedItems();
        relatedItems.stream().forEach(item -> log.info("item " + item.toString()));
        final List<Item> items = relatedItems.stream().filter(relatedItem -> relatedItem.getType().equals(childType)).collect(Collectors.toList());
        if (items != null && !items.isEmpty()) {
            Collections.sort(items, ITEM_COMPARATOR);
            request.setAttribute(HEADING_TEXT, items.get(0).getType());
            request.setAttribute(ITEMS_REQUEST_KEY, items);
            log.info("Successfully retrieved Item and set in the request");
        } else {
            request.setAttribute(HEADING_TEXT, "No Child");
            request.setAttribute(ITEMS_REQUEST_KEY, Collections.EMPTY_LIST);
            log.info("No Child data elements and set in the request");
        }

    }

    private void getAllRootItemsFromView(HstRequest request, ResourceServiceBroker resourceServiceBroker, Resource resource) {
        log.info("Retrieving all Capabilities");
        final ResourceBeanMapper resourceBeanMapper = resourceServiceBroker.getResourceBeanMapper(PLATFORM_RESOURCE_RESOLVER);
        AaltoView aaltoView = resourceBeanMapper.map(resource, AaltoView.class);
        final List<Item> items = aaltoView.getItems();
        Collections.sort(items, ITEM_COMPARATOR);
        request.setAttribute(HEADING_TEXT, items.stream().findAny().get().getType());
        request.setAttribute(ITEMS_REQUEST_KEY, items);
        log.info("Successfully retrieved and set in the request");
    }

}
