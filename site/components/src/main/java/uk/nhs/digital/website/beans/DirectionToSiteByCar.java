package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:directiontositebycar")
@Node(jcrType = "website:directiontositebycar")
public class DirectionToSiteByCar extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:bycartext")
    public HippoHtml getBycartext() {
        return getHippoHtml("website:bycartext");
    }

    @HippoEssentialsGenerated(internalName = "website:bycarpicture")
    public CorporateWebsiteImageset getBycarpicture() {
        return getLinkedBean("website:bycarpicture", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bycarpicturealttext")
    public String getByCarPictureAltText() {
        return getSingleProperty("website:bycarpicturealttext");
    }

    @HippoEssentialsGenerated(internalName = "website:bycarvideo")
    public HippoHtml getBycarvideo() {
        return getHippoHtml("website:bycarvideo");
    }

    public String getSectionType() {
        return "directiontositebycar";
    }

}
