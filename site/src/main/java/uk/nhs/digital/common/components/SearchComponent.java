package uk.nhs.digital.common.components;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.*;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoResultSetBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.nil.beans.Indicator;
import uk.nhs.digital.ps.beans.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * We are not extending "EssentialsSearchComponent" because we could not find a elegant way of using our own search
 * HstObject in the faceted search.
 */
@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends CommonComponent {

    private static final String WILDCARD_IN_USE_CHAR = "*";
    private static final int WILDCARD_POSTFIX_MIN_LENGTH = 3;

    private static final String REQUEST_PARAM_SORT = "sort";
    private static final String SORT_RELEVANCE = "relevance";
    private static final String SORT_DATE = "date";
    private static final String SORT_DEFAULT = SORT_DATE;

    private static final int PAGEABLE_SIZE = 5;

    private static final Logger log = LoggerFactory.getLogger(SearchComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        EssentialsListComponentInfo paramInfo = getComponentInfo(request);
        HippoFacetNavigationBean facetNavigationBean = getFacetNavigationBean(request);

        if (facetNavigationBean != null) {
            HippoResultSetBean resultSet = facetNavigationBean.getResultSet();
            Pageable<HippoBean> pageable = getPageableFactory()
                .createPageable(
                    resultSet.getDocumentIterator(HippoBean.class),
                    facetNavigationBean.getCount().intValue(),
                    paramInfo.getPageSize(),
                    getCurrentPage(request)
                );

            request.setAttribute("pageable", pageable);
            request.setAttribute("pageNumbers", getPageNumbers(pageable));
        }

        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("facets", facetNavigationBean);
        request.setAttribute("sort", getSortOption(request));
        request.setAttribute("cparam", paramInfo);
    }

    protected List<Integer> getPageNumbers(Pageable<HippoBean> pageable) {
        int currentPage = pageable.getCurrentPage();
        int buffer = (PAGEABLE_SIZE - 1) / 2;
        int end = min((int) pageable.getTotalPages(), max(currentPage + buffer, PAGEABLE_SIZE));
        int start = max(1, end - PAGEABLE_SIZE + 1);

        return IntStream.rangeClosed(start, end)
            .boxed()
            .collect(toList());
    }

    protected HippoFacetNavigationBean getFacetNavigationBean(HstRequest request) {
        // If we went to a random URL and are on the 404 error page we won't have
        // a relative content path to get the facets from so return null
        String relativeContentPath = request.getRequestContext()
            .getResolvedSiteMapItem()
            .getRelativeContentPath();
        if (relativeContentPath == null) {
            return null;
        }

        return ContentBeanUtils.getFacetNavigationBean(buildQuery(request));
    }

    protected String getQueryParameter(HstRequest request) {
        return getAnyParameter(request, REQUEST_PARAM_QUERY);
    }

    /**
     * Convert query to include wildcard (*) character after each search term.
     * If the term is a phrase, the search is for an exact match, so no wildcard is required,
     * but the escaped backslash must be removed for the results to be accurate.
     * If the term ends with fullstop or comma, do not apply wildcard
     * <p>
     * e.g. lorem ipsum                 =>      lorem* ipsum*
     * "dolor sit" lorem ipsum     =>      "dolor sit" lorem* ipsum*
     * lor ipsum                   =>      lor* ipsum*
     */
    protected String parseAndApplyWildcards(String query) {

        ArrayList<String> terms = new ArrayList<>();

        // Split terms by space, treat phrases (wrapped in double quotes) as a single term
        Matcher matcher = Pattern.compile("(\"[^\"]*\")|([^\\s\"]+)").matcher(query);
        while (matcher.find()) {
            String term = null;
            if (matcher.group(1) != null) {
                // phrase that was quoted so add without wild card
                term = matcher.group(1);
            } else if (matcher.group(2) != null) {
                term = matcher.group(2);

                if (term.length() >= WILDCARD_POSTFIX_MIN_LENGTH
                    && !term.endsWith(".")
                    && !term.endsWith(",")) {
                    // single unquoted word so add wildcard
                    term = matcher.group(2) + WILDCARD_IN_USE_CHAR;
                }
            }

            terms.add(term);
        }

        query = String.join(" ", terms);

        // Escape any special chars but not the quotes, we have already dealt with those
        // Do this last to properly sanitize any input
        return SearchInputParsingUtils.parse(query, true, new char[]{'"'});
    }


    protected int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    protected EssentialsListComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }

    private HstQuery buildQuery(HstRequest request) {
        String queryParameter = getQueryParameter(request);
        Constraint searchStringConstraint = null;

        HstQueryBuilder queryBuilder = HstQueryBuilder.create(request.getRequestContext().getSiteContentBaseBean());

        if (queryParameter != null) {
            String query = SearchInputParsingUtils.parse(queryParameter, true);
            String queryIncWildcards = parseAndApplyWildcards(queryParameter);

            searchStringConstraint = or(
                commonConstraint(query),
                commonConstraint(queryIncWildcards),
                publicationSystemConstraint(query),
                publicationSystemConstraint(queryIncWildcards),
                indicatorLibraryConstraint(query),
                indicatorLibraryConstraint(queryIncWildcards),
                constraint(".").contains(query),
                constraint(".").contains(queryIncWildcards),
                alphabetConstraint(query),
                alphabetConstraint(queryIncWildcards)
            );
        }

        // register content classes
        addPublicationSystemTypes(queryBuilder);
        addIndicatorLibraryTypes(queryBuilder);

        String sortParam = getSortOption(request);
        switch (sortParam) {
            case SORT_DATE:
                queryBuilder.orderByDescending("publicationsystem:NominalDate", "nationalindicatorlibrary:assuranceDate", 
                    "publicationsystem:Title", "nationalindicatorlibrary:title");
                break;
            case SORT_RELEVANCE:
                // no op - relevence is the default sort order
                break;
            default:
                log.error("Unknown sort mode: " + sortParam);
                break;
        }

        return constructQuery(queryBuilder, searchStringConstraint);
    }

    private String getSortOption(HstRequest request) {
        return Optional.ofNullable(getAnyParameter(request, REQUEST_PARAM_SORT)).orElse(SORT_DEFAULT);
    }

    private HstQuery constructQuery(HstQueryBuilder queryBuilder, Constraint searchStringConstraint) {
        ArrayList<Constraint> constraints = new ArrayList<>(2);
        constraints.add(constraint("common:searchable").equalTo(true));

        if (searchStringConstraint != null) {
            constraints.add(searchStringConstraint);
        }

        return queryBuilder.where(and(constraints.toArray(new Constraint[0]))).build();
    }

    /**
     * Publication System content types that should be included in search
     */
    private void addPublicationSystemTypes(HstQueryBuilder query) {
        query.ofTypes(
            Archive.class,
            Dataset.class,
            LegacyPublication.class,
            Publication.class,
            Series.class
        );
    }

    /**
     * National Indicator Library content type(s) that should be included in the search results
     */
    private void addIndicatorLibraryTypes(HstQueryBuilder query) {
        query.ofTypes(Indicator.class);
    }

    /**
     * Publication System search constraint
     */
    private Constraint publicationSystemConstraint(String query) {
        return or(
            constraint("publicationsystem:Title").contains(query),
            constraint("publicationsystem:Summary").contains(query),
            constraint("publicationsystem:KeyFacts").contains(query)
        );
    }

    /**
     * National Indicator Library search constraint
     */
    private Constraint indicatorLibraryConstraint(String query) {
        return or(
            constraint("nationalindicatorlibrary:title").contains(query),
            constraint("nationalindicatorlibrary:definition").contains(query),
            constraint("nationalindicatorlibrary:purpose").contains(query)
        );
    }

    /**
     * Common search constraint
     */
    private Constraint commonConstraint(String query) {
        return or(
            constraint("common:SearchableTags").contains(query)
        );
    }

    /**
     * Constraint builder for the A-Z buttons on the NI Hub
     */
    private Constraint alphabetConstraint(String query) {
        String[] queryWords = query.split(" ");
        Constraint alpha = or(
            constraint("common:FullTaxonomy").contains(queryWords[0]),
            constraint("common:SearchableTags").contains(queryWords[0])
        );
        for (int i = 1; i < queryWords.length ; i++) {
            alpha = or(
                alpha,
                constraint("common:FullTaxonomy").contains(queryWords[i]),
                constraint("common:SearchableTags").contains(queryWords[i])
            );
        }
        
        return alpha;
    }
}
