package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:person")
@Node(jcrType = "website:person")
public class Person extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:knowsabout")
    public String[] getKnowsabout() {
        return getProperty("hippotaxonomy:knowsabout");
    }

    @HippoEssentialsGenerated(internalName = "website:postnominals")
    public List<HippoBean> getPostnominals() {
        return getChildBeansByName("website:postnominals");
    }

    @HippoEssentialsGenerated(internalName = "website:lawfulbasises")
    public LawfulBasis getLawfulbasises() {
        return getBean("website:lawfulbasises", LawfulBasis.class);
    }

    @HippoEssentialsGenerated(internalName = "website:clinician")
    public Boolean getClinician() {
        return getProperty("website:clinician");
    }

    @HippoEssentialsGenerated(internalName = "website:socialmedias")
    public SocialMedia getSocialmedias() {
        return getBean("website:socialmedias", SocialMedia.class);
    }

    @HippoEssentialsGenerated(internalName = "website:personalinfos")
    public PersonalInfo getPersonalinfos() {
        return getBean("website:personalinfos", PersonalInfo.class);
    }

    @HippoEssentialsGenerated(internalName = "website:personimages")
    public PersonImage getPersonimages() {
        return getBean("website:personimages", PersonImage.class);
    }

    @HippoEssentialsGenerated(internalName = "website:biographies")
    public Biography getBiographies() {
        return getBean("website:biographies", Biography.class);
    }

    @HippoEssentialsGenerated(internalName = "website:responsibilities")
    public List<HippoBean> getResponsibilities() {
        return getChildBeansByName("website:responsibilities");
    }

    @HippoEssentialsGenerated(internalName = "website:qualifications")
    public List<HippoBean> getQualifications() {
        return getChildBeansByName("website:qualifications");
    }

    @HippoEssentialsGenerated(internalName = "website:awards")
    public List<HippoBean> getAwards() {
        return getChildBeansByName("website:awards");
    }

    @HippoEssentialsGenerated(internalName = "website:managedby")
    public HippoBean getManagedby() {
        return getLinkedBean("website:managedby", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:manages")
    public List<HippoBean> getManages() {
        return getChildBeansByName("website:manages");
    }
}
