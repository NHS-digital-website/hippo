package uk.nhs.digital.ps.chart.input;

import static uk.nhs.digital.ps.chart.ChartType.FUNNEL_PLOT;
import static uk.nhs.digital.ps.chart.ChartType.SCATTER_PLOT;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.jcr.RepositoryException;

public class ScatterHighchartsXlsxInputParser extends AbstractHighchartsXlsxInputParser {

    private static final int SCATTER_SHEET_INDEX = 0;
    private static final int FUNNEL_SHEET_INDEX = 1;
    private static final String TOOLTIP_HEADER_FORMAT = "<b>{series.name}:</b>";

    public ScatterHighchartsXlsxInputParser() {
        super(SCATTER_PLOT, FUNNEL_PLOT);
    }

    protected HighchartsModel parseXlsxChart(final AbstractHighchartsParameters parameters
    ) throws IOException, RepositoryException {

        final ChartType chartType = parameters.getChartType();

        final XSSFWorkbook workbook = readXssfWorkbook(parameters.getInputFileContent());
        final ChartData chartData = new ChartData();

        // Funnel plot requires 2 sheets (1 for scatter, 1 for control limits). If not provided, halt processing
        if (chartType.equals(FUNNEL_PLOT) && workbook.getNumberOfSheets() != 2) {
            throw new RuntimeException("Funnel plot chart requires 2 worksheets in the Excel file");
        }

        // Get scatter plot data
        Series series = getScatterSeries(workbook, chartData);
        chartData.getSeries().put(0, series);

        // If Funnel plot selected and file has more than 1 sheet (funnel definition)
        // get the funnel plot data on sheet 2 which should comprise
        // of control limits (lower and upper) for the funnel lines
        if (chartType.equals(FUNNEL_PLOT)) {

            series = getControlLimitSeries(workbook, 1, chartData);
            chartData.getSeries().put(1, series);

            series = getControlLimitSeries(workbook, 2, chartData);
            chartData.getSeries().put(2, series);
        }

        final ArrayList<Series> seriesValues = new ArrayList<>(chartData.getSeries().values());

        return new HighchartsModel(chartType, parameters.getTitle(), seriesValues, chartData.getyAxisTitle(),
            chartData.getxAxisTitle(), null);
    }

    private Series getScatterSeries(XSSFWorkbook workbook, ChartData chartData) {

        // Get x/y axis names based on sheet 1 header values
        XSSFSheet sheet = workbook.getSheetAt(SCATTER_SHEET_INDEX);
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row header = rowIterator.next();
        String scatterName = getStringValue(header.getCell(0));
        chartData.setxAxisTitle(getStringValue(header.getCell(1)));
        chartData.setyAxisTitle(getStringValue(header.getCell(2)));

        // Generate tooltip text used for scatter points
        String pointFormat = "<br>{point.name}</br><br>" + chartData.getyAxisTitle() + ": {point.y}"
            + "</br><br>" + chartData.getxAxisTitle() + ": {point.x}</br>";
        Tooltip tooltip = new Tooltip(pointFormat, TOOLTIP_HEADER_FORMAT);

        // Create series and populate
        Series series = new Series(scatterName, SCATTER_PLOT, tooltip);
        rowIterator.forEachRemaining(row -> {

            String pointName = getStringValue(row.getCell(0));
            Cell x = row.getCell(1);
            Cell y = row.getCell(2);

            series.add(new Point(pointName, getDoubleValue(x), getDoubleValue(y)));
        });

        return series;
    }

    private Series getControlLimitSeries(XSSFWorkbook workbook, int yCellNum, ChartData chartData) {

        // Get control limits based on sheet 2 values
        XSSFSheet sheet = workbook.getSheetAt(FUNNEL_SHEET_INDEX);

        // From header, get series name
        Iterator<Row> rowIterator = sheet.rowIterator();
        Row header = rowIterator.next();
        String seriesTitle = getStringValue(header.getCell(yCellNum));

        // Create tooltip
        String pointFormat = "<br>" + chartData.getyAxisTitle() + ": {point.y}</br><br>" + chartData.getxAxisTitle() + ": {point.x}</br>";
        Tooltip tooltip = new Tooltip(pointFormat, TOOLTIP_HEADER_FORMAT);

        // Create series and populate
        Series controlLimitSeries = new Series(seriesTitle, ChartType.LINE, tooltip);
        rowIterator.forEachRemaining(row -> {

            Cell x = row.getCell(0);
            Cell y = row.getCell(yCellNum);

            controlLimitSeries.add(new Point("", getDoubleValue(x), getDoubleValue(y)));
        });

        return controlLimitSeries;
    }

    private static class ChartData {
        private HashMap<Integer, Series> series;
        private String xAxisTitle;
        private String yAxisTitle;

        ChartData() {
            series = new HashMap<>();
        }

        HashMap<Integer, Series> getSeries() {
            return series;
        }

        String getxAxisTitle() {
            return xAxisTitle;
        }

        void setxAxisTitle(String xAxisTitle) {
            this.xAxisTitle = xAxisTitle;
        }

        String getyAxisTitle() {
            return yAxisTitle;
        }

        void setyAxisTitle(String yAxisTitle) {
            this.yAxisTitle = yAxisTitle;
        }
    }
}
