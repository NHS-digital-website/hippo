package uk.nhs.digital.apispecs.swagger;

import static java.util.Collections.emptyList;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SwaggerCodeGenApiSpecificationHtmlProvider implements ApiSpecificationHtmlProvider {

    private static final String SPEC_HTML_FILE_NAME = "index.html";

    private final OpenApiSpecificationRepository openApiSpecificationRepository;

    public SwaggerCodeGenApiSpecificationHtmlProvider(final OpenApiSpecificationRepository openApiSpecificationRepository) {
        this.openApiSpecificationRepository = openApiSpecificationRepository;
    }

    public String getHtmlForSpec(final ApiSpecificationDocument apiSpecificationDocument) {

        File openApiSpecificationJson = null;
        List<File> apiSpecificationOutputFiles = emptyList();
        File swaggerOutputDirectory = null;

        try {
            openApiSpecificationJson = getOpenApiSpecFor(apiSpecificationDocument);

            swaggerOutputDirectory = getSwaggerOutputDirectory();

            apiSpecificationOutputFiles = convertToHtml(openApiSpecificationJson, swaggerOutputDirectory);

            return extractHtml(apiSpecificationOutputFiles);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to generate HTML for specification " + apiSpecificationDocument, e);
        } finally {
            cleanup(apiSpecificationOutputFiles, openApiSpecificationJson, swaggerOutputDirectory);
        }
    }

    private String extractHtml(final List<File> apiSpecificationOutputFiles) {

        final File indexFile = apiSpecificationOutputFiles.stream()
            .filter(file -> file.getName().equals(SPEC_HTML_FILE_NAME))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(SPEC_HTML_FILE_NAME + " not found."));

        return readFileContent(indexFile);
    }

    private String readFileContent(final File indexFile) {
        try {
            return FileUtils.readFileToString(indexFile, "UTF-8");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read file " + indexFile, e);
        }
    }

    private void cleanup(final List<File> filesCollection, final File... individualFiles) {
        Stream.concat(
            filesCollection.stream(),
            Arrays.stream(Optional.ofNullable(individualFiles).orElse(new File[0]))
        ).forEach(this::deleteFileIfExists);
    }

    private void deleteFileIfExists(final File fileToDelete) {

        try {
            if (fileToDelete != null) {
                if (fileToDelete.isFile()) {
                    fileToDelete.delete();
                } else if (fileToDelete.isDirectory()) {
                    FileUtils.deleteDirectory(fileToDelete);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<File> convertToHtml(final File openApiSpecificationJson, final File swaggerOutputDirectory) {

        final ClientOptInput clientOptInput = new ClientOptInput();

        final ClientOpts clientOptions = new ClientOpts();

        final CodegenConfig codegenConfig = new StaticHtml2Codegen();
        codegenConfig.setOutputDir(swaggerOutputDirectory.getPath());

        clientOptInput
            .opts(clientOptions)
            .config(codegenConfig)
            .setOpenAPI(new OpenAPIV3Parser().read(openApiSpecificationJson.toURI().toASCIIString(), null, null));

        final Generator generator = new DefaultGenerator().opts(clientOptInput);
        final List<File> outputFiles = Optional.ofNullable(generator.generate())
            .orElseGet(Collections::emptyList);

        return outputFiles;
    }

    private File getSwaggerOutputDirectory() throws IOException {
        return Files.createTempDirectory("cms_apispec_swagger_codegen_output_").toFile();
    }

    private File getOpenApiSpecFor(final ApiSpecificationDocument apiSpecificationDocument) {

        final String openApiSpecJson = openApiSpecificationRepository.getSpecification(apiSpecificationDocument.getId());

        final File tempFile;
        try {
            tempFile = Files.createTempFile("cms_apispec_jsonfile_", ".tmp").toFile();
            FileUtils.write(tempFile, openApiSpecJson, "UTF-8");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to obtain API specification.", e);
        }

        return tempFile;
    }
}
