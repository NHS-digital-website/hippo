package uk.nhs.digital.apispecs.swagger;

import io.swagger.codegen.v3.*;
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.apache.commons.io.FileUtils;
import uk.nhs.digital.apispecs.ApiSpecificationHtmlProvider;
import uk.nhs.digital.apispecs.OpenApiSpecificationRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SwaggerCodeGenApiSpecificationHtmlProvider implements ApiSpecificationHtmlProvider {

    private static final String SPEC_HTML_FILE_NAME = "index.html";

    private final OpenApiSpecificationRepository openApiSpecificationRepository;

    public SwaggerCodeGenApiSpecificationHtmlProvider(final OpenApiSpecificationRepository openApiSpecificationRepository) {
        this.openApiSpecificationRepository = openApiSpecificationRepository;
    }

    public String getHtmlForSpec(final ApiSpecificationDocument specificationDocument) {

        File specificationTempOutputDirectory = null;

        try {
            specificationTempOutputDirectory = createSpecificationTempOutputDirectory();

            final File specificationJson = obtainSpecificationJsonFor(specificationDocument, specificationTempOutputDirectory);

            final List<File> swaggerSpecificationOutputFiles = convertSpecificationJsonToHtml(specificationJson, specificationTempOutputDirectory);

            return specificationHtmlFileFrom(swaggerSpecificationOutputFiles);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to generate HTML for specification " + specificationDocument, e);
        } finally {
            cleanupIfPresent(specificationTempOutputDirectory);
        }
    }

    private List<File> convertSpecificationJsonToHtml(final File openApiSpecificationJson, final File specificationTempOutputDirectory) {

        final File swaggerOutputDirectory = createSwaggerOutputDirectoryIn(specificationTempOutputDirectory);

        final Generator generator = configureSwaggerCodeGen(openApiSpecificationJson, swaggerOutputDirectory);

        return generateSpecificationHtmlFilesWith(generator);
    }

    private List<File> generateSpecificationHtmlFilesWith(final Generator generator) {
        return Optional.ofNullable(generator.generate())
            .orElseGet(Collections::emptyList);
    }

    private Generator configureSwaggerCodeGen(final File openApiSpecificationJson, final File swaggerOutputDirectory) {
        final ClientOptInput clientOptInput = new ClientOptInput();

        final ClientOpts clientOptions = new ClientOpts();

        final CodegenConfig codegenConfig = new StaticHtml2Codegen();
        codegenConfig.setOutputDir(swaggerOutputDirectory.getPath());

        clientOptInput
            .opts(clientOptions)
            .config(codegenConfig)
            .setOpenAPI(new OpenAPIV3Parser().read(openApiSpecificationJson.toURI().toASCIIString(), null, null));

        return new DefaultGenerator().opts(clientOptInput);
    }

    private File createSpecificationTempOutputDirectory() throws IOException {
        return Files.createTempDirectory("cms_apispecification_temp_output_").toFile();
    }

    private File createSwaggerOutputDirectoryIn(final File tempDir) {
        final Path swaggerCodegenOutputDir = Paths.get(tempDir.getPath(), "swagger_codegen_output");

        try {
            return Files.createDirectory(swaggerCodegenOutputDir).toFile();
        } catch (final IOException e) {
            throw new RuntimeException("Failed to create Swagger Codegen output directory " + swaggerCodegenOutputDir, e);
        }
    }

    private File obtainSpecificationJsonFor(final ApiSpecificationDocument apiSpecificationDocument,
                                            final File specificationTempOutputDirectory) {

        final String openApiSpecJson = openApiSpecificationRepository.apiSpecificationJsonForSpecId(apiSpecificationDocument.getId());

        return apiSpecificationJsonFileWith(openApiSpecJson, specificationTempOutputDirectory);
    }

    private File apiSpecificationJsonFileWith(final String openApiSpecJson, final File tempDir) {

        final Path apiSpecJsonFilePath = Paths.get(tempDir.getPath(), "openApiSpecification.json");

        final File apiSpecJsonFile;
        try {
            apiSpecJsonFile = Files.createFile(apiSpecJsonFilePath).toFile();
            FileUtils.write(apiSpecJsonFile, openApiSpecJson, "UTF-8");
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to save API Specification JSON in " + apiSpecJsonFilePath, e);
        }

        return apiSpecJsonFile;
    }

    private String specificationHtmlFileFrom(final List<File> apiSpecificationOutputFiles) {

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
