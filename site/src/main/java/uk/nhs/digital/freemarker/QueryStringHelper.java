package uk.nhs.digital.freemarker;

import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.ArrayList;
import java.util.List;

public class QueryStringHelper implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 2 && !(list.get(0) instanceof SimpleSequence) && !(list.get(1) instanceof SimpleSequence) ) {
            throw new TemplateModelException("Wrong arguments. 1st argument should be an array. 2nd argument should be an array.");
        } else {

            List<String> queryStrings = new ArrayList<>();
            for (int i = 0; i < ((SimpleSequence) list.get(0)).size(); i++) {
                queryStrings.add(((SimpleScalar) ((SimpleSequence) list.get(0)).get(i)).getAsString());
            }

            for (int i = 0; i < ((SimpleSequence) list.get(1)).size(); i++) {
                queryStrings.remove(((SimpleScalar) ((SimpleSequence) list.get(1)).get(i)).getAsString());
            }

            return queryStrings;
        }
    }
}
