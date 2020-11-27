package uk.nhs.digital.common.forms.behavior;

import com.onehippo.cms7.eforms.hst.api.DoBeforeRenderBehavior;
import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.behaviors.FormIntroBehavior;
import com.onehippo.cms7.eforms.hst.behaviors.MailFormDataBehavior;
import com.onehippo.cms7.eforms.hst.model.Form;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormApiSettingsBehavior extends FormIntroBehavior implements DoBeforeRenderBehavior {


    private static Logger LOGGER = LoggerFactory.getLogger(MailFormDataBehavior.class);

    public void doBeforeRender(HstRequest request, HstResponse response, final ComponentConfiguration config,
                               FormBean formBean, Form form, FormMap map) {
        super.doBeforeRender(request, response, config, formBean, form, map);

        if (!isEnabled(formBean)) {
            LOGGER.info("Form Api Settings Behavior disabled");
            return;
        }
        String topicId = formBean.getSingleProperty("eforms:topicId", null);
        Boolean apiEnabled = Boolean.valueOf(formBean.getSingleProperty("eforms:govdeliveryapi", "false"));
        Boolean apiScriptServiceEnabled = Boolean.valueOf(formBean.getSingleProperty("eforms:govdeliveryScriptService", "false"));
        request.setAttribute("topicId", topicId);
        request.setAttribute("apiEnabled", apiEnabled);
        request.setAttribute("apiScriptServiceEnabled", apiScriptServiceEnabled);
    }

    @Override
    public boolean isEnabled(FormBean formBean) {
        return Boolean.parseBoolean(formBean.getSingleProperty("eforms:govdeliveryapi", "false"))
            || Boolean.parseBoolean(formBean.getSingleProperty("eforms:govdeliveryScriptService", "false"));
    }
}
