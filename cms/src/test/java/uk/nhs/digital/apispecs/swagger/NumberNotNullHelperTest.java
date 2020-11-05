package uk.nhs.digital.apispecs.swagger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
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
import uk.nhs.digital.apispecs.handlebars.NumberNotNullHelper;

import java.io.IOException;

public class NumberNotNullHelperTest {

    private static final String TEMPLATE_CONTENT_FROM_THE_POSITIVE_BLOCK = RandomStringUtils.random(10);
    private static final String TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK = RandomStringUtils.random(10);
    private static final int RANDOM_NUMBER = RandomUtils.nextInt();

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private Options options;
    @Mock private Options.Buffer buffer;

    private final NumberNotNullHelper numberNotNullHelper = NumberNotNullHelper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        given(options.fn()).willReturn(TEMPLATE_CONTENT_FROM_THE_POSITIVE_BLOCK);
        given(options.inverse()).willReturn(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
        given(options.buffer()).willReturn(buffer);
    }

    @Test
    public void rendersPrimaryBlockWhenNumberIsNotNull() throws IOException {

        // given
        final Integer nonNullNumber = RANDOM_NUMBER;

        // when
        final Options.Buffer actualBuffer = numberNotNullHelper.apply(nonNullNumber, options);

        // then
        assertThat("Returns Buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_POSITIVE_BLOCK);
    }

    @Test
    public void rendersInverseBlockWhenNumberIsNull() throws IOException {

        // given
        final Integer nullNumber = null;

        // when
        final Options.Buffer actualBuffer = numberNotNullHelper.apply(nullNumber, options);

        // then
        assertThat("Returns Buffer provided by Handlebars through Options.",
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
        given(options.buffer()).willThrow(expectedCause);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Failed to render number conditionally; provided value: " + anyNumber);
        expectedException.expectCause(sameInstance(expectedCause));

        // when
        numberNotNullHelper.apply(anyNumber, options);

        // then
        // expectations in 'given' are satisfied
    }
}