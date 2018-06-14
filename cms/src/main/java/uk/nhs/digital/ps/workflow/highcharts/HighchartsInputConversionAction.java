package uk.nhs.digital.ps.workflow.highcharts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.scxml2.SCXMLExpressionException;
import org.apache.commons.scxml2.model.ModelException;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.documentworkflow.action.AbstractDocumentTaskAction;
import uk.nhs.digital.common.util.json.JacksonJsonSerialiser;
import uk.nhs.digital.ps.chart.input.HighchartsInputParser;
import uk.nhs.digital.ps.chart.input.HighchartsJcrNodeReader;

public class HighchartsInputConversionAction
    extends AbstractDocumentTaskAction<HighchartsInputConversionTask> {

    private static final String VARIANT_EXPR = "variantExpr";

    @Override
    protected HighchartsInputConversionTask createWorkflowTask() {

        return new HighchartsInputConversionTask(
            newJsonSerialiser(),
            HippoServiceRegistry.getService(HighchartsInputParser.class),
            HippoServiceRegistry.getService(HighchartsJcrNodeReader.class));
    }

    @Override
    protected void initTask(final HighchartsInputConversionTask task)
        throws ModelException, SCXMLExpressionException {
        super.initTask(task);

        task.setVariant(eval(getVariant()));
    }

    public String getVariant() {
        return getParameter(VARIANT_EXPR);
    }

    public void setVariant(final String variant) {
        setParameter(VARIANT_EXPR, variant);
    }

    private JacksonJsonSerialiser newJsonSerialiser() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return new JacksonJsonSerialiser(objectMapper);
    }

}
