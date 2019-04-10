package uk.nhs.digital.common.components;

import uk.nhs.digital.website.beans.BaseCompound;

import java.util.Comparator;

public class CompoundTitleComparator implements Comparator<BaseCompound> {

    public static final CompoundTitleComparator COMPARATOR = new CompoundTitleComparator();

    @Override
    public int compare(BaseCompound c1, BaseCompound c2) {
        return c1.getTitle().toLowerCase().compareTo(c2.getTitle().toLowerCase());
    }

}
