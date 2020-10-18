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
import uk.nhs.digital.platformexplorer.response.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class PrinciplesComponent extends CommonComponent {

    private static Logger log = LoggerFactory.getLogger( CapabilitiesComponent.class );

    public static final String HEADING_TEXT = "headingText";

    public static final String ITEMS_REQUEST_KEY = "items";

    public static final String PLATFORM_RESOURCE_RESOLVER = "platformResourceResolver";

    public static final String EXPAND_ATTRIBUTES_PARAM = "&$expand=Attributes($select=Implication,Rationale)";

    public static final String EXPAND_ITEMS_PARAM = "$filter=Library eq 'Published Library' AND Type eq 'Principle'";

    public static final String PRINCIPLES_VIEW_REQUEST_PATH = "/api/item?" + EXPAND_ITEMS_PARAM + EXPAND_ATTRIBUTES_PARAM;;



    public static final Comparator<Item> ITEM_COMPARATOR = Comparator.comparing(Item::getName);
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        try {
            ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker( HstServices.getComponentManager( ) );
            final Resource principlesResource = resourceServiceBroker.findResources( PLATFORM_RESOURCE_RESOLVER,
                                                                                     PRINCIPLES_VIEW_REQUEST_PATH );
            getPrinciples( request, resourceServiceBroker, principlesResource );

        } catch (Exception exception) {
            log.error((( ResourceException ) exception).getCause( ).toString( ) );
            log.error("Exception while fetching alto data for principles", exception);
        }
    }


    public void getPrinciples( HstRequest request, ResourceServiceBroker resourceServiceBroker, Resource principlesResource ) {
        log.info("getPrinciples Start");
        final ResourceBeanMapper resourceBeanMapper = resourceServiceBroker.getResourceBeanMapper(PLATFORM_RESOURCE_RESOLVER);
        final Collection<Item> itemCollection =
            resourceBeanMapper.mapCollection( principlesResource.getChildren( ),
                                              Item.class );

        log.info(itemCollection.toString()  );
        Collections.sort(new ArrayList<>( itemCollection), ITEM_COMPARATOR);
        request.setAttribute(HEADING_TEXT, itemCollection.stream().findAny().get().getType());
        request.setAttribute(ITEMS_REQUEST_KEY, itemCollection);
        log.info("getPrinciples End");
    }

}
