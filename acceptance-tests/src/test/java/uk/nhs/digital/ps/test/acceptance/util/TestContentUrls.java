package uk.nhs.digital.ps.test.acceptance.util;

import static org.openqa.selenium.net.Urls.urlEncode;

import uk.nhs.digital.ps.test.acceptance.pages.site.AbstractSitePage;

import java.util.HashMap;
import java.util.Map;

public class TestContentUrls {

    private final Map<String, String> urlLookup = new HashMap();

    public TestContentUrls() {
        setup();
    }

    public String lookupUrl(String pageName) {
        String url = urlLookup.get(pageName.toLowerCase());
        if (url == null) {
            throw new RuntimeException("Unknown pageName: " + pageName);
        }

        return AbstractSitePage.URL + url;
    }

    private void setup() {
        add("home", "/");

        add("search", "/search");

        add("shmi", "/shmi");

        // data sets pages
        add("publication with datasets",
            "/data-and-information/publications/acceptance-tests/publication-with-datasets");
        add("publication with datasets dataset",
            "/data-and-information/publications/acceptance-tests/publication-with-datasets/datasets-subfolder/publication-with-datasets-dataset");
        add("series with publication with datasets",
            "/data-and-information/publications/acceptance-tests/series-with-publication-with-datasets");

        // folder
        add("acceptence tests folder", "/data-and-information/publications/acceptance-tests");

        // series page
        add("valid publication series",
            "/data-and-information/publications/valid-publication-series");
        add("valid publication series direct",
            "/data-and-information/publications/valid-publication-series/content");
        add("valid publication series old url",
            "/publications/valid-publication-series/content");

        add("series without latest",
            "/data-and-information/publications/acceptance-tests/series-without-latest");

        // archive page
        add("valid publication archive",
            "/data-and-information/publications/acceptance-tests/valid-publication-archive");
        add("valid publication archive direct",
            "/data-and-information/publications/acceptance-tests/valid-publication-archive/content");

        add("publication with rich content",
            "/data-and-information/publications/acceptance-tests/publication-rich");

        // unpublished dataset
        add("upcoming publication dataset",
            "/data-and-information/publications/acceptance-tests/upcoming-publication/upcoming-dataset");

        // dataset with no parent publication
        add("dataset without publication",
            "/data-and-information/publications/acceptance-tests/dataset-without-publication/dataset-without-publication");

        // attachment tests
        add("attachment test publication",
            "/data-and-information/publications/acceptance-tests/attachment-test");
        add("attachment test dataset",
            "/data-and-information/publications/acceptance-tests/attachment-test/dataset");

        // coverage date test
        add("coverage date publication",
            "/data-and-information/publications/acceptance-tests/coveragedates-test/coverage-test");

        // bare minimum documents
        add("bare minimum publication",
            "/data-and-information/publications/acceptance-tests/bare-minimum-publication");
        add("bare minimum dataset",
            "/data-and-information/publications/acceptance-tests/bare-minimum-publication/bare-minimum-dataset");

        // invalid urls
        add("invalid root", "/invalid");
        add("invalid document", "/data-and-information/publications/invalid");
        add("invalid sub document", "/data-and-information/publications/acceptance-tests/invalid");

        // about pages
        add("terms and conditions", "/about/terms-and-conditions");

        // CI Hub/Landing
        add("SHMI landing", "/data-and-information/publications/ci-hub/summary-hospital-level-mortality-indicator-shmi");
        add("SHMI_publication_timetable.xlsx",
            "/binaries/content/documents/corporate-website/publication-system/ci-hub/summary-hospital-level-mortality-indicator-shmi/summary-hospital-level-mortality-indicator-shmi/publicationsystem%3Acilandingasset/publicationsystem%3AAttachments/publicationsystem%3AattachmentResource");
        add("ci hub root", "/data-and-information/publications/ci-hub");

        // attachments
        add("attachment-text.pdf",
            getAttachmentUrl("attachment-test/content/content", "Attachments-v3"));
        add("attachment.pdf",
            getAttachmentUrl("attachment-test/content/content", "Attachments-v3[2]"));

        add("dataset-attachment-text.pdf",
            getAttachmentUrl("attachment-test/dataset/dataset", "Files-v3"));
        add("dataset-attachment.pdf",
            getAttachmentUrl("attachment-test/dataset/dataset", "Files-v3[2]"));

        // Ordered publication
        add("ordered publication",
            "/data-and-information/publications/acceptance-tests/ordered-publication");

        // Sectioned publication
        add("sectioned publication",
            "/data-and-information/publications/acceptance-tests/sectioned-publication");
        add("sectioned publication robots",
            getAttachmentUrl("sectioned-publication/content/content", "bodySections[2]", "image"));
        add("sectioned publication snowman",
            getAttachmentUrl("sectioned-publication/content/content", "bodySections[5]", "image"));

        // Publication with National Statistic logo
        add("national statistic publication",
            "/data-and-information/publications/lorem-ipsum-content/morbi-tempor-euismod-vehicula");

        // National Indicator Library
        add("nihub", "/data-and-information/national-indicator-library/nihub");
        add("sample-indicator", "/data-and-information/national-indicator-library/sample-indicator");

        // Legacy publication
        add("legacy publication",
            "/data-and-information/publications/acceptance-tests/legacy-series/legacy-publication");
        add("legacy publication direct",
            "/data-and-information/publications/acceptance-tests/legacy-series/legacy-publication/content");

        add("service with rich content - parent",
            "/services/service-document-1/");

        add("homeland",
            "/homeland");
    }

    private String getAttachmentUrl(String siteUrl, String attachmentTag) {
        return getAttachmentUrl(siteUrl, attachmentTag, "attachmentResource");
    }

    private String getAttachmentUrl(String siteUrl, String attachmentTag, String resourceTag) {
        return "/binaries/content/documents/corporate-website/publication-system/acceptance-tests/"
            + siteUrl + "/"
            + urlEncode("publicationsystem:" + attachmentTag) + "/"
            + urlEncode("publicationsystem:" + resourceTag);
    }

    private void add(String pageName, String url) {
        urlLookup.put(pageName.toLowerCase(), url);
    }
}
