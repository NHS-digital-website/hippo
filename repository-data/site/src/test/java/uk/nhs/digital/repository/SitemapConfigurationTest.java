package uk.nhs.digital.repository;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class SitemapConfigurationTest {

    private static final Path SITEMAP_ENTRY = Paths.get(
        "src/main/resources/hcm-config/hst/configurations/default/sitemap-entry.yaml");
    private static final Path HCM_CONFIG_ROOT = Paths.get("src/main/resources/hcm-config");

    @Test
    public void sitemapXmlUsesV2IndexComponent() throws IOException {
        Map<String, Object> sitemapXml = readSitemapXmlNode();

        assertThat("sitemap.xml configuration should exist", sitemapXml, is(notNullValue()));
        assertThat(
            "sitemap.xml must use the sitemap v2 index component",
            sitemapXml.get("hst:componentconfigurationid"),
            is("hst:components/forge-sitemapv2-default-index-feed"));
        assertThat("sitemap.xml should stay cacheable", sitemapXml.get("hst:cacheable"), is(true));
        assertThat(
            "sitemap.xml should stay hidden in the channel manager",
            sitemapXml.get("hst:hiddeninchannelmanager"),
            is(true));
        assertThat("sitemap.xml should keep a stable refId", sitemapXml.get("hst:refId"), is("sitemap"));
    }

    @Test
    public void legacySitemapComponentIsNotReferenced() throws IOException {
        final String legacyClassName = "org.onehippo.forge.sitemap.components";

        try (Stream<Path> files = Files.walk(HCM_CONFIG_ROOT)) {
            boolean legacyReferenceFound = files
                .filter(path -> path.toString().endsWith(".yaml") || path.toString().endsWith(".yml"))
                .map(SitemapConfigurationTest::readFileQuietly)
                .anyMatch(content -> content.contains(legacyClassName));

            assertThat("The legacy sitemap component must not be referenced anymore", legacyReferenceFound, is(false));
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> readSitemapXmlNode() throws IOException {
        try (InputStream inputStream = Files.newInputStream(SITEMAP_ENTRY)) {
            Map<String, Object> root = new Yaml().load(inputStream);
            Map<String, Object> definitions = (Map<String, Object>) root.get("definitions");
            Map<String, Object> config = (Map<String, Object>) definitions.get("config");
            return (Map<String, Object>) config.get(
                "/hst:hst/hst:configurations/hst:default/hst:sitemap/sitemap.xml");
        }
    }

    private static String readFileQuietly(final Path path) {
        try {
            return Files.readString(path, UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read " + path, exception);
        }
    }
}
