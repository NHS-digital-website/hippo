package uk.nhs.digital.common.util;

import uk.nhs.digital.website.beans.Update;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class DocumentUtils {

    private DocumentUtils() {
    }

    public static List<Update> getFilteredAndSortedUpdates(List<Update> updates) {
        return updates.stream()
            .filter(
                update -> update.getExpirydate() == null || !update.getExpirydate()
                    .before(Calendar.getInstance()))
            .sorted(
                Comparator.<Update, Integer>comparing(
                    update -> Update.Severity.getSortOrder(update.getSeverity()))
                    .thenComparing(Update::getExpirydate, Comparator.nullsLast(Comparator.naturalOrder())))
            .collect(Collectors.toList());
    }
}
