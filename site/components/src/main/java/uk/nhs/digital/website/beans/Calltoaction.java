package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;
import uk.nhs.digital.svg.SvgProvider;

@HippoEssentialsGenerated(internalName = "website:calltoaction")
@Node(jcrType = "website:calltoaction")
public class Calltoaction extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:Label")
    public String getLabel() {
        return getSingleProperty("website:Label");
    }

    @HippoEssentialsGenerated(internalName = "website:external")
    public String getExternal() {
        return getSingleProperty("website:external");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:internal")
    public HippoBean getInternal() {
        return getLinkedBean("website:internal", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public String getContent() {
        return getSingleProperty("website:content");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:altText")
    public String getAltText() {
        return getSingleProperty("website:altText");
    }

    @HippoEssentialsGenerated(internalName = "website:isDecorative")
    public String getIsDecorative() {
        return Boolean.toString(getSingleProperty("website:isDecorative"));
    }

    @HippoEssentialsGenerated(internalName = "website:categoryInfo")
    public String getCategoryInfo() {
        return getSingleProperty("website:categoryInfo");
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public CorporateWebsiteImageset getIcon() {
        return getLinkedBean("website:icon", CorporateWebsiteImageset.class);
    }

    public String getSvgXmlFromRepository() {
        HippoBean imageBean = getIcon();
        return SvgProvider.getSvgXmlFromBean(imageBean);
    }

    public String getSvgXmlFromRepositoryImage() {
        HippoBean imageBean = getImage();
        return SvgProvider.getSvgXmlFromBean(imageBean);
    }
}
