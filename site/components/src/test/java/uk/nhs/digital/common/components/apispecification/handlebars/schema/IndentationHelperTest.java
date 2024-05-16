package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mock;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.Hash;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub;
import uk.nhs.digital.common.components.apispecification.handlebars.TemplateRenderingException;

public class IndentationHelperTest {

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

    @Test (expected = TemplateRenderingException.class)
    public void throwsException_onCalculationFailure() {

        // given
        final RuntimeException expectedCauseException = new RuntimeException();

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(any())).willThrow(expectedCauseException);

        IndentationHelper indentationHelper = IndentationHelper.with(contextStackFactory);

        final Context irrelevantContext = Context.newContext("irrelevant model");
        final Options options = OptionsStub.with(irrelevantContext);

        // when
        indentationHelper.apply(null, options);
        fail("Failed to calculate indentation level.");

        // then
        // expectations set in 'given' are satisfied
    }
}
