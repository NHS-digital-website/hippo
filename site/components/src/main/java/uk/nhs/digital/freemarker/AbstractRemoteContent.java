package uk.nhs.digital.freemarker;

import static org.hippoecm.hst.site.HstServices.getComponentManager;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class AbstractRemoteContent implements TemplateMethodModelEx {

    private final String resourceResolver;
    private final Class type;
    private final Object fallback;

    protected AbstractRemoteContent() {
        // This no args constructor keeps the PMD validator at bay.
        throw new UnsupportedOperationException("The name of a 'ResourceResolver' and 'Class' type must be given.");
    }

    protected AbstractRemoteContent(final String resourceResolver, final Class type) {
        this.resourceResolver = resourceResolver;
        this.type = type;
        this.fallback = null;
    }

    protected AbstractRemoteContent(final String resourceResolver, final Class type, Object fallback) {
        this.resourceResolver = resourceResolver;
        this.type = type;
        this.fallback = fallback;
    }

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1 && !(list.get(0) instanceof SimpleScalar)) {
            throw new TemplateModelException("Wrong argument. Take 1 argument of type string");
        }
        try {
            RemoteContentService remoteContentService = getComponentManager().getComponent("remoteContentService");
            return remoteContentService.getContentObjectFrom(new URL(((SimpleScalar) list.get(0)).getAsString()), resourceResolver, type, fallback);
        } catch (MalformedURLException e) {
            throw new TemplateModelException("1st argument should be a valid URL");
        }
    }

}
