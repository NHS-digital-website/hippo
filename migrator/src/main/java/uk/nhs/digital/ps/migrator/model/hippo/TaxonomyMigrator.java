package uk.nhs.digital.ps.migrator.model.hippo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.migrator.MigrationReport;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.Property;
import uk.nhs.digital.ps.migrator.model.nesstar.PublishingPackage;
import uk.nhs.digital.ps.migrator.model.taxonomy.TaxonomyTerm;

import static java.util.stream.Collectors.toList;

/**
 * This class is responsible for providing the taxonomy definition that we are going
 * to import into hippo and a mapping to those terms from existing P Codes.
 */
public class TaxonomyMigrator {

    private static final String TAXONOMY_ROOT_NODE_NAME = "publication_taxonomy";
    private static final String TAXONOMY_DEFINITION_COLUMN = "concept";
    private static final String TAXONOMY_JCR_PATH = "/content/taxonomies/publication_taxonomy";
    private static final String TAXONOMY_MAPPING_COLUMN_PREFIX = "Leaf";
    private static final String P_CODE_COLUMN = "P Code";

    private MigrationReport migrationReport;
    private ExecutionParameters executionParameters;

    private HashSet<String> taxonomyKeys;
    private HashMap<String, List<String>> taxonomyMapping;
    private TaxonomyTerm taxonomyDefinition;

    public TaxonomyMigrator(MigrationReport migrationReport, ExecutionParameters executionParameters) {
        this.migrationReport = migrationReport;
        this.executionParameters = executionParameters;
    }

    public void init() {
        if (taxonomyMapping == null) {
            readTaxonomyMapping();
        }

        if (taxonomyDefinition == null) {
            readTaxonomyDefinition();
        }
    }

    public List<String> getTaxonomyKeys(PublishingPackage dataset) {
        List<String> taxonomyKeys = taxonomyMapping.get(dataset.getUniqueIdentifier());
        if (taxonomyKeys == null || taxonomyKeys.isEmpty()) {
            migrationReport.add("No taxonomy terms were provided in the mapping for dataset: " + dataset,
                "Dataset will be imported without any taxonomy terms.");
            return Collections.emptyList();
        }

        Map<Boolean, List<String>> map = taxonomyKeys.stream()
            .collect(Collectors.partitioningBy(key -> this.taxonomyKeys.contains(key)));

        List<String> notAllowedKeys = map.get(false);
        if (!notAllowedKeys.isEmpty()) {
            migrationReport.add("Invalid taxonomy terms were present in the taxonomy mapping",
                "Dataset: " + dataset,
                "Taxonomy Terms: " + String.join(", ", notAllowedKeys));
        }

        return map.get(true);
    }

    public TaxonomyTerm getTaxonomyDefinition() {
        return taxonomyDefinition;
    }

    private void readTaxonomyMapping() {
        Path taxonomyMappingImportPath = executionParameters.getTaxonomyMappingImportPath();
        if (taxonomyMappingImportPath == null) {
            return;
        }

        Iterator<Row> rowIterator = getRowIterator(taxonomyMappingImportPath);

        Row headerRow = rowIterator.next();
        List<Integer> pCodeCols = streamRow(headerRow)
            .filter(cell -> cell.getStringCellValue().equals(P_CODE_COLUMN))
            .map(Cell::getColumnIndex)
            .collect(toList());

        if (pCodeCols.size() != 1) {
            throw new RuntimeException("Spreadsheet needs to have a single P Code column.");
        }

        int pCodeCol = pCodeCols.get(0);

        List<Integer> taxonomyColumnIndexes = streamRow(headerRow)
            .filter(cell -> cell.getStringCellValue().startsWith(TAXONOMY_MAPPING_COLUMN_PREFIX))
            .map(Cell::getColumnIndex)
            .collect(toList());

        taxonomyMapping = new HashMap<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            String pCode = row.getCell(pCodeCol).getStringCellValue().trim();

            List<String> taxonomyKeys = streamRow(row)
                .filter(c -> taxonomyColumnIndexes.contains(c.getColumnIndex()))
                .map(Cell::getStringCellValue)
                .map(TaxonomyTerm::covertTermToKey)
                .filter(key -> !key.isEmpty())
                .collect(toList());

            List<String> existingMapping = taxonomyMapping.put(pCode, taxonomyKeys);
            if (existingMapping != null) {
                migrationReport.add("Taxonomy mapping file has duplicate entries for identifier: " + pCode,
                    "The last mapping in the spreadsheet will be used and the following mapping will be ignored.",
                    pCode + " -> " + String.join(",", existingMapping));
            }
        }
    }

    private void readTaxonomyDefinition() {

        Iterator<Row> rowIterator = getRowIterator(executionParameters.getTaxonomyDefinitionImportPath());

        // Which column headers are for the taxonomy terms that we are interested in
        Row headerRow = rowIterator.next();
        List<Integer> taxonomyColumnIndexes = streamRow(headerRow)
            .filter(cell -> TAXONOMY_DEFINITION_COLUMN.equals(cell.getStringCellValue()))
            .map(Cell::getColumnIndex)
            .collect(Collectors.toList());

        // Scheme row, which is the root for all our terms
        Row schemeRow = rowIterator.next();
        TaxonomyTerm root = new TaxonomyTerm(
            TAXONOMY_ROOT_NODE_NAME,
            null,
            "hippotaxonomy:taxonomy",
            new Property("hippotaxonomy:locales", true, "en"),
            new Property("jcr:path", false, TAXONOMY_JCR_PATH),
            new Property("jcr:localizedName", false, TAXONOMY_ROOT_NODE_NAME)
        );

        TaxonomyTerm current = root;
        int currentColumnIndex = schemeRow.getCell(schemeRow.getFirstCellNum()).getColumnIndex();
        HashSet<String> keys = new HashSet<>();

        while (rowIterator.hasNext()) {
            // Get the cell that has taxonomy term in it
            List<Cell> cells = streamRow(rowIterator.next())
                .filter(c -> taxonomyColumnIndexes.contains(c.getColumnIndex()))
                .filter(c -> !c.getStringCellValue().isEmpty())
                .collect(Collectors.toList());

            // Each row should only have one taxonomy term in it
            if (cells.size() > 1) {
                throw new RuntimeException("Multiple terms found on the same row");
            } else if (cells.size() == 0) {
                // There are some empty rows an the end of the spreadsheet
                continue;
            }

            Cell cell = cells.get(0);
            int depthDelta = cell.getColumnIndex() - currentColumnIndex;
            if (depthDelta > 1) {
                throw new RuntimeException("Found a child without a parent");
            }

            // Go back up the tree to find our parent
            for (int i=0; i>=depthDelta; i--) {
                current = current.getParent();
            }

            current = current.addChild(cell.getStringCellValue());
            String taxonomyKey = current.getName();
            if (!keys.add(taxonomyKey)) {
                throw new RuntimeException("Duplicate taxonomy key: " + taxonomyKey);
            }

            currentColumnIndex = cell.getColumnIndex();
        }

        taxonomyKeys = keys;
        taxonomyDefinition = root;
    }

    private static Stream<Cell> streamRow(Row row) {
        return StreamSupport.stream(((Iterable<Cell>) row::cellIterator).spliterator(), false);
    }

    private static Iterator<Row> getRowIterator(Path path) {
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
