package uk.nhs.digital.common.components.apispecification.handlebars;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_INVERSE_BLOCK;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

public class IfNotNullHelperTest {

    private static final int RANDOM_NUMBER = RandomUtils.nextInt();

    private Options options;

    @Mock private Options.Buffer buffer;

    private final IfNotNullHelper ifNotNullHelper = IfNotNullHelper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
        options = OptionsStub.with(buffer);
    }

    @Test
    public void rendersMainBlock_whenModelIsNotNull() throws IOException {

        // given
        final Object nonNullModel = RANDOM_NUMBER;

        // when
        final Options.Buffer actualBuffer = ifNotNullHelper.apply(nonNullModel, options);

        // then
        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK);
    }

    @Test
    public void rendersInverseBlock_whenModelIsNull() throws IOException {

        // given
        final Object nullObject = null;

        // when
        final Options.Buffer actualBuffer = ifNotNullHelper.apply(nullObject, options);

        // then
        assertThat("Returns buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }

    @Test(expected = RuntimeException.class)
    public void throwsException_onAnyFailure() throws IOException {

        // given
        final Integer anyNumber = RANDOM_NUMBER;

        final RuntimeException expectedCause = new RuntimeException();
        given(buffer.append(any())).willThrow(expectedCause);

        // when
        ifNotNullHelper.apply(anyNumber, options);
        fail("Failed to render property; provided value: " + anyNumber);

        // then
        // expectations in 'given' are satisfied
    }
}