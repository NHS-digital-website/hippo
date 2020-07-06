package uk.nhs.digital.freemarker;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class RemoteContentService {

    @HystrixCommand(
        fallbackMethod = "getReliableFallBackObject",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "250")
        }
    )
    public Object getContentObjectFrom(URL url, String resourceResolver, Class type, Object fallback) {
        ResourceServiceBroker broker =  CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        try {
            Resource r = broker.resolve(resourceResolver, url.toString());
            return broker.getResourceBeanMapper(resourceResolver).map(r, type);
        } catch (ResourceException e) {
            LoggerFactory.getLogger(type).warn(String.format("Issue with URL: %s", url.toString()), e);
            return fallback;
        }
    }

    /**
     * The fallback method when the Remote URL is unavailable or in error
     *
     * @return The fallback default object
     */
    public Object getReliableFallBackObject(URL url, String resourceResolver, Class type, Object fallback, Throwable e) {
        LoggerFactory.getLogger(type).warn(String.format("Issue with URL: %s", url.toString()), e);
        return fallback;
    }

}
