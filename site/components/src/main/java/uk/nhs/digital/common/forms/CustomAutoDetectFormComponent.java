package uk.nhs.digital.common.forms;

import com.onehippo.cms7.eforms.hst.components.AutoDetectFormComponent;
import com.onehippo.cms7.eforms.hst.components.info.AutoDetectFormComponentInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import uk.nhs.digital.common.forms.behavior.SubscriptionBehavior;

@SuppressWarnings("rawtypes")
@ParametersInfo(type = AutoDetectFormComponentInfo.class)
public class CustomAutoDetectFormComponent extends AutoDetectFormComponent {

    private static final Class[] ADDITIONAL_AUTO_DETECT_BEHAVIORS = new Class[]{
        SubscriptionBehavior.class,
        ReCaptchaValidationPlugin.class
    };

    @Override
    protected Class[] getAutoDetectBehaviors() {
        return ArrayUtils.addAll(super.getAutoDetectBehaviors(), ADDITIONAL_AUTO_DETECT_BEHAVIORS);
    }
}
