package uk.nhs.digital.common.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = "common:basedocument")
public class BaseDocument extends HippoDocument {
    /**
     * In order to keep the sitemap logic in one place, this function needs to return Hippo Bean which will be used
     * to generate link (url) to this document. In most cases this will be a "this", but in more complex sitemap this
     * can be something more convoluted.
     *
     * @return Object used to render a link to this page
     */
    public HippoBean getSelfLink() {
        return this;
    }
}
