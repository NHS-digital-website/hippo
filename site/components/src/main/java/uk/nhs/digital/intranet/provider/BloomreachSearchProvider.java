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
import org.hippoecm.repository.util.DateTools.Resolution;
import org.onehippo.cms7.essentials.components.ext.PageableFactory;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.ComponentUtils;
import uk.nhs.digital.intranet.enums.SearchTypes;
import uk.nhs.digital.website.beans.Team;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class BloomreachSearchProvider {
    private static final Logger LOG = LoggerFactory
        .getLogger(BloomreachSearchProvider.class);

    private final PageableFactory pageableFactory;

    public BloomreachSearchProvider(PageableFactory pageableFactory) {
        this.pageableFactory = pageableFactory;
    }

    public Pageable<HippoBean> getBloomreachResults(String queryString, int pageSize, int currentPage,
            List<SearchTypes> searchAreas, List<Team> teams, List<String> years, List<String> taxonomies, String sort) {
        HstQuery query = getQuery(queryString, searchAreas, teams, years, taxonomies, sort);
        query.setLimit(pageSize);
        query.setOffset((currentPage - 1) * pageSize);
        if (queryString == null) {
            query.addOrderByDescending("hippostdpubwf:lastModificationDate");
        }
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

    private HstQuery getQuery(String queryString, List<SearchTypes> searchAreas,
            List<Team> teams, List<String> years, List<String> taxonomies, String sort) {
        List<Constraint> constraintList = new ArrayList<>();
        constraintList.add(
            or(
                constraint("common:searchable").equalTo(true),
                constraint("common:searchable").notExists()
            )
        );

        if (teams != null) {
            ArrayList<Constraint> teamConstraints = new ArrayList<>();
            for (Team team : teams) {
                teamConstraints.add(constraint("intranet:relateddocuments/hippo:docbase").equalTo(team.getCanonicalHandleUUID()));
                teamConstraints.add(constraint("hippo:paths").equalTo(team.getCanonicalHandleUUID()));
            }

            constraintList.add(or(teamConstraints.toArray(new Constraint[0])));
        }

        if (years != null) {
            ArrayList<Constraint> yearConstraints = new ArrayList<>();
            for (String year : years) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(year));
                yearConstraints.add(constraint("intranet:publicationdate").equalTo(calendar, Resolution.YEAR));
            }

            constraintList.add(or(yearConstraints.toArray(new Constraint[0])));
        }

        if (taxonomies != null) {
            ArrayList<Constraint> taxonomyConstraints = new ArrayList<>();
            for (String taxonomy : taxonomies) {
                taxonomyConstraints.add(constraint("common:FullTaxonomy").equalTo(taxonomy));
            }

            constraintList.add(or(taxonomyConstraints.toArray(new Constraint[0])));
        }

        if (searchAreas != null && searchAreas.contains(SearchTypes.NEWS)) {
            constraintList.add(
                or(
                    constraint("intranet:typeofnews").notExists(),
                    constraint("intranet:typeofnews").equalTo("permanent"),
                    constraint("intranet:expirydate")
                        .greaterOrEqualThan(Calendar.getInstance(), Resolution.HOUR)
                )
            );
        }

        final Optional<String> optionalSearchQuery = Optional.ofNullable(queryString);
        optionalSearchQuery.ifPresent(searchQuery -> {
            String queryIncWildcards = ComponentUtils.parseAndApplyWildcards(searchQuery);
            Constraint searchStringConstraint = or(
                constraint(".").contains(searchQuery),
                constraint(".").contains(queryIncWildcards)
            );
            constraintList.add(searchStringConstraint);
        });

        HstQueryBuilder queryBuilder = HstQueryBuilder.create(
            RequestContextProvider.get().getSiteContentBaseBean());

        List<Class> docTypesClass = SearchTypes.getDocTypesFromSearchTypes(searchAreas);
        Class[] docTypes = docTypesClass.toArray(new Class[0]);
        queryBuilder.ofTypes(docTypes);

        queryBuilder.where(and(constraintList.toArray(new Constraint[0])));

        if (sort != null) {
            if (sort.equals("date-asc")) {
                queryBuilder.orderByAscending("intranet:publicationdate", "hippostdpubwf:lastModificationDate");
            } else if (sort.equals("date-desc")) {
                queryBuilder.orderByDescending("intranet:publicationdate", "hippostdpubwf:lastModificationDate");
            }
        }

        return queryBuilder.build();
    }
}
