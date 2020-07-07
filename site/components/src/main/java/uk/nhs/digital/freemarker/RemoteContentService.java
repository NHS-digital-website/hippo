package uk.nhs.digital.freemarker;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;

import java.net.URL;

public abstract class RemoteContentService {

    @HystrixCommand(
        fallbackMethod = "getReliableFallBackObject",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "400")
        }
    )
    public Object getContentObjectFrom(URL url, String resourceResolver, Class type) {
        ResourceServiceBroker broker =  CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        Resource r = broker.resolve(resourceResolver, url.toString());
        return broker.getResourceBeanMapper(resourceResolver).map(r, type);
    }

    /**
     * The fallback method when the Remote URL is unavailable or in error
     *
     * @return The fallback default object
     */
    public abstract Object getReliableFallBackObject(URL url, String resourceResolver, Class type, Throwable e);

}
