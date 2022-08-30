package uk.nhs.digital.apispecs.jcr;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static uk.nhs.digital.test.util.MockJcrRepoProvider.initJcrRepoFromYaml;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.hippoecm.repository.util.JcrUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.JcrRowUtils;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.SpecificationSyncData;
import uk.nhs.digital.test.util.JcrTestUtils;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.jcr.Node;
import javax.jcr.Session;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JcrRowUtils.class)
public class ApiSpecificationDocumentJcrRepositoryQueryTest {

    private static final String TEST_SPEC_ID_A = "api-specification-id-a";
    private static final String TEST_SPEC_ID_B = "api-specification-id-b";
    private static final String TEST_DRAFT_SPEC_PATH_A = "/content/documents/corporate-website/api-specifications-location-a/api-spec-a/api-spec-a";
    private static final String TEST_DRAFT_SPEC_PATH_B = "/content/documents/corporate-website/api-specifications-location-b/api-spec-b/api-spec-b";
    private static final String TEST_INITIAL_SYNC_DATA_QUERY =
        "/jcr:root/content/documents/corporate-website//element(*, website:apispecification)[hippostd:state = 'draft']/(@website:specification_id union @hippo:paths)";
    private static final String TEST_HANDLE_NODE_PATH_A = "/content/documents/corporate-website/api-specifications-location-a/api-spec-a";
    private static final String TEST_HANDLE_NODE_PATH_B = "/content/documents/corporate-website/api-specifications-location-b/api-spec-b";
    private static final String TEST_HANDLE_NODE_ID_A = "api-spec-a-id";
    private static final String TEST_HANDLE_NODE_ID_B = "api-spec-b-id";
    private static final String TEST_JSON_SPEC_A = "{ \"json\" : \"payload A on PUBLISHED\" }";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ApiSpecificationDocumentJcrRepository apiSpecJcrRepository;

    private Session session;

    @Before
    public void setUp() {

        session = MockJcr.newSession();

        apiSpecJcrRepository = spy(new ApiSpecificationDocumentJcrRepository(session));

    }

    @Test
    public void findInitialSyncDataForAllApiSpecifications_returnsFullyPopulatedSyncData() {

        // given
        queryReturnsDraftApiSpecsPresentInTheSystem();

        stubSelectorNameCalls();

        // when
        final SpecificationSyncData initialSyncDataForApiSpec = apiSpecJcrRepository.findInitialSyncDataForAllApiSpecifications().get(0);

        // then
        assertThat("Sync data contains specification id as present on draft API spec variant in the JCR repo.",
            initialSyncDataForApiSpec.specificationId(),
            is(TEST_SPEC_ID_A)
        );

        assertThat("Sync data contains the JCR path as present on draft API spec variant in the JCR repo.",
            initialSyncDataForApiSpec.specJcrPath(),
            is(TEST_DRAFT_SPEC_PATH_A)
        );

        assertThat("Sync data contains the JCR ID of the API spec's parent handle node in the JCR repo.",
            initialSyncDataForApiSpec.specJcrHandleNodeId(),
            is(TEST_HANDLE_NODE_ID_A)
        );
    }

    @Test
    public void findInitialSyncDataForAllApiSpecifications_returnsAllSpecificationsPresentInTheSystem() {

        // given
        queryReturnsDraftApiSpecsPresentInTheSystem();

        stubSelectorNameCalls();

        // when
        final List<SpecificationSyncData> initialSyncDataForApiSpec = apiSpecJcrRepository.findInitialSyncDataForAllApiSpecifications();

        // then
        assertThat("All specifications present in the system have been found and returned",
            initialSyncDataForApiSpec.size(),
            is(2)
        );

        assertThat("First spec result corresponds to one of the specifications in JCR.",
            initialSyncDataForApiSpec.get(0).specificationId(),
            is(TEST_SPEC_ID_A)
        );

        assertThat("Second spec result corresponds to the other specifications in JCR.",
            initialSyncDataForApiSpec.get(1).specificationId(),
            is(TEST_SPEC_ID_B)
        );
    }

