package uk.nhs.digital.apispecs.jcr;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.JcrRowUtils.getMultipleStringValueQuietly;
import static uk.nhs.digital.JcrRowUtils.streamOf;
import static uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository.JcrNames.JCR_PATH_IDS;
import static uk.nhs.digital.apispecs.jcr.ApiSpecificationDocumentJcrRepository.JcrNames.SPECIFICATION_ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.JcrQueryHelper;
import uk.nhs.digital.apispecs.ApiSpecificationDocumentRepository;
import uk.nhs.digital.apispecs.model.ApiSpecificationDocument;
import uk.nhs.digital.apispecs.model.SpecificationSyncData;

import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;

@SuppressWarnings("PMD.TooManyStaticImports")
public class ApiSpecificationDocumentJcrRepository implements ApiSpecificationDocumentRepository {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecificationDocumentJcrRepository.class);

    private static final String WEBSITE_DOCS_FOLDER_JCR_PATH = "/jcr:root/content/documents/corporate-website";
    private static final String API_SPEC_HANDLE_NODE_PREDICATE_FORMAT = "//element(*, website:apispecification)/..[@jcr:primaryType='hippo:handle' and @jcr:uuid='%s']";
    private static final String API_SPEC_HANDLE_NODE_QUERY_FORMAT = WEBSITE_DOCS_FOLDER_JCR_PATH + API_SPEC_HANDLE_NODE_PREDICATE_FORMAT;
    private static final String INITIAL_API_SPEC_SYNC_DATA_PATH = "//element(*, website:apispecification)[hippostd:state = 'draft']/(@website:specification_id union @hippo:paths)";
    private static final String INITIAL_SYNC_DATA_FOR_ALL_API_SPECS_QUERY = WEBSITE_DOCS_FOLDER_JCR_PATH + INITIAL_API_SPEC_SYNC_DATA_PATH;
    private static final int API_SPEC_HANDLE_PARENT_NODE_INDEX = 1;

    private final Session session;

    public ApiSpecificationDocumentJcrRepository(final Session session) {
        this.session = session;
    }

    @Override
    public List<SpecificationSyncData> findInitialSyncDataForAllApiSpecifications() {
        try {
            log.debug("Looking for initial sync data for all API specifications...");

            final QueryResult result = executeInitialSyncDataQuery();

            final String defaultNodeSelectorName = getDefaultNodeSelectorName(result);

            final List<SpecificationSyncData> initialSyncDataForAllApiSpecs =
                streamOf(result.getRows())
                    .map(row -> getInitialSyncData(row, defaultNodeSelectorName))
                    .collect(toList());

            log.debug("Found sync data for {} API Specifications.", initialSyncDataForAllApiSpecs.size());

            return unmodifiableList(initialSyncDataForAllApiSpecs);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to find initial sync data for all API specifications at "
                + INITIAL_SYNC_DATA_FOR_ALL_API_SPECS_QUERY,
                e);
        }
    }

    @Override
    public ApiSpecificationDocument findApiSpecification(String specHandleNodeId) {

        try {
            log.debug("Looking for API Specification handle node with id "
                + specHandleNodeId);

            final Node apiSpecHandleNode = findApiSpecificationHandleNode(specHandleNodeId);

            log.debug("Found API Specification document.");

            return createApiSpecificationFrom(apiSpecHandleNode);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to find API Specification document "
                + specHandleNodeId, e);
        }
    }

    /**
     * We use an XPath query to avoid loading in the whole node including
     * the substantial content of the spec's JSON into the JCR bundle cache.
     * A SQL2 query would have been preferred given XPath query language is
     * depreciated as of JCR 2.0; however, SQL2 was found to load the entire
     * node whereas XPath only loaded the required initial sync data.
     */
    private QueryResult executeInitialSyncDataQuery() {
        return JcrQueryHelper.executeJcrXpathQuery(session, INITIAL_SYNC_DATA_FOR_ALL_API_SPECS_QUERY);
    }

    /**
     * As the XPath query executed does not use selectors to identify the
     * nodes (unlike SQL based queries), and each row only corresponds to one node,
     * the node selector name should default to "s".
     * This is required to get an array value type from the result table using our
     * own custom method getMultipleStringValueQuietly(...) without resolving the
     * entire node, as the row.getValue() method only supports getting single value
     * types.
     */
    public String getDefaultNodeSelectorName(QueryResult result) {
        try {
            return result.getSelectorNames()[0];
        } catch (Exception e) {
            throw new RuntimeException("Failed to get default selector name used in the query to identify nodes", e);
        }
    }

    private SpecificationSyncData getInitialSyncData(Row row, String defaultNodeSelectorName) {
        try {
            String specificationId = row.getValue(SPECIFICATION_ID.jcrName())
                .getString();

            List<String> jcrPathIds = getMultipleStringValueQuietly(row,
                defaultNodeSelectorName, JCR_PATH_IDS.jcrName());

            String draftSpecJcrPath = row.getPath();

            if (specificationId != null && jcrPathIds != null && draftSpecJcrPath != null) {
                String apiSpecHandleNodeId = jcrPathIds.get(API_SPEC_HANDLE_PARENT_NODE_INDEX);
                return SpecificationSyncData.with(
                    specificationId,
                    apiSpecHandleNodeId,
                    draftSpecJcrPath
                );
            }

            throw new RuntimeException("Data for row in query result table not available");

        } catch (final Exception e) {
            throw new RuntimeException(
                "Failed to retrieve initial sync data for API specification",
                e);
        }
    }

    private Node findApiSpecificationHandleNode(String specHandleNodeId) {
        final String apiSpecHandleNodeQuery = String.format(
            API_SPEC_HANDLE_NODE_QUERY_FORMAT,
            specHandleNodeId
        );

        try {
            final QueryResult result = JcrQueryHelper.executeJcrXpathQuery(
                session,
                apiSpecHandleNodeQuery
            );

            final NodeIterator nodes = result.getNodes();

            if (nodes.hasNext()) {
                return nodes.nextNode();
            }

            throw new RuntimeException(String.format(
                "API specification handle node with id %s does not exist.",
                specHandleNodeId));

        } catch (final Exception e) {
            throw new RuntimeException(
                "Failed to retrieve API specification handle node at " + apiSpecHandleNodeQuery,
                e
            );
        }
    }

    private ApiSpecificationDocument createApiSpecificationFrom(final Node apiSpecsHandleNode) {
        final JcrDocumentLifecycleSupport jcrDocumentLifecycleSupport =
            JcrDocumentLifecycleSupport.from(apiSpecsHandleNode);

        return ApiSpecificationDocument.from(jcrDocumentLifecycleSupport);
    }

    enum JcrNames {
        SPECIFICATION_ID("website:specification_id"),
        JCR_PATH_IDS("hippo:paths");

        private final String jcrName;

        JcrNames(final String jcrName) {
            this.jcrName = jcrName;
        }

        public String jcrName() {
            return jcrName;
        }
    }
}
