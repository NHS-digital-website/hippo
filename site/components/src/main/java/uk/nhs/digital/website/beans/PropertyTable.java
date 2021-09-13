package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:propertytable")
@Node(jcrType = "website:propertytable")
public class PropertyTable extends HippoCompound {

    public String getSectionType() {
        return "propertytable";
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:headingType")
    public String getHeadingType() {
        return getSingleProperty("website:headingType");
    }

    @HippoEssentialsGenerated(internalName = "website:tableFormat")
    public String getTableFormat() {
        return getSingleProperty("website:tableFormat");
    }

    @HippoEssentialsGenerated(internalName = "website:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:property")
    public String getPropertyDetails() {
        return getSingleProperty("website:property");
    }

}
