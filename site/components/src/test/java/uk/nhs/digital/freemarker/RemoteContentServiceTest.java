package uk.nhs.digital.freemarker;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.ResourceResolver;
import org.onehippo.cms7.crisp.core.resource.jackson.SimpleJacksonRestTemplateResourceResolver;
import org.onehippo.cms7.crisp.mock.broker.MockResourceServiceBroker;
import org.onehippo.cms7.crisp.mock.module.MockCrispHstServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RemoteContentServiceTest {

    @Configuration
    @EnableAspectJAutoProxy
    static class MyTestConfiguration {

        @Bean
        public static HystrixCommandAspect hystrixCommandAspect() {
            return new HystrixCommandAspect();
        }

        @Bean
        public static RemoteContentService testRemoteContentService()  {
            return new TestRemoteContentServiceImpl();
        }

        @Bean static ResourceResolver testResourceResolver() {
            SimpleJacksonRestTemplateResourceResolver resourceResolver = new SimpleJacksonRestTemplateResourceResolver();
            resourceResolver.setBaseUri("");
            resourceResolver.setCacheEnabled(false);
            resourceResolver.setRestTemplate(new RestTemplate(new SimpleClientHttpRequestFactory()));
            return resourceResolver;
        }

    }

    @Autowired
    private RemoteContentService testRemoteContentService;

    @Autowired
    private ResourceResolver testResourceResolver;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Before
    public void before() {
        Map<String, ResourceResolver> resourceResolverMap = new HashMap<>();
        resourceResolverMap.put("testResourceResolver", testResourceResolver);
        ResourceServiceBroker mockBroker = new MockResourceServiceBroker(resourceResolverMap);
        MockCrispHstServices.setDefaultResourceServiceBroker(mockBroker);
    }

    @Test
    public void requestResource_fastResponse_noFallback() throws MalformedURLException {
        stubFor(get(urlEqualTo("/resource"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{ \"testValue\" : \"All Good\" }")));

        TestObject testObject = (TestObject) testRemoteContentService.getContentObjectFrom(new URL("http://localhost:8089/resource"), "testResourceResolver", TestObject.class);

        assertThat(testObject.getTestValue(), is("All Good"));
    }

    @Test
    public void requestResource_slowResponse_fallbackUsed() throws MalformedURLException {
        stubFor(get(urlEqualTo("/resource"))
            .willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(2000)
                .withHeader("Content-Type", "application/json")
                .withBody("{ \"testValue\" : \"Don't look at me, I'm to slow\" }")));

        TestObject testObject = (TestObject) testRemoteContentService.getContentObjectFrom(new URL("http://localhost:8089/resource"), "testResourceResolver", TestObject.class);

        assertThat(testObject.getTestValue(), is(TestRemoteContentServiceImpl.fallbackValue));
    }




}
