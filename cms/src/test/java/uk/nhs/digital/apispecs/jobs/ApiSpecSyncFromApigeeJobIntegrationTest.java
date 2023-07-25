package uk.nhs.digital.apispecs.jobs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.*;
import static uk.nhs.digital.test.util.JcrTestUtils.*;
import static uk.nhs.digital.test.util.JcrTestUtils.BloomReachJcrDocumentVariantType.DRAFT;
import static uk.nhs.digital.test.util.MockJcrRepoProvider.initJcrRepoFromYaml;
import static uk.nhs.digital.test.util.TestFileUtils.contentOfFileFromClasspath;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.hippoecm.repository.api.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import uk.nhs.digital.JcrRowUtils;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.test.util.JcrTestUtils;
import uk.nhs.digital.toolbox.secrets.ApplicationSecrets;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.query.Query;


@RunWith(PowerMockRunner.class)
@PrepareForTest({JcrDocumentUtils.class, JcrNodeUtils.class, ApiSpecSyncFromApigeeJob.class, ApplicationSecrets.class, JcrRowUtils.class})
@PowerMockIgnore({"javax.net.ssl.*", "javax.crypto.*", "javax.management.*"})
public class ApiSpecSyncFromApigeeJobIntegrationTest {

    private static final String PROPERTY_NAME_WEBSITE_JSON = "website:json";

    // Test data
    private static final String TEST_SPEC_ID = "269326";
    private static final String TEST_DATA_FILES_DIR = "/test-data/api-specifications/ApiSpecSyncFromApigeeJobIntegrationTest/";
    private static final String SPEC_JSON_FROM_APIGEE = "{\"spec\":\"json from Apigee\"}";

    // Apigee access
    private static final String PARAM_APIGEE_RESOURCES_SPECS_ALL_URL = "devzone.apigee.resources.specs.all.url";
    private static final String PARAM_APIGEE_RESOURCES_SPECS_INDIVIDUAL_URL = "devzone.apigee.resources.specs.individual.url";
    private static final String PARAM_APIGEE_OAUTH_TOKEN_URL = "devzone.apigee.oauth.token.url";
    private static final String PARAM_APIGEE_OAUTH_USERNAME = "DEVZONE_APIGEE_OAUTH_USERNAME";
    private static final String PARAM_APIGEE_OAUTH_PASSWORD = "DEVZONE_APIGEE_OAUTH_PASSWORD";
    private static final String PARAM_APIGEE_OAUTH_BASICAUTHTOKEN = "DEVZONE_APIGEE_OAUTH_BASICAUTHTOKEN";
    private static final String PARAM_APIGEE_OAUTH_OTPKEY = "DEVZONE_APIGEE_OAUTH_OTPKEY";

    private static final String OAUTH_USERNAME = "oauthUsername";
    private static final String OAUTH_PASSWORD = "oauthPassword";
    private static final String OAUTH_BASIC_AUTH_TOKEN = "ZWRnZWNsaTplZGdlY2xpc2VjcmV0";
    private static final String OAUTH_OTP_KEY = "JBSWY3DPEHPK3PXP";

    private static final String OAUTH_ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiJ9.sv-gfMUntSWYZPsDok0w";

    private static final String OAUTH_TOKEN_URL_PATH = "/oauth/token";
    private static final String APIGEE_ALL_SPECS_URL_PATH = "/dapi/api/organizations/test-org/specs/folder/home/specs";
    private static final String APIGEE_SINGLE_SPEC_URL_PATH_TEMPLATE = "/dapi/api/organizations/test-org/specs/doc/{specificationId}/content";
    private static final String APIGEE_SINGLE_SPEC_URL_PATH = APIGEE_SINGLE_SPEC_URL_PATH_TEMPLATE.replace("{specificationId}", TEST_SPEC_ID);


