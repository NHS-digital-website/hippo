package uk.nhs.digital.apispecs.swagger.request.bodyextractor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import java.util.*;

public class RequestBodyMediaTypeObjectTest {

    @Test
    public void getExamples_returnsExamplesFromValuesOfInternalMap() {

        // given
        final ParamExample expectedExample1 = mock(ParamExample.class);
        final ParamExample expectedExample2 = mock(ParamExample.class);

        final RequestBodyMediaTypeObject requestBodyMediaTypeObject = new RequestBodyMediaTypeObject();

        final Map<String, ParamExample> expectedExamples = new HashMap<>();
        expectedExamples.put("example-key-1", expectedExample1);
        expectedExamples.put("example-key-2", expectedExample2);
        ReflectionTestUtils.setField(requestBodyMediaTypeObject, "examples", expectedExamples);

        // when
        final Collection<ParamExample> actualParamExamples = requestBodyMediaTypeObject.getExamples();

        // then
        assertThat("Returns all examples from the internal map", actualParamExamples.size(), is(2));

        final List<ParamExample> actualExamples = new ArrayList<>(actualParamExamples);

        final ParamExample actualExample1 = actualExamples.get(0);
        assertThat("First example object comes is a value from the internal map",
            actualExample1, sameInstance(expectedExample1)
        );
        verifyZeroInteractions(actualExample1);

        final ParamExample actualExample2 = actualExamples.get(1);
        assertThat("Second example object comes is a value from the internal map",
            actualExample2, sameInstance(expectedExample2)
        );
        verifyZeroInteractions(actualExample2);
    }
}