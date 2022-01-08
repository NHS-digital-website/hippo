package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import uk.nhs.digital.common.components.apispecification.handlebars.TemplateRenderingException;

/**
 * <p>'Basic' helper calculating indentation level based on the number of contexts in
 * the context stack.
 *
 * <p>The resulting value is the number of contexts with unique models in the stack
 * multiplied by {@code weighting} parameter if provided.
 *
 * <p>It can then be used in places such as HTML '{@code class}' attribute as part of
 * the name of CSS classes that define the offset, or directly in '{@code style}'
 * as a value of padding or margin. A weighting parameter can be supplied,
 * so as to more directly control the numerical value returned.
 *
 * <p>Examples of use:
 *
 * <p> Indentation using '{@code style}' attribute:
 *
 * <pre>
 *
 *     &lt;div style=&quot;padding-left: {{indentation}}em&quot;&gt;...indented content...&lt;/div&gt;
 * </pre>
 *
 * <p>Indentation using '{@code class}' attribute:
 *
 * <pre>
 *
 *     &lt;div class=&quot;indentationLevel_{{indentation}}&quot;&gt;...indented content...&lt;/div&gt;
 * </pre>
 *
 * <p> Returning a weighted indentation level:
 *
 * <pre>
 *
 *     &lt;div style=&quot;padding-left: {{indentation weighting=1.5}}em&quot;&gt;...indented content...&lt;/div&gt;
 * </pre>
 *
 * @param <M> Model type; irrelevant as the {@code model} parameter is not used.
 */
@SuppressWarnings("rawtypes")
public class IndentationHelper implements Helper<Object> {

    public static final String NAME = "indentation";

    private ContextModelsStack.Factory contextStackFactory;

    public IndentationHelper(final ContextModelsStack.Factory contextStackFactory) {
        this.contextStackFactory = contextStackFactory;
    }

    public static IndentationHelper with(final ContextModelsStack.Factory contextStackFactory) {
        return new IndentationHelper(contextStackFactory);
    }

    @Override
    public String apply(
        final Object model, // ignored, we only use models from the stack given by options
        final Options options
    ) {

        try {
            final ContextModelsStack contextModelsStack = contextStackFactory.from(options.context);

            double weighting = options.hash("weighting", 1.0);

            return indentationLevelFor(contextModelsStack, weighting);

        } catch (final Exception e) {
            throw new TemplateRenderingException("Failed to calculate indentation level.", e);
        }
    }

    private String indentationLevelFor(final ContextModelsStack contextModelsStack, final double weighting) {
        return weighting == 1.0
            ? String.valueOf(contextModelsStack.ancestorModelsCount())
            : String.format("%.1f", weighting * contextModelsStack.ancestorModelsCount());
    }
}
