package uk.nhs.digital.arc.json;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class PublicationPageTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void assertThatWeCanFindReferencedUrls() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/publication_with_urls.json");
        PublicationPage publicationPage = (PublicationPage) mapper.readValue(in, PublicationPage.class);

        List<String> urls = publicationPage.getAllReferencedExternalUrls();
        assertEquals(1, urls.size());
    }

    @Test
    public void assertNoUrlsReeferencedWhenNoneinJsonFile() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/publication_no_urls.json");
        PublicationPage publicationPage = (PublicationPage) mapper.readValue(in, PublicationPage.class);

        List<String> urls = publicationPage.getAllReferencedExternalUrls();
        assertEquals(0, urls.size());
    }
}