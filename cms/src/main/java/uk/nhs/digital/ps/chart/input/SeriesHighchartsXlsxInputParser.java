package uk.nhs.digital.ps.chart.input;

import static java.util.Collections.singletonList;
import static uk.nhs.digital.ps.chart.ChartType.BAR;
import static uk.nhs.digital.ps.chart.ChartType.COLUMN;
import static uk.nhs.digital.ps.chart.ChartType.LINE;
import static uk.nhs.digital.ps.chart.ChartType.PIE;
import static uk.nhs.digital.ps.chart.ChartType.STACKED_BAR;
import static uk.nhs.digital.ps.chart.ChartType.STACKED_COLUMN;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.HighchartsParameters;
import uk.nhs.digital.ps.chart.model.HighchartsModel;
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.IOException;
import java.util.*;
import javax.jcr.RepositoryException;

@SuppressWarnings("PMD.TooManyStaticImports")
public class SeriesHighchartsXlsxInputParser extends AbstractHighchartsXlsxInputParser {

    private static final int CATEGORIES_INDEX = 0;

    public SeriesHighchartsXlsxInputParser() {
        super(PIE, BAR, COLUMN, LINE, STACKED_BAR, STACKED_COLUMN);
    }

    protected HighchartsModel parseXlsxChart(final AbstractHighchartsParameters abstractParameters
    ) throws IOException, RepositoryException {
        HighchartsParameters paramaters = (HighchartsParameters) abstractParameters;

        final Map<Integer, Series> indexedSeries = new HashMap<>();

        final List<String> categories = new ArrayList<>();

        final XSSFWorkbook workbook = readXssfWorkbook(paramaters.getInputFileContent());

        final XSSFSheet sheet = workbook.getSheetAt(0);

        // Get the headers
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row header = rowIterator.next();
        for (int i = 1; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            if (cell.getCellType() != CellType.BLANK) {
                indexedSeries.put(i, new Series(getStringValue(cell)));
            }
        }

        // Get the data
        rowIterator.forEachRemaining(row -> {
            // First column is the series name (category)
            String category = getStringValue(row.getCell(CATEGORIES_INDEX));
            categories.add(category);

            for (int i = 1; i < indexedSeries.size() + 1; i++) {
                Cell cell = row.getCell(i);
                Optional<Double> cellValue = getDoubleValue(cell);
                indexedSeries.computeIfAbsent(i, key -> new Series(""))
                    .add(cell != null && cell.getCellType() != CellType.BLANK
                        ? new Point(category, cellValue.isPresent() ? cellValue.get() : null) : new Point(category, null));
            }
        });

        String title = paramaters.getTitle();

        final ArrayList<Series> seriesValues = new ArrayList<>(indexedSeries.values());

        ChartType chartType = paramaters.getChartType();
        String yTitle = paramaters.getYTitle();

        return PIE.equals(chartType)
            // We only have one series in a pie chart so just get the first
            ? new HighchartsModel(chartType, title, singletonList(seriesValues.get(0)), null, null, null)
            : new HighchartsModel(chartType, title, seriesValues, yTitle, null, categories);
    }
}
