package uk.nhs.digital.ps.components;

import uk.nhs.digital.ps.beans.PublicationBase;

import java.util.Comparator;

public class DateComparator implements Comparator<PublicationBase> {

    public static final DateComparator COMPARATOR = new DateComparator();

    @Override
    public int compare(PublicationBase publicationBase, PublicationBase t1) {
        if (publicationBase.getNominalPublicationDateCalender() == null || t1.getNominalPublicationDateCalender() == null ) {
            return 0;
        }
        return publicationBase.getNominalPublicationDateCalender().compareTo(t1.getNominalPublicationDateCalender());
    }

}
