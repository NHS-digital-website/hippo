package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.platformexplorer.response.Item;

import java.util.Comparator;

public class PrinciplesComponent extends CommonComponent {

    private static Logger log =
        LoggerFactory.getLogger( PrinciplesComponent.class );

    public static final String HEADING_TEXT = "headingText";

    public static final String ITEMS_REQUEST_KEY = "items";

    public static final String PLATFORM_RESOURCE_RESOLVER =
        "platformResourceResolver";

    public static final String EXPAND_ITEMS_PARAM =
        "$expand=Items($expand=RelatedItems($levels=1;$expand=Attributes))";

    public static final String ALTO_POC_VIEW_REQUEST_PATH =
        "/api/views/Website Collection/Principle List?" + EXPAND_ITEMS_PARAM;

    public static final String ITEM_REQUEST_PATH = "/api/item/";

    public static final String EXPAND_ATTRIBUTES_PARAM =
        "$expand=RelatedItems($expand=Attributes)";

    public static final Comparator<Item> ITEM_COMPARATOR =
        Comparator.comparing( Item::getName );

    @Override
    public void doBeforeRender( HstRequest request,
                                HstResponse response ) {
        super.doBeforeRender( request,
                              response );
        log.info( request.toString() );
        log.info( response.toString()  );
        getItemById( request,null,null );
        getAllRootItemsFromView( null,null,null );
    }

    private void getItemById( HstRequest request,
                              ResourceServiceBroker resourceServiceBroker,
                              Resource resource ) {
        log.info( request.toString() );
        log.info( resourceServiceBroker.toString()  );
        log.info( resource.toString()  );
    }

    private void getAllRootItemsFromView( HstRequest request,
                                          ResourceServiceBroker resourceServiceBroker,
                                          Resource resource ) {
        log.info( request.toString()  );
        log.info( resourceServiceBroker.toString()  );
        log.info( resource.toString()  );
    }

}
