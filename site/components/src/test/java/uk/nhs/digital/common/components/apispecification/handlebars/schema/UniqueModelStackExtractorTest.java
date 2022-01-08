package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.jknack.handlebars.Context;
import org.junit.Test;

import java.util.List;

public class UniqueModelStackExtractorTest {

    private final UniqueModelStackExtractor uniqueModelStackExtractor = new UniqueModelStackExtractor();

    @Test
    public void returnsModels_ofAllContextsInTheStack_whenContextStackReferencesOnlyUniqueModels() {

        // given
        final Object model_2 = new Object();
        final Object model_1 = new Object();
        final Object model_0 = new Object();

        final Context context_2 = Context.newContext(model_2);
        final Context context_1 = Context.newContext(context_2, model_1);
        final Context context_0 = Context.newContext(context_1, model_0);

        // when
        final List actualContextModelsStack = uniqueModelStackExtractor.uniqueModelsStackFrom(context_0);

        // then
        assertThat("All models from the Contexts stack are returned.",
            actualContextModelsStack,
            is(asList(
                model_0,
                model_1,
                model_2
            ))
        );
    }

    @Test
    public void returnsModels_withNoAdjacentDuplicates_whenContextStackReferencesDuplicateModelsBothAdjacentAndNotAdjacent() {

        // given
        final Object model_1 = new Object();
        final Object model_0 = new Object();

        final Context context_5 = Context.newContext(model_0);
        final Context context_4 = Context.newContext(context_5, model_0);
        final Context context_3 = Context.newContext(context_4, model_0);
        final Context context_2 = Context.newContext(context_3, model_1);
        final Context context_1 = Context.newContext(context_2, model_0);
        final Context context_0 = Context.newContext(context_1, model_0);

        // when
        final List actualContextModelsStack = uniqueModelStackExtractor.uniqueModelsStackFrom(context_0);

        // then
        assertThat("Unique models from the Contexts' stack are returned as long as they were not adjacent.",
            actualContextModelsStack,
            is(asList(
                model_0,
                model_1,
                model_0
            ))
        );
    }

    @Test
    @SuppressWarnings({"StringOperationCanBeSimplified", "StringEquality"})
    public void doesNotConsiderModelsInTheStackDuplicate_whenEqualByValueButNotReference() {

        // given
        final Object model_1 = new String("content equal in both models");
        final Object model_0 = new String("content equal in both models");

        final Context context_1 = Context.newContext(model_1);
        final Context context_0 = Context.newContext(context_1, model_0);

        // when
        final List actualContextModelsStack = uniqueModelStackExtractor.uniqueModelsStackFrom(context_0);

        // then
        assertThat("Models unique by reference are returned.",
            actualContextModelsStack,
            is(asList(
                model_0,
                model_1
            ))
        );
    }
}