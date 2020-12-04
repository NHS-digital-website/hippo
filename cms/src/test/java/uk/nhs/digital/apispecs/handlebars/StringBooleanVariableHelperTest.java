package uk.nhs.digital.apispecs.handlebars;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.apispecs.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_INVERSE_BLOCK;
import static uk.nhs.digital.apispecs.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK;

import com.github.jknack.handlebars.Options;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import uk.nhs.digital.apispecs.handlebars.OptionsStub.Data;
import uk.nhs.digital.test.util.RandomTestUtils;

import java.io.IOException;

public class StringBooleanVariableHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock Options.Buffer buffer;


    private final StringBooleanVariableHelper helper = StringBooleanVariableHelper.INSTANCE;

    private final String variableName = RandomTestUtils.randomString("variableName_");

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void rendersMainBlock_whenStringVariableOfGivenNameIsFalse() throws IOException {

        // given
        final Options options = OptionsStub.with(buffer, Data.of(variableName, "false"));

        // when
        final Options.Buffer actualBuffer = helper.apply(variableName, options);

        // then
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK);

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersInverseBlock_whenStringVariableOfGivenNameIsTrue() throws IOException {

        // given
        final Options options = OptionsStub.with(buffer, Data.of(variableName, "true"));

        // when
        final Options.Buffer actualBuffer = helper.apply(variableName, options);

        // then
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersMainBlock_whenStringVariableOfGivenNameIsEmptyString() throws IOException {

        // given
        final Options options = OptionsStub.with(buffer, Data.of(variableName, ""));

        // when
        final Options.Buffer actualBuffer = helper.apply(variableName, options);

        // then
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK);

        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void throwsException_onSchemaRenderingFailure() throws IOException {

        // given
        final RuntimeException expectedCauseException = new RuntimeException();
        given(buffer.append(any())).willThrow(expectedCauseException);

        final Options options = OptionsStub.with(buffer, Data.of(variableName, "irrelevant value"));

        expectedException.expectMessage("Failed to interpret value of variable " + variableName + ".");
        expectedException.expect(TemplateRenderingException.class);
        expectedException.expectCause(sameInstance(expectedCauseException));

        // when
        helper.apply(variableName, options);

        // then
        // expectations set up in 'given' are satisfied
    }
}