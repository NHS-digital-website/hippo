package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:socialmedia")
@Node(jcrType = "website:socialmedia")
public class SocialMedia extends HippoCompound {

    public String getSectionType() {
        return "socialmedia";
    }

    @HippoEssentialsGenerated(internalName = "website:linkedinlink")
    public String getLinkedinlink() {
        return getSingleProperty("website:linkedinlink");
    }

    @HippoEssentialsGenerated(internalName = "website:twitteruser")
    public String getTwitteruser() {
        return getSingleProperty("website:twitteruser");
    }

    @HippoEssentialsGenerated(internalName = "website:hellomynameis")
    public String getHellomynameis() {
        return getSingleProperty("website:hellomynameis");
    }

    @HippoEssentialsGenerated(internalName = "website:twitterhashtags")
    public HippoHtml getTwitterhashtags() {
        return getHippoHtml("website:twitterhashtags");
    }

    @HippoEssentialsGenerated(internalName = "website:othersocialmedias")
    public List<HippoBean> getOthersocialmedias() {
        return getChildBeansByName("website:othersocialmedias");
    }
}

