package uk.nhs.digital.apispecs.handlebars;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mock;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IndentationHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();
    
    private IndentationHelper indentationHelper = new IndentationHelper(new ContextModelsStack.Factory(new UniqueModelStackExtractor()));

    @Test
    public void returnsIndentationLevel_equalToNumberOfAllUniqueModelsFromTheContextStack() {

        // given
        final int expectedIndentationLevel = RandomUtils.nextInt();

        final Context context = Context.newContext(new Object());

        final ContextModelsStack contextModelsStack = mock(ContextModelsStack.class);
        given(contextModelsStack.ancestorModelsCount()).willReturn(expectedIndentationLevel);

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);

        final IndentationHelper indentationHelper = IndentationHelper.with(contextStackFactory);

        final Options options = new OptionsStub(context);

        // when
        final Integer actualIndentationLevel = indentationHelper.apply(null, options);

        // then
        assertThat("Total number of ancestor Contexts is returned.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void throwsException_onCalculationFailure() {

        // given
        final RuntimeException expectedCauseException = new RuntimeException();

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(any())).willThrow(expectedCauseException);

        indentationHelper = IndentationHelper.with(contextStackFactory);

        expectedException.expect(TemplateRenderingException.class);
        expectedException.expectMessage("Failed to calculate indentation level.");
        expectedException.expectCause(sameInstance(expectedCauseException));

        final Context irrelevantContext = Context.newContext("irrelevant model");
        final Options options = new OptionsStub(irrelevantContext);

        // when
        indentationHelper.apply(null, options);

        // then
        // expectations set in 'given' are satisfied
    }

    private static class OptionsStub extends Options {

        public OptionsStub(final Context context) {
            super(null, null, null, context, null, null, null, null, emptyList());
        }
    }
}