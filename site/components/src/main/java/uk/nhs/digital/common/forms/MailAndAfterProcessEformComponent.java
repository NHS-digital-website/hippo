package uk.nhs.digital.common.forms;

import com.onehippo.cms7.eforms.hst.behaviors.*;
import com.onehippo.cms7.eforms.hst.components.*;
import com.onehippo.cms7.eforms.hst.model.ErrorMessage;
import com.onehippo.cms7.eforms.hst.model.Form;

import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import javax.servlet.http.HttpServletResponse;

public class MailAndAfterProcessEformComponent extends EformComponent {

    private static Logger log = LoggerFactory.getLogger(MailAndAfterProcessEformComponent.class);

    @Override
    public void onProcessDone(final HstRequest request, final HstResponse response, final Form form,
                              final FormMap map) {
        request.setAttribute("processDone", "true");
    }

    @Override
    public void onProcessFail(HstRequest request, HstResponse response, Form form, FormMap map) {
        request.setAttribute("processFailed", "true");
    }

    @Override
    public boolean onValidationSuccess(HstRequest request, HstResponse response, Form form, FormMap map) {
        // nothing to be done, everything is handled by pluggable behaviors
        return true;
    }

    @Override
    public void onValidationError(HstRequest request, HstResponse response, Form form, FormMap map,
                                  Map<String, ErrorMessage> errors) {
        log.info("Form contains errors");
        // Called on second pass (RENDER_PHASE)
        if ("RENDER_PHASE".equals(request.getLifecyclePhase()) && map.getFormMap().get("eforms_process_done") == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void addConfiguredBehaviors(HstRequest request) {
        super.addConfiguredBehaviors(request);
        addBehavior(new MailFormDataBehavior());
        addBehavior(new AfterProcessBehavior());
        addBehavior(new ReCaptchaValidationPlugin());
    }
}
