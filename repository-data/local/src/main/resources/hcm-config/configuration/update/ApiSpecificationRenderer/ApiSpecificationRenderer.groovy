package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils

import javax.jcr.*
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import java.util.stream.Collectors

class ApiSpecificationRenderer extends BaseNodeUpdateVisitor {

    // target/tomcat9x is the current working directory.
    // All relative paths in this script are relative to that directory.

    // CLASSES_DIR is relative to that directory.
    private static final String CLASSES_DIR = "temp/CodegenRunner"

    private String specificationJsonFileToRender

    private String html

    private String homePath = System.getProperty("user.home")

    @Override
    void initialize(final Session session) throws RepositoryException {
        super.initialize(session)

        log.info("INITIALISING...")

        specificationJsonFileToRender = parametersMap['specificationJsonFile']

        "mkdir ${CLASSES_DIR}".execute()

        compileClasses()

        html = generateHtml()

        log.info("INITIALISING DONE")
    }

    @Override
    boolean doUpdate(Node node) {
        // Query returns the hippo:handle node for the document
        // (which has the 3 variants)
        try {
            if (node.hasNodes()) {
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process record ${node}.", e)
        }

        return false
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }

    boolean updateNode(Node n) {
        def nodeType = n.getPrimaryNodeType().getName()

        if ("website:apispecification".equals(nodeType)) {

            JcrUtils.ensureIsCheckedOut(n)

            n.setProperty("website:html", html)

            log.info("UPDATED")

            return true
        }

        return false
    }

    void compileClasses() {

        log.info("COMPILATION...")


        def command = [
                "javac",
                "-d", CLASSES_DIR,
                "-classpath", String.join(":",
            "${homePath}/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.12.2/jackson-annotations-2.12.2.jar",
            "${homePath}/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.12.2/jackson-core-2.12.2.jar",
            "${homePath}/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.12.2/jackson-databind-2.12.2.jar",
            "${homePath}/.m2/repository/com/github/jknack/handlebars-helpers/4.2.0/handlebars-helpers-4.2.0.jar",
            "${homePath}/.m2/repository/com/github/jknack/handlebars/4.2.0/handlebars-4.2.0.jar",
            "${homePath}/.m2/repository/commons-io/commons-io/2.8.0/commons-io-2.8.0.jar",
            "${homePath}/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar",
            "${homePath}/.m2/repository/io/swagger/codegen/v3/swagger-codegen-generators/1.0.26/swagger-codegen-generators-1.0.26.jar",
            "${homePath}/.m2/repository/io/swagger/codegen/v3/swagger-codegen/3.0.26/swagger-codegen-3.0.26.jar",
            "${homePath}/.m2/repository/io/swagger/core/v3/swagger-models/2.1.7/swagger-models-2.1.7.jar",
            "${homePath}/.m2/repository/io/swagger/parser/v3/swagger-parser-core/2.0.26/swagger-parser-core-2.0.26.jar",
            "${homePath}/.m2/repository/io/swagger/parser/v3/swagger-parser-v3/2.0.26/swagger-parser-v3-2.0.26.jar",
            "${homePath}/.m2/repository/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar",
            "${homePath}/.m2/repository/org/commonmark/commonmark-ext-gfm-tables/0.17.0/commonmark-ext-gfm-tables-0.17.0.jar",
            "${homePath}/.m2/repository/org/commonmark/commonmark/0.17.0/commonmark-0.17.0.jar",
            "${homePath}/.m2/repository/org/jetbrains/annotations/18.0.0/annotations-18.0.0.jar",
            "${homePath}/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar",
        ),
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/ApiSpecificationStaticHtml2Codegen.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/SchemaHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/MarkdownHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/IndentationHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/TemplateRenderingException.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/ContextModelsStack.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/IfNotNullHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/IfRequiredHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/EnumHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/TypeAnyHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/SchemaRenderingException.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/JacksonPrettyJsonHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/TypeAnySanitisingHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/VariableValueHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/BorderHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/UuidHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/ContextModelsStack.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/schema/UniqueModelStackExtractor.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/HasOneItemHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/IsAnyTrueHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/HeadingsHyperlinksFromMarkdownHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/handlebars/StringBooleanVariableHelper.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/model/BodyWithMediaTypesExtractor.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/request/examplerenderer/CodegenParameterExampleHtmlRenderer.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/request/examplerenderer/CodegenParamDefinition.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/request/examplerenderer/CodegenParamSchema.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/request/examplerenderer/ParamExample.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/commonmark/CommonmarkMarkdownConverter.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/commonmark/TableSortOffAttributeProvider.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/commonmark/HeadingAttributeProvider.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/commonmark/CodeAttributeProvider.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/commonmark/MarkdownConversionException.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/request/bodyextractor/ToPrettyJsonStringDeserializer.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/OpenApiSpecificationJsonToHtmlConverter.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/request/examplerenderer/CodegenParamDefinition.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/model/BodyWithMediaTypeObjects.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/model/MediaTypeObjects.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/model/MediaTypeObject.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/model/ExampleObject.java",
                "../../cms/src/main/java/uk/nhs/digital/apispecs/swagger/model/SchemaObject.java",
                "../../cms/src/main/java/uk/nhs/digital/ExceptionUtils.java",
                "../../repository-data/local/src/main/resources/hcm-config/configuration/update/ApiSpecificationRenderer/CodegenRunner.java",
        ].stream().collect(Collectors.joining(" "))

        log.info("COMPILATION COMMAND: ${command}")

        def stderr = new StringBuilder()
        def stdout = new StringBuilder()

        def process = command.execute()
        process.consumeProcessOutputStream(stdout)
        process.consumeProcessErrorStream(stderr)
        process.waitFor()

        log.info("COMPILATION STDOUT: $stdout")
        log.info("COMPILATION STDERR: $stderr")

        log.info("COMPILATION DONE")
    }

    String generateHtml() {

        log.info("RENDERING...")

        final command = [
                "java",
                "-classpath", String.join(":",
                CLASSES_DIR,
            "${homePath}/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.12.2/jackson-annotations-2.12.2.jar",
            "${homePath}/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.12.2/jackson-core-2.12.2.jar",
            "${homePath}/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.12.2/jackson-databind-2.12.2.jar",
            "${homePath}/.m2/repository/com/github/jknack/handlebars-helpers/4.2.0/handlebars-helpers-4.2.0.jar",
            "${homePath}/.m2/repository/com/github/jknack/handlebars/4.2.0/handlebars-4.2.0.jar",
            "${homePath}/.m2/repository/commons-io/commons-io/2.8.0/commons-io-2.8.0.jar",
            "${homePath}/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar",
            "${homePath}/.m2/repository/io/swagger/codegen/v3/swagger-codegen-generators/1.0.26/swagger-codegen-generators-1.0.26.jar",
            "${homePath}/.m2/repository/io/swagger/codegen/v3/swagger-codegen/3.0.26/swagger-codegen-3.0.26.jar",
            "${homePath}/.m2/repository/io/swagger/core/v3/swagger-models/2.1.7/swagger-models-2.1.7.jar",
            "${homePath}/.m2/repository/io/swagger/parser/v3/swagger-parser-core/2.0.26/swagger-parser-core-2.0.26.jar",
            "${homePath}/.m2/repository/io/swagger/parser/v3/swagger-parser-v3/2.0.26/swagger-parser-v3-2.0.26.jar",
            "${homePath}/.m2/repository/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar",
            "${homePath}/.m2/repository/org/commonmark/commonmark-ext-gfm-tables/0.17.0/commonmark-ext-gfm-tables-0.17.0.jar",
            "${homePath}/.m2/repository/org/commonmark/commonmark/0.17.0/commonmark-0.17.0.jar",
            "${homePath}/.m2/repository/org/jetbrains/annotations/18.0.0/annotations-18.0.0.jar",
            "${homePath}/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar",
            "${homePath}/.m2/repository/com/fasterxml/jackson/dataformat/jackson-dataformat-yaml/2.12.2/jackson-dataformat-yaml-2.12.2.jar",
            "${homePath}/.m2/repository/org/yaml/snakeyaml/1.28/snakeyaml-1.28.jar",
            "${homePath}/.m2/repository/io/swagger/core/v3/swagger-core/2.1.4/swagger-core-2.1.4.jar",
            "${homePath}/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.12.1/jackson-datatype-jsr310-2.12.1.jar",
            "../../cms/src/main/resources"
        ),
                "CodegenRunner",
                specificationJsonFileToRender,
        ].stream().collect(Collectors.joining(" "))

        log.info("RENDERING COMMAND: ${command}")

        def stderr = new StringBuilder()
        def stdout = new StringBuilder()

        def process = command.execute()
        process.consumeProcessOutputStream(stdout)
        process.consumeProcessErrorStream(stderr)
        process.waitFor()

        log.info("RENDERING STDOUT: $stdout")
        log.info("RENDERING STDERR: $stderr")

        log.info("RENDERING DONE")

        return stdout
    }

}
