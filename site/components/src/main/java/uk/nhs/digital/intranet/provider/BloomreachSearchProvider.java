package uk.nhs.digital.intranet.provider;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.and;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.components.ext.PageableFactory;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.ComponentUtils;
import uk.nhs.digital.website.beans.Blog;
import uk.nhs.digital.website.beans.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BloomreachSearchProvider {

    private static final Logger LOG = LoggerFactory
        .getLogger(BloomreachSearchProvider.class);

    private final PageableFactory pageableFactory;

    public BloomreachSearchProvider(PageableFactory pageableFactory) {
        this.pageableFactory = pageableFactory;
    }

    public Pageable<HippoBean> getBloomreachResults(String queryString,
        int pageSize, int currentPage) {
        HstQuery query = getQuery(queryString);
        try {
            final HstQueryResult execute = query.execute();
            return pageableFactory
                .createPageable(execute.getHippoBeans(), execute.getTotalSize(),
                    pageSize, currentPage);
        } catch (QueryException e) {
            LOG.error("Error running query: {}", e.getMessage());
            LOG.debug("Query exception: ", e);
            return null;
        }
    }

    private HstQuery getQuery(String queryString) {
        HstQueryBuilder queryBuilder = HstQueryBuilder.create(
            RequestContextProvider.get().getSiteContentBaseBean().getParentBean());

        addAllTypes(queryBuilder);

        List<Constraint> constraintList = new ArrayList<>();
        constraintList.add(constraint("common:searchable").equalTo(true));

        final Optional<String> optionalSearchQuery = Optional.ofNullable(queryString);
        optionalSearchQuery.ifPresent(searchQuery -> {
            String queryIncWildcards = ComponentUtils.parseAndApplyWildcards(searchQuery);
            Constraint searchStringConstraint = or(
                constraint(".").contains(searchQuery),
                constraint(".").contains(queryIncWildcards)
            );
            constraintList.add(searchStringConstraint);
        });

        return queryBuilder.where(and(constraintList.toArray(new Constraint[0])))
            .build();
    }

    /**
     * Add all document types available in intranet search
     */

    private void addAllTypes(HstQueryBuilder query) {
        query.ofTypes(
            Blog.class,
            Team.class
        );
        // TODO add Task and NewsInternal
    }
}
