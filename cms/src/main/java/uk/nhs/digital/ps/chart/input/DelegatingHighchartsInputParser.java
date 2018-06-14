package uk.nhs.digital.ps.chart.input;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;

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
    public AbstractHighchartsModel parse(final AbstractHighchartsParameters parameters) {

        final ChartType chartType = parameters.getChartType();

        final SpecialisedHighchartsInputParser parser = getChartParserFor(chartType);

        return parser.parse(parameters);
    }

    private SpecialisedHighchartsInputParser getChartParserFor(final ChartType chartType) {
        return chartParsers.stream()
            .filter(chartParser -> chartParser.supports(chartType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unsupported chart type: " + chartType));
    }
}
