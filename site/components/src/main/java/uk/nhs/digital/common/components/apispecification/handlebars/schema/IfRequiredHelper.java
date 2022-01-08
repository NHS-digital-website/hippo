package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.swagger.v3.oas.models.media.Schema;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * As per OAS spec, field {@code required} is of type {@code array}
 * and contains 'names' of objects contained in field '{@code properties}'.
 * <p>
 * This helper follows and expects this model, and only returns {@code true} for
 * <em>objects enumerated in their parent's 'properties' if the parent's 'items'
 * contains their 'name'</em>.
 * <p>
 * For example, when rendered from JSON below, {@code true} will be returned for
 * 'first-object' but {@code false} will be returned for 'second-object':
 * <pre>
 *     {
 *         "title": "Parent object with 'properties' and 'required' fields.",
 *         "required": ['first-object'],
 *         "properties": {
 *             "first-object": {...},
 *             "second-object": {...}
 *         }
 *     }
 * </pre>
 */
public class IfRequiredHelper implements Helper<Schema<?>> {

    public static final String NAME = "ifRequired";

    private final ContextModelsStack.Factory modelStackFactory;

    public IfRequiredHelper(final ContextModelsStack.Factory modelStackFactory) {
        this.modelStackFactory = modelStackFactory;
    }

    @Override public Options.Buffer apply(
        final Schema<?> model,  // ignored, we only use models from the stack given by options
        final Options options
    ) {
        try {
            return renderDependingOnCurrentSchemaObjectBeingRequiredByItsParent(options);

        } catch (final Exception e) {
            throw  new RuntimeException("Failed to render 'required' status.", e);
        }
    }

    private Options.Buffer renderDependingOnCurrentSchemaObjectBeingRequiredByItsParent(final Options options) throws IOException {

        final Options.Buffer buffer = options.buffer();

        final ContextModelsStack contextModelsStack = modelStackFactory.from(options.context);

        if (isCurrentSchemaObjectRequiredByItsParent(contextModelsStack)) {
            buffer.append(options.fn());
        } else {
            buffer.append(options.inverse());
        }
        return buffer;
    }

    private boolean isCurrentSchemaObjectRequiredByItsParent(final ContextModelsStack contextModelsStack) {

        final Optional<Schema<?>> currentModel = asSchema(contextModelsStack.currentModel());

        final Optional<Schema<?>> parentModel = asSchema(contextModelsStack.parentModel());

        final List<String> namesOfRequiredObjects = parentModel.map(Schema::getRequired).orElse(emptyList());

        final Optional<String> currentObjectName = nameOfCurrentSchemaObjectFrom(
            currentModel,
            parentModel.map(Schema::getProperties).orElse(emptyMap())
        );

        return currentObjectName
            .map(namesOfRequiredObjects::contains)
            .orElse(false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Schema<?>> asSchema(final Optional<?> schemaCandidate) {

        return schemaCandidate
            .filter(model -> model instanceof Schema)
            .map(model -> (Schema<?>) model);
    }

    private Optional<String> nameOfCurrentSchemaObjectFrom(
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<Schema<?>> currentModel,
        @SuppressWarnings("rawtypes") final Map<String, Schema> parentSchemaProperties
    ) {
        return parentSchemaProperties.entrySet().stream()
            .filter(stringSchemaEntry -> currentModel.map(schema -> stringSchemaEntry.getValue() == schema).orElse(false))
            .map(Map.Entry::getKey)
            .findAny();
    }
}
