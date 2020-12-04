package uk.nhs.digital.apispecs.handlebars.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jknack.handlebars.Options;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.apispecs.handlebars.OptionsStub;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(DataProviderRunner.class)
public class TypeAnyHelperTest {

    private static final String EMPTY_STRING = "";

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private Options.Buffer buffer;

    private JsonNodeFactory jsonFactory = new JsonNodeFactory(true);
    private Options options;

    private TypeAnyHelper helper = TypeAnyHelper.INSTANCE;

    @Before
    public void setUp() {
        initMocks(this);

        options = OptionsStub.with(buffer);
    }

    @Test
    public void rendersMainBlock_forJsonObject() throws IOException {

        // given
        final ObjectNode jsonObject = new ObjectNode(jsonFactory);

        // when
        final Options.Buffer actualBuffer = helper.apply(jsonObject, options);

        // then
        then(actualBuffer).should().append(OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK);

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersMainBlock_forArrayOfJsonObjects() throws IOException {

        // given
        final ArrayNode arrayWithJsonObject = new ArrayNode(jsonFactory).add(new ObjectNode(jsonFactory));

        // when
        final Options.Buffer actualBuffer = helper.apply(arrayWithJsonObject, options);

        // then
        then(actualBuffer).should().append(OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK);

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersInverseBlock_forArrayOfSimpleValues() throws IOException {

        // given
        final ArrayNode arrayWithSimpleValues = new ArrayNode(jsonFactory).add("simple value");

        // when
        final Options.Buffer actualBuffer = helper.apply(arrayWithSimpleValues, options);

        // then
        then(actualBuffer).should().append(OptionsStub.TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersInverseBlock_forEmptyArray() throws IOException {

        // given
        final ArrayNode arrayWithSimpleValues = new ArrayNode(jsonFactory);

        // when
        final Options.Buffer actualBuffer = helper.apply(arrayWithSimpleValues, options);

        // then
        then(actualBuffer).should().append(OptionsStub.TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersEmptyString_whenModelIsNull() throws IOException {

        // given
        final ObjectNode nullObject = null;

        // when
        final Options.Buffer actualBuffer = helper.apply(nullObject, options);

        // then
        then(actualBuffer).should().append(EMPTY_STRING);

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersEmptyString_forModelOfIgnoredType() throws IOException {

        // given
        final String typeToIgnoreName = "java.util.Map";
        //noinspection rawtypes
        final Map modelOfIgnoredType = new HashMap();

        options = OptionsStub.with(buffer, OptionsStub.Hash.of("ignoreClass", typeToIgnoreName));

        // when
        final Options.Buffer actualBuffer = helper.apply(modelOfIgnoredType, options);

        // then
        then(actualBuffer).should().append(EMPTY_STRING);

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void throwsException_onAnyFailure() throws IOException {

        // given
        final RuntimeException expectedCause = new RuntimeException();
        given(buffer.append(any())).willThrow(expectedCause);

        expectedException.expect(SchemaRenderingException.class);
        expectedException.expectMessage("Failed to render value of type 'any' for: model");
        expectedException.expectCause(sameInstance(expectedCause));

        final String irrelevantModel = "model";

        // when
        helper.apply(irrelevantModel, options);

        // then
        // expectations in 'given' are satisfied
    }

}