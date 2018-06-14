package uk.nhs.digital.ps.chart;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.jackrabbit.value.BinaryImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import uk.nhs.digital.ps.chart.input.ScatterHighchartsXlsxInputParser;
import uk.nhs.digital.ps.chart.model.HighchartsModel;
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.FileInputStream;
import java.util.List;
import java.util.function.Function;
import javax.jcr.Binary;

@RunWith(DataProviderRunner.class)
public class ScatterHighchartsXlsxParserTest {

    private Binary binaryScatter;
    private Binary binaryScatterFunnel;
    private String chartTitle;
    private String xTitleFromSheet;
    private String yTitleFromSheet;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ScatterHighchartsXlsxInputParser scatterHighchartsXlsxParser;

    @Before
    public void setUp() throws Exception {

        binaryScatter = new BinaryImpl(new FileInputStream("src/test/resources/ChartTestScatterData.xlsx"));
        binaryScatterFunnel = new BinaryImpl(new FileInputStream("src/test/resources/ChartTestScatterAndFunnelData.xlsx"));
        chartTitle = "chart title";
        xTitleFromSheet = "888.0";
        yTitleFromSheet = "SHMI value";
        scatterHighchartsXlsxParser = new ScatterHighchartsXlsxInputParser();
    }

    @Test
    @UseDataProvider("supportedChartTypes")
    public void returnsTrueForSupportedChartTypes(ChartType supportedChartType) {

        // when
        boolean actualSupportFlag = scatterHighchartsXlsxParser.supports(supportedChartType);

        // then
        assertThat("Scatter chart type is supported", actualSupportFlag, is(true));
    }

    @Test
    @UseDataProvider("unsupportedChartTypes")
    public void returnsFalseForUnsupportedChartTypes(ChartType unsupportedChartType) {

        // when
        boolean actualSupportFlag = scatterHighchartsXlsxParser.supports(unsupportedChartType);

        // then
        assertThat("Scatter chart type is not supported", actualSupportFlag, is(false));
    }

    @Test
    public void reportsException_whenParseCalledForUnsupportedChartType() {

        // given
        String type = "Line";
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(format(
            "Unsupported chart type: {0}",
            ChartType.toChartType("Line")
        ));

        // when
        scatterHighchartsXlsxParser.parse(
            new HighchartsParameters(type, chartTitle, yTitleFromSheet, binaryScatterFunnel)
        );

        // then
        // expectations set in given
    }

    @Test
    public void reportsException_whenFunnelTypeSelectedButOnlyScatterSheetProvided() {

        // given
        String type = "Funnel_plot";
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Failed to parse chart input.");

        // when
        scatterHighchartsXlsxParser.parse(
            new HighchartsParameters(type, chartTitle, yTitleFromSheet, binaryScatter)
        );

        // then
        // expectations set in given
    }

    @Test
    public void parseScatterChartFile() {

        //given
        final String type = "scatter_plot";

        // when
        HighchartsModel chart = (HighchartsModel) scatterHighchartsXlsxParser.parse(
            new HighchartsParameters(type, chartTitle, null, binaryScatterFunnel)
        );

        // then
        assertEquals("Chart type is set",
            ChartType.SCATTER_PLOT.getHighChartsType(), chart.getChart().getType());
        assertEquals("Chart title is set",
            chartTitle, chart.getTitle().getText());
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
    public void parseFunnelChartFile() {

        // given
        final String type = "funnel_plot";

        // when
        HighchartsModel chart = (HighchartsModel) scatterHighchartsXlsxParser.parse(
            new HighchartsParameters(type, chartTitle, null, binaryScatterFunnel)
        );

        // then
        assertEquals("Chart type is set",
            ChartType.FUNNEL_PLOT.getHighChartsType(), chart.getChart().getType());
        assertEquals("Chart title is set",
            chartTitle, chart.getTitle().getText());
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

    private <T> List<T> getValues(Series<Point> first, Function<Point, T> function) {
        return first.getData().stream()
            .map(function)
            .collect(toList());
    }

    @DataProvider
    public static Object[][] supportedChartTypes() {
        return new Object[][] {
            {ChartType.SCATTER_PLOT},
            {ChartType.FUNNEL_PLOT}
        };
    }

    @DataProvider
    public static Object[][] unsupportedChartTypes() {
        return new Object[][] {
            {ChartType.LINE},
            {ChartType.PIE},
            {ChartType.STACKED_BAR},
            {ChartType.BAR},
            {ChartType.COLUMN},
            {ChartType.STACKED_COLUMN}
        };
    }
}
