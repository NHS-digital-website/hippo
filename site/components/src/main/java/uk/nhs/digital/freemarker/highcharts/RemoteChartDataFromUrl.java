package uk.nhs.digital.freemarker.highcharts;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import uk.nhs.digital.freemarker.AbstractRemoteContent;

public class RemoteChartDataFromUrl extends AbstractRemoteContent {

    private static final String RESOURCE_RESOLVER = "chartValueResourceResolver";

    public RemoteChartDataFromUrl() {
        super(RESOURCE_RESOLVER, ChartData.class, getComponentManager().getComponent("basicRemoteContentService"));
    }

}
