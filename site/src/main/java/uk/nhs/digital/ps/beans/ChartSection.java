package uk.nhs.digital.ps.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.chart.ChartFactory;
import uk.nhs.digital.ps.chart.ChartType;
import uk.nhs.digital.ps.chart.SeriesChart;

@HippoEssentialsGenerated(internalName = "publicationsystem:chartSection")
@Node(jcrType = "publicationsystem:chartSection")
public class ChartSection extends HippoCompound {
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    @HippoEssentialsGenerated(internalName = "publicationsystem:dataFile")
    public HippoResource getDataFile() {
        return getChildBeansByName("publicationsystem:dataFile", HippoResource.class).get(0);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:type")
    public String getType() {
        return getProperty("publicationsystem:type");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:yTitle")
    public String getYTitle() {
        return getProperty("publicationsystem:yTitle");
    }

    /**
     * This is needed for the HTML element that contains the chart to have a unique ID
     */
    public String getUniqueId() {
        return String.valueOf(getPath().hashCode());
    }

    public String getSectionType() {
        return "chart";
    }

    public String getChart() {
        SeriesChart chart = new ChartFactory(this).build();

        try {
            return OBJECT_MAPPER.writeValueAsString(chart);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
