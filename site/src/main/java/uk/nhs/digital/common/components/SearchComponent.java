package uk.nhs.digital.common.components;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.or;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.Series;

/**
 * We are not extending "EssentialsSearchComponent" because we could not find a elegant way of using our own search
 * HstObject in the faceted search.
 */
@ParametersInfo(type = EssentialsListComponentInfo.class)
public class SearchComponent extends CommonComponent {

    private static final String WILDCARD_IN_USE_CHAR = "*";
    private static final String WILDCARD_NOT_IN_USE_CHAR = "?";
    private static final int WILDCARD_POSTFIX_MIN_LENGTH = 3;

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        Pageable<HippoBean> pageable;
        EssentialsListComponentInfo paramInfo = getComponentInfo(request);
        HippoResultSetBean resultSet = getFacetNavigationBean(request).getResultSet();

        pageable = getPageableFactory()
            .createPageable(
                resultSet.getDocumentIterator(HippoBean.class),
                resultSet.getCount().intValue(),
                paramInfo.getPageSize(),
                getCurrentPage(request)
            );

        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("pageable", pageable);
        request.setAttribute("cparam", paramInfo);
    }

    public HippoFacetNavigationBean getFacetNavigationBean(HstRequest request) {
        return ContentBeanUtils.getFacetNavigationBean(buildQuery(request));
    }

    protected String getQueryParameter(HstRequest request) {
        return SearchInputParsingUtils.parse(getAnyParameter(request, REQUEST_PARAM_QUERY), true);
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
    protected String applyWildcardsToQuery(String query) {

        String queryWithWildcards = "";

        // Split terms by space, treat phrases (wrapped in double quotes) as a single term
        String[] splitTerms = query.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (int i = 0; i <= splitTerms.length - 1; i++) {

            // The SearchInputParsingUtils.parse function escapes quote characters, but this results in the search
            // not finding that specific term (because of the back slash), therefore remove escaped back slashes
            String term = splitTerms[i].replace("\\", "");

            // WILDCARD_POSTFIX_CHAR (*) is being used as the wildcard character, but the search term could
            // include (?) which is the other accepted wildcard in JCR repository.  If exists as last char, remove.
            if (term.endsWith(WILDCARD_NOT_IN_USE_CHAR)) {
                term = term.substring(0, term.length() - 1);
            }

            queryWithWildcards += term;

            // if the term has a trailing fullstop or comma, appending a wildcard will result in no matches
            if (term.length() >= WILDCARD_POSTFIX_MIN_LENGTH
                && !term.contains(WILDCARD_IN_USE_CHAR)
                && !term.endsWith(".")
                && !term.endsWith(",")
                && !term.contains("\"")
                ) {
                queryWithWildcards += WILDCARD_IN_USE_CHAR;
            }

            if (i != splitTerms.length - 1) {
                queryWithWildcards += " ";
            }
        }

        return queryWithWildcards;
    }


    protected int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    protected EssentialsListComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }

    private HstQuery buildQuery(HstRequest request) {
        String query = getQueryParameter(request);
        String queryIncWildcards = applyWildcardsToQuery(query);
        HstQueryBuilder queryBuilder = HstQueryBuilder
            .create(request.getRequestContext().getSiteContentBaseBean());

        // register content classes
        addPublicationSystemTypes(queryBuilder);

        return queryBuilder
            .where(or(
                publicationSystemConstraint(query),
                publicationSystemConstraint(queryIncWildcards),
                constraint(".").contains(query),
                constraint(".").contains(queryIncWildcards)
            ))
            .build();
    }

    /**
     * Publication System content types that should be included in search
     */
    private void addPublicationSystemTypes(HstQueryBuilder query) {
        query.ofTypes(Publication.class, Series.class, Dataset.class);
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
}
