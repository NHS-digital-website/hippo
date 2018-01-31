package uk.nhs.digital.ps.migrator.report;

import static java.text.MessageFormat.format;
import static java.util.Arrays.*;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT;
import static org.apache.poi.ss.usermodel.VerticalAlignment.CENTER;

import org.apache.commons.io.FileUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.nesstar.DataSetRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MigrationReport {

    private static final Logger log = LoggerFactory.getLogger(MigrationReport.class);

    private final ExecutionParameters executionParameters;
    private final ArrayList<ErrorLogEntry> errors = new ArrayList<>();

    private final ArrayList<IncidentLogEntry> incidentLogEntries = new ArrayList<>();
    private DataSetRepository datasetRepository;

    public MigrationReport(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }

    public void report(final String pCode, final IncidentType incidentType) {
        report(pCode, incidentType, "");
    }

    public void report(final String pCode, final IncidentType incidentType, final String... supportingData) {
        final List<String> supportingDataList = Optional.ofNullable(supportingData)
            .map(strings -> asList(supportingData))
            .orElse(emptyList());

        report(pCode, incidentType, supportingDataList);
    }

    public void report(final String pCode, final IncidentType incidentType, final List<String> supportingData) {
        incidentLogEntries.add(new IncidentLogEntry(pCode, incidentType, supportingData));
    }

    public void logError(String error) {
        log.error(error);
        errors.add(new ErrorLogEntry(error));
    }

    public void logError(Exception e, String... output) {

        String error = String.join("\n", output);
        if (e != null) {
            error += "\nException: " + e;
            Throwable cause = e.getCause();
            if (cause != null) {
                error += "\nCause: " + cause;
            }
        }

        log.error(error, e);

        errors.add(new ErrorLogEntry(e, error));
    }

    public void generate() {

        final Workbook workbook = new XSSFWorkbook();

        addReportSheet(workbook);

        final String reportGenerationTimestamp = getNowTimestamp();
        addMetadataSheet(workbook, reportGenerationTimestamp);

        addErrorsSheet(workbook);

        final File migrationReportFile = getMigrationReportFile(reportGenerationTimestamp);

        try (final FileOutputStream fileOutputStream = new FileOutputStream(migrationReportFile)) {

            workbook.write(fileOutputStream);
        } catch (final IOException e) {
            throw new UncheckedIOException(
                format("Failed to write migration report file {0}", migrationReportFile.toPath()), e
            );
        }
    }

    private void addErrorsSheet(final Workbook workbook) {
        final Sheet sheet = workbook.createSheet("Errors");

        AtomicInteger rowNum = new AtomicInteger(0);

        errors.forEach(errorLogEntry -> {
            final Row row = sheet.createRow(rowNum.getAndAdd(1));
            addCell(row, 0, errorLogEntry.getDescription());
        });
    }

    private File getMigrationReportFile(final String reportGenerationTimestamp) {
        Path migrationReportFilePath = executionParameters.getMigrationReportFilePath();

        final String migrationReportFileName = migrationReportFilePath.getFileName().toString()
            .replaceAll("\\{TIMESTAMP\\}", reportGenerationTimestamp);

        migrationReportFilePath = Paths.get(migrationReportFilePath.getParent().toString(), migrationReportFileName);

        executionParameters.setMigrationReportFilePath(migrationReportFilePath);

        File migrationReportFile = migrationReportFilePath.toFile();

        FileUtils.deleteQuietly(migrationReportFile);

        try {
            Files.createDirectories(migrationReportFilePath.getParent());

        } catch (final IOException e) {
            throw new UncheckedIOException(
                format("Failed to create directory to contain report file {0}", migrationReportFile.toPath()), e
            );
        }

        return migrationReportFile;
    }

    private String getNowTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd kkmm").withZone(ZoneId.systemDefault()).format(Instant.now());
    }

    public void setDatasetRepository(final DataSetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    public void logErrors() {
        errors.forEach(error -> log.error(error.getDescription(), error.getException()));
    }

    private void addReportSheet(final Workbook workbook) {
        final Sheet reportSheet = workbook.createSheet("Report");
        final CreationHelper creationHelper = workbook.getCreationHelper();

        final int PCODE_COL_NUM = 0;
        final int INCIDENT_COL_NUM = 1;
        final int DATASET_IMPORT_STATUS = 2;
        final int SUPPORTING_DATA_DESC_COL_NUM = 3;
        final int SUPPORTING_DATA_VALUES_COL_NUM = 4;
        final int DATASET_TITLE_COL_NUM = 5;

        final CellStyle headerCellStyle = workbook.createCellStyle();
        final Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

        final CellStyle labelValueCellStyle = workbook.createCellStyle();
        labelValueCellStyle.setAlignment(RIGHT);


        final AtomicInteger rowNum = new AtomicInteger(0);

        final Row headersRow = reportSheet.createRow(rowNum.getAndAdd(1));
        int headerColumnNum = PCODE_COL_NUM;
        addCell(headersRow, headerColumnNum++, "P-code", headerCellStyle);
        addCell(headersRow, headerColumnNum++, "Incident", headerCellStyle);
        addCell(headersRow, headerColumnNum++, "Impact on\nDataset migration", headerCellStyle);

        addCell(headersRow, headerColumnNum++, "Incident Data Description", headerCellStyle);
        addCell(headersRow, headerColumnNum++, "Incident Data", headerCellStyle);

        addCell(headersRow, headerColumnNum++, "Indicator", headerCellStyle);

        incidentLogEntries.forEach(logEntry -> {
            final Row row = reportSheet.createRow(rowNum.getAndAdd(1));

            int valueColumnNum = 0;

            final Cell pCodeCell = addCell(row, valueColumnNum++, logEntry.getPCode());
            final Hyperlink pCodeHyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
            pCodeHyperlink.setAddress(
                "https://indicators.hscic.gov.uk/webview/velocity?v=2&mode=documentation&submode=ddi&study=http%3A%2F%2F192.168.229.22%3A80%2Fobj%2FfStudy%2F"
                    + logEntry.getPCode()
            );
            pCodeCell.setHyperlink(pCodeHyperlink);

            addCell(row, valueColumnNum++, logEntry.getIncidentType().getDescription());
            addCell(row, valueColumnNum++, logEntry.getIncidentType().getDatasetMigrationImpact().getDescription());
            addCell(row, valueColumnNum++, logEntry.getIncidentType().getSupportingDataDescription() +
                    (isBlank(logEntry.getIncidentType().getSupportingDataDescription()) ? "" : ":"),
                labelValueCellStyle
            );
            addCell(row, valueColumnNum++, logEntry.getSupportingData().stream().collect(joining("\n")));
            addCell(row, valueColumnNum++, datasetRepository.findById(logEntry.getPCode()).getTitle());
        });

        reportSheet.autoSizeColumn(PCODE_COL_NUM);
        reportSheet.autoSizeColumn(INCIDENT_COL_NUM);
        reportSheet.autoSizeColumn(DATASET_IMPORT_STATUS);
        reportSheet.autoSizeColumn(SUPPORTING_DATA_DESC_COL_NUM);
        reportSheet.autoSizeColumn(SUPPORTING_DATA_VALUES_COL_NUM);
        reportSheet.autoSizeColumn(DATASET_TITLE_COL_NUM);
    }

    private void addMetadataSheet(final Workbook workbook, final String reportGenerationTimestamp) {
        final CellStyle labelCellStyle = workbook.createCellStyle();
        labelCellStyle.setAlignment(RIGHT);
        labelCellStyle.setVerticalAlignment(CENTER);
        final Font labelFont = workbook.createFont();
        labelFont.setBold(true);
        labelCellStyle.setFont(labelFont);

        final CellStyle valueCellStyle = workbook.createCellStyle();
        valueCellStyle.setAlignment(LEFT);
        valueCellStyle.setVerticalAlignment(CENTER);

        final Sheet metadataSheet = workbook.createSheet("Metadata");

        final AtomicInteger currentRowNumber = new AtomicInteger(5);

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "Description",
            "This report describes 'incidents' encountered while converting Clinical"
                + "\nIndicator data exported from Nesstar (legacy Portal)."
                + "\n"
                + "\n'Incident' mean anything that may be of interest to the operator"
                + "\nthat may require investigation or manual intervention. They may"
                + "\nrange from individual data field not being migrated to whole"
                + "\nDataset being ignored."
                + "\n"
                + "\nAll incidents should be reviewed and as many as possible should be"
                + "\naddressed in cooperation with the development team, so that"
                + "\nthe number of incidents reported on final migration and the number of"
                + "\nmanual interventions required post-migration remain as low as possible."
        );

        currentRowNumber.addAndGet(1);

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "Conversion timestamp", reportGenerationTimestamp
        );

        currentRowNumber.addAndGet(1);

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "Nesstar export file used", executionParameters.getNesstarZippedExportFile().getFileName()
        );

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "Compendium Mapping file used", executionParameters.getNesstarCompendiumMappingFile().getFileName()
        );

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "Fields Mapping file used", executionParameters.getNesstarFieldMappingImportPath().getFileName()
        );

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "Taxonomy definition file used", executionParameters.getTaxonomyDefinitionImportPath().getFileName()
        );

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "Taxonomy mapping file used", executionParameters.getTaxonomyMappingImportPath().getFileName()
        );

        currentRowNumber.addAndGet(3);

        addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
            "More on individual types of incidents", ":"
        );

        currentRowNumber.addAndGet(1);

        final IncidentType[] incidentTypes = IncidentType.values();
        sort(incidentTypes, comparing(IncidentType::getDescription));
        stream(incidentTypes)
            .filter(incidentType -> !isBlank(incidentType.getExtraDescription()))
            .forEach(incidentType ->
                addMetaDataRow(labelCellStyle, valueCellStyle, metadataSheet, currentRowNumber.addAndGet(1),
                    incidentType.getDescription(), incidentType.getExtraDescription()
                ));

        metadataSheet.autoSizeColumn(2);
        metadataSheet.autoSizeColumn(3);
    }

    private void addMetaDataRow(final CellStyle labelCellStyle,
                                final CellStyle valueCellStyle,
                                final Sheet sheet,
                                final int rowNumber,
                                final String label,
                                final Object value
    ) {

        final int LABEL_COLUMN_NUM = 2;
        final int VALUE_COLUMN_NUM = 3;

        final Row row = sheet.createRow(rowNumber);

        final String text = String.valueOf(value);

        addCell(row, LABEL_COLUMN_NUM, label, labelCellStyle);
        addCell(row, VALUE_COLUMN_NUM, text, valueCellStyle);

        final int linesCount = text.split("\n").length;
        if (linesCount > 1) {
            row.setHeight((short) (row.getHeight() * linesCount));
        }
    }

    private Cell addCell(final Row row, int colNum, final String value) {
        return addCell(row, colNum, value, null);
    }

    private Cell addCell(final Row row, int headerColumnNum, final String value, final CellStyle cellStyle) {
        final Cell cell = row.createCell(headerColumnNum);
        cell.setCellValue(value);
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
        return cell;
    }
}
