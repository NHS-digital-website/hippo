package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:parameters")
@Node(jcrType = "website:parameters")
public class Parameters extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:parametertype")
    public String getParametertype() {
        return getProperty("website:parametertype");
    }

    @HippoEssentialsGenerated(internalName = "website:ismandatory")
    public Boolean getIsmandatory() {
        return getProperty("website:ismandatory");
    }

    @HippoEssentialsGenerated(internalName = "website:path")
    public String getPath() {
        return getProperty("website:path");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    public String getSectionType() {
        return "parameters";
    }
}
