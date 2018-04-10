package resources.site.freemarker;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Test to check freemarker .ftl templates have correct output formatting and auto escaping set as the first line
 * in the .ftl template file.  Freemarker recommend using *.ftlh files along with version 2.3.24 or higher to auto set
 * output formatting (https://freemarker.apache.org/docs/pgui_config_outputformatsautoesc.html), however at present,
 * doing this affects the design time refreshing of content in Hippo ftl templates (the refresh takes place,
 * however, the content is not updated until a full rebuild takes place). Currently acknowledged that Hippo does not
 * work out of the box correctly with *.ftlh (https://issues.onehippo.com/browse/ARCHE-523)
 */
public class FreemarkerFileTest {

    private static final String FTL_FILES_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/site/freemarker";
    private static final String DIRECTIVE_OUTPUT_FORMAT_HTML = "<#ftl output_format=\"HTML\"";
    private static final String DIRECTIVE_OUTPUT_FORMAT_PLAIN_TEXT = "<#ftl output_format=\"plainText\"";

    @Test
    public void checkOutputFormatDirectiveIncluded() {

        // given (files in freemarker directory)

        // when
        final List<String> filesMissingOutputFormatDirective = checkFtlFilesForMissingOutputFormatDirective(FTL_FILES_DIRECTORY);

        // then
        assertThat("Output Format correctly set in all freemarker templates.",
            filesMissingOutputFormatDirective,
            is(empty()));
    }

    private List<String> checkFtlFilesForMissingOutputFormatDirective(final String pathToCheck) {

        final List<String> filesMissingOutputFormatDirective = new ArrayList<>();

        try {
            // Check for files in this folder and all sub folders
            Files.walkFileTree(Paths.get(pathToCheck), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                    if (attrs.isDirectory()) {
                        return FileVisitResult.CONTINUE;
                    }

                    if ("ftl".equalsIgnoreCase(getExtension(file.getFileName().toString()))) {

                        // Directive must always be first line in ftl file
                        try (Stream<String> stream = Files.lines(file.toAbsolutePath())) {
                            stream
                                .limit(1)
                                .filter(s -> !s.contains(DIRECTIVE_OUTPUT_FORMAT_HTML)
                                            && !s.contains(DIRECTIVE_OUTPUT_FORMAT_PLAIN_TEXT))
                                .map(f -> file.getFileName())
                                .forEach(element -> filesMissingOutputFormatDirective.add(element.toString()));

                        } catch (final Exception exception) {
                            throw new RuntimeException(exception);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (final Exception exception) {
            throw new RuntimeException("Failed to check ftl files in " + pathToCheck, exception);
        }

        return filesMissingOutputFormatDirective;
    }
}
