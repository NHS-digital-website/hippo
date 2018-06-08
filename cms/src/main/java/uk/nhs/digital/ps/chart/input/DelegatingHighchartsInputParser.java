package uk.nhs.digital.ps.chart.input;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import uk.nhs.digital.ps.ChartConfig;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.SeriesChart;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

public class DelegatingHighchartsInputParser implements HighchartsInputParser {

    private Set<SpecialisedHighchartsInputParser> chartParsers;

    public DelegatingHighchartsInputParser(
        final AbstractHighchartsXlsxInputParser... chartParsers) {
        this.chartParsers = Stream.of(chartParsers).collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    @Override
    public SeriesChart parse(final ChartConfig chartConfig) {

        final ChartType chartType = ChartType.toChartType(chartConfig.getType());

        final SpecialisedHighchartsInputParser parser = getChartParserFor(chartType);

        final SeriesChart seriesChart = parser.parse(chartConfig);

        return seriesChart;
    }

    private SpecialisedHighchartsInputParser getChartParserFor(final ChartType chartType) {
        return chartParsers.stream()
            .filter(chartParser -> chartParser.supports(chartType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported chart type: " + chartType));
    }
}
