package uk.nhs.digital.ps.migrator.task;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.migrator.model.hippo.HippoImportableItem;

import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static uk.nhs.digital.ps.migrator.misc.TextHelper.toLowerCaseDashedValue;

public class ImportableFileWriter {

    private static final Logger logger = LoggerFactory.getLogger(ImportableFileWriter.class);
    private static Configuration cfg;

    static void writeImportableFiles(final List<? extends HippoImportableItem> importableItems,
                                     final Path targetDir) {


        for (int i = 1; i <= importableItems.size(); i++) {

            final HippoImportableItem importableItem = importableItems.get(i - 1);

            writeImportableFile(
                importableItem,
                getFileName(i, importableItem),
                targetDir
            );
        }
    }

    private static void writeImportableFile(final HippoImportableItem importableItem,
                                            final String fileName,
                                            final Path targetDir) {

        try {

            Path targetFilePath = Paths.get(targetDir.toString(), fileName);


            final String itemTypeName = importableItem.getClass().getSimpleName().toLowerCase();

            final Template template = getFreemarkerConfiguration()
                .getTemplate(itemTypeName + ".json.ftl");

            final Writer writer = new StringWriter();

            template.process(new HashMap<String, Object>(){{
                put(itemTypeName, importableItem);
            }}, writer);

            final String importableFileContent = writer.toString();

            logger.debug("Writing file {}: {}", fileName, importableFileContent);

            Files.write(targetFilePath, importableFileContent.getBytes());

        } catch (final Exception e) {
            throw new RuntimeException("Failed to write export files.", e);
        }
    }

    private static Configuration getFreemarkerConfiguration() {
        if (cfg == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_26);
            cfg.setClassForTemplateLoading(ImportableFileWriter.class, "/templates");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
        }

        return cfg;
    }

    private static String getFileName(final int i, final HippoImportableItem importableItem) {
        return String.format(
            "%06d%s_%s_%s.json",
            i,
            StringUtils.leftPad("",importableItem.getDepth(), '_'),
            importableItem.getClass().getSimpleName().toUpperCase(),
            toLowerCaseDashedValue(importableItem.getLocalizedName())
        );
    }

}
