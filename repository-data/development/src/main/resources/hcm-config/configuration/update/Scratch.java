import org.apache.commons.io.FileUtils;
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
class Main {
    public static void main(String[] args) {
        final SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter converter =
            new SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter();
        final String pathToSpecJsonFile = args[0];
        final String json = getJson(pathToSpecJsonFile);
        final String html = converter.htmlFrom(json);
        System.out.println(html);
    }
    private static String getJson(final String specJsonFilePath) {
        try {
            return FileUtils.readFileToString(new File(specJsonFilePath), "UTF-8");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read spec JSON from " + specJsonFilePath, e);
        }
    }
}
