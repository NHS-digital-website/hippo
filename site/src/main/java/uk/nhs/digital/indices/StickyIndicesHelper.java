package uk.nhs.digital.indices;

import org.hippoecm.hst.content.beans.standard.HippoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StickyIndicesHelper {

    public static List<StickyIndex> indexSections(List<HippoBean> stickySections) {
        return stickySections.stream()
            .filter(s -> s instanceof StickySection)
            .filter(s -> ((StickySection) s).isMainHeading())
            .map(s -> new StickyIndex(((StickySection) s).getHeading()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

}
