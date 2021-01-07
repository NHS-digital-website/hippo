package uk.nhs.digital.apispecs.handlebars;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.*;

public class IsAnyTrueHelperTest {

    private static final String TEMPLATE_CONTENT_FROM_THE_MAIN_BLOCK = RandomStringUtils.random(10);
    private static final String TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK = RandomStringUtils.random(10);

    private final IsAnyTrueHelper isAnyTrueHelper = IsAnyTrueHelper.INSTANCE;

    @Mock private Options.Buffer buffer;
    private String item = "some-item";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void rendersBlockWhenCollectionHasAtLeastOneTrueItem() throws IOException {

        Boolean[] params = {false,false,true,false,false};
        StubOptions options = new StubOptions(params);

        // when
        final Options.Buffer actualBuffer = (Options.Buffer) isAnyTrueHelper.apply(item, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_MAIN_BLOCK);
    }

    @Test
    public void rendersBlockWhenCollectionHasNoTrueItem() throws IOException {

        Boolean[] params = {false,false,false,false,false};
        StubOptions options = new StubOptions(params);

        // when
        final Options.Buffer actualBuffer = (Options.Buffer) isAnyTrueHelper.apply(item, options);

        // then
        assertThat("Returns Options.Buffer",
            buffer,
            sameInstance(actualBuffer)
        );

        then(actualBuffer).should().append(TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK);
    }

    @Test
    public void throwExceptionWhenCollectionIsEmpty() throws IOException {

        Boolean[] params = {};
        StubOptions options = new StubOptions(params);

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
    public void throwExceptionWhenCollectionIsNull() throws IOException {

        Boolean[] params = null;
        StubOptions options = new StubOptions(params);

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
    public void throwExceptionWhenAtLeastOneIsNull() throws IOException {
        Boolean[] params = {false, null};
        StubOptions options = new StubOptions(params);

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
    public void throwExceptionWhenAtLeastOneIsNonBoolean() throws IOException {
        Object[] params = {false, "test"};
        StubOptions options = new StubOptions(params);

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

    class StubOptions extends Options {

        public StubOptions(Object[] params) {
            super(null, null, null, null, null, null, params, null, Collections.emptyList());
        }

        @Override
        public CharSequence fn() throws IOException {
            return TEMPLATE_CONTENT_FROM_THE_MAIN_BLOCK;
        }

        @Override
        public CharSequence inverse() throws IOException {
            return TEMPLATE_CONTENT_FROM_THE_INVERSE_BLOCK;
        }

        @Override
        public Buffer buffer() {
            return buffer;
        }
    }
}
