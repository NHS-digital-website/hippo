package uk.nhs.digital.common.components.apispecification.handlebars;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.openMocks;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_INVERSE_BLOCK;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK;

import com.github.jknack.handlebars.Options;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Collection;

public class HasOneItemHelperTest {

    private final HasOneItemHelper hasOneItemHelper = HasOneItemHelper.INSTANCE;

    private Options options;

    @Mock private Options.Buffer buffer;

    @Before
    public void setUp() throws Exception {
        openMocks(this);
        options = OptionsStub.with(buffer);
    }

    @Test
    public void rendersBlockWhenCollectionHasExactlyOneItem() throws IOException {

        // given
        final Collection<?> collectionWithOneItem = singletonList("single item in the list, actual content irrelevant");

        // when
        final Options.Buffer actualBuffer = hasOneItemHelper.apply(collectionWithOneItem, options);

        // then
        assertThat("Returns Buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK);
    }

    @Test
    public void doesNotRenderBlockWhenCollectionIsNull() throws IOException {

        // given
        final Collection<?> nullCollection = null;

        // when
        final Options.Buffer actualBuffer = hasOneItemHelper.apply(nullCollection, options);

        // then
        assertThat("Returns Buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }

    @Test
    public void doesNotRenderBlockWhenCollectionHasZeroItems() throws IOException {

        // given
        final Collection<?> emptyCollection = emptyList();

        // when
        final Options.Buffer actualBuffer = hasOneItemHelper.apply(emptyCollection, options);

        // then
        assertThat("Returns Buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }

    @Test
    public void doesNotRenderBlockWhenCollectionHasManyItems() throws IOException {

        // given
        final Collection<?> collectionWithManyItems = asList(
            "first item in the list, actual content irrelevant",
            "second item in the list, actual content irrelevant"
        );

        // when
        final Options.Buffer actualBuffer = hasOneItemHelper.apply(collectionWithManyItems, options);

        // then
        assertThat("Returns Buffer provided by Handlebars through Options.",
            actualBuffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }
}