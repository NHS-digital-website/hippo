package uk.nhs.digital.apispecs.handlebars.schema;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import uk.nhs.digital.apispecs.handlebars.TemplateRenderingException;

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
 * <p> Indentation using '{@code style}' attribute:
 *
 * <pre>
 *
 *     &lt;div style=&quot;padding-left: {{indentation}}&quot;&gt;...indented content...&lt;/div&gt;
 * </pre>
 *
 * <p>Indentation using '{@code class}' attribute:
 *
 * <pre>
 *
 *     &lt;div class=&quot;indentationLevel_{{indentation}}&quot;&gt;...indented content...&lt;/div&gt;
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
    public Integer apply(
        final Object model, // ignored, we only use models from the stack given by options
        final Options options
    ) {

        try {
            final ContextModelsStack contextModelsStack = contextStackFactory.from(options.context);

            return indentationLevelFor(contextModelsStack);

        } catch (final Exception e) {
            throw new TemplateRenderingException("Failed to calculate indentation level.", e);
        }
    }

    private int indentationLevelFor(final ContextModelsStack contextModelsStack) {
        return contextModelsStack.ancestorModelsCount();
    }
}
