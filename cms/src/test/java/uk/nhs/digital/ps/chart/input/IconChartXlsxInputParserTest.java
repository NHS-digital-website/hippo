package uk.nhs.digital.ps.chart.input;

import static com.google.common.collect.Streams.stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.apache.jackrabbit.value.BinaryImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.nhs.digital.ps.chart.enums.ChartType;
import uk.nhs.digital.ps.chart.enums.IconType;
import uk.nhs.digital.ps.chart.enums.IconXlsxAllowList;
import uk.nhs.digital.ps.chart.enums.VisualisationColourOption;
import uk.nhs.digital.ps.chart.model.IconVisualisationModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;
import uk.nhs.digital.ps.chart.parameters.VisualisationParameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public class IconChartXlsxInputParserTest {


    private static final String EMPTY_STRING = "";
    private Binary binary;
    private String chartTitle;
    private String colour;
    private XSSFSheet testSheet;
    private String iconType;
    private List<String> allowList;

    private IconChartXlsxInputParser iconChartXlsxInputParser;

    @Before
    public void setUp() throws IOException {
        binary = new BinaryImpl(Files.newInputStream(Paths.get("src/test/resources/ChartTestIconChart.xlsx")));
        chartTitle = "a chart title";
        colour = VisualisationColourOption.LIGHT.getVisualisationColour();
        iconType = IconType.SQUARE.getIconType();
        allowList = IconXlsxAllowList.getAllowedValuesList();

        iconChartXlsxInputParser = new IconChartXlsxInputParser();

    }

    /**
     * Method under test: {@link IconChartXlsxInputParser#parseXlsxChart(AbstractVisualisationParameters)}
     */
    @Test
    public void testParseXlsxChart() throws IOException, RepositoryException {
        // given
        VisualisationParameters parameters = new VisualisationParameters(ChartType.ICON.name(), chartTitle, colour, binary, iconType);
        testSheet = iconChartXlsxInputParser.readXssfWorkbook(parameters.getInputFileContent()).getSheetAt(0);
        Iterable<Row> iterable = () -> testSheet.rowIterator();
        List<String> rowList = stream(iterable)
            .filter(Objects::nonNull)
            .filter(row -> !Objects.equals(getFormatCellValue(row), EMPTY_STRING))
            .map(this::getFormatCellValue)
            .collect(Collectors.toList());

        // when
        IconVisualisationModel iconVisualisationModel = iconChartXlsxInputParser.parseXlsxChart(parameters);

        // then
        assertEquals(rowList.size(), iconVisualisationModel.getXlxsValues().size());
        assertEquals(iconType, iconVisualisationModel.getIcon().getType());
        assertEquals(colour, iconVisualisationModel.getColour().getColourOption());
        assertEquals(chartTitle, iconVisualisationModel.getTitle().getText());
        // all values parsed are allowed values
        for (String rowString : iconVisualisationModel.getXlxsValues().keySet()) {
            assertTrue(allowList.contains(rowString.toUpperCase()));
        }
    }

    private String getFormatCellValue(Row currentRow) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(currentRow.getCell(0));
    }

    @Test(expected = RuntimeException.class)
    public void invalidValueThrowsException() throws Exception {
        // given
        VisualisationParameters parameters = new VisualisationParameters(ChartType.ICON.name(), chartTitle, colour, binary, iconType);
        IconChartXlsxInputParser spy = Mockito.spy(iconChartXlsxInputParser);
        given(spy.getStringValue(any(Cell.class)))
            .willReturn("incorrect");

        // when
        IconVisualisationModel visualisationModel = iconChartXlsxInputParser.parseXlsxChart(parameters);

        // then exception thrown (expected in test annotation)
        fail();
    }

    @Test(expected = RuntimeException.class)
    public void emptySheetThrowsException() throws Exception {
        // given
        VisualisationParameters parameters = new VisualisationParameters(ChartType.ICON.name(), chartTitle, colour, binary, iconType);
        IconChartXlsxInputParser spy = Mockito.spy(iconChartXlsxInputParser);
        given(spy.readXssfWorkbook(any(Binary.class)))
            .willReturn(null);

        // when
        IconVisualisationModel visualisationModel = iconChartXlsxInputParser.parseXlsxChart(parameters);

        // then exception thrown (expected in test annotation)
        fail();
    }


}

