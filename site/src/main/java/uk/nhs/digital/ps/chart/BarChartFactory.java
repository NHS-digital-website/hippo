package uk.nhs.digital.ps.chart;

import static java.util.stream.Collectors.toList;

import org.apache.jackrabbit.JcrConstants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.InputStream;
import java.util.*;
import javax.jcr.Binary;

public class BarChartFactory {
    private static final int CATEGORIES_INDEX = 0;

    private final String title;
    private final HippoResource dataFile;

    private HashMap<Integer, Series> columns;

    public BarChartFactory(String title, HippoResource dataFile) {
        this.title = title;
        this.dataFile = dataFile;
    }

    public BarChart build() {
        try {
            parse();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse chart data file: " + dataFile.getPath(), ex);
        }

        return new BarChart(title, getCategories(), "TODO", getSeries());
    }

    private void parse() throws Exception {
        //TODO - remove after validation
        if (dataFile.isBlank()) {
            return;
        }

        Binary binary = dataFile.getNode().getProperty(JcrConstants.JCR_DATA).getBinary();

        XSSFWorkbook workbook;
        try (InputStream is = binary.getStream()) {
            workbook = new XSSFWorkbook(is);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);

        // Get the headers
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row header = rowIterator.next();
        columns = new HashMap<>();
        header.cellIterator().forEachRemaining(cell ->
            columns.put(cell.getColumnIndex(), new Series(cell.getStringCellValue())));

        // Get the data
        rowIterator.forEachRemaining(row ->
            row.cellIterator().forEachRemaining(cell ->
                columns.computeIfAbsent(cell.getColumnIndex(), key -> new Series(""))
                    .add(convertCellValue(cell))));
    }

    private Object convertCellValue(Cell cell) {
        // If it is the categories, we want to provide the names as strings otherwise data must be numeric
        return cell.getColumnIndex() == CATEGORIES_INDEX ? getStringValue(cell) : getDoubleValue(cell);
    }

    private Double getDoubleValue(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_STRING ? Double.valueOf(cell.getStringCellValue()) : cell.getNumericCellValue();
    }

    private String getStringValue(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    private List<String> getCategories() {
        return columns.get(CATEGORIES_INDEX).getData();
    }

    private List<Series> getSeries() {
        return columns.entrySet().stream()
            .filter(entry -> entry.getKey() != CATEGORIES_INDEX)
            .map(Map.Entry::getValue)
            .collect(toList());
    }

}
