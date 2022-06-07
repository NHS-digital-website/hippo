package uk.nhs.digital.apispecs.jobs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.*;
import static uk.nhs.digital.test.util.JcrTestUtils.*;
import static uk.nhs.digital.test.util.JcrTestUtils.BloomReachJcrDocumentVariantType.DRAFT;
import static uk.nhs.digital.test.util.MockJcrRepoProvider.initJcrRepoFromYaml;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.hippoecm.repository.api.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onehippo.cms7.crisp.api.resource.ResourceResolver;
import org.onehippo.cms7.crisp.core.resource.jackson.SimpleJacksonRestTemplateResourceResolver;
import org.onehippo.cms7.crisp.mock.broker.MockResourceServiceBroker;
import org.onehippo.cms7.crisp.mock.module.MockCrispHstServices;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;
import uk.nhs.digital.JcrDocumentUtils;
import uk.nhs.digital.JcrNodeUtils;
import uk.nhs.digital.test.util.JcrTestUtils;
import uk.nhs.digital.toolbox.secrets.ApplicationSecrets;

import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.query.Query;


@RunWith(PowerMockRunner.class)
@PrepareForTest({JcrDocumentUtils.class, JcrNodeUtils.class, ApiSpecSyncFromProxygenJob.class, ApplicationSecrets.class})
@PowerMockIgnore({"javax.net.ssl.*", "javax.crypto.*", "javax.management.*"})
public class ApiSpecSyncFromProxygenJobIntegrationTest {

    private static final String PROPERTY_NAME_WEBSITE_JSON = "website:json";

    // Test data
    private static final String TEST_SPEC_ID = "test-proxygen-spec";
    private static final String TEST_DATA_FILES_DIR = "/test-data/api-specifications/ApiSpecSyncFromProxygenJobIntegrationTest/";
    private static final String SPEC_JSON_FROM_PROXYGEN = "{\"spec\":\"json from Proxygen\"}";

    // Proxygen access
    private static final String PARAM_PROXYGEN_RESOURCES_SPECS_ALL_URL = "devzone.proxygen.resources.specs.all.url";
    private static final String PARAM_PROXYGEN_RESOURCES_SPECS_INDIVIDUAL_URL = "devzone.proxygen.resources.specs.individual.url";
    private static final String PARAM_PROXYGEN_OAUTH_TOKEN_URL = "devzone.proxygen.oauth.token.url";
    private static final String PARAM_PROXYGEN_OAUTH_AUD_URL = "devzone.proxygen.oauth.aud.url";
    private static final String PARAM_PROXYGEN_OAUTH_CLIENT_ID = "DEVZONE_PROXYGEN_OAUTH_CLIENT_ID";
    private static final String PARAM_PROXYGEN_OAUTH_PRIVATE_KEY = "proxygenMachineUser.key";

