package uk.nhs.digital.freemarker.statistics;

import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;

import java.net.URL;

public class RemoteStatisticFromJson extends AbstractRemoteStatistic {

    protected Statistic getDataFromApi(URL url) {
        ResourceServiceBroker broker =  CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        Resource r = broker.resolve("statisticsRestResourceResolver", url.toString());
        return broker.getResourceBeanMapper("statisticsRestResourceResolver").map(r, Statistic.class);
    }

}
