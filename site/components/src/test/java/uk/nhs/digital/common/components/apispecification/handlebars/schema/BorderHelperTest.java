package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mock;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import com.google.common.collect.ImmutableMap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.common.components.apispecification.handlebars.BorderHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.OptionsStub;

import java.util.ArrayList;


@RunWith(DataProviderRunner.class)
public class BorderHelperTest {
    private BorderHelper borderHelper = new BorderHelper(new ContextModelsStack.Factory(new UniqueModelStackExtractor()));
    private static final String[] lightnessLevels = new String[]{"31.4%", "34.0%"};

    @Test
    public void doesNotReturnParentBorder_whenElementHasNone() {
        // given
        final Context context = Context.newContext(new Object());

        final Schema currentModel = new Schema().properties(
            ImmutableMap.of("child_1", new Schema())
        );

        final ContextModelsStack contextModelsStack = new ContextModelsStack(asList(currentModel));

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);
        borderHelper = BorderHelper.with(contextStackFactory);

        final Options options = OptionsStub.with(context);

        // when
        final CharSequence expectedBorders = getBorder(SchemaBorder.CHILD,  0);
        final CharSequence actualBorders = borderHelper.apply(null, options).toString();

        // then
        assertThat("Helper returns HTML for child element's border and does not return any other vertical borders",
            actualBorders,
            is(expectedBorders)
        );
    }


    @Test
    public void returnsHorizontalBorder_whenElementHasParent() {
        // given
        final Context context = Context.newContext(new Object());

        final Schema currentModel = new Schema();
        final Schema parentModel = new Schema();

        final ContextModelsStack contextModelsStack = new ContextModelsStack(
            asList(currentModel, parentModel)
        );

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);
        borderHelper = BorderHelper.with(contextStackFactory);

        final Options options = OptionsStub.with(context);

        // when
        final CharSequence expectedBorders = getBorder(SchemaBorder.HORIZONTAL, 1, 0)
             + getBorder(SchemaBorder.SHORT_VERTICAL, 1, 0);
        final CharSequence actualBorders = borderHelper.apply(null, options).toString();

        // then
        assertThat("Helper returns HTML with one vertical border for each ancestor model, and a horizontal border",
            actualBorders,
            is(expectedBorders)
        );
    }

    @Test
    public void returnsParentBorder_forEachIndentationLevel() {
        // given
        final Context context = Context.newContext(new Object());

        final Schema currentModel = new Schema();
        final Schema parentModel = new Schema();
        final Schema grandparentModel = new Schema();

        final ContextModelsStack contextModelsStack = new ContextModelsStack(
            asList(currentModel, parentModel, grandparentModel)
        );

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);
        borderHelper = BorderHelper.with(contextStackFactory);

        final Options options = OptionsStub.with(context);

        // when
        final CharSequence expectedBorders = getBorder(SchemaBorder.HORIZONTAL, 1, 1)
            + getBorder(SchemaBorder.SHORT_VERTICAL, 1, 1)
            + getBorder(SchemaBorder.VERTICAL, 2, 0);
        final CharSequence actualBorders = borderHelper.apply(null, options).toString();

        // then
        assertThat("Helper returns HTML with one vertical border for each ancestor model, and a horizontal border",
            actualBorders,
            is(expectedBorders)
        );
    }

    @Test
    @UseDataProvider("lastChildModels")
    public void returnsShortVerticalBorder_whenElementIsLastPropertyOfParent(
        final Schema currentModel,
        final Schema parentModel,
        final String expectedBorders
    ) {
        // given
        final Context context = Context.newContext(new Object());

        final ContextModelsStack contextModelsStack = new ContextModelsStack(
            asList(currentModel, parentModel)
        );

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);
        borderHelper = BorderHelper.with(contextStackFactory);

        final Options options = OptionsStub.with(context);

        // when
        final CharSequence actualBorders = borderHelper.apply(null, options).toString();

        // then
        assertThat("Helper returns HTML with one short vertical border",
            actualBorders,
            is(expectedBorders)
        );
    }

    @Test
    @UseDataProvider("modelsWithChildren")
    public void returnsChildBorder_whenElementHasChildren(
        final Schema currentModel,
        final String expectedBorders
    ) {
        // given
        final Context context = Context.newContext(new Object());

        final ContextModelsStack contextModelsStack = new ContextModelsStack(
            asList(currentModel)
        );

        final ContextModelsStack.Factory contextStackFactory = mock(ContextModelsStack.Factory.class);
        given(contextStackFactory.from(context)).willReturn(contextModelsStack);
        borderHelper = BorderHelper.with(contextStackFactory);

        final Options options = OptionsStub.with(context);

        // when
        final CharSequence actualBorders = borderHelper.apply(null, options).toString();

        // then
        assertThat("Helper returns HTML with a child border",
            actualBorders,
            is(expectedBorders)
        );
    }

    @DataProvider
    public static Object[][] lastChildModels() {
        final String expectedBorder = getBorder(SchemaBorder.HORIZONTAL, 1, 0) + getBorder(SchemaBorder.SHORT_VERTICAL, 1, 0);
        final Schema currentModel = new Schema();
        return new Object[][] {
            // "properties" child
            {
                currentModel,
                new Schema().properties(ImmutableMap.of("child", currentModel)),
                expectedBorder
            },
            // "allOf" child
            {
                currentModel,
                new ComposedSchema().allOf(new ArrayList<>(asList(currentModel))),
                expectedBorder
            },
            // "anyOf" child
            {
                currentModel,
                new ComposedSchema().anyOf(new ArrayList<>(asList(currentModel))),
                expectedBorder
            },
            // "oneOf" child
            {
                currentModel,
                new ComposedSchema().oneOf(new ArrayList<>(asList(currentModel))),
                expectedBorder
            },
            // "array" -> "items" child
            {
                currentModel,
                new ArraySchema().items(currentModel),
                expectedBorder
            }
        };
    }

    @DataProvider
    public static Object[][] modelsWithChildren() {
        final String childBorder = getBorder(SchemaBorder.CHILD, 0);
        return new Object[][] {
            // "properties" child
            {
                new Schema().properties(ImmutableMap.of("child", new Schema())),
                childBorder
            },
            // "allOf" child
            {
                new ComposedSchema().allOf(asList(new Schema())),
                childBorder
            },
            // "anyOf" child
            {
                new ComposedSchema().anyOf(asList(new Schema())),
                childBorder
            },
            // "oneOf" child
            {
                new ComposedSchema().oneOf(asList(new Schema())),
                childBorder
            },
            // "array" -> "items" child
            {
                new ArraySchema().items(new Schema<>()),
                childBorder
            }
        };
    }

    private static String getBorder(SchemaBorder type, int lightnessLevel) {
        return String.format(
            "<span class=\"nhsd-o-schema__border-%s\" style=\"--border-padding: 1.25em; --border-lightness: %s\"></span>",
            type.getString(), lightnessLevels[lightnessLevel]
        );
    }

    private static String getBorder(SchemaBorder type, int paddingLevel, int lightnessLevel) {
        if (type == SchemaBorder.CHILD) {
            return getBorder(type, lightnessLevel);
        }
        return String.format(
            "<span class=\"nhsd-o-schema__border-%s\" style=\"--border-padding: %sem; --border-lightness: %s\"></span>",
            type.getString(), 1.25 - (1.5 * paddingLevel), lightnessLevels[lightnessLevel]
        );
    }

    enum SchemaBorder {
        CHILD("child"),
        HORIZONTAL("horizontal"),
        VERTICAL("vertical"),
        SHORT_VERTICAL("short-vertical");

        private final String cssString;

        SchemaBorder(String cssString) {
            this.cssString = cssString;
        }

        public String getString() {
            return this.cssString;
        }
    }
}
