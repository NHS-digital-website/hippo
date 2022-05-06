package uk.nhs.digital.common.components.apispecification.swagger;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.helper.AssignHelper;
import com.github.jknack.handlebars.helper.ConditionalHelpers;
import com.github.jknack.handlebars.helper.StringHelpers;
import io.swagger.codegen.v3.*;
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.apispecification.commonmark.CommonmarkMarkdownConverter;
import uk.nhs.digital.common.components.apispecification.handlebars.EnumHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.HasOneItemHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.HeadingsHyperlinksFromMarkdownHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.IfNotNullHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.IsAnyTrueHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.MarkdownHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.StringBooleanVariableHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.UuidHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.VariableValueHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.schema.SchemaHelper;
import uk.nhs.digital.common.components.apispecification.handlebars.schema.TypeAnySanitisingHelper;
import uk.nhs.digital.common.components.apispecification.swagger.model.BodyWithMediaTypesExtractor;
import uk.nhs.digital.common.components.apispecification.swagger.request.examplerenderer.CodegenParameterExampleHtmlRenderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ApiSpecificationStaticHtml2Codegen extends StaticHtml2Codegen {

    public static Logger log = LoggerFactory.getLogger(ApiSpecificationStaticHtml2Codegen.class);

    private static final String VENDOR_EXT_KEY_BODY = "x-body";
    private static final String VENDOR_EXT_KEY_SCHEMA = "x-schema";
    private final String templateFolderPath;

    private CommonmarkMarkdownConverter markdownConverter = new CommonmarkMarkdownConverter();

    private CodegenParameterExampleHtmlRenderer codegenParameterExampleRenderer =
        new CodegenParameterExampleHtmlRenderer(markdownConverter);

    private BodyWithMediaTypesExtractor bodyWithMediaTypesExtractor = new BodyWithMediaTypesExtractor();

    public ApiSpecificationStaticHtml2Codegen(final String templateFolderPath) {
        this.templateFolderPath = templateFolderPath;
    }

    @Override
    public void preprocessOpenAPI(final OpenAPI openApi) {
        this.openAPI = openApi;

        populateComponentsFieldWithEmptyObjectWhenNull(openApi);

        preProcessOperations(openApi);
    }

    @Override
    public Map<String, Object> postProcessOperations(final Map<String, Object> codegenMapWithOperations) {

        codegenOperationsFrom(codegenMapWithOperations).forEach(this::postProcessOperation);

        return codegenMapWithOperations;
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);

        fixDefaultValueForDateDataFormat(parameter);

        postProcessRequestBody(parameter);
    }

    @Override
    public CodegenParameter fromRequestBody(
        final RequestBody body,
        final String name,
        final Schema schema,
        final Map<String, Schema> schemas,
        final Set<String> imports
    ) {
        final CodegenParameter requestBody = super.fromRequestBody(body, name, schema, schemas, imports);

        setAsVendorExtension(requestBody, VENDOR_EXT_KEY_SCHEMA, schema);

        return requestBody;
    }

    @Override
    public void setParameterExampleValue(final CodegenParameter parameter) {
        try {
            parameter.example = codegenParameterExampleRenderer.htmlForExampleValueOf(parameter.getJsonSchema());
        } catch (Exception e) {
            throw new RuntimeException("Failed to process example value(s) for parameter " + parameter);
        }
    }

    @Override
    public void addHandlebarHelpers(final Handlebars handlebars) {
        super.addHandlebarHelpers(handlebars);

        final MarkdownHelper markdownHelper = new MarkdownHelper(markdownConverter);

        handlebars.registerHelper(EnumHelper.NAME, EnumHelper.INSTANCE)
            .registerHelper(MarkdownHelper.NAME, markdownHelper)
            .registerHelper(HasOneItemHelper.NAME, HasOneItemHelper.INSTANCE)
            .registerHelper(SchemaHelper.NAME, new SchemaHelper(markdownHelper, templateFolderPath))
            .registerHelper(IsAnyTrueHelper.NAME, IsAnyTrueHelper.INSTANCE)
            .registerHelper(AssignHelper.NAME, AssignHelper.INSTANCE)
            .registerHelper(HeadingsHyperlinksFromMarkdownHelper.NAME, HeadingsHyperlinksFromMarkdownHelper.INSTANCE)
            .registerHelper(TypeAnySanitisingHelper.NAME, TypeAnySanitisingHelper.INSTANCE)
            .registerHelper(StringBooleanVariableHelper.NAME, StringBooleanVariableHelper.INSTANCE)
            .registerHelper(ConditionalHelpers.eq.name(), ConditionalHelpers.eq)
            .registerHelper(ConditionalHelpers.or.name(), ConditionalHelpers.or)
            .registerHelper(VariableValueHelper.NAME, VariableValueHelper.INSTANCE)
            .registerHelper(StringHelpers.lower.name(), StringHelpers.lower)
            .registerHelper(IfNotNullHelper.NAME, IfNotNullHelper.INSTANCE)
            .registerHelper(UuidHelper.NAME, UuidHelper.INSTANCE);
    }


    @Override
    public String removeNonNameElementToCamelCase(final String operationName) {
        return operationName;
    }

    private void fixDefaultValueForDateDataFormat(final CodegenProperty responseHeader) {
        sanitiseDefaultDate(responseHeader.defaultValue, responseHeader.dataFormat, responseHeader.toString())
            .ifPresent(sanitisedDefaultDate -> responseHeader.defaultValue = sanitisedDefaultDate);
    }

    private void fixDefaultValueForDateDataFormat(final CodegenParameter requestParameter) {
        sanitiseDefaultDate(requestParameter.getDefaultValue(), requestParameter.getDataFormat(), requestParameter.toString())
            .ifPresent(sanitisedDefaultDate -> requestParameter.defaultValue = sanitisedDefaultDate);
    }

    private Optional<String> sanitiseDefaultDate(final String defaultValue, final String dataFormat, final String parameter) {
        // For parameters with data format of 'date' Codegen's parser emits values of 'default' field as java.util.Date.
        // This makes us lose access to the raw value of that field as defined in the source JSON.
        //
        // A consequence of this is that value returned by parameter.getDefaultValue() is one that Codegen obtains from Date.toString() which
        // looks like 'Sun Jan 01 00:00:00 GMT 1984' which is not a correct date representation for fields of type 'date'.
        // To compensate for this, if the data format is 'date' we parse the value into ISO 'full-date' format as per
        // https://datatracker.ietf.org/doc/html/rfc3339 which is specified as definition of 'date' format in https://spec.openapis.org/oas/v3.0.3#data-types
        //
        // Default values of parameters are defined in parameters' schemas. See io.swagger.codegen.v3.generators.DefaultCodegenConfig.fromParameter for how it:
        // * calls Date.toString() to populate defaultValue,
        // * calls JSON.pretty(parameter) to populate codegenParameter.jsonSchema - which is why parameter.getJsonSchema() does not help us in obtaining raw
        //   value defined in OAS file, because is _not_ the original JSON.

        if ("date".equals(dataFormat) && StringUtils.isNotBlank(defaultValue)) {
            final String pattern = "E MMM dd HH:mm:ss VV uuuu"; // Sun Jan 01 00:00:00 GMT 1984

            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

            try {
                return Optional.of(LocalDate.parse(defaultValue.trim(), dateTimeFormatter).toString());
            } catch (final Exception e) {
                final String apiTitle = Optional.ofNullable(openAPI.getInfo()).map(Info::getTitle).orElse("n/a");
                log.warn(
                    "Unable to fix default value of '{}' as ISO-8601 format uuuu-MM-dd despite format being 'date' for parameter '{}' in API specification '{}'.",
                    defaultValue, parameter, apiTitle
                );
            }
        }
        return Optional.empty();
    }

    /**
     * OAS does not specify '{@code components}' field as required,
     * and yet CodeGen fails with NullPointerException when the field is missing,
     * happily accepting empty Components object, though.
     */
    private void populateComponentsFieldWithEmptyObjectWhenNull(final OpenAPI openApi) {
        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }
    }

    private void preProcessOperations(final OpenAPI openApi) {

        openApi.getPaths().values().stream()
            .flatMap(pathItem -> Stream.of(
                pathItem.getHead(),
                pathItem.getOptions(),
                pathItem.getGet(),
                pathItem.getTrace(),
                pathItem.getPost(),
                pathItem.getPut(),
                pathItem.getPatch(),
                pathItem.getDelete()
            ).filter(Objects::nonNull))
            .forEach(this::preProcessOperation);
    }

    private void preProcessOperation(final Operation operation) {
        preProcessRequests(operation.getRequestBody());
        preProcessResponses(operation.getResponses());
    }

    private void preProcessRequests(RequestBody requestBody) {
        Content content = Optional.ofNullable(requestBody)
            .orElse(new RequestBody())
            .getContent();
        preProcessRequestContent(content);
    }

    private void preProcessRequestContent(Content content) {
        Optional.ofNullable(content)
            .orElse(new Content())
            .values()
            .stream()
            .forEach(this::preProcessRequestMediaTypes);
    }

    private void preProcessRequestMediaTypes(MediaType mediaType) {
        preProcessSchema(mediaType.getSchema());
    }

    private void preProcessResponses(final ApiResponses responses) {
        Optional.ofNullable(responses)
            .orElse(new ApiResponses())
            .values()
            .forEach(this::preProcessResponse);
    }

    private void preProcessResponse(final ApiResponse apiResponse) {
        preProcessResponseHeaders(apiResponse.getHeaders());
        preProcessResponseContent(apiResponse.getContent());
    }

    private void preProcessResponseContent(Content content) {
        Optional.ofNullable(content)
            .orElse(new Content())
            .values()
            .stream()
            .forEach(this::preProcessResponseMediaTypes);
    }

    private void preProcessResponseMediaTypes(MediaType mediaType) {
        preProcessSchema(mediaType.getSchema());
    }

    private void preProcessSchema(Schema<?> schema) {
        setPropertyNames(schema);
        visitSchemaChildren(schema);
    }

    private void setPropertyNames(Schema<?> schema) {
        Map<String, Schema> properties = Optional.ofNullable(schema)
            .orElse(new Schema<>())
            .getProperties();
        Optional.ofNullable(properties)
            .orElse(emptyMap())
            .forEach((propertyName, property) -> {
                property.addExtension("x-property-name", propertyName);
            });
    }

    private void visitSchemaChildren(Schema<?> schema) {
        if (schema instanceof ObjectSchema) {
            Optional.ofNullable(schema.getProperties())
                .orElse(emptyMap())
                .forEach((propertyName, property) -> preProcessSchema(property));
        } else if (schema instanceof ArraySchema) {
            Optional.ofNullable(((ArraySchema) schema).getItems())
                .ifPresent(this::preProcessSchema);
        } else if (schema instanceof ComposedSchema) {
            List<Function<ComposedSchema, List<Schema>>> getters = Arrays.asList(
                ComposedSchema::getAllOf,
                ComposedSchema::getAnyOf,
                ComposedSchema::getOneOf
            );
            getters.forEach(getter -> {
                Optional.ofNullable(getter.apply((ComposedSchema) schema))
                    .orElse(emptyList())
                    .forEach(this::preProcessSchema);
            });
        }
    }

    private void preProcessResponseHeaders(final Map<String, Header> apiResponseHeaders) {
        Optional.ofNullable(apiResponseHeaders)
            .orElse(emptyMap())
            .values()
            .forEach(this::preProcessApiResponseHeader);
    }

    private void preProcessApiResponseHeader(final Header header) {
        propagateDescriptionFromHeaderToItsEmbeddedSchema(header);
    }

    private void postProcessOperation(final CodegenOperation codegenOperation) {
        postProcessResponses(codegenOperation.getResponses());
    }

    private void postProcessResponses(final List<CodegenResponse> responses) {
        Optional.ofNullable(responses)
            .orElse(emptyList())
            .forEach(this::postProcessResponse);
    }

    private void postProcessResponse(final CodegenResponse codegenResponse) {
        postProcessResponseHeaders(codegenResponse.getHeaders());

        updateResponseBodiesFromJsonSchema(codegenResponse);
    }

    private void postProcessResponseHeaders(final List<CodegenProperty> codegenResponseHeaders) {
        Optional.ofNullable(codegenResponseHeaders)
            .orElse(emptyList())
            .forEach(this::postProcessResponseHeader);
    }

    private void postProcessResponseHeader(final CodegenProperty codegenResponseHeader) {

        fixCodegenResponseHeaderNullDefaultValue(codegenResponseHeader);

        fixDefaultValueForDateDataFormat(codegenResponseHeader);

        renderResponseHeaderExamples(codegenResponseHeader);
    }

    private void updateResponseBodiesFromJsonSchema(final CodegenResponse codegenResponse) {

        bodyWithMediaTypesExtractor.bodyObjectFromJsonSchema(
                codegenResponse.getJsonSchema(), "response " + codegenResponse
            )
            .ifPresent(body -> {
                setAsVendorExtension(codegenResponse, VENDOR_EXT_KEY_BODY, body);
                setAsVendorExtension(codegenResponse, VENDOR_EXT_KEY_SCHEMA, codegenResponse.getSchema());
            });
    }

    private void renderResponseHeaderExamples(final CodegenProperty codegenResponseHeader) {
        try {
            codegenResponseHeader.example =
                codegenParameterExampleRenderer.htmlForExampleValueOf(codegenResponseHeader.getJsonSchema());
        } catch (Exception e) {
            throw new RuntimeException("Failed to process example value(s) for response header " + codegenResponseHeader);
        }
    }

    private void fixCodegenResponseHeaderNullDefaultValue(final CodegenProperty codegenResponseHeader) {
        // io.swagger.codegen.v3.generators.DefaultCodegenConfig.toDefaultValue incorrectly emits string "null"
        // when the property is missing in JSON schema, so we need to fix this here, otherwise we'd be incorrectly
        // displaying value "null".

        if ("null".equals(codegenResponseHeader.defaultValue)) {
            codegenResponseHeader.defaultValue = null;
        }
    }

    private void propagateDescriptionFromHeaderToItsEmbeddedSchema(final Header header) {
        // When populating CodegenProperty.description, Codegen takes value from Header.schema.description,
        // ignoring value defined against the header JSON object itself (https://swagger.io/specification/#header-object).
        // The result is that if the schema does not contain a 'description' property, the CodegenProperty.description
        // field remains empty (see io.swagger.codegen.v3.generators.DefaultCodegenConfig.fromProperty,
        // invoked from io.swagger.codegen.v3.generators.DefaultCodegenConfig.addHeaders).
        //
        // For this reason, we populate Header.description with value from Header.Schema.description, here.
        // Later in the processing, when Codegen creates CodegenProperty from io.swagger.v3.oas.models.media.Schema,
        // it will pick this value to populate CodegenProperty.description with.
        //
        // Note that if Header has a description, we're overwriting any description that Schema could have, here.
        // This is (roughly) in line with the behaviour described for 'example/examples' fields in
        // https://swagger.io/specification/#media-type-object

        if (StringUtils.isNotBlank(header.getDescription()) && header.getSchema() != null) {
            header.getSchema().setDescription(header.getDescription());
        }
    }

    private List<CodegenOperation> codegenOperationsFrom(final Map<String, Object> codegenOperationsMap) {
        final Map<String, Object> operationsToPostProcess = super.postProcessOperations(codegenOperationsMap);

        final Map<String, Object> operationsMap =
            (Map<String, Object>) Optional.ofNullable(operationsToPostProcess.get("operations")).orElse(emptyMap());
        final String groupName = Optional.ofNullable(operationsMap.get("pathPrefix"))
            .map(String.class::cast)
            .orElse("");
        final List<CodegenOperation> operations =
            (List<CodegenOperation>) Optional.ofNullable(operationsMap.get("operation")).orElse(emptyList());
        return sortedAccordingToCustomOrder(operations, groupName);
    }

    private List<CodegenOperation> sortedAccordingToCustomOrder(List<CodegenOperation> operations, String groupName) {
        final Predicate matchesGroupName = operationOrderEntry ->
            Optional.ofNullable(
                ((LinkedHashMap<?, ?>) operationOrderEntry).get("group")
            ).map(String.class::cast)
            .map(this::toApiVarName)
            .orElse("default")
            .equals(groupName);

        LinkedHashMap<String,Object> operationOrderGroup = (LinkedHashMap<String, Object>) Optional.ofNullable(openAPI.getExtensions())
            .map(extensions -> extensions.get("x-spec-publication"))
            .map(LinkedHashMap.class::cast)
            .map(xSpecPublication -> xSpecPublication.get("operation-order"))
            .map(List.class::cast)
            .orElse(emptyList())
            .stream()
            .map(LinkedHashMap.class::cast)
            .filter(matchesGroupName)
            .findFirst()
            .orElse(new LinkedHashMap<>());

        if (operationOrderGroup.isEmpty()) {
            return operations;
        }

        List<LinkedHashMap> ops = (List<LinkedHashMap>) Optional.ofNullable(operationOrderGroup.get("operations")).orElse(new ArrayList<>());
        Function<CodegenOperation, Integer> getReferenceIndexOf = op -> {
            LinkedHashMap<?,?> matchingReference = ops.stream().filter(
                opReference -> ((String) opReference.get("method")).equalsIgnoreCase(op.getHttpMethod()) && opReference.get("path").equals(op.getPath())
            ).findFirst().orElse(new LinkedHashMap());
            return ops.indexOf(matchingReference);
        };

        operations.sort(Comparator.comparingInt(getReferenceIndexOf::apply));
        return operations;
    }


    private void postProcessRequestBody(final CodegenParameter parameter) {

        if (parameter.getIsBodyParam()) {
            bodyWithMediaTypesExtractor.bodyObjectFromJsonSchema(
                parameter.getJsonSchema(),
                "request parameter " + parameter
            )
                .ifPresent(requestBody -> setAsVendorExtension(parameter, VENDOR_EXT_KEY_BODY, requestBody));
        }
    }

    private <T extends CodegenObject> void setAsVendorExtension(
        final T codegenObject,
        final String vendorExtensionKey, final Object vendorExtensionValue
    ) {
        codegenObject.getVendorExtensions().put(vendorExtensionKey, vendorExtensionValue);
    }
}
