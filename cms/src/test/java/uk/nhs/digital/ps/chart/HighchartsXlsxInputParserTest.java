package uk.nhs.digital.ps.chart;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.apache.jackrabbit.value.BinaryImpl;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.FileInputStream;
import java.util.List;
import java.util.function.Function;
import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public class HighchartsXlsxInputParserTest {

    private Binary binary;
    private Binary binaryScatterFunnel;

    private HighchartsXlsxInputParser chartParser;

    @Before
    public void setUp() throws Exception {
        binary = new BinaryImpl(new FileInputStream("src/test/resources/ChartTestData.xlsx"));
        binaryScatterFunnel = new BinaryImpl(new FileInputStream("src/test/resources/ChartTestScatterAndFunnelData.xlsx"));

        chartParser = new HighchartsXlsxInputParser();
    }

    @Test
    public void parseFunnelChartFile() {
        final String type = "funnel_plot";
        final String title = "chart title";
        final String xTitleFromSheet = "888.0";
        final String yTitleFromSheet = "SHMI value";

        // when
        SeriesChart chart = chartParser.parse(new ChartConfig(type, title, null, binaryScatterFunnel));

        // then
        assertEquals("Chart type is set",
            ChartType.FUNNEL_PLOT.getHighChartsType(), chart.getChart().getType());
        assertEquals("Chart title is set",
            title, chart.getTitle().getText());
        assertEquals("Chart x title is correctly set",
            xTitleFromSheet, chart.getxAxis().getTitle().getText());
        assertEquals("Chart y title is correctly set",
            yTitleFromSheet, chart.getyAxis().getTitle().getText());

        // Check plot options
        assertNull("No plot options for funnel chart", chart.getPlotOptions());

        // Check the data parsed from the document is correct
        // Categories
        List<String> categoriesActual = chart.getxAxis().getCategories();
        assertNull("No categories for funnel chart", categoriesActual);

        // Series data is set correctly
        List<Series> series = chart.getSeries();
        assertThat("Correct number of series for funnel chart", series, hasSize(3));

        // Check the data parsed from the scatter and control limits are correct
        // Scatter points
        Series scatter = series.get(0);
        assertEquals("Organisation", scatter.getName());
        assertThat(getValues(scatter, Point::getName), contains("R1F - ISLE OF WIGHT NHS TRUST", "R1H - BARTS HEALTH NHS TRUST",
            "R1K - LONDON NORTH WEST HEALTHCARE NHS TRUST",
            "RA2 - ROYAL SURREY COUNTY HOSPITAL NHS FOUNDATION TRUST",
            "RA7 - UNIVERSITY HOSPITALS BRISTOL NHS FOUNDATION TRUST",
            "RA9 - TORBAY AND SOUTH DEVON NHS FOUNDATION TRUST",
            "RAE - BRADFORD TEACHING HOSPITALS NHS FOUNDATION TRUST",
            "RAJ - SOUTHEND UNIVERSITY HOSPITAL NHS FOUNDATION TRUST",
            "123.0",
            "572.11",
            "RAS - THE HILLINGDON HOSPITALS NHS FOUNDATION TRUST"));
        assertThat(getValues(scatter, Point::getX), contains(672.11, 372.11, 572.11, 622.11, 422.11, 622.11, 422.11, 129.24, 387.11, 383.18, 629.74));
        assertThat(getValues(scatter, Point::getY), contains(1.04, 0.88, 0.81, 0.91, 0.99, 0.83, 0.97, 1.17, 0.9, 0.88, 0.93));

        // lower limit
        Series lower = series.get(1);
        assertEquals("555.0", lower.getName());
        assertThat(getValues(lower, Point::getX), contains(50d, 100d, 150d, 200d, 250d, 300d, 350d, 400d, 450d, 500d));
        assertThat(getValues(lower, Point::getY), contains(0.741, 0.812, 0.822, 0.842, 0.851, 0.858, 0.863, 0.867, 0.871, 0.883));

        // upper limit
        Series upper = series.get(2);
        assertEquals("Upper limit", upper.getName());
        assertThat(getValues(upper, Point::getX), contains(50d, 100d, 150d, 200d, 250d, 300d, 350d, 400d, 450d, 500d));
        assertThat(getValues(upper, Point::getY), contains(1.343, 1.247, 1.209, 1.188, 1.174, 1.164, 1.157, 1.152, 1.172, 1.144));
    }

    @Test
    public void parseScatterChartFile() {
        final String type = "scatter_plot";
        final String title = "chart title";
        final String xTitleFromSheet = "888.0";
        final String yTitleFromSheet = "SHMI value";

        // when
        SeriesChart chart = chartParser.parse(new ChartConfig(type, title, null, binaryScatterFunnel));

        // then
        assertEquals("Chart type is set",
            ChartType.SCATTER_PLOT.getHighChartsType(), chart.getChart().getType());
        assertEquals("Chart title is set",
            title, chart.getTitle().getText());
        assertEquals("Chart x title is correctly set",
            xTitleFromSheet, chart.getxAxis().getTitle().getText());
        assertEquals("Chart y title is correctly set",
            yTitleFromSheet, chart.getyAxis().getTitle().getText());

        // Check plot options
        assertNull("No plot options for scatter chart", chart.getPlotOptions());

        // Check the data parsed from the document is correct
        // Categories
        List<String> categoriesActual = chart.getxAxis().getCategories();
        assertNull("No categories for scatter chart", categoriesActual);

        // Series data is set correctly
        List<Series> series = chart.getSeries();
        assertThat("Only one series for Scatter chart", series, hasSize(1));

        // Check the data parsed from scatter
        Series scatter = series.get(0);
        assertEquals("Organisation", scatter.getName());
        assertThat(getValues(scatter, Point::getName), contains("R1F - ISLE OF WIGHT NHS TRUST", "R1H - BARTS HEALTH NHS TRUST",
            "R1K - LONDON NORTH WEST HEALTHCARE NHS TRUST",
            "RA2 - ROYAL SURREY COUNTY HOSPITAL NHS FOUNDATION TRUST",
            "RA7 - UNIVERSITY HOSPITALS BRISTOL NHS FOUNDATION TRUST",
            "RA9 - TORBAY AND SOUTH DEVON NHS FOUNDATION TRUST",
            "RAE - BRADFORD TEACHING HOSPITALS NHS FOUNDATION TRUST",
            "RAJ - SOUTHEND UNIVERSITY HOSPITAL NHS FOUNDATION TRUST",
            "123.0",
            "572.11",
            "RAS - THE HILLINGDON HOSPITALS NHS FOUNDATION TRUST"));
        assertThat(getValues(scatter, Point::getX), contains(672.11, 372.11, 572.11, 622.11, 422.11, 622.11, 422.11, 129.24, 387.11, 383.18, 629.74));
        assertThat(getValues(scatter, Point::getY), contains(1.04, 0.88, 0.81, 0.91, 0.99, 0.83, 0.97, 1.17, 0.9, 0.88, 0.93));
    }

    @Test
    public void parseChartFile() {
        String type = "Line";
        String title = "a chart title";
        String yTitle = "a y axis title";

        // when
        SeriesChart chart = chartParser.parse(new ChartConfig(type, title, yTitle, binary));

        // then
        assertEquals("Chart type is set",
            ChartType.LINE.getHighChartsType(), chart.getChart().getType());
        assertEquals("Chart title is set",
            title, chart.getTitle().getText());
        assertEquals("Chart y title is set",
            yTitle, chart.getyAxis().getTitle().getText());

        assertNull("No plot options for line chart", chart.getPlotOptions());

        // Check the data parsed from the document is correct
        // Categories
        List<String> categoriesActual = chart.getxAxis().getCategories();
        Matcher<Iterable<? extends String>> categoriesExpected = contains("75.0", "65.0", "55 to 64", "45 to 54", "35 to 44", "25 to 34", "16 to 24", "Under 16");
        assertThat("Categories set to correct values", categoriesActual, categoriesExpected);

        // Series data is set correctly
        List<Series> series = chart.getSeries();
        assertThat(series, hasSize(2));

        Series first = series.get(0);
        assertEquals("Measure", first.getName());
        assertThat(getValues(first, Point::getY), contains(43d, 57d, 65d, 67d, 44d, 34d, 23d, 6d));
        assertThat(getValues(first, Point::getName), categoriesExpected);

        Series second = series.get(1);
        assertEquals("100.0", second.getName());
        assertThat(getValues(second, Point::getY), contains(57d, 43d, 35d, 33d, 56d, 66d, 77d, 94d));
        assertThat(getValues(second, Point::getName), categoriesExpected);
    }

    @Test
    public void parseAsPieSetsCorrectOptions() {
        // when
        SeriesChart chart = chartParser.parse(
            new ChartConfig("Pie", "chart title", "y axis title", binary)
        );

        // then
        assertEquals("Correct chart type is set",
            ChartType.PIE.getHighChartsType(), chart.getChart().getType());

        assertNull("There is no Y Axis Title",
            chart.getyAxis().getTitle().getText());

        assertNull("No plot options for pie",
            chart.getPlotOptions());

        // Check the data parsed from the document is correct
        assertNull("No categories for Pie",
            chart.getxAxis().getCategories());

        List<Series> series = chart.getSeries();
        assertThat("Pie only has one series", series, hasSize(1));

        Series first = series.get(0);
        assertEquals("Measure", first.getName());
        assertThat(getValues(first, Point::getY), contains(43d, 57d, 65d, 67d, 44d, 34d, 23d, 6d));
        assertThat(getValues(first, Point::getName), contains("75.0", "65.0", "55 to 64", "45 to 54", "35 to 44", "25 to 34", "16 to 24", "Under 16"));
    }

    @Test
    public void parseAsStackedChartSetsCorrectOptions() {
        // when
        SeriesChart chart = chartParser.parse(
            new ChartConfig("Stacked Bar", "chart title", "y axis title", binary)
        );

        // then
        assertEquals("Correct chart type is set",
            ChartType.STACKED_BAR.getHighChartsType(), chart.getChart().getType());

        assertEquals("Stacked chart should have the option set for stacking",
            chart.getPlotOptions().getSeries().getStacking(), "normal");
    }

    private <T> List<T> getValues(Series first, Function<Point, T> function) {
        return first.getData().stream()
            .map(function)
            .collect(toList());
    }
}
