package uk.nhs.digital.ps.chart.input;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import uk.nhs.digital.ps.chart.AbstractHighchartsParameters;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.model.AbstractHighchartsModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;
import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public abstract class AbstractHighchartsXlsxInputParser implements SpecialisedHighchartsInputParser {

    private final Set<ChartType> supportedChartTypes;

    AbstractHighchartsXlsxInputParser(final ChartType... supportedChartTypes) {
        this.supportedChartTypes = Stream.of(supportedChartTypes)
            .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    @Override
    public boolean supports(final ChartType chartType) {
        return supportedChartTypes.contains(chartType);
    }

    @Override
    public AbstractHighchartsModel parse(final AbstractHighchartsParameters config) {

        final ChartType chartType = config.getChartType();

        validateSupportFor(chartType);

        try {
            return parseXlsxChart(config);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to parse chart input.", ex);
        }
    }

    protected abstract AbstractHighchartsModel parseXlsxChart(AbstractHighchartsParameters chartConfig)
        throws IOException, RepositoryException;

    Double getDoubleValue(Cell cell) {
        return cell.getCellTypeEnum() == CellType.STRING ? Double.valueOf(cell.getStringCellValue()) : cell.getNumericCellValue();
    }

    String getStringValue(Cell cell) {
        return cell.getCellTypeEnum() == CellType.STRING ? cell.getStringCellValue() : String.valueOf(cell.getNumericCellValue());
    }

    private void validateSupportFor(final ChartType chartType) {
        if (!supports(chartType)) {
            // should've tested whether this parser supports this chart type by calling 'supports(ChartType')
            // method before calling this one
            throw new IllegalArgumentException("Unsupported chart type: " + chartType);
        }
    }

    protected XSSFWorkbook readXssfWorkbook(final Binary inputFileContent)
        throws IOException, RepositoryException {

        try (final InputStream is = inputFileContent.getStream()) {
            return new XSSFWorkbook(is);
        }
    }
}
