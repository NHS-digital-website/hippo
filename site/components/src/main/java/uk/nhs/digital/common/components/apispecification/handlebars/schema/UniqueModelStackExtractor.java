package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import com.github.jknack.handlebars.Context;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * <p>
 * Extracts models from given stack of {@linkplain Context} objects
 * preserving original order and ensuring that unique instances of models
 * are returned where such models were adjacent in the stack (see tests
 * for illustration).
 * <p>
 * In some cases the parent object contains the same model as the current
 * context; this appears to be a result of certain expressions in Handlebars'
 * templates that change 'context'
 * (e.g. <code>{{#each aProperty}...{{>nestedTemplate}}...{{each}}</code>) as is
 * the case with Schema Objects.
 * <p>
 * This incorrectly inflates the number of, say, indentation levels
 * and so this class makes sure that adjacent duplicates are eliminated from
 * the returned stack of model objects.
 * <p>
 * We rely on equality by reference rather than by value because some model objects
 * may be quite empty, thus risking {@linkplain Object#equals(Object)} returning
 * {@code true} when they are actually different instances. Also, it's enough to
 * just compare to the previous model as, in real life, the duplicate references
 * were only ever seen to be adjacent to each other in the stack.
 */
public class UniqueModelStackExtractor {

    @SuppressWarnings({"unchecked", "rawtypes"}) // Models in the stack can be of any/mix of types.
    public List uniqueModelsStackFrom(final Context initialContext) {

        final Deque modelsUnique = new ArrayDeque();

        for (Context context = initialContext; context != null; context = context.parent()) {
            if (modelsUnique.isEmpty() || modelsUnique.peekLast() != context.model()) {
                modelsUnique.addLast(context.model());
            }
        }
        return new ArrayList(modelsUnique);
    }
}
