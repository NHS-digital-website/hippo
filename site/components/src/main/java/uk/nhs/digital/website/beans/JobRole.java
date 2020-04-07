package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.core.component.HstComponentException;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:jobrole")
@Node(jcrType = "website:jobrole")
public class JobRole extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:genericjobrole")
    public String getGenericjobrole() {
        return getProperty("website:genericjobrole");
    }

    @HippoEssentialsGenerated(internalName = "website:businessunit")
    public HippoBean getBusinessunit() {
        return getLinkedBean("website:businessunit", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:reportsto")
    public HippoBean getReportsto() {
        return getLinkedBean("website:reportsto", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:responsibilities")
    public List<HippoHtml> getResponsibilities() {
        return getChildBeansByName("website:responsibilities", HippoHtml.class);
    }

    @HippoEssentialsGenerated(internalName = "website:accountabilities")
    public List<HippoHtml> getAccountablities() {
        return getChildBeansByName("website:accountabilities", HippoHtml.class);
    }

    public List<Person> getRelatedPeople() throws HstComponentException, QueryException {

        int limit = 3;
        return getRelatedDocuments(
           "website:roles/website:rolepicker/website:primaryrolepicker/@hippo:docbase",
           limit,
           Person.class);
    }
}
