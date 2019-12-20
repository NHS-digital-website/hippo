package uk.nhs.digital.freemarker.roadmap;

import static java.util.Comparator.comparing;

import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;
import uk.nhs.digital.website.beans.EffectiveDate;
import uk.nhs.digital.website.beans.RoadmapItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface RoadmapItemSorter {

    static Object sortList(List list, Comparator<EffectiveDate> comparing) throws TemplateModelException {
        if (list.size() != 1 && !(list.get(0) instanceof SimpleSequence)) {
            throw new TemplateModelException("Wrong argument. The argument should be a list.");
        } else {
            List<RoadmapItem> items = new ArrayList<>();
            if (((SimpleSequence) list.get(0)).size() > 0) {
                for (int i = 0; i < ((SimpleSequence) list.get(0)).size(); i++) {
                    items.add((RoadmapItem) DeepUnwrap.unwrap(((SimpleSequence) list.get(0)).get(i)));
                }
                return items.stream()
                    .sorted(comparing(RoadmapItem::getEffectiveDate, comparing))
                    .collect(Collectors.toList());
            }
            return items;
        }
    }
}
