package uk.nhs.digital.ps.chart;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;
import uk.nhs.digital.ps.chart.model.Tooltip;

import java.io.InputStream;
import java.util.Iterator;
import javax.jcr.Binary;

public class ScatterChartParser extends ChartParserBase {

    private static final int SCATTER_SHEET_INDEX = 0;
    private static final int FUNNEL_SHEET_INDEX = 1;
    private static final String TOOLTIP_HEADER_FORMAT = "<b>{series.name}:</b>";
    private ChartType chartType;

    public ScatterChartParser(ChartType type) {
        this.chartType = type;
    }

    @Override
    protected ChartData parseData(Binary data) throws Exception {
        ChartData chartData = new ChartData();

        XSSFWorkbook workbook;
        try (InputStream is = data.getStream()) {
            workbook = new XSSFWorkbook(is);
        }

        // Funnel plot requires 2 sheets (1 for scatter, 1 for control limits). If not provided, halt processing
        if (chartType.equals(ChartType.FUNNEL_PLOT) && workbook.getNumberOfSheets() != 2) {
            throw new Exception("Funnel plot chart requires 2 worksheets in the Excel file");
        }

        // Get scatter plot data
        Series series = getScatterSeries(workbook, chartData);
        chartData.getSeries().put(0, series);

        // If Funnel plot selected and file has more than 1 sheet (funnel definition)
        // get the funnel plot data on sheet 2 which should comprise
        // of control limits (lower and upper) for the funnel lines
        if (chartType.equals(ChartType.FUNNEL_PLOT)) {

            series = getControlLimitSeries(workbook, 1, chartData);
            chartData.getSeries().put(1, series);

            series = getControlLimitSeries(workbook, 2, chartData);
            chartData.getSeries().put(2, series);
        }

        return chartData;
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
        Series series = new Series(scatterName, ChartType.SCATTER_PLOT, tooltip);
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
}
