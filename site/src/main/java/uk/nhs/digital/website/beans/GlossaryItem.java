package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:glossaryitem")
@Node(jcrType = "website:glossaryitem")
public class GlossaryItem extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getTitle() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:definition")
    public HippoHtml getDefinition() {
        return getHippoHtml("website:definition");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public HippoBean getLink() {
        return getBean("website:items", HippoBean.class);
    }

}