    private static final String PROXYGEN_CLIENT_ID = "clientId";
    private static final String PROXYGEN_TEST_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n"
        + "MIIEpQIBAAKCAQEAnpW/wYwZFcPl5+jvjBjfG8KLPEidfiCb5m16sMb64ULSuYg0\n"
        + "LkR/hHmzLgslf62xozk6Z44ZN0dYss/szrW1IkK+cVFhe+EZHGifrMiZ+gGQaWRp\n"
        + "np8w04I3K2NhqX7DYY0xxIxAwb7N2zmAhwHBnQaj69HGhK6zx/myblIM/zRSoKdj\n"
        + "cDZnR0ZlbutwasBxKpdvduJ5ANHg+9LHzTpjpCiKowdGQRBDI5kjleKaNlW7YY4p\n"
        + "qvLMpl/BzAA4/0ewpAbZ1WvBWwjoQP5DUpivikTJgFqB3r4NKB7XAkruDUqMqvVF\n"
        + "Dr4IfWT+pllb4N5I1Yo0nfbsTgbzCRGRp4jHtwIDAQABAoIBAA/xBcGzD6J8etQB\n"
        + "OnNe3K6xs8Vvl0Viicl0H+09U2LpkL1Nz9EG2bq7cSOjwzU+hKjfy9s1EZvY9DWl\n"
        + "aSyuX3L+Op6xi5ckXZ3ZdpOr5q6XEalGiseDQSlB4cHez6Z2M7XOt74GS0wungr3\n"
        + "kNABgTEDxd6iy83ysefcVe95GA1fUHkIX6/3L+i5OeSofm8jOUMDf8As3nQK/06O\n"
        + "TPZ/PPjYs+Rk7Fs5HEaAOBuTe9kxiS3IXbP6Wlc434STW4Y4U5MqxsJJkpj9S3Ao\n"
        + "zg++3SuGqgSIXeS6emwseqidSvQpX4wHIG4UOcHepDm6+elA4HgVmmyWO36bTn2j\n"
        + "hJUVqsECgYEA2uatvWtjdEhXhUXhF2/2gBqvCqDGVf70lr8O9B7ptS+NvRQ6mIpM\n"
        + "4iFNl2tOTuwV5tJ0PCAhI3fXs0tQp8DE3wbNAbOAraOWsRU5kmGDeirARxAOFeWd\n"
        + "xAOTPBsFi2MUhps61t5Xe6YA4Jnc6PyGWorLhYK2yOumkBCVTzJChZkCgYEAuXYs\n"
        + "SsQNPesmG8YXcHbqkTpPzju9YA61hiyxWVLBs+RzwqKCCoCdePX/IMTw9IgO7or/\n"
        + "/YniW48WLCJhiRmtzedG022aD7suRjELLAYvmMIRvTlWbkMZKzJEJpifjdGtuCJu\n"
        + "j4+ANAHPIUxmi+hujqmic/TVMkX6iNweSoRrac8CgYEAsrB/vxJ8mx26pAZJGpLB\n"
        + "DA28OdbIUMbQ7oWENW9OpCLvdxueQYsT+7vn1OU5hV9e+Y9uZu5rXsKjh67BZk8L\n"
        + "5mtsyii3sAbMKj4DQyTq6L0hPGE0vlc0FCA+/l+ohQIabU8mFuRlUYBPUwkGtgH9\n"
        + "wsduH8x2XUlQLGX9HCsQ9nECgYEAqKQNSHsXc2tEvtKU874eM6qrcOOfox4c8AHp\n"
        + "zkhTPMoKfTyaNQSYrKo7sMeEN0cVFNIuTpS30Pu+dKlauXkxMt8P5ydthMp3HHpy\n"
        + "JzkExMoLqV3e+upmk4HkuJfl1eaJKbudf0tEj4Xpm+TGBoh3J6CWyQwjZNB/LT3D\n"
        + "CY15wr8CgYEArpiT2uch3cUzPTe6r4Iew4wkrHHhLwrGpwSSFVy7RxaFWEY5Optj\n"
        + "e3hJttb3KgXD4Rx/LLvBto6hhBVo+qLdbALZMBPQNKiCgY0hL1+lhKeUyYiZbx76\n"
        + "puFQeyOxa0XT7lrib0qGGS1nHzt62YxTBzHziNwNo6YexTp7ouE0578=\n"
        + "-----END RSA PRIVATE KEY-----\n";

    private static final String PROXYGEN_OAUTH_ACCESS_TOKEN = "accessToken";
    private static final String PROXYGEN_CLIENT_ASSERTION_TYPE = "urn%3Aietf%3Aparams%3Aoauth%3Aclient-assertion-type%3Ajwt-bearer";
    private static final String PROXYGEN_CLIENT_ASSERTION_REGEX = "(?:[\\w-]+\\.){2}[\\w-]+$";

    private static final String PROXYGEN_OAUTH_TOKEN_URL_PATH = "/auth/realms/api-producers/protocol/openid-connect/token";
    private static final String PROXYGEN_OAUTH_AUD_URL_PATH = "/auth/realms/api-producers";
    private static final String PROXYGEN_ALL_SPECS_URL_PATH = "/specs";
    private static final String PROXYGEN_SINGLE_SPEC_URL_PATH_TEMPLATE = "/apis/{specificationId}/spec";
    private static final String PROXYGEN_SINGLE_SPEC_URL_PATH = PROXYGEN_SINGLE_SPEC_URL_PATH_TEMPLATE.replace("{specificationId}", TEST_SPEC_ID);

