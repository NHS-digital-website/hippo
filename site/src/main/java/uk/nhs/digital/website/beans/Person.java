package uk.nhs.digital.website.beans;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.Node;

import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.ArrayList;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:person")
@Node(jcrType = "website:person")
public class Person extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle()  {
        PersonalInfo personalInfo = getPersonalinfos();
        String preferredName = personalInfo.getPreferredname();

        if (preferredName != null) {
            return preferredName;
        }
        return personalInfo.getFirstname() + " " + personalInfo.getLastname();
    }

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

    public <T extends HippoBean> List<T> getRelatedDocuments(String property, Class<T> beanClass) throws HstComponentException, QueryException {

        final HstRequestContext context = RequestContextProvider.get();

        HippoBeanIterator hippoBeans = ContentBeanUtils.createIncomingBeansQuery(
            this.getCanonicalBean(), context.getSiteContentBaseBean(),
            property, beanClass, false)
            .execute()
            .getHippoBeans();

        List<T> list = new ArrayList<T>();
        
        while (hippoBeans.hasNext()) {
            list.add( (T) hippoBeans.nextHippoBean());
        }

        return list;
    }

    public List<News> getRelatedNews() throws HstComponentException, QueryException {
        return getRelatedDocuments("website:relateddocuments/@hippo:docbase", News.class);
    }

    public List<Event> getRelatedEvents() throws HstComponentException, QueryException {
        return getRelatedDocuments("website:relatedDocuments/@hippo:docbase", Event.class);
    }

}
