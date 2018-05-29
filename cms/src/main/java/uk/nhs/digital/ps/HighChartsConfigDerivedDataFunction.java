package uk.nhs.digital.ps;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.hippoecm.repository.ext.DerivedDataFunction;
import uk.nhs.digital.ps.chart.ChartParser;
import uk.nhs.digital.ps.chart.SeriesChart;

import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

public class HighChartsConfigDerivedDataFunction extends DerivedDataFunction {

    private static final long serialVersionUID = 1;

    private final ChartParser parser;
    private final ObjectMapper objectMapper;

    public HighChartsConfigDerivedDataFunction() {
        this(ChartParser.getInstance(), createObjectMapper());
    }

    HighChartsConfigDerivedDataFunction(ChartParser parser, ObjectMapper objectMapper) {
        this.parser = parser;
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Value[]> compute(Map<String, Value[]> parameters) {
        try {
            // required fields
            Value dataValue = getValue(parameters, "data");
            Value typeValue = getValue(parameters, "type");
            Value titleValue = getValue(parameters, "title");

            if (!allNotNull(dataValue, typeValue, titleValue)) {
                return emptyMap();
            }

            // optional field
            Value yTitleValue = getValue(parameters, "yTitle");
            String yTitle = yTitleValue == null ? null : yTitleValue.getString();

            SeriesChart chart = parser.parse(typeValue.getString(), titleValue.getString(), yTitle, dataValue.getBinary());

            String chartConfig = objectMapper.writeValueAsString(chart);

            parameters.put("chartConfig", new Value[]{getValueFactory().createValue(chartConfig)});
        } catch (JsonProcessingException | RepositoryException ex) {
            throw new RuntimeException("Failed to create chart config derived data", ex);
        }

        return parameters;
    }

    private Value getValue(Map<String, Value[]> parameters, String parameterName) {
        Value[] values = parameters.get(parameterName);
        return ArrayUtils.isEmpty(values) ? null : values[0];
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
