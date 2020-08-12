package uk.nhs.digital.crisp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHint;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.crisp.api.resource.ResourceResolver;
import org.onehippo.cms7.crisp.mock.broker.MockResourceServiceBroker;
import org.onehippo.cms7.crisp.mock.module.MockCrispHstServices;
import org.onehippo.cms7.crisp.mock.resource.MockJdomResourceResolverAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscriptionResourceTest {

    private final SubscriptionResource resource = SubscriptionResource.getInstance();

    @Before
    public void setUp() {
        final Map<String, ResourceResolver> resourceResolverMap = new HashMap<>();
        resourceResolverMap.put("govDeliveryApi", new MockJdomResourceResolverAdapter() {
            @Override
            public Resource resolve(String absPath, Map<String, Object> pathVariables, ExchangeHint exchangeHint) throws ResourceException {
                return urlToResource(SubscriptionResourceTest.class.getResource("/subscriber-output.xml"));
            }
        });
        final ResourceServiceBroker mockBroker = new MockResourceServiceBroker(resourceResolverMap);
        MockCrispHstServices.setDefaultResourceServiceBroker(mockBroker);
    }

    @Test
    public void getSubscriberId_calls_api_to_subscribe_user() {
        final String email = "user@domain.com";
        final List<String> topics = Arrays.asList("TOPIC1", "TOPIC2");

        final String subscriberId = resource.subscribe(email, topics);

        assertEquals("ZW1haWxAdGVzdC5nb3ZkZWxpdmVyeS5jb20=", subscriberId);
    }
}
