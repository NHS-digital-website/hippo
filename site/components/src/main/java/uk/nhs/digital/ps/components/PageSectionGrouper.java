package uk.nhs.digital.ps.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import uk.nhs.digital.ps.beans.ImageSection;

import java.util.ArrayList;
import java.util.List;

public class PageSectionGrouper {
    public List<Object> groupSections(List<? extends HippoBean> sections) {
        // convert the section into a grouped list so we can
        // show multiple images next to each other in a section
        List<Object> groupedSections = new ArrayList<>();

        for (HippoBean section : sections) {
            if (isHalfImage(section)) {
                // if the last entry was a Image Pair with a space then we are the second one
                ImageSection imageSection = (ImageSection) section;

                Object last = groupedSections.size() > 0 ? groupedSections.get(groupedSections.size() - 1) : null;

                if (isImagePairWithSpace(last)) {
                    ((ImagePairSection) last).setSecond(imageSection);
                } else {
                    groupedSections.add(new ImagePairSection(imageSection));
                }
            } else {
                // just add it, no grouping required
                groupedSections.add(section);
            }
        }

        return groupedSections;
    }

    private boolean isImagePairWithSpace(Object last) {
        if (last instanceof ImagePairSection) {
            return ((ImagePairSection) last).getSecond() == null;
        }
        return false;
    }

    private boolean isHalfImage(HippoBean section) {
        if (section instanceof ImageSection) {
            return ((ImageSection) section).getSize() == ImageSection.Size.HALF;
        }
        return false;
    }
}
