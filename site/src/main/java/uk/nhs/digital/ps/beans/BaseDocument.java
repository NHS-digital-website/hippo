package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

import java.util.List;

@Node(jcrType="publicationsystem:basedocument")
public class BaseDocument extends HippoDocument {

    /**
     * <p>
     * In order to keep the sitemap logic in one place, this function needs to return Hippo Bean which will be used
     * to generate link (url) to this document. In most cases this will be a "this".
     *
     * For publication series this is simply parent folder object but in other cases this can become something more
     * convoluted.
     *
     * @return Object used to render a link to this page
     */
    public HippoBean getSelfLinkBean() {
        return this;
    }

    protected <T extends HippoBean> List<T> getChildBeansIfPermitted(final String propertyName, final
    Class<T> beanMappingClass) {
        assertPropertyPermitted(propertyName);

        return getChildBeansByName(propertyName, beanMappingClass);
    }

    protected <T> T getPropertyIfPermitted(final String propertyKey) {
        assertPropertyPermitted(propertyKey);

        return getProperty(propertyKey);
    }

    protected void assertPropertyPermitted(String propertyName) {
        // To be overwritten by subclasses for specific implementation
    }
}
