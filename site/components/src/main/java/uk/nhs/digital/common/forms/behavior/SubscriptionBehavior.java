package uk.nhs.digital.common.forms.behavior;

import com.onehippo.cms7.eforms.hst.api.OnValidationSuccessBehavior;
import com.onehippo.cms7.eforms.hst.beans.FormBean;
import com.onehippo.cms7.eforms.hst.model.Form;
import org.hippoecm.hst.component.support.forms.FormField;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.ComponentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import uk.nhs.digital.crisp.SubscriptionResource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class SubscriptionBehavior implements OnValidationSuccessBehavior {

    private static final String EMAIL_FIELD_NAME = "email";
    private static final String TOPICS_FIELD_NAME = "topics";
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionBehavior.class);
    private static final String PROP_NAME_GOVDELIVERY_API = "eforms:govdeliveryapi";

    private final SubscriptionResource subscriptionResource = SubscriptionResource.getInstance();

    @Override
    public void onValidationSuccess(HstRequest request, HstResponse response, ComponentConfiguration config, FormBean bean, Form form, FormMap map) {

        if (!isEnabled(bean)) {
            LOGGER.info("Subscription Behavior disabled");
            return;
        }

        final FormField emailField = map.getField(EMAIL_FIELD_NAME);
        final FormField topicsField = map.getField(TOPICS_FIELD_NAME);
        if (emailField == null || !StringUtils.hasText(emailField.getValue())) {
            LOGGER.error("No email address found.");
            return;
        }

        if (topicsField == null) {
            LOGGER.error("No topics found.");
            return;
        }

        final String emailAddress = emailField.getValue();
        final List<String> topics = getListOfValues(topicsField.getValue());
        final String subscriberId = subscriptionResource.subscribe(emailAddress, topics);
        LOGGER.debug("Successfully created subscriber with id {}", subscriberId);
    }

    private List<String> getListOfValues(final String value) {
        if (value == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(value.split(","));
    }

    @Override
    public boolean isEnabled(FormBean bean) {
        Node node = bean.getNode();
        try {
            if (node.hasProperty(PROP_NAME_GOVDELIVERY_API)) {
                return Boolean.parseBoolean(node.getProperty(PROP_NAME_GOVDELIVERY_API).getString());
            }
        } catch (RepositoryException e) {
            LOGGER.warn("Error reading property {}: {}", PROP_NAME_GOVDELIVERY_API, e.getMessage());
        }
        return false;
    }
}
