package uk.nhs.digital.apispecs.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.util.Stack;

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
public class IndentationHelper<M> implements Helper<M> {

    public static final String NAME = "indentation";

    public static final IndentationHelper INSTANCE = new IndentationHelper();

    @Override
    public Long apply(
        final M model, // ignored, we only use models from the stack given by options
        final Options options
    ) {

        try {
            return indentationLevelFor(options.context);

        } catch (final Exception e) {
            throw new TemplateRenderingException("Failed to calculate indentation level.", e);
        }
    }

    private long indentationLevelFor(final Context context) {
        return numberOfAncestorsWithUniqueModelsOf(context);
    }

    private long numberOfAncestorsWithUniqueModelsOf(final Context initialContext) {

        // In some cases the parent object contains the same model as the current
        // context; possibly when when the templates are invoked recursively,
        // as is the case with Schema Objects.
        // This incorrectly inflates the number of indentation levels
        // and so we eliminate the duplicates here.
        final Stack modelsUnique = new Stack();

        // We rely on equality by reference rather than by value because some model objects
        // may be quite empty, thus risking equals returning 'true' when they are actually
        // different instances. Also, it's enough to just compare to the previous model
        // as, in real life, the duplicate references were always seen to be adjacent to
        // each other in the stack.
        for (Context context = initialContext; context != null; context = context.parent()) {
            if (modelsUnique.isEmpty() || modelsUnique.peek() != context.model()) {
                //noinspection unchecked
                modelsUnique.add(context.model());
            }
        }

        return modelsUnique.size() - 1;
    }
}
