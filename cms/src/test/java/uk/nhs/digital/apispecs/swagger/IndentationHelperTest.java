package uk.nhs.digital.apispecs.swagger;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mock;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.apispecs.handlebars.IndentationHelper;
import uk.nhs.digital.apispecs.handlebars.TemplateRenderingException;

@RunWith(PowerMockRunner.class)
public class IndentationHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();
    
    private final IndentationHelper indentationHelper = IndentationHelper.INSTANCE;

    @Test
    public void returnsNumberOfAllAncestorsOfTheCurrentContext_whenContextStackReferencesOnlyUniqueModels() {

        // given
        final long expectedIndentationLevel = 2;

        final Context context0 = Context.newContext("model A");
        final Context context1 = Context.newContext(context0, "model B");
        final Context stackWithOnlyUniqueModels = Context.newContext(context1, "model C");

        final Options options = new OptionsStub(stackWithOnlyUniqueModels);

        // when
        final Long actualIndentationLevel = indentationHelper.apply(null, options);

        // then
        assertThat("Total number of ancestor Contexts is returned.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void returnsNumberOfUniqueAncestorsOfTheCurrentContext_whenContextStackReferencesDuplicateModels() {

        // given
        final long expectedIndentationLevel = 1;

        final Context context0 = Context.newContext("model A");
        final Context context1 = Context.newContext(context0, "model A");
        final Context stackWithDuplicateModels = Context.newContext(context1, "model C");

        final Options options = new OptionsStub(stackWithDuplicateModels);

        // when
        final Long actualIndentationLevel = indentationHelper.apply(null, options);

        // then
        assertThat("Number of ancestor Contexts with unique models is returned.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    @SuppressWarnings({"StringOperationCanBeSimplified", "StringEquality"})
    public void doesNotConsiderModelsInTheStackDuplicate_whenEqualByValueButNotReference() {

        // given
        final long expectedIndentationLevel = 2;

        // Using String constructor generates new reference
        final String model_a_1 = new String("model A");
        final String model_a_2 = new String("model A");
        assert model_a_1 != model_a_2; // ...as illustrated by this assumption.

        final Context context0 = Context.newContext(model_a_1);
        final Context context1 = Context.newContext(context0, model_a_2);
        final Context stackWithDuplicateModels = Context.newContext(context1, "model C");

        final Options options = new OptionsStub(stackWithDuplicateModels);

        // when
        final Long actualIndentationLevel = indentationHelper.apply(null, options);

        // then
        assertThat("Number of ancestor Contexts with unique models is returned.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void returnsZero_whenSingleContextInStack() {

        // given
        final long expectedIndentationLevel = 0;

        final Context stackWithSingleContext = Context.newContext("model A");

        final Options options = new OptionsStub(stackWithSingleContext);

        // when
        final Long actualIndentationLevel = indentationHelper.apply(null, options);

        // then
        assertThat("Number of ancestor Contexts with unique models is returned.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    @PrepareForTest(Context.class)
    public void throwsException_onCalculationFailure() {

        // given
        final RuntimeException expectedCauseException = new RuntimeException();

        // This contrived way of causing an exception to be thrown has been used
        // because there isn't much it the tested method that can actually go wrong,
        // but we do want to ensure that should anything does go wrong (say, due to
        // future changes) we throw a meaningful and useful exception.
        final Context context = mock(Context.class);
        given(context.parent()).willThrow(expectedCauseException);

        expectedException.expect(TemplateRenderingException.class);
        expectedException.expectMessage("Failed to calculate indentation level.");
        expectedException.expectCause(sameInstance(expectedCauseException));

        final Options options = new OptionsStub(context);

        // when
        indentationHelper.apply(null, options);

        // then
        // expectations set in 'given' are satisfied
    }

    public static class OptionsStub extends Options {

        public OptionsStub(final Context context) {
            super(null, null, null, context, null, null, null, null, emptyList());
        }
    }
}