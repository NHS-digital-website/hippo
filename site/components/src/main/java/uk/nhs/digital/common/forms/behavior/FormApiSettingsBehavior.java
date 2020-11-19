package uk.nhs.digital.common.forms.behavior;

import com.onehippo.cms7.eforms.hst.api.DoBeforeRenderBehavior;
import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.model.Form;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;

public class FormApiSettingsBehavior implements DoBeforeRenderBehavior {


    public void doBeforeRender(HstRequest request, HstResponse response, final ComponentConfiguration config,
                               FormBean formBean, Form form, FormMap map) {
        String formId = formBean.getSingleProperty("eforms:formId", null);
        Boolean apiEnabled = Boolean.valueOf(formBean.getSingleProperty("eforms:govdeliveryapi", "false"));
        request.setAttribute("formId", formId);
        request.setAttribute("apiEnabled", apiEnabled);
    }

    @Override
    public boolean isEnabled(FormBean formBean) {
        String intro = formBean.getSingleProperty("eforms:formId", null);
        return StringUtils.isNotBlank(intro);
    }
}
