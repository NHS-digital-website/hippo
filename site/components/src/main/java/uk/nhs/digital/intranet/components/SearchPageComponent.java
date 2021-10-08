package uk.nhs.digital.intranet.components;

import static org.apache.commons.collections.IteratorUtils.toList;
import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.*;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.builder.Constraint;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.repository.l10n.LocalizationService;
import org.onehippo.repository.l10n.ResourceBundle;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.CategoryInfo;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.intranet.enums.SearchTypes;
import uk.nhs.digital.intranet.factory.PersonFactory;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.SearchFilter;
import uk.nhs.digital.intranet.provider.BloomreachSearchProvider;
import uk.nhs.digital.intranet.utils.Constants;
import uk.nhs.digital.website.beans.Team;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ParametersInfo(type = SearchPageComponentInfo.class)
public class SearchPageComponent extends CommonComponent {
    static final String PEOPLE_API_LIMIT = "peopleApiLimit";
    static final String JSON_RESPONSE = "jsonResponse";
    static final String REQUEST_ATTR_PAGEABLE = "pageable";
    private static final String REQUEST_ATTR_ACCESS_TOKEN_REQUIRED = "accessTokenRequired";
    private static final String REQUEST_ATTR_MISSING_TERM_ERROR_MESSAGE = "searchTermErrorMessage";
    static final String REQUEST_ATTR_AREA = "area";
    static final String REQUEST_ATTR_TYPE = "type";
    static final String REQUEST_ATTR_TEAM = "team";
    static final String REQUEST_ATTR_YEAR = "year";
    static final String REQUEST_ATTR_SORT = "sort";
    static final String REQUEST_ATTR_TAXONOMIES = "taxonomy";

    private final PersonFactory personFactory;
    private final BloomreachSearchProvider bloomreachSearchProvider;
    private final String applicationId;
    private final String redirectUri;
    private final String baseUri;

    public SearchPageComponent(final PersonFactory personFactory,
                               final BloomreachSearchProvider bloomreachSearchProvider,
                               final String applicationId,
                               final String redirectUri,
                               final String baseUri) {
        this.personFactory = personFactory;
        this.bloomreachSearchProvider = bloomreachSearchProvider;
        this.applicationId = applicationId;
        this.redirectUri = redirectUri;
        this.baseUri = baseUri;
    }

    public void jsonResponse(HstRequest request) {
        Pageable<Person> peopleResults = searchPeople(request, false);
        Pageable<HippoBean> contentResults = searchContent(request, true);

        HstRequestContext context = request.getRequestContext();

        HstLink personLink = context.getHstLinkCreator().createByRefId("person", context.getResolvedMount().getMount());

        List<JSONObject> peopleList = peopleResults.getItems().stream()
            .limit(4)
            .map(person -> {
                JSONObject personObj = new JSONObject();
                personObj.put("name", person.getDisplayName());
                personObj.put("role", person.getJobRole());
                personObj.put("url", personLink.toUrlForm(context, false) + '/' + person.getId());
                return personObj;
            })
            .collect(Collectors.toList());

        List<JSONObject> documentList = Collections.emptyList();

        if (contentResults != null) {
            documentList = contentResults.getItems().stream()
                .limit(4)
                .map(document -> {
                    JSONObject documentObj = new JSONObject();
                    documentObj.put("name", document.getDisplayName());
                    Calendar date = document.getSingleProperty("intranet:publicationdate");
                    if (date == null) {
                        date = document.getSingleProperty("hippostdpubwf:lastModificationDate");
                    }
                    if (date != null) {
                        int year = date.get(Calendar.YEAR);
                        documentObj.put("year", year);
                    }
                    HstLink hstLink = context.getHstLinkCreator().create(document, context);
                    documentObj.put("url", hstLink.toUrlForm(context, false));
                    return documentObj;
                })
                .collect(Collectors.toList());
        }

        JSONArray peopleArray = new JSONArray();
        JSONArray documentArray = new JSONArray();

        if (documentList.size() > 0 && peopleList.size() > 0) {
            int docToIndex = documentList.size();
            if (docToIndex > 2) {
                docToIndex = 2;
            }
            documentList = documentList.subList(0 ,docToIndex);

            int peopleToIndex = peopleList.size();
            if (peopleToIndex > 2) {
                peopleToIndex = 2;
            }
            peopleList = peopleList.subList(0, peopleToIndex);
        }

        peopleArray.addAll(peopleList);
        documentArray.addAll(documentList);

        JSONObject json = new JSONObject();
        json.put("people", peopleArray);
        json.put("content", documentArray);

        request.setAttribute("jsonString", json.toJSONString());
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        boolean jsonResponse = Boolean.parseBoolean(getComponentParameter(JSON_RESPONSE));
        if (jsonResponse) {
            jsonResponse(request);
            return;
        }

        final String searchArea = Optional.ofNullable(getAnyParameter(request, REQUEST_ATTR_AREA)).orElse("DOCUMENTS");
        request.setAttribute(REQUEST_ATTR_AREA, searchArea);

        try {
            if (searchArea.equals("PEOPLE")) {
                searchPeople(request, true);
            } else {
                setFacets(request);
                searchContent(request, false);
            }
        } finally {
            request.setAttribute(REQUEST_ATTR_TYPE, getSearchTypes(request));
            request.setAttribute(REQUEST_ATTR_QUERY, getSearchQuery(request, true));
        }
    }

