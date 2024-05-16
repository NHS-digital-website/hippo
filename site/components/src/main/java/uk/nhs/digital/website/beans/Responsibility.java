package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:responsibility")
@Node(jcrType = "website:responsibility")
public class Responsibility extends HippoCompound {

    public String getSectionType() {
        return "responsibility";
    }

    @HippoEssentialsGenerated(internalName = "website:responsible")
    public String[] getResponsible() {
        return getMultipleProperty("website:responsible");
    }

    @HippoEssentialsGenerated(internalName = "website:responsibleforservice")
    public List<HippoBean> getResponsibleforservice() {
        return getLinkedBeans("website:responsibleforservice", HippoBean.class);
    }
}

