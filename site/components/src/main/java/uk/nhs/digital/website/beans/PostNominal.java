package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:postnominal")
@Node(jcrType = "website:postnominal")
public class PostNominal extends HippoCompound {

    public String getSectionType() {
        return "postnominal";
    }

    @HippoEssentialsGenerated(internalName = "website:letters")
    public String getLetters() {
        return getSingleProperty("website:letters");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public String getDescription() {
        return getSingleProperty("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public List<?> getLink() {
        return getChildBeansByName("website:link");
    }

    @HippoEssentialsGenerated(internalName = "website:awardingbody")
    public String getAwardingbody() {
        return getSingleProperty("website:awardingbody");
    }
}
