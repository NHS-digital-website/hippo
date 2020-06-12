package uk.nhs.digital.freemarker.utils;

import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.ArrayList;
import java.util.List;

public class InArray implements TemplateMethodModelEx {
    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 2 && !(list.get(0) instanceof SimpleScalar) && !(list.get(1) instanceof SimpleSequence) ) {
            throw new TemplateModelException("Wrong arguments. 1st argument should be a string. 2nd argument should be an array.");
        } else {
            String needle = ((SimpleScalar) list.get(0)).getAsString();
            List<String> haystack = new ArrayList<>();
            for (int i = 0; i < ((SimpleSequence) list.get(1)).size(); i++) {
                haystack.add(((SimpleScalar) ((SimpleSequence) list.get(1)).get(i)).getAsString());
            }
            return check(needle, haystack);
        }
    }

    boolean check(String needle, List<String> haystack) {
        return haystack.contains(needle);
    }
}
