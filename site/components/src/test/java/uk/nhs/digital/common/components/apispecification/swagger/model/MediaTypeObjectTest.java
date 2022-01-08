package uk.nhs.digital.common.components.apispecification.swagger.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.nhs.digital.test.util.AssertionUtils.assertClassHasFieldWithAnnotationWithAttributeValue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;
import uk.nhs.digital.common.components.apispecification.swagger.request.bodyextractor.ToPrettyJsonStringDeserializer;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import java.util.*;

public class MediaTypeObjectTest {

    @Test
    public void exampleValueIsDeserialised_usingCustomDeserializer() {

        assertClassHasFieldWithAnnotationWithAttributeValue(
            MediaTypeObject.class, "example",
            JsonDeserialize.class, "using", ToPrettyJsonStringDeserializer.class
        );
    }

    @Test
    public void getExamples_returnsExamplesFromValuesOfInternalMap() {

        // given
        final ExampleObject expectedExample1 = mock(ExampleObject.class);
        final ExampleObject expectedExample2 = mock(ExampleObject.class);

        final MediaTypeObject mediaTypeObject = new MediaTypeObject();

        final Map<String, ExampleObject> expectedExamples = new HashMap<>();
        expectedExamples.put("example-key-1", expectedExample1);
        expectedExamples.put("example-key-2", expectedExample2);
        ReflectionTestUtils.setField(mediaTypeObject, "examples", expectedExamples);

        // when
        final Collection<ExampleObject> actualExampleObjects = mediaTypeObject.getExamples();

        // then
        assertThat("Returns all examples from the internal map", actualExampleObjects.size(), is(2));

        final List<ExampleObject> actualExamples = new ArrayList<>(actualExampleObjects);

        final ExampleObject actualExample1 = actualExamples.get(0);
        assertThat("First example object comes is a value from the internal map",
            actualExample1, sameInstance(expectedExample1)
        );
        verifyZeroInteractions(actualExample1);

        final ExampleObject actualExample2 = actualExamples.get(1);
        assertThat("Second example object comes is a value from the internal map",
            actualExample2, sameInstance(expectedExample2)
        );
        verifyZeroInteractions(actualExample2);
    }
}