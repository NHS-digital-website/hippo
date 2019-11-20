package uk.nhs.digital.freemarker.roadmap;

import freemarker.template.*;
import uk.nhs.digital.website.beans.EffectiveDate;

import java.util.Comparator;
import java.util.List;

public class RoadmapItemSorterByStartDate implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        return RoadmapItemSorter.sortList(list, Comparator.comparing(EffectiveDate::getStartDate));
    }



}
