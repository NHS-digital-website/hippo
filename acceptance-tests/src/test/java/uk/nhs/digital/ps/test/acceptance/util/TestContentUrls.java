package uk.nhs.digital.ps.test.acceptance.util;

import java.util.HashMap;
import java.util.Map;

public class TestContentUrls {

    private final Map<String, String> urlLookup = new HashMap();

    public TestContentUrls() {
        setup();
    }

    public String lookupUrl(String pageName) {
        return urlLookup.get(pageName.toLowerCase());
    }

    private void setup() {
        add("home", "/");

        add ("publications overview",
            "/publications");

        // data sets pages
        add("publication with datasets",
            "/publications/acceptance-tests/publication-with-datasets");
        add("publication with datasets dataset",
            "/publications/acceptance-tests/publication-with-datasets/datasets-subfolder/publication-with-datasets-dataset");
        add("series with publication with datasets",
            "/publications/acceptance-tests/series-with-publication-with-datasets");

        // series page
        add("valid publication series",
            "/publications/valid-publication-series");

        add("publication with rich content",
            "/publications/acceptance-tests/publication-rich");

        // unpublished dataset
        add("upcoming publication dataset",
            "/publications/acceptance-tests/upcoming-publication/upcoming-dataset");

        // attachment tests
        add("attachment test publication",
            "/publications/acceptance-tests/attachment-test");
        add("attachment test dataset",
            "/publications/acceptance-tests/attachment-test/dataset");
    }

    private void add(String pageName, String url) {
        urlLookup.put(pageName.toLowerCase(), url);
    }
}
