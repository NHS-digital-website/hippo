package uk.nhs.digital.ps.chart.input;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.apache.jackrabbit.value.BinaryImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.HighmapsParameters;
import uk.nhs.digital.ps.chart.MapSource;
import uk.nhs.digital.ps.chart.model.HighmapsModel;

import java.io.FileInputStream;
import java.util.List;
import javax.jcr.Binary;

public class HighmapsXlsxInputParserTest {

    private Binary binary;
    private String chartTitle;
    private String yAxisTitle;
    private MapSource mapSource;

    private HighmapsXlsxInputParser highmapsXlsxInputParser;

    @Before
    public void setUp() throws Exception {

        binary = new BinaryImpl(new FileInputStream("src/test/resources/MapTestData.xlsx"));
        chartTitle = "a chart title";
        mapSource = MapSource.BRITISH_ISLES_COUNTRIES;

        highmapsXlsxInputParser = new HighmapsXlsxInputParser();
    }

    @Test
    public void parseMap() throws Exception {
        // given
        HighmapsParameters parameters = new HighmapsParameters(ChartType.AREA_MAP.name(), mapSource.name(), chartTitle, binary);

        // when
        HighmapsModel highmapsModel = highmapsXlsxInputParser.parseXlsxChart(parameters);

        // then
        assertEquals(chartTitle, highmapsModel.getTitle().getText());

        assertEquals(mapSource.getHighmapsMapKey(), highmapsModel.getChart().getMap());
        assertEquals("", highmapsModel.getChart().getZoomType());

        assertEquals(0, highmapsModel.getColorAxis().getMin());

        assertEquals("bottom", highmapsModel.getMapNavigation().getButtonOptions().getVerticalAlign());

        assertEquals(highmapsModel.getSeries().get(0).getName(), "Example Data");
        assertThat(highmapsModel.getSeries(), hasSize(1));
        Assert.<List<Object[]>>assertThat(highmapsModel.getSeries().get(0).getData(), Matchers.contains(
            new Object[]{"ie-irl", 10d},
            new Object[]{"gb-eng", 20d},
            new Object[]{"gb-wls", 30d},
            new Object[]{"gb-sct", 40d},
            new Object[]{"gb-imn", 50d},
            new Object[]{"gb-nir", 60d}
        ));
    }

    @Test(expected = RuntimeException.class)
    public void invalidHeadersThrowsException() throws Exception {
        // given
        HighmapsParameters abstractParameters = new HighmapsParameters(ChartType.AREA_MAP.name(), mapSource.name(), chartTitle, binary);
        HighmapsXlsxInputParser spy = Mockito.spy(highmapsXlsxInputParser);
        given(spy.getStringValue(any(Cell.class)))
            .willReturn("");

        // when
        HighmapsModel highmapsModel = highmapsXlsxInputParser.parseXlsxChart(abstractParameters);

        // then exception thrown (expected in test annotation)
        fail();
    }
}
