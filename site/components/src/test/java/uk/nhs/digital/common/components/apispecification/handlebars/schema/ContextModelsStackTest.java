package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@RunWith(DataProviderRunner.class)
public class ContextModelsStackTest {

    @Test
    public void currentModel_returnsFirstModel() {

        // given
        final Object grandparentModel = new Object();
        final Object parentModel = new Object();
        final Object currentModel = new Object();

        final ContextModelsStack contextModelsStack = new ContextModelsStack(
            asList(currentModel, parentModel, grandparentModel)
        );

        // when
        final Optional<Object> actualModelReturned = contextModelsStack.currentModel();

        // then
        assertThat(
            "Model from the current Context is produced.",
            actualModelReturned.orElse(null),
            is(sameInstance(currentModel))
        );
    }

    @Test
    public void currentModel_returnsNotingWhenStackEmpty() {

        // given
        final ContextModelsStack contextModelsStack = new ContextModelsStack(
            emptyList()
        );

        // when
        final Optional<Object> actualModelReturned = contextModelsStack.currentModel();

        // then
        assertThat(
            "Nothing is returned.",
            actualModelReturned,
            is(Optional.empty())
        );
    }

    @Test
    public void parentModel_returnsSecondModel() {

        // given
        final Object grandparentModel = new Object();
        final Schema<?> parentModel = new Schema<>();
        final Object currentModel = new Object();

        final ContextModelsStack contextModelsStack = new ContextModelsStack(
            asList(currentModel, parentModel, grandparentModel)
        );

        // when
        final Optional<Object> actualModelReturned = contextModelsStack.currentModel();

        // then
        assertThat(
            "Model from the parent Context is produced.",
            actualModelReturned.orElse(null),
            is(sameInstance(currentModel))
        );
    }

    @Test
    @DataProvider(value = {"0", "1"})
    public void parentModel_returnsNothing_ifModelsStackHasLessThanTwoItems(final int stackSize) {

        // given
        final List<Integer> rawModelsStack = IntStream.rangeClosed(1, stackSize).boxed().collect(toList());

        final ContextModelsStack actualContextModelsStack = new ContextModelsStack(rawModelsStack);

        // when
        final Object actualModelReturned = actualContextModelsStack.parentModel();

        // then
        assertThat(
            "No model is produced.",
            actualModelReturned,
            is(Optional.empty())
        );
    }

    @Test
    @DataProvider(value = {
            "0 | 0",
            "1 | 0",
            "2 | 1",
            "3 | 2",
        }, splitBy = "\\|")
    public void ancestorsModelsCount_returnsSizeOfStackLessOne(final int stackSize, final int expectedAncestorsCount) {

        // given
        final List<Integer> rawModelsStack = IntStream.rangeClosed(1, stackSize).boxed().collect(toList());

        final ContextModelsStack contextModelsStack = new ContextModelsStack(rawModelsStack);

        // when
        final int actualAncestorsCount = contextModelsStack.ancestorModelsCount();

        // then
        assertThat(
            "Ancestors count is one less than the stack size for stacks longer than one element, zero otherwise.",
            actualAncestorsCount,
            is(expectedAncestorsCount)
        );
    }
}