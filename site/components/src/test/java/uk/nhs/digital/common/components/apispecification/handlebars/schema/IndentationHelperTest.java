package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mock;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.Hash;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub;
import uk.nhs.digital.common.components.apispecification.handlebars.TemplateRenderingException;

public class IndentationHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private IndentationHelper indentationHelper = new IndentationHelper(new ContextModelsStack.Factory(new UniqueModelStackExtractor()));

    @Test
    public void returnsIndentationLevel_equalToNumberOfAllUniqueModelsFromTheContextStack() {

        // given
        final int ancestorCount = RandomUtils.nextInt();
        final String expectedIndentationLevel = String.valueOf(ancestorCount);

        final Context context = Context.newContext(new Object());

        final ContextModelsStack contextModelsStack = mock(ContextModelsStack.class);
        given(contextModelsStack.ancestorModelsCount()).willReturn(ancestorCount);

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);

        final IndentationHelper indentationHelper = IndentationHelper.with(contextStackFactory);

        final Options options = OptionsStub.with(context);

        // when
        final String actualIndentationLevel = indentationHelper.apply(null, options);

        // then
        assertThat("Total number of ancestor Contexts is returned.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void returnsWeightedIndentationLevel_whenWeightingSet() {

        // given
        final int ancestorCount = RandomUtils.nextInt();
        final double weighting = 1.5;
        final String expectedIndentationLevel = String.format("%.1f",ancestorCount * weighting);

        final Context context = Context.newContext(new Object());

        final ContextModelsStack contextModelsStack = mock(ContextModelsStack.class);
        given(contextModelsStack.ancestorModelsCount()).willReturn(ancestorCount);

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);

        final IndentationHelper indentationHelper = IndentationHelper.with(contextStackFactory);

        final Hash hash = Hash.of("weighting", weighting);
        final Options options = OptionsStub.with(context, hash);

        // when
        final String actualIndentationLevel = indentationHelper.apply(null, options);

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
        final Options options = OptionsStub.with(irrelevantContext);

        // when
        indentationHelper.apply(null, options);

        // then
        // expectations set in 'given' are satisfied
    }
}