    private String apigeeAllSpecsUrl;
    private String apigeeSingleSpecUrlTemplate;
    private String oauthTokenUrl;

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Rule public WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicPort());

    // JCR repo access
    private Session session;
    private Node repositoryRootNode;

    private ConfigurableEnvironment crispApiSpringApplicationContextEnvironment;

    private RepositoryJobExecutionContext repositoryJobExecutionContext;

    private ApiSpecSyncFromApigeeJob apiSpecSyncFromApigeeJob;

    @Before
    public void setUp() throws Exception {

        session = spy(MockJcr.newSession());
        doNothing().when(session).logout(); // prevent the job from closing the session - it's needed in assertions

        repositoryJobExecutionContext = mock(RepositoryJobExecutionContext.class);
        given(repositoryJobExecutionContext.createSystemSession()).willReturn(session);

        apiSpecSyncFromApigeeJob = new ApiSpecSyncFromApigeeJob();
    }

    @Test
    public void downloadsSpecUpdateFromApigeeAndPublishesOnWebsite() throws Exception {

        // given
        cmsConfiguredForApigeeAccess();
        crispApiConfiguredForOAuth2();
        apigeeReturnsAccessToken();
        apigeeReturnsListOfSpecsIncludingTheOneConfiguredInCms();
        apigeeReturnsDetailsOfTheSpecConfiguredInCms();
        cmsContainsSpecDocMatchingUpdatedSpecInApigee();

        // when
        apiSpecSyncFromApigeeJob.execute(repositoryJobExecutionContext);

        // then
        verifyOauth2AccessTokenRetrievedFromApigee();
        verifyApigeeRequestForSpecificationsStatuses();
        verifyApigeeRequestForSpecification();
        verifyJsonReceivedFromApigeeSpecWasSetOnApiSpecificationDocument();
        verifyApiSpecificationDocumentWasPublished();
        verifySessionLogOut();
    }

    private void verifySessionLogOut() {
        then(session).should().logout();
    }

    private void cmsConfiguredForApigeeAccess() {

        final String baseWiremockUrl = "http://localhost:" + wireMock.port();

        apigeeAllSpecsUrl = baseWiremockUrl + APIGEE_ALL_SPECS_URL_PATH;
        apigeeSingleSpecUrlTemplate = baseWiremockUrl + APIGEE_SINGLE_SPEC_URL_PATH_TEMPLATE;
        oauthTokenUrl = baseWiremockUrl + OAUTH_TOKEN_URL_PATH;

        mockStatic(System.class);

        given(System.getProperty(PARAM_APIGEE_RESOURCES_SPECS_ALL_URL)).willReturn(apigeeAllSpecsUrl);
        given(System.getProperty(PARAM_APIGEE_RESOURCES_SPECS_INDIVIDUAL_URL)).willReturn(apigeeSingleSpecUrlTemplate);
        given(System.getProperty(PARAM_APIGEE_OAUTH_TOKEN_URL)).willReturn(oauthTokenUrl);

        given(System.getenv(PARAM_APIGEE_OAUTH_USERNAME)).willReturn(OAUTH_USERNAME);
        given(System.getenv(PARAM_APIGEE_OAUTH_PASSWORD)).willReturn(OAUTH_PASSWORD);
        given(System.getenv(PARAM_APIGEE_OAUTH_BASICAUTHTOKEN)).willReturn(OAUTH_BASIC_AUTH_TOKEN);
        given(System.getenv(PARAM_APIGEE_OAUTH_OTPKEY)).willReturn(OAUTH_OTP_KEY);

        // This config is required to create the proxygenApi bean in custom-resource-resolvers
        given(System.getenv("proxygenMachineUser.key")).willReturn("privateKey");
        given(System.getenv("DEVZONE_PROXYGEN_OAUTH_CLIENT_ID")).willReturn("clientId");
        given(System.getenv("devzone.proxygen.oauth.aud.url")).willReturn("audUrl");
        given(System.getenv("devzone.proxygen.oauth.kid")).willReturn("test-1");
    }

    private void crispApiConfiguredForOAuth2() {

        crispApiSpringApplicationContextEnvironment = new MockEnvironment()
            .withProperty(PARAM_APIGEE_RESOURCES_SPECS_ALL_URL, apigeeAllSpecsUrl)
            .withProperty(PARAM_APIGEE_OAUTH_TOKEN_URL, oauthTokenUrl)
            .withProperty(PARAM_APIGEE_RESOURCES_SPECS_INDIVIDUAL_URL, apigeeSingleSpecUrlTemplate)
            .withProperty(PARAM_APIGEE_OAUTH_USERNAME, OAUTH_USERNAME)
            .withProperty(PARAM_APIGEE_OAUTH_PASSWORD, OAUTH_PASSWORD)
            .withProperty(PARAM_APIGEE_OAUTH_BASICAUTHTOKEN, OAUTH_BASIC_AUTH_TOKEN)
            .withProperty(PARAM_APIGEE_OAUTH_OTPKEY, OAUTH_OTP_KEY);

        // See https://documentation.bloomreach.com/14/library/concepts/crisp-api/unit-testing.html

        final ClassPathXmlApplicationContext crispApiSpringApplicationContext = new ClassPathXmlApplicationContext();
        crispApiSpringApplicationContext.setEnvironment(crispApiSpringApplicationContextEnvironment);
        crispApiSpringApplicationContext.setConfigLocations(
            testDataFileLocation("crisp-spring-context-properties-support.xml"),
            "/META-INF/hst-assembly/addon/crisp/overrides/custom-resource-resolvers.xml"
        );
        crispApiSpringApplicationContext.refresh();

        final String apigeeManagementApiCrispApiNamespace = "apigeeManagementApi";

        final SimpleJacksonRestTemplateResourceResolver simpleJacksonRestTemplateResourceResolver =
            crispApiSpringApplicationContext.getBean(
                apigeeManagementApiCrispApiNamespace,
                SimpleJacksonRestTemplateResourceResolver.class
            );

        final ImmutableMap<String, ResourceResolver> resourceResolverMap = ImmutableMap.<String, ResourceResolver>builder()
            .put(apigeeManagementApiCrispApiNamespace, simpleJacksonRestTemplateResourceResolver)
            .build();

        final MockResourceServiceBroker mockResourceServiceBroker = new MockResourceServiceBroker(
            resourceResolverMap
        );

        MockCrispHstServices.setDefaultResourceServiceBroker(mockResourceServiceBroker);
    }

    private void verifyJsonReceivedFromApigeeSpecWasSetOnApiSpecificationDocument() {

        final Node specificationHandleNode = existingSpecHandleNode();

        final Node documentVariantNodeDraft = getDocumentVariantNode(specificationHandleNode, DRAFT);

        final String actualSpecJsonSavedInCms = JcrTestUtils.findStringProperty(documentVariantNodeDraft, PROPERTY_NAME_WEBSITE_JSON).orElse(null);

        assertThat(
            "API Specification JSON received from Apigee is saved on document.",
            actualSpecJsonSavedInCms,
            is(SPEC_JSON_FROM_APIGEE)
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

    private Node existingSpecDraftVariantNode() {
        return getRelativeNode(
            repositoryRootNode, "/content/documents/corporate-website/api-specifications-location-a/api-spec-a/api-spec-a");
    }

    private Node existingSpecImportMetadataNode() {
        return getRelativeNode(
            repositoryRootNode, "/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata");
    }

    private void apigeeReturnsAccessToken() {

        wireMock.givenThat(
            post(urlEqualTo(OAUTH_TOKEN_URL_PATH))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(apigeeAccessTokenResponse())
                )
        );
    }

    private void apigeeReturnsListOfSpecsIncludingTheOneConfiguredInCms() {

        wireMock.givenThat(
            get(urlMatching(APIGEE_ALL_SPECS_URL_PATH))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(apigeeApiSpecificationsJson())
                )
        );
    }

    private void verifyOauth2AccessTokenRetrievedFromApigee() {

        // @formatter:off
        final String apigeeOauth2AccessTokenRequestBodyRegex =
                  "grant_type=password"
                + "&username=" + OAUTH_USERNAME
                + "&password=" + OAUTH_PASSWORD
                + "&mfa_token=\\d+";
        // @formatter:on

        wireMock.verify(
            postRequestedFor(urlEqualTo(OAUTH_TOKEN_URL_PATH))
                .withHeader("Authorization", equalTo("Basic " + OAUTH_BASIC_AUTH_TOKEN))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded;charset=UTF-8"))
                .withRequestBody(matching(
                    apigeeOauth2AccessTokenRequestBodyRegex
                ))
        );
    }

    private void verifyApigeeRequestForSpecification() {

        wireMock.verify(
            getRequestedFor(urlEqualTo(APIGEE_ALL_SPECS_URL_PATH))
                .withHeader("Authorization", equalTo("Bearer " + OAUTH_ACCESS_TOKEN))
        );
    }

    private void verifyApigeeRequestForSpecificationsStatuses() {

        wireMock.verify(
            getRequestedFor(urlEqualTo(APIGEE_SINGLE_SPEC_URL_PATH))
                .withHeader("Authorization", equalTo("Bearer " + OAUTH_ACCESS_TOKEN))
        );
    }

    private void apigeeReturnsDetailsOfTheSpecConfiguredInCms() {

        wireMock.givenThat(
            get(urlMatching(APIGEE_SINGLE_SPEC_URL_PATH))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(SPEC_JSON_FROM_APIGEE)
                )
        );
    }

    private void cmsContainsSpecDocMatchingUpdatedSpecInApigee() throws Exception {
        initJcrRepoFromYaml(session, testDataFileLocation("specification-documents-in-cms.yml"));

        repositoryRootNode = getRootNode(session);

        setJcrQueryResultForInitialSyncDataForAllApiSpecifications();

        final Node specDocHandleNode = existingSpecHandleNode();
        MockJcr.setQueryResult(
            session,
            "/jcr:root/content/documents/corporate-website//element(*, website:apispecification)/..[@jcr:primaryType='hippo:handle' and @jcr:uuid='api-spec-a-handle-node-id']",
            Query.XPATH,
            singletonList(specDocHandleNode)
        );

        MockJcr.setQueryResult(session,
            "/jcr:root/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata",
            Query.XPATH,
            singletonList(existingSpecImportMetadataNode())
        );

        // Low-level JCR-related components mocked where it was prohibitively hard to avoid:
        stubJcrQuerySelectorNameCalls();

        final DocumentManager documentManager = mock(DocumentManager.class);

        mockStatic(JcrDocumentUtils.class);
        given(JcrDocumentUtils.documentManagerFor(session)).willReturn(documentManager);

        final Node documentVariantNodeDraft = getDocumentVariantNode(specDocHandleNode, DRAFT);

        final Document documentVariantDraft = new DocumentVariant(documentVariantNodeDraft);
        given(documentManager.obtainEditableDocument(specDocHandleNode)).willReturn(documentVariantDraft);
    }

    private String apigeeAccessTokenResponse() {
        return contentOfFileFromClasspath(testDataFileLocation("auth-access-token-response.json"))
            .replace("{ACCESS_TOKEN}", OAUTH_ACCESS_TOKEN);
    }

    private String apigeeApiSpecificationsJson() {
        return testDataFromFile("specifications-in-apigee.json");
    }

    private String testDataFromFile(final String testDataFileName) {
        return contentOfFileFromClasspath(testDataFileLocation(testDataFileName));
    }

    private String testDataFileLocation(final String fileName) {
        return TEST_DATA_FILES_DIR + fileName;
    }

    private void setJcrQueryResultForInitialSyncDataForAllApiSpecifications() {
        // MockJcr.setQueryResult(...) does not support setting column names in the
        // result that is required for findInitialSyncDataForAllApiSpecification(),
        // hence the need for a more manual approach for this query

        final Node specDocDraftVariantNode = existingSpecDraftVariantNode();

        final List<String> initialSyncDataColumnNames = asList(
            "website:specification_id", "hippo:paths");

        final AtomicReference<String> actualQuery = new AtomicReference<>();

        MockJcr.addQueryResultHandler(session, query -> {
            final String expectedStatement =
                "/jcr:root/content/documents/corporate-website//element(*, website:apispecification)[hippostd:state = 'draft']/(@website:specification_id union @hippo:paths)";
            actualQuery.set(query.getStatement());   // intercept actual query
            if (expectedStatement.equals(actualQuery.get())) {
                return new MockQueryResult(singletonList(specDocDraftVariantNode), initialSyncDataColumnNames);
            } else {
                return null;
            }
        });
    }

    private void stubJcrQuerySelectorNameCalls() throws Exception {
        // As the MockQueryResult object does not support the getSelectorNames()
        // method and the result nodes do not seem to have a selector name concept,
        // we must stub out anything that requires the selector name within the
        // findInitialSyncDataForAllApiSpecification() method

        ApiSpecificationDocumentJcrRepository apiSpecJcrRepository = spy(new ApiSpecificationDocumentJcrRepository(session));
        doReturn("selectorName").when(apiSpecJcrRepository).getDefaultNodeSelectorName(any());
        whenNew(ApiSpecificationDocumentJcrRepository.class).withAnyArguments().thenReturn(apiSpecJcrRepository);

        mockStatic(JcrRowUtils.class);
        given(JcrRowUtils.streamOf(any())).willCallRealMethod();
        given(JcrRowUtils.getMultipleStringValueQuietly(any(), any(), any()))
            .willReturn(asList("api-spec-current-node-id", "api-spec-a-handle-node-id"));
    }

    // Invoked from the test-specific crisp-spring-context-properties-support.xml
    // For some reason this can't be extracted to be used by multiple test files.
    // If the namespace changes it can't find any system properties or env vars.
    public static class DummyApplicationSecrets {

        public String getValue(final String propertyName) {
            String val = Optional.ofNullable(System.getProperty(propertyName))
                .orElse(System.getenv(propertyName));

            System.out.println("printf: " + propertyName + " " + val);

            return val;
        }

        public String getValueChained(final String propertyName) {
            String value = getValue(propertyName);

            if (StringUtils.isNotBlank(value)) {
                String valueIndirect = getValueChained(value);
                return valueIndirect;
            } else {
                return propertyName;
            }
        }

        public String getFromFile(final String propertyName) {
            return getValue(propertyName);
        }
    }
}
