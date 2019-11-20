package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = "nationalindicatorlibrary:basedocument")
public class BaseDocument extends HippoDocument {

    /**
     * <p>
     * In order to keep the sitemap logic in one place, this function needs to return Hippo Bean which will be used
     * to generate link (url) to this document. In most cases this will be a "this".
     *
     * @return Object used to render a link to this page
     */
    public HippoBean getSelfLinkBean() {
        return this;
    }
}