    @Test
    public void findInitialSyncDataForAllApiSpecifications_returnsEmptyCollectionWhenNoApiSpecificationsFound() {

        // given
        noApiSpecificationsPresentInTheSystem();

        stubSelectorNameCalls();

        // when
        final List<SpecificationSyncData> initialSyncDataForApiSpec = apiSpecJcrRepository.findInitialSyncDataForAllApiSpecifications();

        // then
        assertThat("No specifications were found and none was returned",
            initialSyncDataForApiSpec,
            is(emptyList())
        );
    }

    @Test
    public void findInitialSyncDataForAllApiSpecifications_usesJcrQueryTargetingApiSpecificationDocsAnywhereWithinCorpWebsiteFolder() {

        // given
        final AtomicReference<String> actualQuery = givenJcrRepoCanAcceptQueries();

        stubSelectorNameCalls();

        // when
        apiSpecJcrRepository.findInitialSyncDataForAllApiSpecifications();

        // then
        assertThat(
            "Query targets the draft variant nodes of documents of type 'website:apispecification' under the Corporate Website folder",
            actualQuery.get(),
            is(TEST_INITIAL_SYNC_DATA_QUERY)
        );
    }

    @Test
    public void findInitialSyncDataForAllApiSpecifications_throwsExceptionOnFailureToFindData() {

        // given
        session = mock(Session.class);
        given(session.getWorkspace()).willThrow(new RuntimeException());

        expectedException.expectMessage("Failed to find initial sync data for all API specifications at "
            + TEST_INITIAL_SYNC_DATA_QUERY);
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(notNullValue(Exception.class));

        apiSpecJcrRepository = new ApiSpecificationDocumentJcrRepository(session);

        // when
        apiSpecJcrRepository.findInitialSyncDataForAllApiSpecifications();

        // then
        // expectations set up in 'given' are satisfied
    }

    @Test
    public void findApiSpecification_returnsPopulatedDocumentForTheGivenSpecHandleNodeId() {

        // given
        queryReturnsApiSpecHandleNodePresentInTheSystem(TEST_HANDLE_NODE_PATH_A);

        final Optional<String> jsonInJcr = Optional.of(TEST_JSON_SPEC_A);

        // when
        final ApiSpecificationDocument actualApiSpec = apiSpecJcrRepository.findApiSpecification(TEST_HANDLE_NODE_ID_A);

        // then
        assertThat("Document contains JSON as present on published variant in the JCR repo.",
            actualApiSpec.json(),
            is(jsonInJcr)
        );
    }

    @Test
    public void findApiSpecification_instantiatesSpecDocumentInitialisedWithJcrHandleNode() throws Exception {

        // given
        queryReturnsApiSpecHandleNodePresentInTheSystem(TEST_HANDLE_NODE_PATH_B);

        // when
        final ApiSpecificationDocument actualApiSpec = apiSpecJcrRepository.findApiSpecification(TEST_HANDLE_NODE_ID_B);

        // then
        final Node actualDocumentHandleNode = rootNodeFrom(actualApiSpec);

        assertThat("Returned spec initialised with node with correct path",
            actualDocumentHandleNode.getPath(),
            is(TEST_HANDLE_NODE_PATH_B)
        );
        assertThat("Returned spec initialised with node of correct type",
            JcrUtils.getStringProperty(actualDocumentHandleNode, "jcr:primaryType", "n/a"),
            is("hippo:handle")
        );
    }

    @Test
    public void findApiSpecification_throwsExceptionWhenNoApiSpecificationFoundForGivenHandleNodeId() {

        // given
        noApiSpecificationsPresentInTheSystem();

        expectedException.expectMessage("Failed to find API Specification document incorrect-spec-handle-node-id");
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(notNullValue(Exception.class));

        // when
        apiSpecJcrRepository.findApiSpecification("incorrect-spec-handle-node-id");

        // then
        // expectations set up in 'given' are satisfied
    }