    private String getAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString(baseUri)
            .pathSegment("authorize")
            .queryParam("client_id", applicationId)
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_mode", "query")
            .queryParam("scope", "offline_access user.read.all")
            .queryParam("state", UUID.randomUUID().toString())
            .toUriString();
    }

    private boolean hasValidAccessToken(HstRequestContext requestContext) {
        return StringUtils.hasText((String) requestContext.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME));
    }

    private void setFacets(HstRequest request) {
        final List<String> years = getYears(request);
        final List<String> taxonomies = getTaxonomies(request);

        final Pageable<HippoBean> unfilteredResults = bloomreachSearchProvider
            .getBloomreachResults(null,
                getComponentInfo(request).getPageSize(),
                getCurrentPage(request), SearchTypes.INDIVIDUAL_DOCUMENT_SEARCH_TYPES, null, null, null, null);

        Map<String, Object> docTypeFacet = new HashMap<>();
        docTypeFacet.put("name", "Type");
        docTypeFacet.put("key", "type");
        docTypeFacet.put("values", getDoctypeFilter(unfilteredResults));
        docTypeFacet.put("selected", this.getParameter(request, REQUEST_ATTR_TYPE));
        Map<String, Map<String, Object>> facets = new HashMap<>();
        facets.put("docTypes", docTypeFacet);
        Map<String, Object> teamFacet = new HashMap<>();
        teamFacet.put("name", "Team");
        teamFacet.put("key", "team");
        teamFacet.put("values", getTeamFilter(unfilteredResults));
        teamFacet.put("selected", this.getParameter(request, REQUEST_ATTR_TEAM));
        facets.put("team", teamFacet);
        Map<String, Object> yearFacet = new HashMap<>();
        yearFacet.put("name", "Year");
        yearFacet.put("key", "year");
        yearFacet.put("values", getYearFilter(unfilteredResults));
        yearFacet.put("selected", years);
        facets.put("year", yearFacet);
        Map<String, Object> taxonomyFacet = new HashMap<>();
        taxonomyFacet.put("name", "Taxonomy");
        taxonomyFacet.put("key", "taxonomy");
        taxonomyFacet.put("values", getTaxonomyFilter(unfilteredResults));
        taxonomyFacet.put("selected", taxonomies);
        facets.put("taxonomy", taxonomyFacet);

        request.setAttribute("facets", facets);
    }

    private Pageable<HippoBean> searchContent(HstRequest request, boolean noFilters) {
        List<SearchTypes> searchTypes = SearchTypes.INDIVIDUAL_DOCUMENT_SEARCH_TYPES;
        List<Team> teams = null;
        List<String> years = null;
        List<String> taxonomies = null;

        if (!noFilters) {
            searchTypes = getSearchTypes(request);
            teams = getTeams(request);
            years = getYears(request);
            taxonomies = getTaxonomies(request);

            final String sort = getAnyParameter(request, REQUEST_ATTR_SORT);
            request.setAttribute(REQUEST_ATTR_SORT, sort);
        }

        final String sort = getAnyParameter(request, REQUEST_ATTR_SORT);
        request.setAttribute(REQUEST_ATTR_SORT, sort);

        final String searchQuery = getSearchQuery(request, true);

        if (!StringUtils.hasText(searchQuery)) {
            request.setAttribute(REQUEST_ATTR_MISSING_TERM_ERROR_MESSAGE, true);
        } else {
            final Pageable<HippoBean> bloomreachResults = bloomreachSearchProvider
                .getBloomreachResults(searchQuery,
                    getComponentInfo(request).getPageSize(),
                    getCurrentPage(request), searchTypes, teams, years, taxonomies, sort);
            request.setAttribute(REQUEST_ATTR_PAGEABLE, bloomreachResults);

            return bloomreachResults;
        }

        return null;
    }

    private Pageable<Person> searchPeople(HstRequest request, boolean fetchPhotos) {
        HstRequestContext requestContext = RequestContextProvider.get();
        request.setAttribute("authorizationUrl", getAuthorizationUrl());
        request.setAttribute("loginRequired", !hasValidAccessToken(requestContext));

        final String accessToken = (String) requestContext.getAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME);
        final String searchQuery = getSearchQuery(request, false);
        if (!StringUtils.hasText(accessToken)) {
            request.setAttribute(REQUEST_ATTR_ACCESS_TOKEN_REQUIRED, true);
        } else if (!StringUtils.hasText(searchQuery)) {
            request.setAttribute(REQUEST_ATTR_MISSING_TERM_ERROR_MESSAGE, true);
        } else {
            final int peopleApiLimit = Integer.parseInt(getComponentParameter(PEOPLE_API_LIMIT));
            List<Person> people = personFactory.fetchPeople(searchQuery, peopleApiLimit);

            if (fetchPhotos) {
                people = personFactory.fetchPhotos(people);
            }

            final int peopleLimit = getComponentInfo(request).getPageSize();
            final int peoplePage = getCurrentPage(request);

            Pageable<Person> pageable = new PeopleSearchPageable(people, peoplePage, peopleLimit);

            request.setAttribute(REQUEST_ATTR_PAGEABLE, pageable);

            return pageable;
        }

        return new PeopleSearchPageable();
    }

    private String getSearchQuery(HstRequest request, boolean allowWildcards) {
        return SearchInputParsingUtils.parse(getAnyParameter(request, REQUEST_PARAM_QUERY), allowWildcards);
    }

    private int getCurrentPage(HstRequest request) {
        return getAnyIntParameter(request, REQUEST_PARAM_PAGE, 1);
    }

    private List<String> getParameter(HstRequest request, String paramName) {
        String[] values = getPublicRequestParameters(request, paramName);
        if (values.length > 0) {
            return Arrays.asList(values);
        }
        return null;
    }

    private List<SearchTypes> getSearchTypes(HstRequest request) {
        List<String> areasList = this.getParameter(request, REQUEST_ATTR_TYPE);
        if (areasList != null) {
            List<SearchTypes> areas = SearchTypes.getSearchTypesFromList(areasList);
            if (!areas.isEmpty()) {
                return areas;
            }
        }
        return SearchTypes.INDIVIDUAL_DOCUMENT_SEARCH_TYPES;
    }

    private List<Team> getTeams(HstRequest request) {
        List<String> teamList = this.getParameter(request, REQUEST_ATTR_TEAM);

        if (teamList == null) {
            return null;
        }

        HippoBean folder = RequestContextProvider.get().getSiteContentBaseBean();

        ArrayList<Constraint> constraints = new ArrayList<>();
        for (String team : teamList) {
            constraints.add(constraint("website:title").equalToCaseInsensitive(team));
        }

        HstQueryBuilder hstQueryBuilder = HstQueryBuilder.create(folder).ofTypes(Team.class);
        try {
            HippoBeanIterator hippoBeans = hstQueryBuilder.where(or(constraints.toArray(new Constraint[0])))
                .build()
                .execute()
                .getHippoBeans();
            return toList(hippoBeans);
        } catch (QueryException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getYears(HstRequest request) {
        return this.getParameter(request, REQUEST_ATTR_YEAR);
    }

    public List<String> getTaxonomies(HstRequest request) {
        return this.getParameter(request, REQUEST_ATTR_TAXONOMIES);
    }

    private SearchPageComponentInfo getComponentInfo(HstRequest request) {
        return getComponentParametersInfo(request);
    }

    private Map<String, SearchFilter> getDoctypeFilter(Pageable<HippoBean> bloomreachResults) {
        return bloomreachResults.getItems().stream()
            .map(type -> {
                long noResults = bloomreachResults.getItems().stream()
                    .filter(filterType -> filterType.getContentType().equals(type.getContentType()))
                    .count();
                SearchTypes area = SearchTypes.getSearchTypeFromDocType(type);
                if (area == null) {
                    return null;
                }
                return new SearchFilter(area.name(), area.getDisplayName(), noResults);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(SearchFilter::getKey, Function.identity(),
                (sf1, sf2) -> new SearchFilter(sf1.getKey(), sf1.getName(), sf1.getNoResults() + sf2.getNoResults())));
    }

    private Map<String, SearchFilter> getTeamFilter(Pageable<HippoBean> bloomreachResults) {
        return bloomreachResults.getItems().stream()
            .flatMap(bean -> bean.getLinkedBeans("intranet:relateddocuments", Team.class).stream())
            .map(team -> {
                long noResults = bloomreachResults.getItems().stream()
                    .flatMap(bean -> bean.getLinkedBeans("intranet:relateddocuments", Team.class).stream())
                    .filter(filterTeam -> filterTeam.equals(team))
                    .count();
                return new SearchFilter(team.getTitle(), team.getTitle(), noResults);
            })
            .collect(Collectors.toMap(SearchFilter::getKey, Function.identity(), (sf1, sf2) -> sf1));
    }

    private LinkedHashMap<String, SearchFilter> getYearFilter(Pageable<HippoBean> bloomreachResults) {
        return bloomreachResults.getItems().stream()
            .map(item -> item.getSingleProperty("intranet:publicationdate"))
            .filter(Objects::nonNull)
            .map(date -> ((Calendar) date).get(Calendar.YEAR))
            .map(String::valueOf)
            .map(date -> {
                long noResults = bloomreachResults.getItems().stream()
                    .map(bean -> bean.getSingleProperty("intranet:publicationdate"))
                    .filter(Objects::nonNull)
                    .map(filterDate -> ((Calendar) filterDate).get(Calendar.YEAR))
                    .map(String::valueOf)
                    .filter(filterDate -> filterDate.equals(date))
                    .count();
                return new SearchFilter(date, date, noResults);
            })
            .sorted(Collections.reverseOrder(Comparator.comparing(SearchFilter::getKey)))
            .collect(Collectors.toMap(SearchFilter::getKey, Function.identity(), (sf1, sf2) -> sf1, LinkedHashMap::new));
    }

    private Map<String, SearchFilter> getTaxonomyFilter(Pageable<HippoBean> bloomreachResults) {
        return bloomreachResults.getItems().stream()
            .map(item -> item.getMultipleProperty("common:FullTaxonomy"))
            .filter(Objects::nonNull)
            .flatMap(terms -> Arrays.stream((String[]) terms).parallel()
                .distinct()
            )
            .map(term -> {
                long noResults = bloomreachResults.getItems().stream()
                    .map(item -> item.getMultipleProperty("common:FullTaxonomy"))
                    .filter(Objects::nonNull)
                    .flatMap(terms -> Arrays.stream((String[]) terms).parallel()
                        .distinct()
                    )
                    .filter(filterTerm -> filterTerm.equals(term))
                    .count();
                return new SearchFilter(term, getTaxonomyInfo(term), noResults);
            })
            .collect(Collectors.toMap(SearchFilter::getKey, Function.identity(), (sf1, sf2) -> sf1));
    }

    public String getDocTypeDisplayName(final String jcrName) {
        final String fullName = jcrName.contains(":") ? jcrName : "hipposys:" + jcrName;
        final String bundleName = "hippo:types" + "." + fullName;
        final LocalizationService service = HippoServiceRegistry.getService(LocalizationService.class);
        if (service != null) {
            final ResourceBundle bundle = service.getResourceBundle(bundleName, Locale.ENGLISH);
            if (bundle != null) {
                final String displayName = bundle.getString("jcr:name");
                if (StringUtils.hasText(displayName)) {
                    return displayName;
                }
            }
        }
        return jcrName;
    }

    public String getTaxonomyInfo(String taxonomyKey) {
        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Optional<Category> category = taxonomyManager.getTaxonomies().getRootTaxonomies()
            .stream()
            .map(t -> t.getCategoryByKey(taxonomyKey))
            .filter(Objects::nonNull)
            .findFirst();

        if (category.isPresent()) {
            CategoryInfo info = category.get().getInfo(Locale.ENGLISH);
            return info.getName();
        }
        return taxonomyKey;
    }
}
