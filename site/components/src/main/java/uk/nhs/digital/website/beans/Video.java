package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Node(jcrType = "website:video")
public class Video extends CommonFieldsBean {
    @HippoEssentialsGenerated(internalName = "website:introduction", allowModifications = false)
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:externallinkbase")
    public String getVideoUri() {
        String videoUri = getSingleProperty("website:externallinkbase");

        // Regex adapted from https://regexr.com/3dj5t - named groups added
        Pattern youtubeTest = Pattern.compile(
            "^(?<protocol>(?:https?:)?\\/\\/)"
                + "?(?<subdomain>(?:www|m)\\.)"
                + "?(?<domain>(?:youtube\\.com|youtu.be))"
                + "(?<type>\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)"
                + "(?<videoID>[\\w\\-]+)"
                + "(?<query>\\S+)?$",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
        );
        Matcher youtubeMatch = youtubeTest.matcher(videoUri);

        // Make sure that YouTube URLs use the right format
        if (youtubeMatch.matches()) {
            return String.format(
                "https://www.youtube-nocookie.com/embed/%s?modestbranding=1",
                youtubeMatch.group("videoID")
            );
        }

        // Regex adapted from https://regexr.com/393r3 - named groups added
        Pattern vimeoTest = Pattern.compile(
            "(?:<iframe [^>]*src=\")?"
                + "(?:https?:\\/\\/"
                + "(?:[\\w]+\\.)*vimeo\\.com"
                + "(?:[\\/\\w:]*(?:\\/videos)?)?\\/"
                + "(?<videoID>[0-9]+)[^\\s]*)\"?"
                + "(?:[^>]*><\\/iframe>)?"
                + "(?:<p>.*<\\/p>)?",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
        );
        Matcher vimeoMatch = vimeoTest.matcher(videoUri);

        // Make sure that Vimeo URLs use the right embed format
        if (vimeoMatch.matches()) {
            return String.format(
                "https://player.vimeo.com/video/%s?color=005eb8&portrait=0",
                vimeoMatch.group("videoID")
            );
        }

        return videoUri;
    }

    @HippoEssentialsGenerated(internalName = "website:relateddocuments")
    public List<HippoBean> getRelatedDocuments() {
        return getLinkedBeans("website:relateddocuments", HippoBean.class);
    }
}
