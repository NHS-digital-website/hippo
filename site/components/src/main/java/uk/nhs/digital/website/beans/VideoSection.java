package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
 

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
        return ((String) getSingleProperty("website:videoUrl")).replace("www.youtube.com", "www.youtube-nocookie.com");
    }
    
    @HippoEssentialsGenerated(internalName = "playlist")
    public String getPlaylist() {
        return embedVideo(getSingleProperty("website:videoUrl"));
    }
    
    @HippoEssentialsGenerated(internalName = "caption")
    public String getCaption() {
        return getSingleProperty("website:caption");
    }
    
    @HippoEssentialsGenerated(internalName = "text")
    public String getText() {
        return getSingleProperty("website:text");
    }
    
    @HippoEssentialsGenerated(internalName = "behaviour")
    public Boolean getBehaviour() {
        return getSingleProperty("website:behaviour");
    }
    
    @HippoEssentialsGenerated(internalName = "loop")
    public Boolean getLoop() {
        return getSingleProperty("website:loop");
    }
    
    private String embedVideo(String url) {
        String regex = "(http:|https:|)\\/\\/(player.|www.)?(vimeo\\.com|youtu(be\\.com|\\.be|be\\.googleapis\\.com))/";
        regex += "(video/|embed/|channels/(?:\\w+/)|watch\\?v=|v/)?([A-Za-z0-9._%-]*)(\\&\\S+)?";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url);

        if (m.matches()) {
            return m.group(6);
        }
        return url;
    }
}
