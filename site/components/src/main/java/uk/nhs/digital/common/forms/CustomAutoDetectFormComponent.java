package uk.nhs.digital.common.forms;

import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.components.AutoDetectFormComponent;
import com.onehippo.cms7.eforms.hst.components.info.AutoDetectFormComponentInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.forms.behavior.SubscriptionBehavior;
import uk.nhs.digital.website.beans.Signup;

@SuppressWarnings("rawtypes")
@ParametersInfo(type = AutoDetectFormComponentInfo.class)
public class CustomAutoDetectFormComponent extends AutoDetectFormComponent {

    private static Logger log = LoggerFactory.getLogger(CustomAutoDetectFormComponent.class);


    private static final Class[] ADDITIONAL_AUTO_DETECT_BEHAVIORS = new Class[]{
        SubscriptionBehavior.class,
        //ReCaptchaValidationPlugin.class
    };

    @Override
    protected Class[] getAutoDetectBehaviors() {
        return ArrayUtils.addAll(super.getAutoDetectBehaviors(), ADDITIONAL_AUTO_DETECT_BEHAVIORS);
    }

    @Override
    public FormBean getFormBean(final HstRequest request) {
        FormBean formBean = getFormBeanFromSignupDocument(request);
        if (formBean == null) {
            formBean = getFormBeanFromPicker(request);
        }
        return formBean;
    }

    private FormBean getFormBeanFromPicker(final HstRequest request) {

        final AutoDetectFormComponentInfo paramsInfo = getComponentParametersInfo(request);
        final String formDocPath = paramsInfo.getForm();

        FormBean formBean = getFormBeanFromPath(request, formDocPath);

        if (formBean == null) {
            formBean = super.getFormBean(request);
        }

        return formBean;
    }

    @Nullable
    private FormBean getFormBeanFromSignupDocument(final HstRequest request) {
        final HippoBean document = request.getRequestContext().getContentBean();

        if (document instanceof Signup) {
            final Signup signupDocument = (Signup) document;
            return (FormBean) signupDocument.getFormLink().getLink();
        }

        if (document == null || !document.isHippoDocumentBean() || !(document instanceof FormBean)) {
            if (log.isDebugEnabled()) {
                log.warn("*** EFORMS ***");
                log.warn("Cannot get the form bean, returning null. Reason: the content bean is null or it does not match the FormBean type [eforms:form].");
                log.warn("Override the method [BaseEformComponent#getFormBean(HstRequest)] to get the form bean in a different way, e.g. from a linked bean.");
                log.warn("Will attempt to get the form bean from the component picker.");
                log.warn("*** EFORMS ***");
            }
            return null;
        }
        return (FormBean) document;
    }

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        Object processDone = request.getAttribute("processDone");
        if (processDone != null && "true".equalsIgnoreCase(processDone.toString())) {
            request.setAttribute("processComplete", "true");
            request.setAttribute("demoCheck", "processDone");
        } else {
            request.setAttribute("demoCheck", "processNotDone");
        }
        request.setAttribute("demo", processDone);

    }
}
