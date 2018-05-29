package uk.nhs.digital.ps.chart;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.input.DelegatingHighchartsInputParser;
import uk.nhs.digital.ps.chart.input.ScatterHighchartsXlsxParser;
import uk.nhs.digital.ps.chart.input.SeriesHighchartsXlsxInputParser;

public class DelegatingHighchartsInputParserTest {

    private DelegatingHighchartsInputParser delegatingHighchartsInputParser;

    @Mock private SeriesHighchartsXlsxInputParser seriesHighchartsXlsxInputParser;
    @Mock private ScatterHighchartsXlsxParser scatterHighchartsXlsxParser;
    @Mock private SeriesChart expectedSeriesChart;
    @Mock private ChartConfig chartConfig;

    @Before
    public void setUp() {
        initMocks(this);

        delegatingHighchartsInputParser = new DelegatingHighchartsInputParser(
            seriesHighchartsXlsxInputParser,
            scatterHighchartsXlsxParser
        );
    }

    @Test
    public void delegates_SeriesChartInputParsing_ToDedicatedParser() {

        // given
        given(chartConfig.getType()).willReturn(ChartType.LINE.name());
        given(seriesHighchartsXlsxInputParser.parse(chartConfig)).willReturn(expectedSeriesChart);
        given(seriesHighchartsXlsxInputParser.supports(ChartType.LINE)).willReturn(true);

        // when
        SeriesChart actualChart = delegatingHighchartsInputParser.parse(chartConfig);

        // then
        assertThat("Returns value received from specialised parser", actualChart,
            sameInstance(expectedSeriesChart));
        verifyZeroInteractions(expectedSeriesChart);
    }

    @Test
    public void delegates_ScatterChartInputParsing_ToDedicatedParser() {

        // given
        given(chartConfig.getType()).willReturn(ChartType.SCATTER_PLOT.name());
        given(scatterHighchartsXlsxParser.parse(chartConfig)).willReturn(expectedSeriesChart);
        given(scatterHighchartsXlsxParser.supports(ChartType.SCATTER_PLOT)).willReturn(true);

        // when
        SeriesChart actualChart = delegatingHighchartsInputParser.parse(chartConfig);

        // then
        assertThat("Returns value received from specialised parser", actualChart,
            sameInstance(expectedSeriesChart));
        verifyZeroInteractions(expectedSeriesChart);
    }
}
