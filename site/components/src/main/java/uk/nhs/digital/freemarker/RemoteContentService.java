package uk.nhs.digital.freemarker;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public abstract class RemoteContentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteContentService.class);

    @HystrixCommand(
        fallbackMethod = "getReliableFallBackObject",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "400")
        }
    )
    public Object getContentObjectFrom(URL url, String resourceResolver, Class type) {
        LOGGER.debug("Inside RemoteContentService ");
        ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        LOGGER.debug("Inside RemoteContentService broker  is " + broker + " URL is " + url);
        Resource r = broker.resolve(resourceResolver, url.toString());
        LOGGER.debug("Inside RemoteContentService Resource r is " + r);
        return broker.getResourceBeanMapper(resourceResolver).map(r, type);
    }

    /**
     * The fallback method when the Remote URL is unavailable or in error
     *
     * @return The fallback default object
     */
    public abstract Object getReliableFallBackObject(URL url, String resourceResolver, Class type, Throwable e);

}
