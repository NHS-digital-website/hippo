package uk.nhs.digital.crisp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHint;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHintBuilder;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.forms.factory.SubscriberFactory;
import uk.nhs.digital.common.forms.model.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionResource.class);
    private static final XmlMapper XML_MAPPER = new XmlMapper();
    private static final String ACCOUNT = "UKNHSDIG";
    private static final String URL = "/api/account/{account}/subscriptions.xml";

    /*
    * Singleton Pattern with checked locking
    */
    private static SubscriptionResource instance;

    public static synchronized SubscriptionResource getInstance() {
        if (instance == null) {
            instance = new SubscriptionResource();
        }
        return instance;
    }

    private SubscriptionResource() {
    }

    public String subscribe(final String emailAddress, final List<String> topicCodes) {
        final Subscriber subscriber = SubscriberFactory.create(emailAddress, topicCodes);

        try {
            final ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
            final Map<String, Object> pathVars = new HashMap<>();
            pathVars.put("account", ACCOUNT);
            final ExchangeHint exchangeHint = ExchangeHintBuilder.create()
                .methodName("POST")
                .requestHeader("Content-Type", "application/xml")
                .requestBody(XML_MAPPER.writeValueAsString(subscriber))
                .build();
            final Resource resource = broker.resolve("govDeliveryApi", URL, pathVars, exchangeHint);
            return (String) ((Resource) resource.getValueMap().get("to-param")).getDefaultValue();
        } catch (final JsonProcessingException e) {
            LOGGER.error("Encountered exception when serializing XML.", e);
        } catch (final ResourceException e) {
            LOGGER.error("Encountered communication exception when calling the API.", e);
        } catch (final Exception e) {
            LOGGER.error("Encountered unexpected exception when subscribing user.", e);
        }
        return null;
    }
}
