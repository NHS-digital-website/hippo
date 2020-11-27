package uk.nhs.digital.common.util;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import uk.nhs.digital.website.beans.SupplementaryInformation;
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

    public static List<SupplementaryInformation> getSortedSupplementaryInformation(
        List<SupplementaryInformation> supplementaryInfo) {
        supplementaryInfo.sort(Comparator.comparing(SupplementaryInformation::getPublishedDate, Comparator.nullsLast(Comparator.reverseOrder())));
        return supplementaryInfo;
    }

    public static HippoBeanIterator documentsQuery(final Class clazz) throws QueryException {
        return HstQueryBuilder
            .create(RequestContextProvider.get().getSiteContentBaseBean())
            .ofTypes(clazz)
            .build()
            .execute()
            .getHippoBeans();
    }

    public static String findYearOrDefault(final String target, int fallback) {
        return isYear(target) ? target : String.valueOf(fallback);
    }

    public static boolean isYear(final String candidate) {
        return candidate != null && candidate.matches("20[1-9][0-9]");
    }
}
