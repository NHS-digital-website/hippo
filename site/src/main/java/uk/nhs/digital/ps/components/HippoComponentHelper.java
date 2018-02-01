package uk.nhs.digital.ps.components;

import static java.text.MessageFormat.format;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HippoComponentHelper {

    private static Logger log = LoggerFactory.getLogger(HippoComponentHelper.class);

    public static void reportInvalidTarget(final HstRequest request, final HippoBean contentBean, final int indexDocumentsCount) {
        log.error("Expected exactly one published publication series index documents but found {} in folder {}.",
            indexDocumentsCount, contentBean.getPath());

        reportDisplayError(request, contentBean.getDisplayName());
    }

    public static void reportInvalidInvocation(final HstRequest request, final HippoBean contentBean) {
        log.error("{} invoked for a node ''{}'' that is not a folder. This is likely a result of the site map"
                + " being misconfigured. Node path: {}",
            contentBean.getClass().getSimpleName(), contentBean.getDisplayName(), contentBean.getPath());

        reportDisplayError(request, contentBean.getDisplayName());
    }

    public static void reportDisplayError(final HstRequest request, final String name) {
        request.setAttribute("error", format("Cannot display ''{0}''. The error has been logged.", name));
    }
}
