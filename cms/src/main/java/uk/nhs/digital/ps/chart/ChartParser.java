package uk.nhs.digital.ps.chart;

import static java.util.Collections.singletonList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.InputStream;
import java.util.*;
import javax.jcr.Binary;

public class ChartParser {
    private static final int CATEGORIES_INDEX = 0;

    private static final ChartParser INSTANCE = new ChartParser();

    private ChartParser() {
    }

    public SeriesChart parse(String type, String title, String yTitle, Binary data) {
        ChartType chartType = ChartType.toChartType(type);

        ChartData chartData;
        try {
            chartData = parseData(data);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse chart data file", ex);
        }

        ArrayList<Series> series = new ArrayList<>(chartData.getSeries().values());
        List<String> categories = chartData.getCategories();

        switch (chartType) {
            case PIE:
                // We only have one series in a pie chart so just get the first
                return new SeriesChart(chartType, title, singletonList(series.get(0)), null, null);
            case BAR:
            case COLUMN:
            case LINE:
            case STACKED_BAR:
            case STACKED_COLUMN:
                return new SeriesChart(chartType, title, series, yTitle, categories);
            default:
                throw new RuntimeException("Unknown Chart Type: " + type);
        }
    }

    private ChartData parseData(Binary data) throws Exception {
        ChartData chartData = new ChartData();

        XSSFWorkbook workbook;
        try (InputStream is = data.getStream()) {
            workbook = new XSSFWorkbook(is);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);

        // Get the headers
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row header = rowIterator.next();
        for (int i = 1; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            chartData.getSeries().put(i, new Series(getStringValue(cell)));
        }

        // Get the data
        rowIterator.forEachRemaining(row -> {
            // First column is the series name (category)
            String category = getStringValue(row.getCell(CATEGORIES_INDEX));
            chartData.getCategories().add(category);

            for (int i = 1; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                chartData.getSeries().computeIfAbsent(i, key -> new Series(""))
                    .add(new Point(category, getDoubleValue(cell)));
            }
        });

        return chartData;
    }

    private static Double getDoubleValue(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_STRING ? Double.valueOf(cell.getStringCellValue()) : cell.getNumericCellValue();
    }

    private static String getStringValue(Cell cell) {
        return cell.getCellType() == Cell.CELL_TYPE_STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    public static ChartParser getInstance() {
        return INSTANCE;
    }

    private class ChartData {
        private List<String> categories;
        private HashMap<Integer, Series> series;

        private ChartData() {
            categories = new ArrayList<>();
            series = new HashMap<>();
        }

        private List<String> getCategories() {
            return categories;
        }

        private HashMap<Integer, Series> getSeries() {
            return series;
        }
    }
}
