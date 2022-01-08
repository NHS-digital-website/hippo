package uk.nhs.digital.common.components.apispecification.handlebars;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_INVERSE_BLOCK;
import static uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK;

import com.github.jknack.handlebars.Options;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

public class IsAnyTrueHelperTest {

    private final IsAnyTrueHelper isAnyTrueHelper = IsAnyTrueHelper.INSTANCE;

    @Mock private Options.Buffer buffer;

    private String item = "some-item";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void rendersBlockWhenCollectionHasAtLeastOneTrueItem() throws IOException {

        final Boolean[] params = {false,false,true,false,false};
        final Options options = OptionsStub.with(params, buffer);

        // when
        final Options.Buffer actualBuffer = (Options.Buffer) isAnyTrueHelper.apply(item, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK);
    }

    @Test
    public void rendersBlockWhenCollectionHasNoTrueItem() throws IOException {

        final Boolean[] params = {false,false,false,false,false};
        final Options options = OptionsStub.with(params, buffer);

        // when
        final Options.Buffer actualBuffer = (Options.Buffer) isAnyTrueHelper.apply(item, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_INVERSE_BLOCK);
    }

    @Test
    public void throwExceptionWhenCollectionIsEmpty() throws IOException {

        final Boolean[] params = {};
        final Options options = OptionsStub.with(params, buffer);

        try {
            // when
            isAnyTrueHelper.apply(item, options);

            fail("An exception was expected but none was thrown.");
        } catch (IllegalArgumentException e) {

            // then
            assertThat("Exception in execution",
                e.getMessage(),
                containsString("At least one parameter is required but none was provided.")
            );
        }
    }

    @Test
    public void throwExceptionWhenCollectionIsNull() {

        final Boolean[] params = null;
        final Options options = OptionsStub.with(params, buffer);

        try {
            // when
            isAnyTrueHelper.apply(item, options);

            fail("An exception was expected but none was thrown.");
        } catch (Exception e) {

            // then
            assertThat("Exception in execution",
                e.getMessage(),
                containsString("At least one parameter is required but none was provided.")
            );
        }
    }

    @Test
    public void throwExceptionWhenAtLeastOneIsNull() {

        final Boolean[] params = {false, null};
        final Options options = OptionsStub.with(params, buffer);

        try {
            // when
            isAnyTrueHelper.apply(item,options);

            fail("An exception was expected but none was thrown.");
        } catch (Exception e) {

            // then
            assertThat("Exception in execution",
                e.getMessage(),
                containsString("All parameters are required to be boolean but at least one provided value was not.")
            );
        }
    }

    @Test
    public void throwExceptionWhenAtLeastOneIsNonBoolean() {

        final Object[] params = {false, "test"};
        final Options options = OptionsStub.with(params, buffer);

        try {
            // when
            isAnyTrueHelper.apply(item,options);

            fail("An exception was expected but none was thrown.");
        } catch (Exception e) {

            // then
            assertThat("Exception in execution",
                e.getMessage(),
                containsString("All parameters are required to be boolean but at least one provided value was not.")
            );
        }
    }
}
