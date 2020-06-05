package uk.nhs.digital.freemarker.highcharts;

import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import uk.nhs.digital.freemarker.AbstractRemoteContent;

import java.net.URL;

public class RemoteChartDataFromUrl extends AbstractRemoteContent {

    private static final String RESOURCE_RESOLVER = "chartValueResourceResolver";

    protected Object getContentObjectFrom(URL url) {
        ResourceServiceBroker broker =  CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        Resource r = broker.resolve(RESOURCE_RESOLVER, url.toString());
        return broker.getResourceBeanMapper(RESOURCE_RESOLVER).map(r, ChartData.class);
    }

}
