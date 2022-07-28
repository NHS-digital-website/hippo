package uk.nhs.digital.ps.chart;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.ps.chart.input.DelegatingHighchartsInputParser;
import uk.nhs.digital.ps.chart.input.ScatterHighchartsXlsxInputParser;
import uk.nhs.digital.ps.chart.input.SeriesHighchartsXlsxInputParser;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;
import uk.nhs.digital.ps.chart.model.HighchartsModel;

public class DelegatingHighchartsInputParserTest {

    private DelegatingHighchartsInputParser delegatingHighchartsInputParser;

    @Mock
    private SeriesHighchartsXlsxInputParser seriesHighchartsXlsxInputParser;
    @Mock
    private ScatterHighchartsXlsxInputParser scatterHighchartsXlsxParser;
    @Mock
    private HighchartsModel expectedSeriesChart;
    @Mock
    private HighchartsParameters chartConfig;

    @Before
    public void setUp() {
        openMocks(this);

        delegatingHighchartsInputParser = new DelegatingHighchartsInputParser(
            seriesHighchartsXlsxInputParser,
            scatterHighchartsXlsxParser
        );
    }

    @Test
    public void delegates_SeriesChartInputParsing_ToDedicatedParser() {

        // given
        given(chartConfig.getChartType()).willReturn(ChartType.LINE);
        given(seriesHighchartsXlsxInputParser.parse(chartConfig)).willReturn(expectedSeriesChart);
        given(seriesHighchartsXlsxInputParser.supports(ChartType.LINE)).willReturn(true);

        // when
        AbstractHighchartsModel actualChart = delegatingHighchartsInputParser.parse(chartConfig);

        // then
        assertEquals("Returns value received from specialised parser", actualChart,
            expectedSeriesChart);
        verifyNoInteractions(expectedSeriesChart);
    }

    @Test
    public void delegates_ScatterChartInputParsing_ToDedicatedParser() {

        // given
        given(chartConfig.getChartType()).willReturn(ChartType.SCATTER_PLOT);
        given(scatterHighchartsXlsxParser.parse(chartConfig)).willReturn(expectedSeriesChart);
        given(scatterHighchartsXlsxParser.supports(ChartType.SCATTER_PLOT)).willReturn(true);

        // when
        AbstractHighchartsModel actualChart = delegatingHighchartsInputParser.parse(chartConfig);

        // then
        assertEquals("Returns value received from specialised parser", actualChart,
            expectedSeriesChart);
        verifyNoInteractions(expectedSeriesChart);
    }
}
