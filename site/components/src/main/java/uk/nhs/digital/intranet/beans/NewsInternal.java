package uk.nhs.digital.intranet.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import uk.nhs.digital.website.beans.LeadImageSection;

import java.util.Calendar;
import java.util.List;

@Node(jcrType = "intranet:newsinternal")
public class NewsInternal extends HippoDocument {

    public String getTitle() {
        return getProperty("intranet:title");
    }

    public List<HippoBean> getRelatedDocuments() {
        return getLinkedBeans("intranet:relateddocuments", HippoBean.class);
    }

    public Calendar getPublicationDate() {
        return getProperty("intranet:publicationdate");
    }

    public String getType() {
        return getProperty("intranet:type");
    }

    public Calendar getExpiryDate() {
        return getProperty("intranet:expirydate");
    }

    public LeadImageSection getLeadImageSection() {
        return getBean("intranet:leadimagesection", LeadImageSection.class);
    }

    public List<HippoBean> getSections() {
        return getChildBeansByName("intranet:sections");
    }

    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }
}
