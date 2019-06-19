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
        return getProperty("website:firstname");
    }

    @HippoEssentialsGenerated(internalName = "website:lastname")
    public String getLastname() {
        return getProperty("website:lastname");
    }

    @HippoEssentialsGenerated(internalName = "website:middlenames")
    public String getMiddlenames() {
        return getProperty("website:middlenames");
    }

    @HippoEssentialsGenerated(internalName = "website:pronunciation")
    public String getPronunciation() {
        return getProperty("website:pronunciation");
    }

    @HippoEssentialsGenerated(internalName = "website:preferredname")
    public String getPreferredname() {
        return getProperty("website:preferredname");
    }

    @HippoEssentialsGenerated(internalName = "website:gender")
    public String getGender() {
        return getProperty("website:gender");
    }

    @HippoEssentialsGenerated(internalName = "website:othergender")
    public String getOthergender() {
        return getProperty("website:othergender");
    }

    @HippoEssentialsGenerated(internalName = "website:honorific")
    public String getHonorific() {
        return getProperty("website:honorific");
    }

    @HippoEssentialsGenerated(internalName = "website:otherhonorific")
    public String getOtherhonorific() {
        return getProperty("website:otherhonorific");
    }

    @HippoEssentialsGenerated(internalName = "website:preferredpronouns")
    public String getPreferredpronouns() {
        return getProperty("website:preferredpronouns");
    }

    @HippoEssentialsGenerated(internalName = "website:otherpreferedpronouns")
    public String getOtherpreferedpronouns() {
        return getProperty("website:otherpreferedpronouns");
    }

}
