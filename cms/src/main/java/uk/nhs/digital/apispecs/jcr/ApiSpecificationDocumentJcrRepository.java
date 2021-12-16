package uk.nhs.digital.apispecs.jcr;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.JcrNodeUtils.streamOf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.ApiSpecificationDocumentRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;

import java.util.List;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

public class ApiSpecificationDocumentJcrRepository implements ApiSpecificationDocumentRepository {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecificationDocumentJcrRepository.class);

    private static final String WEBSITE_DOCS_FOLDER_JCR_PATH = "/jcr:root/content/documents/corporate-website";
    private static final String APISPEC_HIPPO_HANDLE_NODE_PREDICATE = "//element(*, website:apispecification)/..[@jcr:primaryType='hippo:handle']";
    private static final String QUERY_APISPEC_HANDLE_NODES_IN_WEBSITE_DOCS_FOLDER = WEBSITE_DOCS_FOLDER_JCR_PATH + APISPEC_HIPPO_HANDLE_NODE_PREDICATE;

    private final Session session;


    public ApiSpecificationDocumentJcrRepository(final Session session) {
        this.session = session;
    }

    @Override
    public List<ApiSpecificationDocument> findAllApiSpecifications() {

        try {
            log.debug("Looking for Api Specifications...");

            final QueryResult cmsSpecsHandleNodes = findApiSpecificationsHandleNodes();

            final List<ApiSpecificationDocument> apiSpecificationDocuments = createApiSpecificationsFrom(cmsSpecsHandleNodes.getNodes());

            log.debug("Found {} Api Specifications.", apiSpecificationDocuments.size());

            return unmodifiableList(apiSpecificationDocuments);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to find API Specification documents.", e);
        }
    }

    private List<ApiSpecificationDocument> createApiSpecificationsFrom(final NodeIterator cmsSpecsHandleNodes) {

        return streamOf(cmsSpecsHandleNodes)
            .map(JcrDocumentLifecycleSupport::from)
            .map(ApiSpecificationDocument::from)
            .collect(toList());
    }

    private QueryResult findApiSpecificationsHandleNodes() {

        return executeJcrXpathQueryForQueryResult(session, QUERY_APISPEC_HANDLE_NODES_IN_WEBSITE_DOCS_FOLDER);
    }

    public static QueryResult executeJcrXpathQueryForQueryResult(final Session session, final String queryStatement) {
        try {
            return session
                .getWorkspace()
                .getQueryManager()
                .createQuery(queryStatement, Query.XPATH)
                .execute();

        } catch (final Exception e) {
            throw new RuntimeException(format("Failed to execute query %s.", queryStatement), e);
        }
    }
}
