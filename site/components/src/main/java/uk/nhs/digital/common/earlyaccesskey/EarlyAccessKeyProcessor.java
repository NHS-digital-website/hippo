package uk.nhs.digital.common.earlyaccesskey;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.HstResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EarlyAccessKeyProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(EarlyAccessKeyProcessor.class);

    private static final String EARLY_ACCESS_KEY = "website:earlyaccesskey";
    private static final String ERROR_404_PATH = "/error/404";
    private static final String PARAMETER_KEY = "key";

    /**
     * Checks the early access key for a given document and compares it with the key parameter.
     * Returns Http 404 if the keys don't match.
     *
     * @param hippoBean The hippo bean
     * @param hstRequest The http hst request
     * @param hstResponse The http hst response
     * @param request The http request
     */
    public void checkInvalidEarlyAccessKey(HippoBean hippoBean, HstRequest hstRequest, HstResponse hstResponse, HttpServletRequest request) {
        if (StringUtils.isNotBlank(hippoBean.getSingleProperty(EARLY_ACCESS_KEY)) && !hippoBean.getSingleProperty(EARLY_ACCESS_KEY).equals(request.getParameter(PARAMETER_KEY))) {
            LOGGER.debug("Early access key is set and null or wrong key is being used. Redirecting to 404 error code");
            hstResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            HstResponseUtils.sendRedirect(hstRequest, hstResponse, ERROR_404_PATH);
        }
    }

}
