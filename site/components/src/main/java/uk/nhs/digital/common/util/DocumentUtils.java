package uk.nhs.digital.common.util;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.SupplementaryInformation;
import uk.nhs.digital.website.beans.Update;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.jcr.Node;

public final class DocumentUtils {

    private static final Logger log = LoggerFactory.getLogger(DocumentUtils.class);

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

    private static boolean isYear(final String candidate) {
        return candidate != null && candidate.matches("20[1-9][0-9]");
    }

    /**
     * Sets the meta tags
     *
     * @param request         HttpRequest
     * @param commonComponent CommonComponent
     */
    public static void setMetaTags(HstRequest request, CommonComponent commonComponent) {

        log.debug("Request Path info " + request.getPathInfo());
        String seoSummary = null;
        String title = null;
        try {
            String path = request.getPathInfo().replaceAll("/", "") + "/content/";
            HippoBean bean = commonComponent.getHippoBeanForPath(path, HippoBean.class);
            Node node = null;
            if (bean != null) {
                node = bean.getNode();
            } else {
                log.info("No Bean found for path " + path);
                return;
            }
            log.debug("Value of node is " + node);
            if (node.hasProperty("website:seosummarytext")) {
                seoSummary = node.getProperty("website:seosummarytext").getValue().getString();
            } else {
                seoSummary = node.getProperty("website:shortsummary").getValue().getString();
            }
            title = node.getProperty("website:title").getValue().getString();
            log.debug("Value of title is " + title);
            log.debug("Value of summary  is " + seoSummary);
            request.setAttribute("title", title);
            request.setAttribute("summary", seoSummary);
        } catch (Exception ex) {
            log.error("Exception reading values ", ex);
        }
    }
}