    // Just asserting the query itself, will throw as no API spec found
    @Test(expected = RuntimeException.class)
    public void findApiSpecification_usesJcrQueryTargetingApiSpecificationDocAnywhereWithinCorpWebsiteFolder() {

        // given
        final AtomicReference<String> actualQuery = givenJcrRepoCanAcceptQueries();

        // when
        apiSpecJcrRepository.findApiSpecification(TEST_HANDLE_NODE_ID_A);

        // then
        assertThat(
            "Query targets handle node of documents of type 'website:apispecification' under Corporate Website folder",
            actualQuery.get(),
            is("/jcr:root/content/documents/corporate-website//element(*, website:apispecification)/..[@jcr:primaryType='hippo:handle' and @jcr:uuid='"
                + TEST_HANDLE_NODE_ID_A
                + "']")
        );
    }

    @Test
    public void findApiSpecification_throwsExceptionOnFailureToFindDocument() {

        // given
        session = mock(Session.class);
        given(session.getWorkspace()).willThrow(new RuntimeException());

        expectedException.expectMessage("Failed to find API Specification document " + TEST_HANDLE_NODE_ID_A);
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(notNullValue(Exception.class));

        apiSpecJcrRepository = new ApiSpecificationDocumentJcrRepository(session);

        // when
        apiSpecJcrRepository.findApiSpecification(TEST_HANDLE_NODE_ID_A);

        // then
        // expectations set up in 'given' are satisfied
    }

    private void noApiSpecificationsPresentInTheSystem() {
        MockJcr.setQueryResult(session, emptyList()); // JCR returns zero matches to any query
    }

    private AtomicReference<String> givenJcrRepoCanAcceptQueries() {

        final AtomicReference<String> actualQuery = new AtomicReference<>();

        MockJcr.addQueryResultHandler(session, query -> {
            actualQuery.set(query.getStatement());   // intercept actual query
            return new MockQueryResult(emptyList()); // results don't matter in this test but have to return something
        });

        return actualQuery;
    }

    private Node rootNodeFrom(final ApiSpecificationDocument apiSpecification) {
        Object cmsDocumentProxy = ReflectionTestUtils.readField(apiSpecification, "jcrDocumentLifecycleSupport");
        return ReflectionTestUtils.readField(cmsDocumentProxy, "documentHandleNode");
    }

    private void queryReturnsDraftApiSpecsPresentInTheSystem() {

        initJcrRepoFromYaml(session, "/test-data/api-specifications/ApiSpecRepositoryQueryTest/api-specifications.yml");

        final Node rootNode = JcrTestUtils.getRootNode(session);

        final List<Node> resultNodes = asList(
            JcrTestUtils.getRelativeNode(rootNode, TEST_DRAFT_SPEC_PATH_A),
            JcrTestUtils.getRelativeNode(rootNode, TEST_DRAFT_SPEC_PATH_B)
        );

        final List<String> resultColumnNames = asList(
            "website:specification_id", "hippo:paths");

        MockJcr.addQueryResultHandler(session, query -> new MockQueryResult(resultNodes, resultColumnNames));
    }

    private void queryReturnsApiSpecHandleNodePresentInTheSystem(String handleNodeJcrPath) {

        initJcrRepoFromYaml(session, "/test-data/api-specifications/ApiSpecRepositoryQueryTest/api-specifications.yml");

        final Node rootNode = JcrTestUtils.getRootNode(session);

        MockJcr.setQueryResult(session, Collections.singletonList(
            JcrTestUtils.getRelativeNode(rootNode, handleNodeJcrPath)
        ));
    }

    /**
     * As the MockQueryResult object does not support the getSelectorNames() method
     * and the result nodes do not seem to have a selector name concept, we must
     * stub out anything that requires the selector name
     */
    private void stubSelectorNameCalls() {
        doReturn("selectorName").when(apiSpecJcrRepository).getDefaultNodeSelectorName(any());

        mockStatic(JcrRowUtils.class);
        given(JcrRowUtils.streamOf(any())).willCallRealMethod();
        given(JcrRowUtils.getMultipleStringValueQuietly(any(), any(), any()))
            .willReturn(asList("api-spec-current-node-id", TEST_HANDLE_NODE_ID_A));
    }
}