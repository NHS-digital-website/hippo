package uk.nhs.digital.apispecs.swagger;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import io.swagger.codegen.v3.*;
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;
import uk.nhs.digital.apispecs.swagger.model.BodyWithMediaTypeObjects;
import uk.nhs.digital.apispecs.swagger.model.BodyWithMediaTypesExtractor;
import uk.nhs.digital.apispecs.swagger.model.MediaTypeObject;
import uk.nhs.digital.apispecs.swagger.request.examplerenderer.CodegenParameterExampleRenderer;

import java.util.*;
import java.util.stream.Stream;

public class ApiSpecificationStaticHtml2Codegen extends StaticHtml2Codegen {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecificationStaticHtml2Codegen.class);

    private CommonmarkMarkdownConverter markdownConverter = new CommonmarkMarkdownConverter();

    private CodegenParameterExampleRenderer codegenParameterExampleRenderer =
        new CodegenParameterExampleRenderer(markdownConverter);

    private BodyWithMediaTypesExtractor bodyWithMediaTypesExtractor = new BodyWithMediaTypesExtractor();

    @Override
    public void preprocessOpenAPI(OpenAPI openApi) {
        this.openAPI = openApi;

        if (openAPI.getInfo() != null) {
            Info info = openAPI.getInfo();
            if (StringUtils.isBlank(jsProjectName) && info.getTitle() != null) {
                // when jsProjectName is not specified, generate it from info.title
                jsProjectName = sanitizeName(dashize(info.getTitle()));
            }
        }

        // default values
        if (StringUtils.isBlank(jsProjectName)) {
            jsProjectName = "swagger-js-client";
        }
        if (StringUtils.isBlank(jsModuleName)) {
            jsModuleName = camelize(underscore(jsProjectName));
        }

        additionalProperties.put("jsProjectName", jsProjectName);
        additionalProperties.put("jsModuleName", jsModuleName);

        preProcessOperations(openApi);

        preparHtmlForGlobalDescription(openAPI);

        prepareHtmlForPathDescriptions(openAPI);

        prepareHtmlForPathParameters(openAPI);

        prepareHtmlForMethodsParameters(openAPI);
    }

    @Override
    public Map<String, Object> postProcessOperations(final Map<String, Object> codegenMapWithOperations) {

        codegenOperationsFrom(codegenMapWithOperations).forEach(this::postProcessOperation);

        return codegenMapWithOperations;
    }

    @Override
    public void postProcessParameter(final CodegenParameter parameter) {
        super.postProcessParameter(parameter);

        postProcessRequestBody(parameter);
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
        handlebars.registerHelper(EnumHelper.NAME, new EnumHelper());
        handlebars.with(EscapingStrategy.NOOP);
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
        preProcessResponses(operation.getResponses());
    }

    private void preProcessResponses(final ApiResponses responses) {
        Optional.ofNullable(responses)
            .orElse(new ApiResponses())
            .values()
            .forEach(this::preProcessResponse);
    }

    private void preProcessResponse(final ApiResponse apiResponse) {
        preProcessResponseHeaders(apiResponse.getHeaders());
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
        postProcessResponses(codegenOperation);
    }

    private void postProcessResponses(final CodegenOperation codegenOperation) {
        Optional.ofNullable(codegenOperation.getResponses())
            .orElse(emptyList())
            .forEach(this::postProcessResponse);
    }

    private void postProcessResponse(final CodegenResponse codegenResponse) {
        postProcessResponseHeaders(codegenResponse.getHeaders());

        convertResponseDescriptionFromMarkdownToHtml(codegenResponse);

        updateResponseBodiesFromJsonSchema(codegenResponse);
    }

    private void postProcessResponseHeaders(final List<CodegenProperty> codegenResponseHeaders) {
        Optional.ofNullable(codegenResponseHeaders).orElse(emptyList())
            .forEach(this::postProcessResponseHeader);
    }

    private void postProcessResponseHeader(final CodegenProperty codegenResponseHeader) {

        fixCodegenResponseHeaderNullDefaultValue(codegenResponseHeader);

        renderResponseHeaderExamples(codegenResponseHeader);

        renderResponseHeaderDescription(codegenResponseHeader);
    }

    private void updateResponseBodiesFromJsonSchema(final CodegenResponse codegenResponse) {

        bodyWithMediaTypesExtractor.extractBody(codegenResponse)
            .flatMap(this::toMediaTypeObjects)
            .orElse(emptyList())
            .stream()
            .map(this::toCodegenContent)
            .forEach(codegenContent -> codegenResponse.getContents().add(codegenContent));
    }

    private Optional<List<MediaTypeObject>> toMediaTypeObjects(final BodyWithMediaTypeObjects bodyWithMediaTypeObjects) {
        return Optional.ofNullable(bodyWithMediaTypeObjects.getMediaTypes());
    }

    private CodegenContent toCodegenContent(final MediaTypeObject mediaTypeObject) {
        return new CodegenContent(mediaTypeObject.getName());
    }

    private void renderResponseHeaderDescription(final CodegenProperty codegenResponseHeader) {

        final String descriptionMarkdown = codegenResponseHeader.getDescription();

        final String descriptionHtml = markdownConverter.toHtml(descriptionMarkdown);

        codegenResponseHeader.setDescription(descriptionHtml);
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

    protected void convertResponseDescriptionFromMarkdownToHtml(final CodegenResponse codegenResponse) {
        codegenResponse.message = markdownConverter.toHtml(codegenResponse.getMessage());
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

        final List<CodegenOperation> operations =
            (List<CodegenOperation>) Optional.ofNullable(operationsMap.get("operation")).orElse(emptyList());

        return operations;
    }

    private void postProcessRequestBody(final CodegenParameter parameter) {

        bodyWithMediaTypesExtractor.extractBody(parameter)
            .ifPresent(requestBody -> {

                convertRequestBodyExampleDescriptionsFromMarkdownToHtml(requestBody);

                saveRequestBodyAsVendorExtensionOnRequestBodyParam(parameter, requestBody);
            });
    }

    private void saveRequestBodyAsVendorExtensionOnRequestBodyParam(final CodegenParameter parameter,
                                                                    final BodyWithMediaTypeObjects bodyWithMediaTypeObjects) {
        parameter.getVendorExtensions().put("x-body", bodyWithMediaTypeObjects);
    }

    private void convertRequestBodyExampleDescriptionsFromMarkdownToHtml(final BodyWithMediaTypeObjects bodyWithMediaTypeObjects) {
        bodyWithMediaTypeObjects.getMediaTypes().stream()
            .flatMap(requestBodyMediaTypeContent -> requestBodyMediaTypeContent.getExamples().stream())
            .forEach(paramExample -> {

                final String descriptionMarkdown = paramExample.getDescription();
                final String descriptionHtml = markdownConverter.toHtml(descriptionMarkdown);

                paramExample.setDescription(descriptionHtml);
            });
    }

    /**
     * Parse Markdown to HTML for the main "Description" attribute
     *
     * @param openApi
     *            The base object containing the global description through
     *            "Info" class
     * @return Void
     */
    private void preparHtmlForGlobalDescription(OpenAPI openApi) {
        String currentDescription = openApi.getInfo().getDescription();
        if (currentDescription != null && !currentDescription.isEmpty()) {
            openApi.getInfo().setDescription(markdownConverter.toHtml(currentDescription));
        } else {
            LOGGER.error("Swagger object description is empty [" + openApi.getInfo().getTitle() + "]");
        }
    }

    private void prepareHtmlForPathDescriptions(final OpenAPI openApi) {

        final Stream<Operation> operationsFromAllPaths = openApi.getPaths().values().stream()
            .flatMap(pathItem -> Stream.of(
                pathItem.getHead(),
                pathItem.getOptions(),
                pathItem.getGet(),
                pathItem.getTrace(),
                pathItem.getPost(),
                pathItem.getPut(),
                pathItem.getPatch(),
                pathItem.getDelete()
            ).filter(Objects::nonNull));

        operationsFromAllPaths.forEach(operation -> {
            final String markdownDescription = operation.getDescription();
            final String htmlDescription = markdownConverter.toHtml(markdownDescription);

            operation.setDescription(htmlDescription);
        });
    }

    private void prepareHtmlForPathParameters(final OpenAPI openApi) {

        final Stream<Parameter> parameterFromAllPaths = openApi.getPaths().values().stream()
            .map(PathItem::getParameters)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream);

        parameterFromAllPaths.forEach(parameter -> {
            final String markdownDescription = parameter.getDescription();
            final String htmlDescription = markdownConverter.toHtml(markdownDescription);

            parameter.setDescription(htmlDescription);
        });
    }

    private void prepareHtmlForMethodsParameters(final OpenAPI openApi) {

        final Stream<Parameter> parameterFromAllMethodsOfAllPaths = openApi.getPaths().values().stream()
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
            .flatMap(operation -> Optional.ofNullable(operation.getParameters()).orElse(emptyList()).stream())
            .filter(Objects::nonNull);

        parameterFromAllMethodsOfAllPaths.forEach(parameter -> {
            final String markdownDescription = parameter.getDescription();
            final String htmlDescription = markdownConverter.toHtml(markdownDescription);

            parameter.setDescription(htmlDescription);
        });
    }
}
