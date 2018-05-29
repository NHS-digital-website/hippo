package uk.nhs.digital.ps.chart;

import static java.util.Collections.singletonList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.InputStream;
import java.util.*;
import javax.jcr.Binary;

public class HighchartsXlsxInputParser extends ChartParserBase
    implements HighchartsInputParser {

    private static final int CATEGORIES_INDEX = 0;

    @Override
    public SeriesChart parse(final ChartConfig chartConfig) {

        final String type = chartConfig.getType();
        final String title = chartConfig.getTitle();
        final String yTitle = chartConfig.getYTitle();
        final Binary data = chartConfig.getInputFileContent();

        final ChartType chartType = ChartType.toChartType(type);

        ChartData chartData;
        try {
            if (isScatterOrFunnel(chartType)) {
                ScatterChartParser scatterParser = new ScatterChartParser(chartType);
                chartData = scatterParser.parseData(data);
            } else {
                chartData = parseData(data);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse chart data file", ex);
        }

        ArrayList<Series> series = new ArrayList<>(chartData.getSeries().values());
        List<String> categories = chartData.getCategories();

        switch (chartType) {
            case PIE:
                // We only have one series in a pie chart so just get the first
                return new SeriesChart(chartType, title, singletonList(series.get(0)), null, null, null);
            case BAR:
            case COLUMN:
            case LINE:
            case STACKED_BAR:
            case STACKED_COLUMN:
                return new SeriesChart(chartType, title, series, yTitle, null, categories);
            case SCATTER_PLOT:
            case FUNNEL_PLOT:
                return new SeriesChart(chartType, title, series, chartData.getyAxisTitle(), chartData.getxAxisTitle(), null);
            default:
                throw new RuntimeException("Unknown Chart Type: " + type);
        }
    }

    @Override
    protected ChartData parseData(Binary data) throws Exception {
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
}
