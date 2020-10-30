package uk.nhs.digital.apispecs.swagger;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>'Basic' helper calculating indentation level based on the number of contexts in
 * the context stack.
 *
 * <p>The resulting value is the number of contexts with unique models in the stack
 * multiplied by provided {@code indentationFactor}.
 *
 * <p>It can then be used in places such as HTML '{@code class}' attribute as part of
 * the name of CSS classes that define the offset, or directly in '{@code style}'
 * as a value of padding or margin.
 *
 * <p>Examples of use:
 *
 * <p> Indentation level with the factor of 10, using '{@code style}' attribute:
 *
 * <pre>
 *
 *     &lt;div style=&quot;padding-left: {{indentation 10}}&quot;&gt;...indented content...&lt;/div&gt;
 * </pre>
 *
 * <p>Indentation level with default factor of 1, using '{@code class}' attribute:
 *
 * <pre>
 *
 *     &lt;div class=&quot;indentationLevel{{indentation}}&quot;&gt;...indented content...&lt;/div&gt;
 * </pre>
 */
@SuppressWarnings("rawtypes")
public class IndentationHelper<T> implements Helper<T> {

    public static final String NAME = "indentation";

    public static final IndentationHelper INSTANCE = new IndentationHelper();

    private static final int DEFAULT_INDENTATION_FACTOR = 1;

    @Override
    public Long apply(
        final T indentationFactorCandidate,
        final Options options
    ) {
        final Object currentContextModel = options.context.model();

        validateArgs(indentationFactorCandidate, currentContextModel, options.params);

        try {
            final Integer indentationFactor = resolveIndentationFactorFrom(
                indentationFactorCandidate,
                currentContextModel
            );

            return indentationLevelFor(options, indentationFactor);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to identify indentation level.", e);
        }
    }

    private void validateArgs(
        final T indentationFactorCandidate,
        final Object currentContextModel,
        final Object[] params
    ) {

        Validate.notNull(
            indentationFactorCandidate,
            "Indentation factor has to be positive integer but was: null."
        );

        if (indentationFactorCandidate instanceof Integer) {
            if (indentationFactorCandidate != currentContextModel) {
                Validate.isTrue(
                    ((Integer) indentationFactorCandidate) > 0,
                    "Indentation factor has to be positive integer but was: %s.", indentationFactorCandidate
                );
            }
        } else {
            Validate.isTrue(
                indentationFactorCandidate == currentContextModel,
                "Indentation factor has to be positive integer but was: %s.", indentationFactorCandidate
            );

        }

        Validate.isTrue(
            params == null || params.length == 0,
            "Unknown parameters provided."
        );
    }

    private Integer resolveIndentationFactorFrom(final T indentationFactorCandidate, final Object currentContextModel) {

        if (indentationFactorCandidate instanceof Integer) {
            return indentationFactorCandidate == currentContextModel
                ? DEFAULT_INDENTATION_FACTOR
                : (Integer) indentationFactorCandidate;

        } else {
            return DEFAULT_INDENTATION_FACTOR;
        }
    }

    private long indentationLevelFor(final Options options, final Integer indentationFactor) {

        return numberOfAncestorsWithUniqueModelsOf(options.context) * indentationFactor;
    }

    private long numberOfAncestorsWithUniqueModelsOf(final Context initialContext) {

        final List models = new ArrayList(31); // schemas in examined sample never exceeded 20 levels of nesting

        for (Context context = initialContext; context != null; context = context.parent()) {
            models.add(context.model());
        }

        // In some cases the parent object contains the same model as the current
        // context; possibly when when the templates are invoked recursively,
        // as is the case with Schema Objects.
        // This incorrectly inflates the number of indentation levels
        // and so we eliminate duplicates, here.
        return models.stream().distinct().count() - 1;
    }
}
