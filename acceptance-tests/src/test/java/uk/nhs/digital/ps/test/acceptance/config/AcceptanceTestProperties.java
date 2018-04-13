package uk.nhs.digital.ps.test.acceptance.config;

import java.nio.file.Path;

@SuppressWarnings("WeakerAccess")
public class AcceptanceTestProperties {

    private final boolean headlessMode;
    private final Path downloadDir;
    private final Path tempDir;
    private final String cmsUrl;
    private final String siteUrl;

    AcceptanceTestProperties(final boolean headlessMode,
                             final Path downloadDir,
                             final Path tempDir,
                             final String cmsUrl,
                             final String siteUrl) {
        this.headlessMode = headlessMode;
        this.downloadDir = downloadDir;
        this.tempDir = tempDir;
        this.cmsUrl = cmsUrl;
        this.siteUrl = siteUrl;
    }

    /**
     * Determines where the Chromedriver will be downloading files into.
     */
    public Path getDownloadDir() {
        return downloadDir;
    }

    /**
     * Determines whether the web driver will be operating in a headless mode.
     */
    public boolean isHeadlessMode() {
        return headlessMode;
    }

    /**
     * Directory where the tests can store their temporary data with the expectation taht the content of this directory
     * may be deleted between builds. Typically, it's useful to point it to the directory used by your build system of
     * choice to generate artefacts into; for example, in projects managed by
     * <a href="https://maven.apache.org/">Maven</a> this would be '{@code target}' and in projects managed by
     * <a href="https://gradle.org/">Gradle</a> this would be '{@code build}'.
     */
    public Path getTempDir() {
        return tempDir;
    }

    @Override
    public String toString() {
        return "AcceptanceTestProperties{"
            + "headlessMode=" + headlessMode
            + ", downloadDir=" + downloadDir
            + ", tempDir=" + tempDir
            + '}';
    }

    /**
     * @return URL of CMS application.
     */
    public String getCmsUrl() {
        return cmsUrl;
    }

    /**
     * @return URL of CMS application.
     */
    public String getSiteUrl() {
        return siteUrl;
    }
}
