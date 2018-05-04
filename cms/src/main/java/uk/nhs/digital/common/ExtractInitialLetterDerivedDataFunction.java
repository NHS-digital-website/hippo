package uk.nhs.digital.common;

import org.hippoecm.repository.ext.*;

import java.util.*;
import javax.jcr.*;

public class ExtractInitialLetterDerivedDataFunction extends DerivedDataFunction {

    private static final long serialVersionUID = 1;

    @Override
    public Map<String, Value[]> compute(Map<String, Value[]> parameters) {
        if (!parameters.containsKey("title")) {
            parameters.clear();
            return parameters;
        } else {
            Value[] title = parameters.get("title");
            try {
                if (title.length > 0 && !title[0].getString().isEmpty()) {
                    parameters.put("titleInitialLetter",
                        new Value[]{
                            this.getValueFactory().createValue(title[0].getString().substring(0,1).toUpperCase())
                        });
                }
            } catch (ValueFormatException valueFormatException) {
                parameters.clear();
            } catch (RepositoryException var4) {
                parameters.clear();
            }
            return parameters;
        }
    }
}
