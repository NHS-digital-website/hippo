package uk.nhs.digital.ps;

import org.apache.jackrabbit.value.BinaryValue;
import org.hippoecm.repository.ext.DerivedDataFunction;

import java.util.Map;
import javax.jcr.Value;

public class AttachmentTextEraserDerivedDataFunction extends DerivedDataFunction {

    private static final long serialVersionUID = 1;

    @Override
    public Map<String, Value[]> compute(Map<String, Value[]> parameters) {
        // erase any searchable content from "resource"
        parameters.put("text", new Value[] {new BinaryValue("")});

        return parameters;
    }
}
