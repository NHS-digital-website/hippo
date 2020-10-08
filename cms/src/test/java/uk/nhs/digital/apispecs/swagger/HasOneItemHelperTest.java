package uk.nhs.digital.apispecs.swagger;

import static com.onehippo.cms7.inference.engine.core.util.ArraysUtils.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Collection;

public class HasOneItemHelperTest {

    private static final String TEMPLATE_CONTENT_FROM_THE_POSITIVE_BLOCK = RandomStringUtils.random(10);
    private static final String TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK = RandomStringUtils.random(10);

    private final HasOneItemHelper hasOneItemHelper = HasOneItemHelper.INSTANCE;

    @Mock private Options options;
    @Mock private Options.Buffer buffer;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        given(options.fn()).willReturn(TEMPLATE_CONTENT_FROM_THE_POSITIVE_BLOCK);
        given(options.inverse()).willReturn(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
        given(options.buffer()).willReturn(buffer);
    }

    @Test
    public void rendersBlockWhenCollectionHasExactlyOneItem() throws IOException {

        // given
        final Collection<?> collectionWithOneItem = singletonList("single item in the list, actual content irrelevant");

        // when
        final Object buffer = hasOneItemHelper.apply(collectionWithOneItem, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            instanceOf(Options.Buffer.class)
        );

        final Options.Buffer actualBuffer = (Options.Buffer) buffer;

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_POSITIVE_BLOCK);
    }

    @Test
    public void doesNotRenderBlockWhenCollectionIsNull() throws IOException {

        // given
        final Collection<?> nullCollection = null;

        // when
        final Object buffer = hasOneItemHelper.apply(nullCollection, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            instanceOf(Options.Buffer.class)
        );

        final Options.Buffer actualBuffer = (Options.Buffer) buffer;

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
    }

    @Test
    public void doesNotRenderBlockWhenCollectionHasZeroItems() throws IOException {

        // given
        final Collection<?> emptyCollection = emptyList();

        // when
        final Object buffer = hasOneItemHelper.apply(emptyCollection, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            instanceOf(Options.Buffer.class)
        );

        final Options.Buffer actualBuffer = (Options.Buffer) buffer;

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
    }

    @Test
    public void doesNotRenderBlockWhenCollectionHasManyItems() throws IOException {

        // given
        final Collection<?> collectionWithManyItems = asList(
            "first item in the list, actual content irrelevant",
            "second item in the list, actual content irrelevant"
        );

        // when
        final Object buffer = hasOneItemHelper.apply(collectionWithManyItems, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            instanceOf(Options.Buffer.class)
        );

        final Options.Buffer actualBuffer = (Options.Buffer) buffer;

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
    }
}