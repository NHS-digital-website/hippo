package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.website.utils.VideoPlayer;

@HippoEssentialsGenerated(internalName = "website:VideoSection")
@Node(jcrType = "website:VideoSection")
public class VideoSection extends HippoCompound {

    public String getSectionType() {
        return "VideoSection";
    }

    @HippoEssentialsGenerated(internalName = "type")
    public String getType() {
        return getSingleProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "videoUrl")
    public String getVideoUrl() {
        return VideoPlayer.getVideoUrl(getSingleProperty("website:videoUrl"));
    }

    @HippoEssentialsGenerated(internalName = "playlist")
    public String getPlaylist() {
        return VideoPlayer.getVideoId(getSingleProperty("website:videoUrl"));
    }

    @JsonProperty("caption")
    public HippoHtml getCaption() {
        return getHippoHtml("website:caption");
    }

    @HippoEssentialsGenerated(internalName = "behaviour")
    public Boolean getBehaviour() {
        return getSingleProperty("website:behaviour");
    }

    @HippoEssentialsGenerated(internalName = "loop")
    public Boolean getLoop() {
        return getSingleProperty("website:loop");
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
