package uk.nhs.digital.website.beans;


import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:responsedefinition")
@Node(jcrType = "website:responsedefinition")
public class ResponseDefinition extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:responseitem")
    public String getResponseitem() {
        return getSingleProperty("website:responseitem");
    }

    @HippoEssentialsGenerated(internalName = "website:customdatatype")
    public String getCustomdatatype() {
        return getSingleProperty("website:customdatatype");
    }

    @HippoEssentialsGenerated(internalName = "website:datatype")
    public String getDatatype() {
        return getSingleProperty("website:datatype");
    }

    public String getSectionType() {
        return "responsedefinition";
    }
}
