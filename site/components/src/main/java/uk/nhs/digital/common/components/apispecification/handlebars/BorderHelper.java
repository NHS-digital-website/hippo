package uk.nhs.digital.common.components.apispecification.handlebars;


import com.github.jknack.handlebars.Handlebars.SafeString;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import uk.nhs.digital.common.components.apispecification.handlebars.schema.ContextModelsStack;

import java.util.*;

public class BorderHelper implements Helper<Object> {
    public static final String NAME = "borders";
    private static final String INDENTATIONS_TO_IGNORE = "indentationsToIgnore";
    private static final Schema EMPTY_SCHEMA = new Schema();

    private ContextModelsStack.Factory contextStackFactory;

    public BorderHelper(final ContextModelsStack.Factory contextStackFactory) {
        this.contextStackFactory = contextStackFactory;
    }

    public static BorderHelper with(final ContextModelsStack.Factory contextStackFactory) {
        return new BorderHelper(contextStackFactory);
    }

    @Override
    public CharSequence apply(
        final Object model,
        final Options options) {

        try {
            final ContextModelsStack contextModelsStack = contextStackFactory.from(options.context);

            final int indentationLevel = contextModelsStack.ancestorModelsCount();
            final boolean hasChildren = currentModelHasChildren(contextModelsStack);

            final StringBuilder htmlWriter = new StringBuilder();

            // get child border
            if (hasChildren) {
                htmlWriter.append(
                    getChildBorder(indentationLevel)
                );
            }
            // if object has no parents, return here
            if (!contextModelsStack.parentModel().isPresent()) {
                return new SafeString(htmlWriter);
            }

            final ArrayList<Integer> indentationsToIgnore = getIndentationsToIgnore(options, indentationLevel);
            final boolean isLastPropertyOfLevel = currentModelIsLastPropertyOfParent(contextModelsStack);

            htmlWriter.append(
                getHorizontalBorder(indentationLevel, hasChildren)
            );
            htmlWriter.append(
                getVerticalBorders(isLastPropertyOfLevel, indentationLevel, indentationsToIgnore)
            );

            if (hasChildren && isLastPropertyOfLevel) {
                indentationsToIgnore.add(indentationLevel);
            }
            options.context.data(INDENTATIONS_TO_IGNORE, indentationsToIgnore);

            return new SafeString(htmlWriter);

        } catch (final Exception e) {
            throw new TemplateRenderingException("Failed to render schema borders.", e);
        }

    }

    private ArrayList<Integer> getIndentationsToIgnore(final Options options, final int indentationLevel) {
        ArrayList<Integer> indentationsToIgnore = options.context.data(INDENTATIONS_TO_IGNORE);
        if (indentationsToIgnore == null) {
            indentationsToIgnore = new ArrayList<>();
        }
        indentationsToIgnore.removeIf(n -> n >= indentationLevel);
        return indentationsToIgnore;
    }


    private String getChildBorder(int indentationLevel) {
        return String.format(
            "<span class=\"nhsd-o-schema__border-child\" style=\"--border-padding: 1.25em; --border-lightness: %.1f%%\"></span>",
            getLightness(indentationLevel + 1)
        );
    }

    // horizontal border connects element to first vertical border
    private String getHorizontalBorder(final int indentationLevel, final boolean hasChildren) {
        return String.format(
            "<span class=\"nhsd-o-schema__border-%shorizontal\" style=\"--border-padding: -0.25em; --border-lightness: %.1f%%\"></span>",
            hasChildren ? "short-" : "",
            getLightness(indentationLevel)
        );
    }

    private String getVerticalBorders(
        final boolean isLastPropertyOfLevel,
        final int indentationLevel,
        final ArrayList<Integer> indentationsToIgnore
    ) {
        final StringBuilder htmlWriter = new StringBuilder();

        final String firstBorder = String.format(
            "<span class=\"nhsd-o-schema__border-%svertical\" style=\"--border-padding: -0.25em; --border-lightness: %.1f%%\"></span>",
            isLastPropertyOfLevel ? "short-" : "",
            getLightness(indentationLevel)
        );

        htmlWriter.append(firstBorder);

        for (int i = 2; i <= indentationLevel; i++) {
            if (indentationsToIgnore.contains(indentationLevel + 1 - i)) {
                continue;
            }
            final String borderHtml = String.format(
                "<span class=\"nhsd-o-schema__border-vertical\" style=\"--border-padding: %sem; --border-lightness: %.1f%%\"></span>",
                1.25 - (1.5 * i),
                getLightness(indentationLevel + 1 - i)
            );
            htmlWriter.append(borderHtml);
        }

        return htmlWriter.toString();
    }

