package uk.nhs.digital.common.components.apicatalogue.repository;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.repository.util.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Optional;
import javax.jcr.Node;
import javax.jcr.Session;

public class ApiCatalogueJcrRepository implements ApiCatalogueRepository {

    private static final Logger log = LoggerFactory.getLogger(ApiCatalogueJcrRepository.class);

    private final String taxonomyFiltersMappingDocumentPath;
    private final Session session;

    public ApiCatalogueJcrRepository(final Session session, final String taxonomyFiltersMappingDocumentPath) {
        this.session = session;
        this.taxonomyFiltersMappingDocumentPath = taxonomyFiltersMappingDocumentPath;
    }

    @Override
    public Optional<String> taxonomyFiltersMapping() {

        try {
            final Node mappingDocumentHandle = JcrUtils.getNodeIfExists(taxonomyFiltersMappingDocumentPath, session);

            if (mappingDocumentHandle == null) {
                log.warn("API Catalogue's taxonomy-filters mapping document not found at {}", taxonomyFiltersMappingDocumentPath);
                return Optional.empty();
            }

            final HstQueryResult hstQueryResult = queryForPublishedVariant(mappingDocumentHandle).execute();

            return Optional.of(hstQueryResult.getHippoBeans())
                .filter(Iterator::hasNext)
                .map(HippoBeanIterator::nextHippoBean)
                .map(HippoBean::getProperty)
                .map(properties -> properties.get("website:text"))
                .filter(String.class::isInstance)
                .map(String.class::cast);

        } catch (final Exception cause) {
            throw new RuntimeException(
                "Failed to retrieve taxonomy-filters mapping YAML for scope node "
                    + taxonomyFiltersMappingDocumentPath,
                cause
            );
        }
    }

    private HstQuery queryForPublishedVariant(final Node documentNode) {
        return HstQueryBuilder.create(documentNode)
            .where(constraint("hippostd:state").equalTo("published"))
            .build();
    }
}
