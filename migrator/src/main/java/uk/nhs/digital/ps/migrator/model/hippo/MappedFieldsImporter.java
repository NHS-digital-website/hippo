package uk.nhs.digital.ps.migrator.model.hippo;

import static java.util.stream.Collectors.*;
import static uk.nhs.digital.ps.migrator.report.IncidentType.FIELD_MAPPING_DUPLICATE;
import static uk.nhs.digital.ps.migrator.report.IncidentType.FIELD_MAPPING_MISSING;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.report.MigrationReport;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Import the mapping spreadsheet for Coverage Start, Coverage End, Geographic Coverage, Granularity
 */
public class MappedFieldsImporter {

    private MigrationReport migrationReport;
    private ExecutionParameters executionParameters;

    private Map<String, List<Integer>> columnIndexes;
    private HashMap<String, MappedFields> mapping;

    public MappedFieldsImporter(MigrationReport migrationReport, ExecutionParameters executionParameters) {
        this.migrationReport = migrationReport;
        this.executionParameters = executionParameters;
    }

    public MappedFields getMappedFields(String pCode) {
        if (mapping == null) {
            readMapping();
        }

        MappedFields mappedFields = mapping.get(pCode);

        if (mappedFields == null) {
            migrationReport.report(pCode, FIELD_MAPPING_MISSING);
            return MappedFields.EMPTY;
        }

        return mappedFields;
    }

    private void readMapping() {
        Path importPath = executionParameters.getNesstarFieldMappingImportPath();

        Iterator<Row> rowIterator = getRowIterator(importPath);

        Row headerRow = rowIterator.next();
        columnIndexes = streamRow(headerRow)
            .collect(groupingBy(
                Cell::getStringCellValue,
                mapping(Cell::getColumnIndex, toList())));

        HashMap<String, MappedFields> mapping = new HashMap<>();

        while (rowIterator.hasNext()) {
            MappedFields mappedFields = new MappedFields(migrationReport, columnIndexes, rowIterator.next());

            String pCode = mappedFields.getPCode();
            MappedFields existingMapping = mapping.put(pCode, mappedFields);
            if (existingMapping != null) {
                migrationReport.report(pCode, FIELD_MAPPING_DUPLICATE, existingMapping.toString());
            }
        }

        this.mapping = mapping;
    }

    private Stream<Cell> streamRow(Row row) {
        return StreamSupport.stream(((Iterable<Cell>) row::cellIterator).spliterator(), false);
    }

    private Iterator<Row> getRowIterator(Path path) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(path.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        return sheet.rowIterator();
    }
}
