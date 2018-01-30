package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

@HippoEssentialsGenerated(internalName = "publicationsystem:section")
@Node(jcrType = "publicationsystem:section")
public class Section extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:html")
    public HippoHtml getHtml() {
        return getHippoHtml("publicationsystem:html");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:type")
    public String getType() {
        return getProperty("publicationsystem:type");
    }
}
