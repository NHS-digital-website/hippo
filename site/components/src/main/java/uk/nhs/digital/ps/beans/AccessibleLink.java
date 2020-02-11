package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType = "publicationsystem:accessiblelink")
public class AccessibleLink extends HippoCompound {

    public String getLink() {
        return getProperty("publicationsystem:link");
    }

    public HippoHtml getContent() {
        return getHippoHtml("publicationsystem:content");
    }

    public String getTitle() {
        return getProperty("publicationsystem:title");
    }
}
