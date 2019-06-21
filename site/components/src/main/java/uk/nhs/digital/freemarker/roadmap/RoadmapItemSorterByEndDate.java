package uk.nhs.digital.freemarker.roadmap;

import static java.util.Comparator.comparing;

import freemarker.template.DefaultListAdapter;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import uk.nhs.digital.website.beans.EffectiveDate;
import uk.nhs.digital.website.beans.RoadmapItem;

import java.util.List;
import java.util.stream.Collectors;

public class RoadmapItemSorterByEndDate implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        if (list.size() != 1 && !(list.get(0) instanceof DefaultListAdapter) ) {
            throw new TemplateModelException("Wrong argument. The argument should be a list.");
        } else {
            List<RoadmapItem> items = (List<RoadmapItem>) ((DefaultListAdapter) list.get(0)).getWrappedObject();
            return items.stream()
                .sorted(comparing(RoadmapItem::getEffectiveDate, comparing(EffectiveDate::getEndDate)))
                .collect(Collectors.toList());
        }
    }

}
