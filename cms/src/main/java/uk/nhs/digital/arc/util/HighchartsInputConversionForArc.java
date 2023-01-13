package uk.nhs.digital.arc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.nhs.digital.common.util.json.JacksonJsonSerialiser;
import uk.nhs.digital.common.util.json.JsonSerialiser;
import uk.nhs.digital.ps.chart.input.DelegatingHighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighmapsXlsxInputParser;
import uk.nhs.digital.ps.chart.input.ScatterHighchartsXlsxInputParser;
import uk.nhs.digital.ps.chart.input.SeriesHighchartsXlsxInputParser;
import uk.nhs.digital.ps.chart.model.AbstractVisualisationModel;
import uk.nhs.digital.ps.chart.parameters.AbstractVisualisationParameters;
import uk.nhs.digital.ps.chart.parameters.HighchartsParameters;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;

public class HighchartsInputConversionForArc {

    public String process(String type, String title, String yTitle, Binary fileData) throws RepositoryException {
        DelegatingHighchartsInputParser parser = new DelegatingHighchartsInputParser(new HighmapsXlsxInputParser(),
            new SeriesHighchartsXlsxInputParser(),
            new ScatterHighchartsXlsxInputParser());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JsonSerialiser jsonSerialiser = new JacksonJsonSerialiser(objectMapper);

        final AbstractVisualisationParameters parameters = new HighchartsParameters(type, title, yTitle, fileData);
        final AbstractVisualisationModel chart = parser.parse(parameters);
        return jsonSerialiser.toJson(chart);
    }
}
