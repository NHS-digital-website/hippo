package uk.nhs.digital.apispecs.swagger;

import static java.util.Objects.requireNonNull;

import io.swagger.codegen.v3.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.OpenApiSpecificationJsonToHtmlConverter;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter implements OpenApiSpecificationJsonToHtmlConverter {

    private static final Logger log = LoggerFactory.getLogger(SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter.class);

    private static final String SPEC_HTML_FILE_NAME = "index.html";

    private static final String TEMPLATE_FOLDER_RELATIVE_RESOURCE_PATH = "api-specification/codegen-templates";

    @Override public String htmlFrom(final String openApiSpecificationJson) {

        try {
            final OpenAPI openApiModel = openApiModelFrom(openApiSpecificationJson);

            return toHtml(openApiModel);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to generate HTML for OpenAPI specification.", e);
        }
    }

    private OpenAPI openApiModelFrom(final String openApiSpecificationJson) {

        final ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);

        final SwaggerParseResult swaggerParseResult = new OpenAPIV3Parser()
            .readContents(openApiSpecificationJson, null, parseOptions);

        Optional.ofNullable(swaggerParseResult.getMessages())
            .orElse(Collections.emptyList())
            .forEach(log::warn);

        return swaggerParseResult.getOpenAPI();
    }

    private String toHtml(final OpenAPI openApi) throws IOException {

        File specificationTempOutputDirectory = null;
        try {
            specificationTempOutputDirectory = createSpecificationTempOutputDirectory();

            final Generator generator = configureCodeGenWith(openApi, specificationTempOutputDirectory);

            final List<File> codegenSpecificationOutputFiles = generator.generate();

            return specificationHtmlFrom(codegenSpecificationOutputFiles);

        } finally {
            cleanupIfPresent(specificationTempOutputDirectory);
        }
    }

    private Generator configureCodeGenWith(final OpenAPI openApiModel, final File specificationTempOutputDirectory) {

        final ApiSpecificationStaticHtml2Codegen codegenConfig = new ApiSpecificationStaticHtml2Codegen();
        codegenConfig.setOutputDir(specificationTempOutputDirectory.getPath());
        codegenConfig.additionalProperties()
            .put(CodegenConstants.TEMPLATE_DIR, filesystemPathFromClassPathResource(TEMPLATE_FOLDER_RELATIVE_RESOURCE_PATH));

        final ClientOptInput clientOptInput = new ClientOptInput();
        clientOptInput
            .opts(new ClientOpts())
            .config(codegenConfig)
            .setOpenAPI(openApiModel);

        return new DefaultGenerator().opts(clientOptInput);
    }

    private String filesystemPathFromClassPathResource(final String resourceClassPath) {
        return requireNonNull(
                getClass().getClassLoader().getResource(resourceClassPath), "Classpath resource not found: " + resourceClassPath
        ).getPath();
    }

    private File createSpecificationTempOutputDirectory() throws IOException {
        return Files.createTempDirectory("cms_apispecification_temp_output_").toFile();
    }

    private String specificationHtmlFrom(final List<File> apiSpecificationOutputFiles) {

        final File indexFile = apiSpecificationOutputFiles.stream()
            .filter(file -> file.getName().equals(SPEC_HTML_FILE_NAME))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(SPEC_HTML_FILE_NAME + " not found."));

        return readFileContent(indexFile);
    }

    private String readFileContent(final File indexFile) {
        try {
            return FileUtils.readFileToString(indexFile, "UTF-8");
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to read file " + indexFile, e);
        }
    }

    private void cleanupIfPresent(final File directoryToCleanUp) {
        Optional.ofNullable(directoryToCleanUp).ifPresent(directory -> {
            try {
                FileUtils.deleteDirectory(directory);
            } catch (final IOException e) {
                throw new UncheckedIOException("Failed to delete temp output dir " + directoryToCleanUp.getAbsolutePath(), e);
            }
        });
    }
}
