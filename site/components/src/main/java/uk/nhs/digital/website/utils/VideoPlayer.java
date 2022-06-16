package uk.nhs.digital.website.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoPlayer {

    private static Pattern youtubeTest;
    private static Pattern vimeoTest;

    static {
        // Regex adapted from https://regexr.com/3dj5t - named groups added
        youtubeTest = Pattern.compile(
            "^(?<protocol>(?:https?:)?\\/\\/)"
                + "?(?<subdomain>(?:www|m)\\.)"
                + "?(?<domain>(?:youtube\\.com|youtu.be|youtube-nocookie\\.com))"
                + "(?<type>\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)"
                + "(?<videoID>[\\w\\-]+)"
                + "(?<query>\\S+)?$",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
        );
        
        vimeoTest = Pattern.compile(
            "^(?:<iframe[^>]+?src=\")?"
                + "(?:https?)?:\\/\\/"
                + "(?:[\\w]+?\\.)*?vimeo\\.com"
                + "(?:[\\/\\w:]+)?\\/(?<videoID>[0-9]+)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
    }

    public static String getVideoUrl(String videoUri) {

        Matcher youtubeMatch = youtubeTest.matcher(videoUri);

        // Make sure that YouTube URLs use the right format
        if (youtubeMatch.matches()) {
            return String.format(
                "https://www.youtube-nocookie.com/embed/%s?modestbranding=1",
                youtubeMatch.group("videoID")
            );
        }

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

    public static String getVideoId(String videoUri) {
        Matcher youtubeMatch = youtubeTest.matcher(videoUri);

        // Make sure that YouTube URLs use the right format
        if (youtubeMatch.matches()) {
            return youtubeMatch.group("videoID");
        }

        Matcher vimeoMatch = vimeoTest.matcher(videoUri);

        // Make sure that Vimeo URLs use the right embed format
        if (vimeoMatch.matches()) {
            return vimeoMatch.group("videoID");
        }

        return videoUri;
    }
}
