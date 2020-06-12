package uk.nhs.digital.freemarker;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class AbstractRemoteContent implements TemplateMethodModelEx {

    private final String resourceResolver;
    private final Class type;

    protected AbstractRemoteContent() {
        // This no args constructor keeps the PMD validator at bay.
        throw new UnsupportedOperationException("The name of a 'ResourceResolver' and 'Class' type must be given.");
    }

    protected AbstractRemoteContent(final String resourceResolver, final Class type) {
        this.resourceResolver = resourceResolver;
        this.type = type;
    }

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1 && !(list.get(0) instanceof SimpleScalar)) {
            throw new TemplateModelException("Wrong argument. Take 1 argument of type string");
        }
        try {
            return getContentObjectFrom(new URL(((SimpleScalar) list.get(0)).getAsString()));
        } catch (MalformedURLException e) {
            throw new TemplateModelException("1st argument should be a valid URL");
        }
    }

    private Object getContentObjectFrom(URL url) {
        ResourceServiceBroker broker =  CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        try {
            Resource r = broker.resolve(resourceResolver, url.toString());
            return broker.getResourceBeanMapper(resourceResolver).map(r, type);
        } catch (ResourceException e) {
            LoggerFactory.getLogger(type).warn(String.format("Issue with URL: %s", url.toString()), e);
            return null;
        }
    }

}
