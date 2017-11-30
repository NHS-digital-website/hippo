package uk.nhs.digital.ps;

import org.hippoecm.repository.ext.DerivedDataFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Value;
import java.util.Map;

public class AttachmentTextEraserDerivedDataFunction extends DerivedDataFunction {

    private static final long serialVersionUID = 1;

    private static Logger log = LoggerFactory.getLogger(AttachmentTextEraserDerivedDataFunction.class);

    @Override
    public Map<String, Value[]> compute(Map<String, Value[]> parameters) {
        // erase any searchable content from "resource"
        parameters.put("text", new Value[] { getValueFactory().createValue("") });

        return parameters;
    }
}
