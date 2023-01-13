package uk.nhs.digital.freemarker.roadmap;

import freemarker.template.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryListIntersectionHelper implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 2 && !(list.get(0) instanceof DefaultListAdapter) && !(list.get(1) instanceof DefaultListAdapter) ) {
            throw new TemplateModelException("Wrong arguments. 1st argument should be a List<RoadmapItem>. 2nd argument should be a List<String>.");
        } else {
            String a = (String) list.get(0);

            List<String> b = new ArrayList<>();
            for (int i = 0; i < ((DefaultListAdapter) list.get(1)).size(); i++) {
                b.add(((SimpleScalar) ((DefaultListAdapter) list.get(1)).get(i)).getAsString().toLowerCase());
            }

            return b.contains(a);
        }
    }

}
