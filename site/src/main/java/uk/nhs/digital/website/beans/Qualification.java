package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:qualification")
@Node(jcrType = "website:qualification")
public class Qualification extends HippoCompound {

    public String getSectionType() {
        return "qualification";
    }

    @HippoEssentialsGenerated(internalName = "website:qualname")
    public String getQualname() {
        return getProperty("website:qualname");
    }

    @HippoEssentialsGenerated(internalName = "website:quallink")
    public List<?> getQuallink() {
        return getChildBeansByName("website:quallink");
    }

    @HippoEssentialsGenerated(internalName = "website:qualawardingbody")
    public String getQualawardingbody() {
        return getProperty("website:qualawardingbody");
    }

    @HippoEssentialsGenerated(internalName = "website:qualawardingbodylink")
    public List<?> getQualawardingbodylink() {
        return getChildBeansByName("website:qualawardingbodylink");
    }

    @HippoEssentialsGenerated(internalName = "website:qualificationlogo")
    public HippoGalleryImageSet getQualificationlogo() {
        return getLinkedBean("website:qualificationlogo", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:dateattained")
    public Calendar getDateattained() {
        return getProperty("website:dateattained");
    }

}