    private boolean currentModelHasChildren(ContextModelsStack contextModelsStack) {
        if (contextModelsStack.currentModel().orElse(EMPTY_SCHEMA) instanceof ArrayList) {
            return !((ArrayList) contextModelsStack.currentModel().get()).isEmpty();
        }

        final Schema<?> currentModel = asSchema(contextModelsStack.currentModel());

        if (currentModel == null) {
            return false;
        }

        if (currentModel instanceof ComposedSchema) {
            return ((ComposedSchema) currentModel).getAllOf() != null
                || ((ComposedSchema) currentModel).getAnyOf() != null
                || ((ComposedSchema) currentModel).getOneOf() != null;
        } else if (currentModel instanceof ArraySchema) {
            return ((ArraySchema) currentModel).getItems() != null;
        } else {
            return currentModel.getProperties() != null;
        }
    }

    private double getLightness(int indentationLevel) {
        // Lightness is calculated as an exponential function, starting at (0, 25) and tending to (infinity, 40).
        // Weighting (any positive value) affects how quickly the lighting scales.
        double maximum = 40;
        double minimum = 25;
        double weighting = 2;

        double weightedVariable = weighting * indentationLevel;
        double offsetConstant = 1 / Math.log(minimum / maximum);
        double exponent = -1 / ( weightedVariable - offsetConstant);
        return maximum * Math.exp(exponent);
    }

    private boolean currentModelIsLastPropertyOfParent(final ContextModelsStack contextModelsStack) {
        final Object currentModel = contextModelsStack.currentModel().orElse(null);
        final Object parentModel = contextModelsStack.parentModel().orElse(null);

        if (parentModel == null || currentModel == null) {
            return true;
        }

        Object lastParentProperty;
        if (parentModel instanceof ArrayList) {
            lastParentProperty = getLastArrayListProperty((ArrayList) parentModel);
        } else if (parentModel instanceof ComposedSchema) {
            lastParentProperty = getLastXofProperty((ComposedSchema) parentModel);
            if (! (currentModel instanceof ArrayList)) {
                lastParentProperty = getLastArrayListProperty((ArrayList) lastParentProperty);
            }
        } else if (parentModel instanceof ArraySchema) {
            lastParentProperty = getLastArrayProperty((ArraySchema) parentModel);
        } else if (parentModel instanceof Schema) {
            lastParentProperty = getLastProperty((Schema) parentModel);
        } else {
            lastParentProperty = EMPTY_SCHEMA;
        }

        return currentModel.equals(lastParentProperty);

    }

    private Schema getLastProperty(final Schema parentModel) {
        final ArrayList<Schema> parentProperties = new ArrayList<Schema>(
            Optional.ofNullable(parentModel.getProperties()).map(Map::values).orElse(Collections.emptyList())
        );
        final Schema lastPropertyOfParent = parentProperties.isEmpty() ? EMPTY_SCHEMA : parentProperties.get(parentProperties.size() - 1);
        return lastPropertyOfParent;
    }

    private Schema getLastArrayProperty(final ArraySchema schema) {
        return Optional.ofNullable(schema.getItems()).orElse(EMPTY_SCHEMA);
    }

    private Object getLastXofProperty(final ComposedSchema schema) {
        List<Schema> xofProperties = Optional.ofNullable(
            schema.getAllOf()
        ).orElse(
            Optional.ofNullable(
                schema.getAnyOf()
            ).orElse(
                Optional.ofNullable(
                    schema.getOneOf()
                ).orElse(Collections.emptyList())
            )
        );

        return xofProperties.isEmpty() ? EMPTY_SCHEMA : xofProperties;
    }

    private Object getLastArrayListProperty(final ArrayList schema) {
        return schema.isEmpty() ? null : schema.get(schema.size() - 1);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Schema<?> asSchema(final Optional<?> schemaCandidate) {
        if (!schemaCandidate.isPresent()) {
            return null;
        }

        return schemaCandidate
            .filter(model -> model instanceof Schema)
            .map(model -> (Schema<?>) model)
            .orElse(null);
    }

}
