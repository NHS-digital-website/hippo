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
import uk.nhs.digital.platformexplorer.response.AltoDataType;
import uk.nhs.digital.platformexplorer.response.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CapabilitiesComponent
    extends CommonComponent {

    private static Logger log = LoggerFactory.getLogger( CapabilitiesComponent.class );

    public static final String HEADING_TEXT = "headingText";

    public static final String ITEMS_REQUEST_KEY = "items";

    public static final String PLATFORM_RESOURCE_RESOLVER = "platformResourceResolver";

    public static final String EXPAND_ITEMS_PARAM = "$filter=Library eq 'Published Library' AND Type eq 'Capability'";

    public static final String CAPABILITIES_VIEW_REQUEST_PATH = "/api/item?" + EXPAND_ITEMS_PARAM;

    public static final String ITEM_REQUEST_PATH = "/api/item/";

    public static final String EXPAND_ATTRIBUTES_PARAM = "$expand=RelatedItems($expand=Attributes)";

    public static final Comparator<Item> ITEM_COMPARATOR = Comparator.comparing(Item::getName);


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
                final Resource capabilitiesResource = resourceServiceBroker.findResources( PLATFORM_RESOURCE_RESOLVER,
                                                                         CAPABILITIES_VIEW_REQUEST_PATH );
                getCapabilities( request, resourceServiceBroker, capabilitiesResource );
            }

        } catch (Exception exception) {
            log.error(((ResourceException) exception).getCause().toString());
            log.error("Exception while fetching alto data for capabilities", exception);
        }
    }

    public void getItemById(HstRequest request, ResourceServiceBroker resourceServiceBroker, Resource resource) {
        log.info("getItemById Start");
        final ResourceBeanMapper resourceBeanMapper = getResourceBeanMapper( resourceServiceBroker );
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
        log.info("getItemById End");

    }


    public void getCapabilities( HstRequest request, ResourceServiceBroker resourceServiceBroker, Resource capabilitiesResource ) {
        log.info("getCapabilities Start");
        final ResourceBeanMapper resourceBeanMapper = getResourceBeanMapper( resourceServiceBroker );
        final Collection<Item> itemCollection =
            resourceBeanMapper.mapCollection( capabilitiesResource.getChildren( ),
                                              Item.class );

        log.info(itemCollection.toString()  );
        ArrayList<Item> items = new ArrayList<>( itemCollection);
        Collections.sort(items, ITEM_COMPARATOR);
        request.setAttribute(HEADING_TEXT, itemCollection.stream().findAny().get().getType());
        request.setAttribute(ITEMS_REQUEST_KEY, itemCollection);
        log.info("getCapabilities End");
    }


    private ResourceBeanMapper getResourceBeanMapper( final ResourceServiceBroker resourceServiceBroker ) {
        return resourceServiceBroker.getResourceBeanMapper( PLATFORM_RESOURCE_RESOLVER );
    }

}
