package uk.nhs.digital.apispecs.swagger;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IndentationHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();
    
    public static Integer DEFAULT_INDENTATION_FACTOR = 1;

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
        final Long actualIndentationLevel = indentationHelper.apply(DEFAULT_INDENTATION_FACTOR, options);

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
        final Long actualIndentationLevel = indentationHelper.apply(DEFAULT_INDENTATION_FACTOR, options);

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
        final Long actualIndentationLevel = indentationHelper.apply(DEFAULT_INDENTATION_FACTOR, options);

        // then
        assertThat("Number of ancestor Contexts with unique models is returned.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void appliesIndentationFactor_whenProvided() {

        // given
        final long expectedIndentationLevel = 33;

        final Context context0 = Context.newContext("model A");
        final Context context1 = Context.newContext(context0, "model B");
        final Context context2 = Context.newContext(context1, "model C");
        final Context contextStack = Context.newContext(context2, "model D");

        final Options options = new OptionsStub(contextStack);

        final int nonDefaultIndentationFactor = 11;

        // when
        final Long actualIndentationLevel = indentationHelper.apply(nonDefaultIndentationFactor, options);

        // then
        assertThat("Indentation level is multiplied by provided factor.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void appliesDefaultIndentationFactor_whenCurrentNonIntegerModelProvidedAsArg() {

        // This edge happens when the helper is invoked with no arguments,
        // in which case Handlebars passes in current context's model.

        // given
        final long expectedIndentationLevel = 3;

        final String nonIntegerModel = "non integer model";

        final Context context0 = Context.newContext("model A");
        final Context context1 = Context.newContext(context0, "model B");
        final Context context2 = Context.newContext(context1, "model C");
        final Context contextStack = Context.newContext(context2, nonIntegerModel);

        final Options options = new OptionsStub(contextStack);

        // when
        final Long actualIndentationLevel = indentationHelper.apply(nonIntegerModel, options);

        // then
        assertThat("Indentation level is multiplied by provided factor.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void appliesDefaultIndentationFactor_whenCurrentIntegerModelProvidedAsArg() {

        // This edge happens when the helper is invoked with no arguments,
        // in which case Handlebars passes in current context's model.

        // given
        final long expectedIndentationLevel = 3;

        final Integer integerModel = 55;

        final Context context0 = Context.newContext("model A");
        final Context context1 = Context.newContext(context0, "model B");
        final Context context2 = Context.newContext(context1, "model C");
        final Context contextStack = Context.newContext(context2, integerModel);

        final Options options = new OptionsStub(contextStack);

        // when
        final Long actualIndentationLevel = indentationHelper.apply(integerModel, options);

        // then
        assertThat("Indentation level is multiplied by default factor.",
            actualIndentationLevel,
            is(expectedIndentationLevel)
        );
    }

    @Test
    public void throwsException_whenIndentationFactorIsNonIntegerOtherThanCurrentModel() {

        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Indentation factor has to be positive integer but was: non integer other than current model.");

        final Context context = Context.newContext("model A");

        final Options options = new OptionsStub(context);

        final String nonIntegerOtherThanCurrentModel = "non integer other than current model";

        // when
        indentationHelper.apply(nonIntegerOtherThanCurrentModel, options);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void throwsException_whenIndentationFactorIsNegative() {

        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Indentation factor has to be positive integer but was: -1.");

        final Context context = Context.newContext("model A");

        final Options options = new OptionsStub(context);

        final int illegalIndentationFactor = -1;

        // when
        indentationHelper.apply(illegalIndentationFactor, options);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void throwsException_whenIndentationFactorIsNull() {

        // given
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Indentation factor has to be positive integer but was: null.");

        final Context context = Context.newContext("model A");

        final Options options = new OptionsStub(context);

        final Integer nullIndentationFactor = null;

        // when
        indentationHelper.apply(nullIndentationFactor, options);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void throwsException_onExtraParams() {

        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unknown parameters provided.");

        final Context context = Context.newContext("model A");

        final Options options = new OptionsStub(context, new String[]{"unexpected param"});

        // when
        indentationHelper.apply(DEFAULT_INDENTATION_FACTOR, options);

        // then
        // expectations set in 'given' are satisfied
    }

    public static class OptionsStub extends Options {

        public OptionsStub(final Context context) {
            super(null, null, null, context, null, null, null, null, emptyList());
        }

        public OptionsStub(final Context context, final Object[] params) {
            super(null, null, null, context, null, null, params, null, emptyList());
        }
    }
}