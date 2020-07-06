package uk.nhs.digital.freemarker.statistics;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import uk.nhs.digital.freemarker.AbstractRemoteContent;

public class RemoteStatisticFromUrl extends AbstractRemoteContent {

    private static final String RESOURCE_RESOLVER = "statisticsRestResourceResolver";

    public RemoteStatisticFromUrl() {
        super(RESOURCE_RESOLVER, Statistic.class, getComponentManager().getComponent("basicRemoteContentService"));
    }

}
