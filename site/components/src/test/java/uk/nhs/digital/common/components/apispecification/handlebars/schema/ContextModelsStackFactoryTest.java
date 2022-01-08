package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.jknack.handlebars.Context;
import org.junit.Test;

public class ContextModelsStackFactoryTest {

    @Test
    public void factory_createsInstance_fromGivenContext() {

        // given
        final Object expectedModel = new Object();
        final Context context = contextWith(expectedModel);

        final UniqueModelStackExtractor uniqueModelStackExtractor = mock(UniqueModelStackExtractor.class);
        given(uniqueModelStackExtractor.uniqueModelsStackFrom(context))
            .willReturn(singletonList(expectedModel));

        final ContextModelsStack.Factory contextStackFactory = new ContextModelsStack.Factory(uniqueModelStackExtractor);

        // when
        final ContextModelsStack actualContextModelsStack = contextStackFactory.from(context);

        // then
        assertThat("An instance of the models' stack was produced.",
            actualContextModelsStack,
            is(notNullValue())
        );

        assertThat("Current model reported by the models' stack is that of the context given to the factory.",
            actualContextModelsStack.currentModel().orElse(null),
            is(sameInstance(expectedModel))
        );
    }

    private Context contextWith(final Object model) {
        return Context.newContext(model);
    }
}