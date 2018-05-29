package uk.nhs.digital.ps.chart.input;

import static java.util.Collections.singletonList;
import static uk.nhs.digital.ps.chart.ChartType.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.SeriesChart;
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public class SeriesHighchartsXlsxInputParser extends AbstractHighchartsXlsxInputParser {

    private static final int CATEGORIES_INDEX = 0;

    public SeriesHighchartsXlsxInputParser() {
        super(PIE, BAR, COLUMN, LINE, STACKED_BAR, STACKED_COLUMN);
    }

    protected SeriesChart newSeriesChart(final ChartType chartType,
                                         final String chartTitle,
                                         final String chartYTitle,
                                         final Binary inputFileContent
    ) throws IOException, RepositoryException {
        final Map<Integer, Series> indexedSeries = new HashMap<>();

        final List<String> categories = new ArrayList<>();

        final XSSFWorkbook workbook = readXssfWorkbook(inputFileContent);

        final XSSFSheet sheet = workbook.getSheetAt(0);

        // Get the headers
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row header = rowIterator.next();
        for (int i = 1; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            indexedSeries.put(i, new Series(getStringValue(cell)));
        }

        // Get the data
        rowIterator.forEachRemaining(row -> {
            // First column is the series name (category)
            String category = getStringValue(row.getCell(CATEGORIES_INDEX));
            categories.add(category);

            for (int i = 1; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                indexedSeries.computeIfAbsent(i, key -> new Series(""))
                    .add(new Point(category, getDoubleValue(cell)));
            }
        });

        final ArrayList<Series> seriesValues = new ArrayList<>(indexedSeries.values());
        final SeriesChart seriesChart = PIE.equals(chartType)
            // We only have one series in a pie chart so just get the first
            ? new SeriesChart(chartType, chartTitle, singletonList(seriesValues.get(0)), null, null, null)
            : new SeriesChart(chartType, chartTitle, seriesValues, chartYTitle, null, categories);

        return seriesChart;
    }
}
