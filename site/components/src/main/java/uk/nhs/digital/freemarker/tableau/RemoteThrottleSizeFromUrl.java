package uk.nhs.digital.freemarker.tableau;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import uk.nhs.digital.freemarker.AbstractRemoteContent;

public class RemoteThrottleSizeFromUrl extends AbstractRemoteContent {

    private static final String RESOURCE_RESOLVER = "tableauThrottlingSizeResourceResolver";

    public RemoteThrottleSizeFromUrl() {
        super(RESOURCE_RESOLVER, ThrottleOptions.class, getComponentManager().getComponent("tableauRemoteContentService"));
    }

}
