package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestBodyTest {

    @Test
    public void getMediaTypes() {

        RequestBody requestBody = new RequestBody();
        RequestBodyMediaTypes mediaTypes = setContent();
        ReflectionTestUtils.setField(requestBody,"content",mediaTypes);

        Collection<RequestBodyMediaTypeContent> contentCollection = requestBody.getMediaTypes();

        RequestBodyMediaTypeContent content = contentCollection.stream().findFirst().get();

        Set<String> keyNames = mediaTypes.keySet();
        assertThat("Name derived from RequestBodyMediaTypes key", keyNames.contains(content.getName()));

        assertThat("Name derived from RequestBodyMediaTypes key",content.getName(),is("application/json"));

        assertThat("Verify content name and  example mapped correctly", content.getExamples().stream().findFirst().get().getDescription(),is("example-description-1"));
    }

    private RequestBodyMediaTypes setContent() {

        ParamExample paramExample1 = new ParamExample();
        paramExample1.setDescription("example-description-1");

        ParamExample paramExample2 = new ParamExample();
        paramExample2.setDescription("example-description-2");

        RequestBodyMediaTypeContent content1 = new RequestBodyMediaTypeContent();
        Map<String,ParamExample> examples1 = new HashMap<>();
        examples1.put("example1", paramExample1);
        ReflectionTestUtils.setField(content1,"examples", examples1);

        RequestBodyMediaTypeContent content2 = new RequestBodyMediaTypeContent();
        Map<String,ParamExample> examples2 = new HashMap<>();
        examples2.put("example2", paramExample2);
        ReflectionTestUtils.setField(content2,"examples", examples2);

        RequestBodyMediaTypes mediaTypes = new RequestBodyMediaTypes();
        mediaTypes.put("application/json", content1);
        mediaTypes.put("application/xml", content2);

        return mediaTypes;
    }
}
