package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:ImageModule")
@Node(jcrType = "website:ImageModule")
public class ImageModule extends HippoCompound {

    public String getSectionType() {
        return "imageModule";
    }

    @HippoEssentialsGenerated(internalName = "website:imageType")
    public String getImageType() {
        return getSingleProperty("website:imageType");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("website:image", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:altText")
    public String getAltText() {
        return getSingleProperty("website:altText");
    }

    @JsonProperty("caption")
    public String getCaptionJson() {
        return getHippoHtmlContent("website:caption");
    }

    @HippoEssentialsGenerated(internalName = "website:caption", allowModifications = false)
    public HippoHtml getCaption() {
        return getHippoHtml("website:caption");
    }

    @JsonProperty("text")
    public String getTextJson() {
        return getHippoHtmlContent("website:text");
    }

    @HippoEssentialsGenerated(internalName = "website:text", allowModifications = false)
    public HippoHtml getText() {
        return getHippoHtml("website:text");
    }

    @JsonIgnore
    protected String getHippoHtmlContent(String property) {
        HippoHtml html = getHippoHtml(property);
        if (html != null) {
            return html.getContent();
        }
        return null;
    }
}