    private String proxygenAllSpecsUrl;
    private String proxygenSingleSpecUrlTemplate;
    private String oauthTokenUrl;

    @Rule
    public WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicPort());

    // JCR repo access
    private Session session;
    private Node repositoryRootNode;
    private RepositoryJobExecutionContext repositoryJobExecutionContext;

    private ApiSpecSyncFromProxygenJob apiSpecSyncFromProxygenJob;

    @Before
    public void setUp() throws Exception {

        session = spy(MockJcr.newSession());
        doNothing().when(session).logout(); // prevent the job from closing the session - it's needed in assertions

        repositoryJobExecutionContext = mock(RepositoryJobExecutionContext.class);
        given(repositoryJobExecutionContext.createSystemSession()).willReturn(session);

        apiSpecSyncFromProxygenJob = new ApiSpecSyncFromProxygenJob();
    }

    @Test
    public void downloadsSpecUpdateFromProxygenAndPublishesOnWebsite() throws Exception {

        // given
        cmsConfiguredForProxygenAccess();
        crispApiConfiguredForOAuth2();
        proxygenReturnsAccessToken();
        proxygenReturnsListOfSpecsIncludingTheOneConfiguredInCms();
        proxygenReturnsDetailsOfTheSpecConfiguredInCms();
        cmsContainsSpecDocMatchingUpdatedSpecInProxygen();

        // when
        apiSpecSyncFromProxygenJob.execute(repositoryJobExecutionContext);

        // then
        verifyOauth2AccessTokenRetrievedFromProxygen();
        verifyProxygenRequestForSpecificationsStatuses();
        verifyProxygenRequestForSpecification();
        verifyJsonReceivedFromProxygenSpecWasSetOnApiSpecificationDocument();
        verifyApiSpecificationDocumentWasPublished();
        verifySessionLogOut();
    }

    private void verifySessionLogOut() {
        then(session).should().logout();
    }

    private void cmsConfiguredForProxygenAccess() {

        final String baseWiremockUrl = "http://localhost:" + wireMock.port();

        proxygenAllSpecsUrl = baseWiremockUrl + PROXYGEN_ALL_SPECS_URL_PATH;
        proxygenSingleSpecUrlTemplate = baseWiremockUrl + PROXYGEN_SINGLE_SPEC_URL_PATH_TEMPLATE;
        oauthTokenUrl = baseWiremockUrl + PROXYGEN_OAUTH_TOKEN_URL_PATH;
        final String oauthAudUrl = baseWiremockUrl + PROXYGEN_OAUTH_AUD_URL_PATH;

        mockStatic(System.class);

        given(System.getProperty(PARAM_PROXYGEN_RESOURCES_SPECS_ALL_URL)).willReturn(proxygenAllSpecsUrl);
        given(System.getProperty(PARAM_PROXYGEN_RESOURCES_SPECS_INDIVIDUAL_URL)).willReturn(proxygenSingleSpecUrlTemplate);
        given(System.getProperty(PARAM_PROXYGEN_OAUTH_TOKEN_URL)).willReturn(oauthTokenUrl);
        given(System.getProperty(PARAM_PROXYGEN_OAUTH_AUD_URL)).willReturn(oauthAudUrl);
        given(System.getenv(PARAM_PROXYGEN_OAUTH_CLIENT_ID)).willReturn(PROXYGEN_CLIENT_ID);
        given(System.getenv(PARAM_PROXYGEN_OAUTH_PRIVATE_KEY)).willReturn(PROXYGEN_TEST_PRIVATE_KEY);

        // This config is required to create the apigeeManagementApi bean in custom-resource-resolvers
        given(System.getenv("DEVZONE_APIGEE_OAUTH_BASICAUTHTOKEN")).willReturn("authToken");
        given(System.getenv("DEVZONE_APIGEE_OAUTH_OTPKEY")).willReturn("otpKey");
    }

    private void crispApiConfiguredForOAuth2() {
        ConfigurableEnvironment crispApiSpringApplicationContextEnvironment = new MockEnvironment()
            .withProperty(PARAM_PROXYGEN_RESOURCES_SPECS_ALL_URL, proxygenAllSpecsUrl)
            .withProperty(PARAM_PROXYGEN_OAUTH_TOKEN_URL, oauthTokenUrl)
            .withProperty(PARAM_PROXYGEN_RESOURCES_SPECS_INDIVIDUAL_URL, proxygenSingleSpecUrlTemplate)
            .withProperty(PARAM_PROXYGEN_OAUTH_CLIENT_ID, PROXYGEN_CLIENT_ID)
            .withProperty(PARAM_PROXYGEN_OAUTH_PRIVATE_KEY, PROXYGEN_TEST_PRIVATE_KEY);

        // See https://documentation.bloomreach.com/14/library/concepts/crisp-api/unit-testing.html

        final ClassPathXmlApplicationContext crispApiSpringApplicationContext = new ClassPathXmlApplicationContext();
        crispApiSpringApplicationContext.setEnvironment(crispApiSpringApplicationContextEnvironment);
        crispApiSpringApplicationContext.setConfigLocations(
            testDataFileLocation("crisp-spring-context-properties-support.xml"),
            "/META-INF/hst-assembly/addon/crisp/overrides/custom-resource-resolvers.xml"
        );
        crispApiSpringApplicationContext.refresh();

        final String proxygenManagementApiCrispApiNamespace = "proxygenApi";

        final SimpleJacksonRestTemplateResourceResolver simpleJacksonRestTemplateResourceResolver =
            crispApiSpringApplicationContext.getBean(
                proxygenManagementApiCrispApiNamespace,
                SimpleJacksonRestTemplateResourceResolver.class
            );

        final ImmutableMap<String, ResourceResolver> resourceResolverMap = ImmutableMap.<String, ResourceResolver>builder()
            .put(proxygenManagementApiCrispApiNamespace, simpleJacksonRestTemplateResourceResolver)
            .build();

        final MockResourceServiceBroker mockResourceServiceBroker = new MockResourceServiceBroker(
            resourceResolverMap
        );

        MockCrispHstServices.setDefaultResourceServiceBroker(mockResourceServiceBroker);
    }

    private void verifyJsonReceivedFromProxygenSpecWasSetOnApiSpecificationDocument() {

        final Node specificationHandleNode = existingSpecHandleNode();

        final Node documentVariantNodeDraft = getDocumentVariantNode(specificationHandleNode, DRAFT);

        final String actualSpecJsonSavedInCms = JcrTestUtils.findStringProperty(documentVariantNodeDraft, PROPERTY_NAME_WEBSITE_JSON).orElse(null);

        assertThat(
            "API Specification JSON received from Proxygen is saved on document.",
            actualSpecJsonSavedInCms,
            is(SPEC_JSON_FROM_PROXYGEN)
        );
    }

    private void verifyApiSpecificationDocumentWasPublished() {

        verifyStatic(JcrDocumentUtils.class);
        JcrDocumentUtils.publish(existingSpecHandleNode());
    }

    private Node existingSpecHandleNode() {
        return getRelativeNode(
            repositoryRootNode, "/content/documents/corporate-website/api-specifications-location-a/api-spec-a");
    }

    private Node existingSpecImportMetadataNode() {
        return getRelativeNode(
            repositoryRootNode, "/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata");
    }

    private void proxygenReturnsAccessToken() {

        wireMock.givenThat(
            post(urlEqualTo(PROXYGEN_OAUTH_TOKEN_URL_PATH))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(proxygenAccessTokenResponse())
                )
        );
    }

    private void proxygenReturnsListOfSpecsIncludingTheOneConfiguredInCms() {

        wireMock.givenThat(
            get(urlMatching(PROXYGEN_ALL_SPECS_URL_PATH))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(proxygenApiSpecificationsJson())
                )
        );
    }

    private void verifyOauth2AccessTokenRetrievedFromProxygen() {

        // @formatter:off
        final String proxygenOauth2AccessTokenRequestBodyRegex =
            "grant_type=client_credentials"
                + "&client_assertion_type=" + PROXYGEN_CLIENT_ASSERTION_TYPE
                + "&client_assertion=" + PROXYGEN_CLIENT_ASSERTION_REGEX;
        // @formatter:on

        wireMock.verify(
            postRequestedFor(urlEqualTo(PROXYGEN_OAUTH_TOKEN_URL_PATH))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded;charset=UTF-8"))
                .withRequestBody(matching(
                    proxygenOauth2AccessTokenRequestBodyRegex
                ))
        );
    }

    private void verifyProxygenRequestForSpecification() {

        wireMock.verify(
            getRequestedFor(urlEqualTo(PROXYGEN_ALL_SPECS_URL_PATH))
                .withHeader("Authorization", equalTo("Bearer " + PROXYGEN_OAUTH_ACCESS_TOKEN))
        );
    }

    private void verifyProxygenRequestForSpecificationsStatuses() {

        wireMock.verify(
            getRequestedFor(urlEqualTo(PROXYGEN_SINGLE_SPEC_URL_PATH))
                .withHeader("Authorization", equalTo("Bearer " + PROXYGEN_OAUTH_ACCESS_TOKEN))
        );
    }

    private void proxygenReturnsDetailsOfTheSpecConfiguredInCms() {

        wireMock.givenThat(
            get(urlMatching(PROXYGEN_SINGLE_SPEC_URL_PATH))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(SPEC_JSON_FROM_PROXYGEN)
                )
        );
    }

    private void cmsContainsSpecDocMatchingUpdatedSpecInProxygen() throws Exception {

        initJcrRepoFromYaml(session, testDataFileLocation("specification-documents-in-cms.yml"));

        repositoryRootNode = getRootNode(session);

        final Node specDocHandleNode = existingSpecHandleNode();
        MockJcr.setQueryResult(
            session,
            "/jcr:root/content/documents/corporate-website//element(*, website:apispecification)/..[@jcr:primaryType='hippo:handle']",
            Query.XPATH,
            singletonList(specDocHandleNode)
        );

        MockJcr.setQueryResult(session,
            "/jcr:root/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata",
            Query.XPATH,
            singletonList(existingSpecImportMetadataNode())
        );

        // Low-level JCR-related components mocked where it was prohibitively hard to avoid:

        final DocumentManager documentManager = mock(DocumentManager.class);

        mockStatic(JcrDocumentUtils.class);
        given(JcrDocumentUtils.documentManagerFor(session)).willReturn(documentManager);

        final Node documentVariantNodeDraft = getDocumentVariantNode(specDocHandleNode, DRAFT);

        final Document documentVariantDraft = new DocumentVariant(documentVariantNodeDraft);
        given(documentManager.obtainEditableDocument(specDocHandleNode)).willReturn(documentVariantDraft);
    }

    private String proxygenAccessTokenResponse() {
        return contentOfFileFromClasspath(testDataFileLocation("auth-access-token-response.json"))
            .replace("{ACCESS_TOKEN}", PROXYGEN_OAUTH_ACCESS_TOKEN);
    }

    private String proxygenApiSpecificationsJson() {
        return contentOfFileFromClasspath(testDataFileLocation("specifications-in-proxygen.json"));
    }

    private String testDataFileLocation(final String fileName) {
        return TEST_DATA_FILES_DIR + fileName;
    }

    // Invoked from the test-specific crisp-spring-context-properties-support.xml
    public static class DummyApplicationSecrets {

        public String getValue(final String propertyName) {
            String val = Optional.ofNullable(System.getProperty(propertyName))
                .orElse(System.getenv(propertyName));

            System.out.println("printf: " + propertyName + " " + val);

            return val;
        }

        public String getFromFile(final String propertyName) {
            return getValue(propertyName);
        }
    }
}
