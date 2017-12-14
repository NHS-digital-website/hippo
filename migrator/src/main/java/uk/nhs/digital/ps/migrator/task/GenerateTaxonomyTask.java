package uk.nhs.digital.ps.migrator.task;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.migrator.config.ExecutionParameters;
import uk.nhs.digital.ps.migrator.model.Property;
import uk.nhs.digital.ps.migrator.model.taxonomy.TaxonomyTerm;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GenerateTaxonomyTask implements MigrationTask {

    private static final String TAXONOMY_COLUMN = "concept";
    private static final String TAXONOMY_JCR_PATH = "/content/taxonomies/publication_taxonomy";

    private static final String TAXONOMY_ROOT_NAME = "publication_taxonomy";

    private final ExecutionParameters executionParameters;

    public GenerateTaxonomyTask(final ExecutionParameters executionParameters) {
        this.executionParameters = executionParameters;
    }

    @Override
    public boolean isRequested() {
        return executionParameters.getTaxonomySpreadsheetPath() != null;
    }

    @Override
    public void execute() {

        Iterator<Row> rowIterator = getRowIterator();

        // Which column headers are for the taxonomy terms that we are interested in
        Row headerRow = rowIterator.next();
        List<Integer> taxonomyColumnIndexes = streamRow(headerRow)
            .filter(cell -> TAXONOMY_COLUMN.equals(cell.getStringCellValue()))
            .map(Cell::getColumnIndex)
            .collect(Collectors.toList());

        // Scheme row, which is the root for all our terms
        Row schemeRow = rowIterator.next();
        TaxonomyTerm root = new TaxonomyTerm(
            TAXONOMY_ROOT_NAME,
            null,
            "hippotaxonomy:taxonomy",
            new Property("hippotaxonomy:locales", true, "en"),
            new Property("jcr:path", false, TAXONOMY_JCR_PATH),
            new Property("jcr:localizedName", false, TAXONOMY_ROOT_NAME)
        );

        TaxonomyTerm current = root;
        int currentColumnIndex = schemeRow.getCell(schemeRow.getFirstCellNum()).getColumnIndex();

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
            currentColumnIndex = cell.getColumnIndex();
        }

        // Write out the JSON files
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            File dir = executionParameters.getTaxonomyOutputPath().toFile();
            dir.mkdirs();
            mapper.writeValue(new File(dir, "publication_taxonomy.json"), root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Iterator<Row> getRowIterator() {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(executionParameters.getTaxonomySpreadsheetPath().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        return sheet.rowIterator();
    }

    private Stream<Cell> streamRow(Row headerRow) {
        return StreamSupport.stream(((Iterable<Cell>) headerRow::cellIterator).spliterator(), false);
    }

}
