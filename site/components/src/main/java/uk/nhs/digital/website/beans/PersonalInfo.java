package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:personalinfo")
@Node(jcrType = "website:personalinfo")
public class PersonalInfo extends HippoCompound {

    public String getSectionType() {
        return "personalinfo";
    }

    @HippoEssentialsGenerated(internalName = "website:firstname")
    public String getFirstname() {
        return getSingleProperty("website:firstname");
    }

    @HippoEssentialsGenerated(internalName = "website:lastname")
    public String getLastname() {
        return getSingleProperty("website:lastname");
    }

    @HippoEssentialsGenerated(internalName = "website:middlenames")
    public String getMiddlenames() {
        return getSingleProperty("website:middlenames");
    }

    @HippoEssentialsGenerated(internalName = "website:pronunciation")
    public String getPronunciation() {
        return getSingleProperty("website:pronunciation");
    }

    @HippoEssentialsGenerated(internalName = "website:preferredname")
    public String getPreferredname() {
        return getSingleProperty("website:preferredname");
    }

    @HippoEssentialsGenerated(internalName = "website:gender")
    public String getGender() {
        return getSingleProperty("website:gender");
    }

    @HippoEssentialsGenerated(internalName = "website:othergender")
    public String getOthergender() {
        return getSingleProperty("website:othergender");
    }

    @HippoEssentialsGenerated(internalName = "website:honorific")
    public String getHonorific() {
        return getSingleProperty("website:honorific");
    }

    @HippoEssentialsGenerated(internalName = "website:otherhonorific")
    public String getOtherhonorific() {
        return getSingleProperty("website:otherhonorific");
    }

    @HippoEssentialsGenerated(internalName = "website:preferredpronouns")
    public String getPreferredpronouns() {
        return getSingleProperty("website:preferredpronouns");
    }

    @HippoEssentialsGenerated(internalName = "website:otherpreferedpronouns")
    public String getOtherpreferedpronouns() {
        return getSingleProperty("website:otherpreferedpronouns");
    }

}
