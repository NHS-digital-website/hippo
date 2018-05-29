package uk.nhs.digital.ps;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.jackrabbit.value.BinaryValue;
import org.apache.jackrabbit.value.StringValue;
import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.ps.chart.ChartParser;
import uk.nhs.digital.ps.chart.SeriesChart;

import java.util.HashMap;
import java.util.Map;
import javax.jcr.Binary;
import javax.jcr.Value;

@RunWith(DataProviderRunner.class)
public class HighChartsConfigDerivedDataFunctionTest {

    private static final String CHART_CONFIG_KEY = "chartConfig";

    private static final String RESULT_STRING = "result_string";

    @Mock private ChartParser parser;
    @Mock private SeriesChart chart;
    @Mock private ObjectMapper objectMapper;

    private HighChartsConfigDerivedDataFunction function;

    @Before
    public void setup() throws JsonProcessingException {
        initMocks(this);

        given(parser.parse(any(), any(), any(), any()))
            .willReturn(chart);

        given(objectMapper.writeValueAsString(chart))
            .willReturn(RESULT_STRING);

        function = new HighChartsConfigDerivedDataFunction(parser, objectMapper);
        function.setValueFactory(ValueFactoryImpl.getInstance());
    }

    @Test
    @UseDataProvider("missingMandatoryParameters")
    public void missingMandatoryParametersGivesNullResult(String type, String title, Binary binary, String desc) throws Exception {
        // given
        Map<String, Value[]> inputMap = generateInputMap(type, title, null, binary);

        // when
        Map<String, Value[]> result = function.compute(inputMap);

        // then
        assertNull("Not computed result for " + desc, result.get("publicationsystem:chartConfig"));
    }

    @Test
    public void computeWithValidParametersGivesResult() throws Exception {
        String type = "some type";
        String title = "some title";
        String yTitle = "some y title";
        Binary binary = mock(Binary.class);

        // given
        Map<String, Value[]> map = generateInputMap(type, title, yTitle, binary);

        // when
        Map<String, Value[]> result = function.compute(map);

        // then - Should parse with correct variables
        then(parser).should().parse(type, title, yTitle, binary);

        assertThat("chart config is returned in map", result, hasEntry(CHART_CONFIG_KEY, new Value[]{new StringValue(RESULT_STRING)}));
    }

    @Test
    public void computeWithoutOptionalParametersGivesResult() throws Exception {
        String type = "some type";
        String title = "some title";
        Binary binary = mock(Binary.class);

        // given - no y title
        Map<String, Value[]> map = generateInputMap(type, title, null, binary);

        // when
        Map<String, Value[]> result = function.compute(map);

        // then - Should parse with correct variables
        then(parser).should().parse(type, title, null, binary);

        assertThat("chart config is returned in map", result, hasEntry(CHART_CONFIG_KEY, new Value[]{new StringValue(RESULT_STRING)}));
    }

    private Map<String, Value[]> generateInputMap(String type, String title, String yTitle, Binary binary) {
        Map<String, Value[]> map = new HashMap<>();

        if (type != null) {
            map.put("type", new Value[]{new StringValue(type)});
        }

        if (title != null) {
            map.put("title", new Value[]{new StringValue(title)});
        }

        if (yTitle != null) {
            map.put("yTitle", new Value[]{new StringValue(yTitle)});
        }

        if (binary != null) {
            map.put("data", new Value[]{new BinaryValue(binary)});
        }
        return map;
    }

    @DataProvider
    public static Object[][] missingMandatoryParameters() {
        Binary binary = mock(Binary.class);

        return new Object[][]{
            // type - title - binary - description
            new Object[]{null, "chart title", binary, "missing type"},
            new Object[]{"a type", null, binary, "missing title"},
            new Object[]{"a type", "chart title", null, "missing binary"},
            new Object[]{null, null, null, "missing all"}
        };
    }
}
