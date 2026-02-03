package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;
import java.util.stream.Collectors;

@HippoEssentialsGenerated(internalName = "website:person")
@Node(jcrType = "website:person")
public class Person extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle()  {
        PersonalInfo personalInfo = getPersonalinfos();
        String preferredName = personalInfo.getPreferredname();

        if (!preferredName.isEmpty()) {
            return preferredName;
        }
        return personalInfo.getFirstname() + " " + personalInfo.getLastname();
    }

    @HippoEssentialsGenerated(internalName = "website:initials")
    public String getInitials()  {
        PersonalInfo personalInfo = getPersonalinfos();
        return "" + personalInfo.getFirstname().charAt(0) + personalInfo.getLastname().charAt(0);
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:knowsabout")
    public String[] getKnowsabout() {
        return getMultipleProperty("hippotaxonomy:knowsabout");
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
        return getSingleProperty("website:clinician");
    }

    @HippoEssentialsGenerated(internalName = "website:socialmedias")
    public SocialMedia getSocialmedias() {
        return getBean("website:socialmedias", SocialMedia.class);
    }

    @HippoEssentialsGenerated(internalName = "website:socialmedia")
    public SocialMedia getSocialmedia() {
        return getBean("website:socialmedia", SocialMedia.class);
    }

    @HippoEssentialsGenerated(internalName = "website:personalinfos")
    public PersonalInfo getPersonalinfos() {
        return getBean("website:personalinfos", PersonalInfo.class);
    }

    @HippoEssentialsGenerated(internalName = "website:roles")
    public Role getRoles() {
        return getBean("website:roles", Role.class);
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
    public Responsibility  getResponsibilities() {
        return getBean("website:responsibilities", Responsibility.class);
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
        return getLinkedBeans("website:manages", HippoBean.class);
    }

    public List<News> getRelatedNews() throws HstComponentException, QueryException {
        return getRelatedDocuments("website:peoplementioned/@hippo:docbase",6, News.class).stream().sorted(
            (n1, n2) -> n2.getPublisheddatetime().compareTo(n1.getPublisheddatetime())
        ).collect(Collectors.toList());
    }

    public List<Blog> getRelatedBlogs() throws HstComponentException, QueryException {
        return getRelatedDocuments("website:authors/@hippo:docbase", 6, "website:dateofpublication", Blog.class);
    }

    @HippoEssentialsGenerated(internalName = "website:socialMediaBar")
    public SocialMediaBar getSocialMediaBar() {
        return getBean("website:socialMediaBar", SocialMediaBar.class);
    }
}
