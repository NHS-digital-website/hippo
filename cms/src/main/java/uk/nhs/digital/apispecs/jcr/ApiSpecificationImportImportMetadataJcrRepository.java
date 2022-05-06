package uk.nhs.digital.apispecs.jcr;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.JcrNodeUtils.getMultipleInstantPropertyQuietly;
import static uk.nhs.digital.JcrNodeUtils.getMultipleStringPropertyQuietly;
import static uk.nhs.digital.JcrNodeUtils.setMultipleInstantPropertyQuietly;
import static uk.nhs.digital.JcrNodeUtils.setMultipleStringPropertyQuietly;
import static uk.nhs.digital.apispecs.ApiSpecificationImportMetadata.Item.metadataItem;
import static uk.nhs.digital.toolbox.exception.ExceptionUtils.wrapCheckedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.JcrQueryHelper;
import uk.nhs.digital.apispecs.ApiSpecificationImportMetadata;
import uk.nhs.digital.apispecs.ApiSpecificationImportMetadata.Item;
import uk.nhs.digital.apispecs.ApiSpecificationImportMetadataRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.QueryResult;

@SuppressWarnings("PMD.TooManyStaticImports")
public class ApiSpecificationImportImportMetadataJcrRepository implements ApiSpecificationImportMetadataRepository {

    private static final Logger log = LoggerFactory.getLogger(ApiSpecificationImportImportMetadataJcrRepository.class);

    private static final String API_SPEC_METADATA_ROOT_NODE_PATH =
        "/jcr:root/hippo:configuration/hippo:modules/api-specification-sync/hippo:moduleconfig/api-specification-metadata";

    private final Session session;

    public ApiSpecificationImportImportMetadataJcrRepository(final Session session) {
        this.session = session;
    }

    @Override
    public ApiSpecificationImportMetadata findApiSpecificationImportMetadata() {
        return metadataFrom(findMetadataNode());
    }

    @Override
    public void save(final ApiSpecificationImportMetadata apiSpecificationImportMetadata) {
        log.debug("Saving API Specifications metadata: start.");

        try {
            final Node metadataNode = findMetadataNode();

            final List<Item> metadataOfSpecsActuallyProcessedInImport =
                apiSpecificationImportMetadata.items().stream().filter(Item::specExists).collect(toList());

            final List<String> jcrIds = metadataOfSpecsActuallyProcessedInImport.stream().map(Item::apiSpecJcrId).collect(toList());
            setMultipleStringPropertyQuietly(metadataNode, JcrNames.JCR_NODE_IDS.jcrName, jcrIds);

            final List<Instant> lastChangeCheckInstants = metadataOfSpecsActuallyProcessedInImport.stream().map(Item::lastChangeCheckInstant).collect(toList());
            setMultipleInstantPropertyQuietly(metadataNode, JcrNames.LAST_CHANGE_CHECK_INSTANTS.jcrName, lastChangeCheckInstants);

            wrapCheckedException(session::save);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to save API Specification Metadata at " + API_SPEC_METADATA_ROOT_NODE_PATH, e);
        }

        log.debug("Saving API Specifications metadata: done.");
    }

    private static ApiSpecificationImportMetadata metadataFrom(final Node apiSpecMetadataNode) {

        final List<String> apiSpecHandleNodeIds = getMultipleStringPropertyQuietly(apiSpecMetadataNode, JcrNames.JCR_NODE_IDS.jcrName);
        final List<Instant> lastChangeCheckInstants = getMultipleInstantPropertyQuietly(apiSpecMetadataNode, JcrNames.LAST_CHANGE_CHECK_INSTANTS.jcrName);

        if (apiSpecHandleNodeIds.size() != lastChangeCheckInstants.size()) {
            log.error("API Specification import metadata are invalid and will be discarded and recreated.");

            return ApiSpecificationImportMetadata.metadataWith(Collections.emptyList());
        }

        final List<Item> metadataItems = IntStream.range(0, apiSpecHandleNodeIds.size())
            .mapToObj(i -> metadataItem(
                apiSpecHandleNodeIds.get(i),
                lastChangeCheckInstants.get(i)
            ))
            .collect(toList());

        return ApiSpecificationImportMetadata.metadataWith(metadataItems);
    }

    private Node findMetadataNode() {

        try {
            final QueryResult queryResult = JcrQueryHelper.executeJcrXpathQuery(session, API_SPEC_METADATA_ROOT_NODE_PATH);

            final NodeIterator nodes = queryResult.getNodes();

            if (nodes.hasNext()) {
                return nodes.nextNode();
            }

            throw new RuntimeException("API Specification Metadata Root node does not exist.");

        } catch (final Exception e) {
            throw new RuntimeException(
                "Failed to retrieve API Specification Metadata node at " + API_SPEC_METADATA_ROOT_NODE_PATH,
                e
            );
        }
    }

    enum JcrNames {
        MODULECONFIG("hipposys:moduleconfig"),
        JCR_NODE_IDS("website:apiSpecHandleNodeIds"),
        LAST_CHANGE_CHECK_INSTANTS("website:lastChangeCheckInstants"),
        ;

        private final String jcrName;

        JcrNames(final String jcrName) {
            this.jcrName = jcrName;
        }
    }
}
