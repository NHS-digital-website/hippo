package uk.nhs.digital.apispecs.jcr;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.MockJcrRepoProvider.initJcrRepoFromYaml;

import org.apache.sling.testing.mock.jcr.MockJcr;
import org.apache.sling.testing.mock.jcr.MockQueryResult;
import org.hippoecm.repository.util.JcrUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.test.util.JcrTestUtils;
import uk.nhs.digital.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.jcr.Node;
import javax.jcr.Session;


public class ApiSpecificationDocumentJcrRepositoryQueryTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private ApiSpecificationDocumentJcrRepository apiSpecJcrRepository;

    private Session session;

    @Before
    public void setUp() {

        session = MockJcr.newSession();

        apiSpecJcrRepository = new ApiSpecificationDocumentJcrRepository(session);
    }

    @Test
    public void findAllApiSpecifications_returnsFullyPopulatedDocument() {

        // given
        apiSpecsPresentInTheSystem();

        final String specificationIdInJcr = "api-specification-id-a";
        final Optional<String> jsonInJcr = Optional.of("{ \"json\" : \"payload A on PUBLISHED\" }");
        final Optional<String> htmlInJcr = Optional.of("<p>API Specification A on PUBLISHED</p>");
        final Optional<Instant> lastPublicationInstantInJcr = Optional.of(Instant.parse("2020-07-08T08:37:03.915Z"));

        // when
        final ApiSpecificationDocument actualApiSpec = apiSpecJcrRepository.findAllApiSpecifications().get(0);

        // then
        assertThat("Document contains specification id as present on draft variant in the JCR repo.",
            actualApiSpec.specificationId(),
            is(specificationIdInJcr)
        );

        assertThat("Document contains JSON as present on published variant in the JCR repo.",
            actualApiSpec.json(),
            is(jsonInJcr)
        );

        assertThat("Document contains HTML as present on published variant in the JCR repo.",
            actualApiSpec.html(),
            is(htmlInJcr)
        );

        assertThat("Document contains last publication instant as present on published variant in the JCR repo.",
            actualApiSpec.lastPublicationInstant(),
            is(lastPublicationInstantInJcr)
        );
    }

    @Test
    public void findAllApiSpecifications_returnsAllSpecificationsPresentInTheSystem() {

        // given
        apiSpecsPresentInTheSystem();

        // when
        final List<ApiSpecificationDocument> actualApiSpecs = apiSpecJcrRepository.findAllApiSpecifications();

        // then
        assertThat("All specifications present in the system have been found and returned",
            actualApiSpecs.size(),
            is(2)
        );

        assertThat("First document corresponds to one of the specifications in JCR.",
            actualApiSpecs.get(0).specificationId(),
            is("api-specification-id-a")
        );

        assertThat("Second document corresponds to the other specifications in JCR.",
            actualApiSpecs.get(1).specificationId(),
            is("api-specification-id-b")
        );
    }

    @Test
    public void findAllApiSpecifications_instantiatesSpecDocumentsInitialisedWithJcrHandleNode() throws Exception {

        // given
        apiSpecsPresentInTheSystem();

        // when
        final List<ApiSpecificationDocument> actualApiSpecs = apiSpecJcrRepository.findAllApiSpecifications();

        // then
        final List<Node> actualDocumentHandleNodes = rootNodesFrom(actualApiSpecs);

        final Node firstDocumentHandleNode = actualDocumentHandleNodes.get(0);
        assertThat("First returned spec: initialised with node with correct path",
            firstDocumentHandleNode.getPath(),
            is("/content/documents/corporate-website/api-specifications-location-a/api-spec-a")
        );
        assertThat("First returned spec: initialised with node of correct type",
            JcrUtils.getStringProperty(firstDocumentHandleNode, "jcr:primaryType", "n/a"),
            is("hippo:handle")
        );

        final Node secondDocumentHandleNode = actualDocumentHandleNodes.get(1);
        assertThat("Second returned spec: initialised with node with correct path",
            secondDocumentHandleNode.getPath(),
            is("/content/documents/corporate-website/api-specifications-location-b/api-spec-b")
        );
        assertThat("Second returned spec: initialised with node of correct type",
            JcrUtils.getStringProperty(secondDocumentHandleNode, "jcr:primaryType", "n/a"),
            is("hippo:handle")
        );
    }

    @Test
    public void findAllApiSpecifications_returnsEmptyCollectionWhenNoApiSpecificationsFound() {

        // given
        noApiSpecificationsPresentInTheSystem();

        // when
        final List<ApiSpecificationDocument> actualApiSpecs = apiSpecJcrRepository.findAllApiSpecifications();

        // then
        assertThat("No specifications were found and none was returned",
            actualApiSpecs,
            is(empty())
        );
    }

    @Test
    public void findAllApiSpecifications_usesJcrQueryTargetingApiSpecificatinDocsAnywhereWithinCorpWebsiteFolder() {

        // given
        final AtomicReference<String> actualQuery = givenJcrRepoCanAcceptQueries();

        // when
        apiSpecJcrRepository.findAllApiSpecifications();

        // then
        assertThat(
            "Query targets handle nodes of documents of type 'website:apispecification' under Corporate Website folder",
            actualQuery.get(),
            is("/jcr:root/content/documents/corporate-website//element(*, website:apispecification)/..[@jcr:primaryType='hippo:handle']")
        );
    }

    @Test
    public void findAllApiSpecifications_throwsExceptionOnFailureToFindDocuments() {

        // given
        session = mock(Session.class);
        given(session.getWorkspace()).willThrow(new RuntimeException());

        expectedException.expectMessage("Failed to find API Specification documents.");
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(notNullValue(Exception.class));

        apiSpecJcrRepository = new ApiSpecificationDocumentJcrRepository(session);

        // when
        apiSpecJcrRepository.findAllApiSpecifications();

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

    private List<Node> rootNodesFrom(final List<ApiSpecificationDocument> actualApiSpecs) {

        return actualApiSpecs.stream()
            .map(apiSpecification -> ReflectionTestUtils.readField(apiSpecification, "jcrDocumentLifecycleSupport"))
            .map(cmsDocumentProxy -> ReflectionTestUtils.readField(cmsDocumentProxy, "documentHandleNode"))
            .map(object -> (Node) object)
            .sorted(comparing(JcrUtils::getNodePathQuietly))
            .collect(toList());
    }

    private void apiSpecsPresentInTheSystem() {

        initJcrRepoFromYaml(session, "/test-data/api-specifications/ApiSpecRepositoryQueryTest/api-specifications.yml");

        final Node rootNode = JcrTestUtils.getRootNode(session);

        MockJcr.setQueryResult(session, asList(
            JcrTestUtils.getRelativeNode(rootNode, "/content/documents/corporate-website/api-specifications-location-a/api-spec-a"),
            JcrTestUtils.getRelativeNode(rootNode, "/content/documents/corporate-website/api-specifications-location-b/api-spec-b")
        ));
    }
}