package uk.nhs.digital.customfield.validation;

import static com.onehippo.cms7.eforms.hst.validation.rules.LuhnRule.MESSAGE_KEY;

import com.onehippo.cms7.eforms.hst.model.ErrorMessage;
import com.onehippo.cms7.eforms.hst.validation.rules.BaseRule;
import com.onehippo.cms7.eforms.hst.validation.rules.RuleType;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;

import java.util.Map;
import java.util.Optional;

public class PostcodeRule extends BaseRule {
    @Override
    public boolean validate(Map<String, String[]> map) {
        String postcodeUrl = null;
        Optional<String> optionalMapKeySet = map.keySet().stream().findFirst();
        if (optionalMapKeySet.isPresent()) {
            String postcode = map.get(optionalMapKeySet.get())[0];
            if (postcode != null && postcode.length() >= 5) {
                postcodeUrl = postcode.replaceAll("\\s+", "").toUpperCase();
                postcodeUrl = postcodeUrl.substring(0, postcodeUrl.length() - 3) + "/" + postcodeUrl.substring(postcodeUrl.length() - 3);
                ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
                try {
                    broker.findResources("postcodeApi", postcodeUrl + ".json");
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public RuleType getType() {
        return RuleType.CUSTOM_FIELD_VALIDATION_RULE;
    }

    @Override
    public ErrorMessage getMessage(String name, String label, String value) {
        return new ErrorMessage(MESSAGE_KEY, name, label, "Field " + label + " is invalid");
    }
}