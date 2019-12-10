package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:supplementaryinformation")
@Node(jcrType = "website:supplementaryinformation")
public class SupplementaryInformation extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "publicationsystem:NominalDate", allowModifications = false)
    public Calendar getPublishedDate() {
        return getProperty("publicationsystem:NominalDate");
    }

    @HippoEssentialsGenerated(internalName = "website:request")
    public HippoHtml getRequest() {
        return getHippoHtml("website:request");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys", allowModifications = false)
    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    @HippoEssentialsGenerated(internalName = "website:interval")
    public Interval getInterval() {
        return getBean("website:interval", Interval.class);
    }

    @HippoEssentialsGenerated(internalName = "website:relateddocuments")
    public List<HippoBean> getRelateddocuments() {
        return getLinkedBeans("website:relateddocuments", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:responsibleteam")
    public HippoBean getResponsibleteam() {
        return getLinkedBean("website:responsibleteam", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:responsibleperson")
    public HippoBean getResponsibleperson() {
        return getLinkedBean("website:responsibleperson", HippoBean.class);
    }

}
