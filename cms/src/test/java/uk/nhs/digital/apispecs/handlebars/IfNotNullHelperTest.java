package uk.nhs.digital.apispecs.handlebars;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.io.IOException;

public class IfNotNullHelperTest {

    private static final String TEMPLATE_CONTENT_FROM_THE_MAIN_BLOCK = randomString("main_block_content_");
    private static final String TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK = randomString("inverse_block_content_");

    private static final int RANDOM_NUMBER = RandomUtils.nextInt();

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private Options options;
    @Mock private Options.Buffer buffer;

    private final IfNotNullHelper ifNotNullHelper = IfNotNullHelper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        given(options.buffer()).willReturn(buffer);
        given(options.fn()).willReturn(TEMPLATE_CONTENT_FROM_THE_MAIN_BLOCK);
        given(options.inverse()).willReturn(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
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

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_MAIN_BLOCK);
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

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
    }

    @Test
    public void throwsException_onAnyFailure() throws IOException {

        // given
        final Integer anyNumber = RANDOM_NUMBER;

        final RuntimeException expectedCause = new RuntimeException();
        given(buffer.append(any())).willThrow(expectedCause);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Failed to render property; provided value: " + anyNumber);
        expectedException.expectCause(sameInstance(expectedCause));

        // when
        ifNotNullHelper.apply(anyNumber, options);

        // then
        // expectations in 'given' are satisfied
    }

    private static String randomString(final String prefix) {
        return prefix + RandomStringUtils.random(10, true, true);
    }
}