package uk.nhs.digital.arc.json;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ArchiveTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void assertThatExpectNoReferencedUrlsFromAnElementThatHasNoProvisionForThem() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/arc-json/archive.json");
        Archive archive = (Archive) mapper.readValue(in, Archive.class);

        List<String> urls = archive.getAllReferencedExternalUrls();
        assertEquals(0, urls.size());
    }
}