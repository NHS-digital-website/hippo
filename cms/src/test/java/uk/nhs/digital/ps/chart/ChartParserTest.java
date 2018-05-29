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
import uk.nhs.digital.ps.chart.model.Point;
import uk.nhs.digital.ps.chart.model.Series;

import java.io.FileInputStream;
import java.util.List;
import java.util.function.Function;
import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public class ChartParserTest {

    private Binary binary;

    private ChartParser chartParser = ChartParser.getInstance();

    @Before
    public void setUp() throws Exception {
        binary = new BinaryImpl(new FileInputStream("src/test/resources/ChartTestData.xlsx"));
    }

    @Test
    public void parseChartFile() {
        String type = "Line";
        String title = "a chart title";
        String yTitle = "a y axis title";

        // when
        SeriesChart chart = chartParser.parse(type, title, yTitle, binary);

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
        SeriesChart chart = chartParser.parse("Pie", "chart title", "y axis title", binary);

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
    public void parseAsStackedChartSetsCorrectOptions() throws RepositoryException {
        // when
        SeriesChart chart = chartParser.parse("Stacked Bar", "chart title", "y axis title", binary);

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
