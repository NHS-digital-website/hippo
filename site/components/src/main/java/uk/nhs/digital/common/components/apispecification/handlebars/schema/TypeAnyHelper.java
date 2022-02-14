package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
import java.util.Optional;

/**
 * <p>
 * OAS and JSON Schema define a few fields of Schema Objects as being effectively of type
 * 'any' (e.g. '{@code example}', '{@code default}').
 * <p>
 * In practice, this means that we have to expect that the values' types may be different
 * between invocations and so we have to render the values in different ways depending
 * on their specific types - which is what this class helps with. It delegates
 * actual styling to the template and invokes rendering of its:
 * <ul>
 *     <li>main block for 'primitive' types, (including arrays of primitive
 *     objects),</li>
 *     <li>inverse block for 'JSON' types, i.e. JSON object and array of
 *     JSON objects.</li>
 * </ul>
 * <p>
 * See:
 * <ul>
 *     <li><a href="https://spec.openapis.org/oas/v3.0.3#schema-object">Schema Object's 'example' and 'default' fields</a></li>
 *     <li><a href="https://tools.ietf.org/html/draft-wright-json-schema-validation-00#section-6.2">JSON Schema's 'default' field</a></li>
 * </ul>
 */
public class TypeAnyHelper implements Helper<Object> {

    public static final TypeAnyHelper INSTANCE = new TypeAnyHelper();

    public static final String NAME = "valueOfTypeAny";

    private static final String HASH_PARAM_IGNORE_CLASS = "ignoreClass";
    private static final String EMPTY_STRING = "";

    private TypeAnyHelper() {
        // private to encourage use of INSTANCE
    }

    @Override public Options.Buffer apply(final Object model, final Options options) throws IOException {

        try {
            final CharSequence rendering = typeSpecificRenderingFrom(model, options);

            return appendRenderingTo(rendering, options.buffer());

        } catch (final Exception e) {
            throw new SchemaRenderingException("Failed to render value of type 'any' for: " + model, e);
        }
    }

    private CharSequence typeSpecificRenderingFrom(final Object model, final Options options) throws IOException {

        if (isNull(model)) {
            return EMPTY_STRING;

        } else if (modelIsOfIgnoredType(model, options)) {
            return EMPTY_STRING;

        } else if (modelIsJacksonObjectNode(model)) {
            return options.fn();

        } else if (modelIsJacksonArrayNodeOfObjectNodes(model)) {
            return options.fn();

        } else {
            // model is of 'simple' type (string, boolean, etc.)
            return options.inverse();
        }
    }

    static boolean modelIsOfIgnoredType(final Object model, final Options options) {
        return Optional.ofNullable(options.hash(HASH_PARAM_IGNORE_CLASS))
            .map(String::valueOf)
            .map(TypeAnyHelper::classForName)
            .filter(classToIgnore -> classToIgnore.isInstance(model))
            .isPresent();
    }

    static boolean modelIsJacksonObjectNode(final Object model) {
        return model instanceof ObjectNode;
    }

    static boolean modelIsJacksonArrayNodeOfObjectNodes(final Object model) {
        if (model instanceof ArrayNode) {
            final ArrayNode arrayNode = (ArrayNode) model;

            return !arrayNode.isEmpty() && arrayNode.elements().next().isContainerNode();
        }

        return false;
    }

    private static Class<?> classForName(final String classToIgnoreName) {
        try {
            return TypeAnyHelper.class.getClassLoader().loadClass(classToIgnoreName);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Options.Buffer appendRenderingTo(final CharSequence rendering, final Options.Buffer buffer) throws IOException {
        buffer.append(rendering);

        return buffer;
    }
}