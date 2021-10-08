<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/tabTileHeadings.ftl">
<#include "./macro/tabTileSection.ftl">

<#include "./macro/heroes/hero.ftl">
<#include "./macro/heroes/hero-options.ftl">
<#include "./macro/filter-menu.ftl">

<#-- Resource bundle -->
<@hst.setBundle basename="rb.intra.search"/>
<@fmt.message key="page.title" var="overridePageTitle"/>
<@fmt.message key="labels.containing" var="textContaining"/>
<@fmt.message key="labels.results" var="resultsLabel"/>
<@fmt.message key="labels.noResults" var="noSearchResults"/>
<@fmt.message key="labels.moreResults" var="peopleMoreResults"/>
<@fmt.message key="labels.expiryBtn" var="sessionExpiryButtonLabel"/>
<@fmt.message key="headers.people" var="peopleHeader"/>
<@fmt.message key="headers.documents" var="documentHeader"/>

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#list facets?keys as facet>
    <#if facets[facet].values?size gt 0>
        <#list facets[facet].values?keys as option>
            <script></script>
        </#list>
    </#if>
</#list>

<#-- PARAMS related -->
<#assign PARAM_ALL = 'all'/>
<#assign PARAM_PEOPLE = 'people'/>
<#assign area = area?lower_case />
<#assign isAllTab = area = PARAM_ALL/>
<#assign isPeopleTab = area = PARAM_PEOPLE/>

<#-- Build tabs -->
<#assign tabs = [] />
<#list searchTabs as tab>
    <#assign tabs += [{
        "tileSectionHeading": tab.tabName,
        "tileCount": tab.numberOfResults
    }] />
</#list>

<#--Build 'people' link tiles-->
<#assign peopleLinks = []/>
<#if peopleResults?? && peopleResults?has_content>
    <@hst.link siteMapItemRefId="person" var="personLink" />

    <#list peopleResults as person>
        <#assign dept = (person.department?? && person.department?has_content)?then(person.department, "") />
        <#assign peopleLinks += [{
        "title": person.displayName,
        "linkType":"external",
        "link": personLink + "/" + person.id,
        "type": "People",
        "dept": dept
        }] />
    </#list>
</#if>

<form id="nhsd-filter-menu-form">
<@hero {
    "title": 'Search'
}>
    <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
        <div class="nhsd-t-col-m-12 nhsd-t-col-l-8 nhsd-!t-no-gutters">
            <div class="nhsd-m-search-bar nhsd-m-search-bar__full-width nhsd-!t-padding-0 nhsd-!t-margin-top-4">
                <div class="nhsd-t-form-control">
                    <input
                        class="nhsd-t-form-input"
                        type="text"
                        id="searchQuery"
                        name="query"
                        autocomplete="off"
                        placeholder="What are you looking for today?"
                        aria-label="Keywords"
                        value="${query}"
                    />
                    <span class="nhsd-t-form-control__button">
                        <button type="button" id="clear-button" data-clear-button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent nhsd-!t-display-hide" aria-label="Clear">
                            <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><polygon points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/></svg>
                            </span>
                        </button>
                        <button type="submit" class="js-search-submit nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" aria-label="Perform search">
                            <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/></svg>
                            </span>
                        </button>
                    </span>
                </div>
            </div>
        </div>
    </div>
</@hero>

<article id="search-content">
    <div class="nhsd-t-grid nhsd-!t-padding-top-8 nhsd-!t-margin-bottom-8">
        <div class="nhsd-t-row">
            <#if searchTabs?has_content>
                <div class="nhsd-t-col-3">
                    <@tabTileHeadings tabs "search" area query />
                </div>
            </#if>

            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                <a href="#search-results" class="nhsd-a-skip-link nhsd-a-skip-link--small nhsd-!t-margin-bottom-3">
                    <span class="nhsd-a-skip-link__label">Skip to content</span>
                </a>
                <div>
                    <span class="nhsd-t-heading-s nhsd-!t-margin-0 nhsd-!t-display-no-js-show">Filters</span>
                    <div class="nhsd-o-dropdown nhsd-o-dropdown--full-width nhsd-!t-display-no-js-hide" id="searchFilterDropdown">
                        <div class="nhsd-o-dropdown__input">
                            <div class="nhsd-o-filter-menu__search-bar">
                                <div class="nhsd-m-search-bar nhsd-m-search-bar__full-width  nhsd-m-search-bar__small">
                                    <div class="nhsd-t-form-control">
                                        <input class="nhsd-t-form-input" type="text" id="searchFilterInput" autocomplete="off" placeholder="Filter search..." aria-label="Keywords" />
                                        <span class="nhsd-t-form-control__button">
                                            <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent nhsd-!t-display-hide" aria-label="Clear" data-clear-button>
                                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><polygon points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/></svg>
                                                </span>
                                            </button>
                                            <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" aria-label="Perform search">
                                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/></svg>
                                                </span>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="nhsd-o-dropdown__dropdown">
                            <ul role="listbox" id="searchFilter1-dropdown-list"></ul>
                        </div>
                    </div>
                    <div id="nhsd-filter-menu">
                        <#assign openFacets = {} />
                        <#assign facetsActive = false />
                        <#list facets?keys as facet>
                            <#assign facetOptions = facets[facet].values />
                            <#if facetOptions?size gt 0>
                                <#assign openFacets += {
                                    facet: false
                                } />
                                <#list facetOptions?keys as option>
                                    <#if facets[facet].selected?seq_contains(option)>
                                        <#assign openFacets += {
                                            facet: true
                                        } />
                                        <#assign facetsActive = true />
                                        <#break />
                                    </#if>
                                </#list>

                                <div class="nhsd-m-filter-menu-section ${openFacets[facet]?then('nhsd-m-filter-menu-section--active', '')}">
                                    <div class="nhsd-m-filter-menu-section__accordion-heading">
                                        <button type="button" class="nhsd-m-filter-menu-section__menu-button" aria-label="Heading 1" aria-expanded="false">
                                            <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                                                <span id="icon">
                                                    <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                            <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/>
                                                        </svg>
                                                    </span>
                                                </span>
                                                ${facets[facet].name}
                                            </span>
                                        </button>
                                        <div class="nhsd-m-filter-menu-section__accordion-panel">
                                            <#list facetOptions?keys as option>
                                                <div class="nhsd-m-filter-menu-section__option-row">
                                                    <span class="nhsd-a-checkbox">
                                                        <label class="js-search-filter" data-keywords="${facets[facet].name} ${facetOptions[option].name}">
                                                            <input id="filter-${facets[facet].key}-${slugify(facetOptions[option].key)}" name="${facets[facet].key}" value="${facetOptions[option].key}" type="checkbox" ${facets[facet].selected?seq_contains(option)?then('checked', '')}/>
                                                            <span class="nhsd-a-checkbox__label">
                                                                <span class="js-filter-name">${facetOptions[option].name}</span>
                                                            </span>
                                                            <span class="checkmark"></span>
                                                        </label>
                                                    </span>
                                                </div>
                                            </#list>
                                        </div>
                                    </div>
                                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs" />
                                </div>
                            </#if>
                        </#list>
                    </div>
                    <div class="nhsd-o-filter-menu__filter-button">
                        <button class="nhsd-a-button" type="submit">
                            <span class="nhsd-a-button__label">Filter results</span>
                        </button>
                    </div>
                </div>
            </div>

            <div id="search-results" class="nhsd-t-col-xs-12 nhsd-t-off-l-1 nhsd-t-off-s-0 nhsd-t-col-s-8">
                <div class="nhsd-t-flex nhsd-t-flex--align-items-centre nhsd-t-flex--justify-content-between nhsd-!t-margin-bottom-2">
                    <#assign queryResultsString = (query?? && query?has_content)?then(textContaining + " '" + query + "'", "") />
                    <span class="nhsd-t-heading-s nhsd-!t-margin-bottom-0">${pageable.total} ${resultsLabel} ${queryResultsString}</span>

                    <#if facetsActive>
                        <a href="?sort=${sort}&query=${query}" class="js-clear-filters nhsd-a-button nhsd-a-button--outline nhsd-!t-margin-0">Clear Filters</a>
                    </#if>
                </div>

                <#assign odd = false />
                <#list facets?keys as facet>
                    <#assign facetOptions = facets[facet].values />
                    <#if facetOptions?size gt 0 && openFacets[facet]>
                        <#assign odd = odd?then(false, true) />
                        <div class="nhsd-t-flex nhsd-t-flex--align-items-centre nhsd-!t-padding-left-2 nhsd-!t-padding-right-2 ${odd?then('nhsd-!t-bg-pale-grey', '')}">
                            <div class="nhsd-t-body-s nhsd-!t-margin-0 nhsd-!t-col-black">${facets[facet].name}:</div>
                            <ul class="nhsd-m-tag-list nhsd-!t-margin-left-2">
                            <#assign firstTag = true />
                            <#list facetOptions?keys as option>
                                <#if facets[facet].selected?seq_contains(option)>
                                    <#assign tagUrl = "?sort=" + sort />
                                    <#if query?has_content>
                                        <#assign tagUrl += "&query=" + query />
                                    </#if>
                                    <#list facets?keys as facet2>
                                        <#list facets[facet2].selected as selectedFacet>
                                            <#if option != selectedFacet>
                                                <#assign tagUrl += "&" + facets[facet2].key + "=" + selectedFacet />
                                            </#if>
                                        </#list>
                                    </#list>

                                    <li class="nhsd-!t-margin-top-2 nhsd-!t-margin-bottom-2 nhsd-!t-margin-left-0">
                                        <#if !firstTag>
                                            <span class="nhsd-t-body-s">or</span>
                                        </#if>
                                        <a href="${tagUrl}" class="nhsd-a-tag nhsd-a-tag--closable js-filter-tag" data-filter-id="filter-${facets[facet].key}-${slugify(facetOptions[option].key)}">
                                            ${facetOptions[option].name}
                                            <span class="nhsd-a-icon nhsd-a-icon--size-xxs nhsd-a-icon--col-blue">
                                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                    <polygon points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/>
                                                </svg>
                                            </span>
                                        </a>
                                    </li>
                                    <#assign firstTag = false />
                                </#if>
                            </#list>
                            </ul>
                        </div>
                    </#if>
                </#list>

                <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s"/>

                <div class="nhsd-t-grid nhsd-t-grid--nested">
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-3">
                            <form class="nhsd-t-form" novalidate="" autocomplete="off">
                                <div class="nhsd-t-form-group nhsd-!t-margin-bottom-3">
                                    <div class="js-search-loading nhsd-!t-display-hide nhsd-!t-margin-bottom-2 nhsd-!t-col-black nhsd-!t-font-weight-bold">Loading...</div>
                                    <label class="nhsd-t-form-hint" for="searchSortBy">Sort By</label>
                                    <div class="nhsd-t-form-control">
                                        <select class="nhsd-t-form-select-s" name="sort" id="searchSortBy">
                                            <option value="rel" ${(sort == "rel")?then('selected', '')}>Relevance</option>
                                            <option value="date-desc" ${(sort == "date-desc")?then('selected', '')}>Latest</option>
                                            <option value="date-asc" ${(sort == "date-asc")?then('selected', '')}>Oldest</option>
                                        </select>
                                        <span class="nhsd-t-form-control__icon">
                                            <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                    <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                                </svg>
                                            </span>
                                        </span>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s"/>

                <div>
                    <#assign indexId = 1 />
                    <#assign isAllOrPeopleTab = isAllTab || isPeopleTab />
                    <#assign isFirstPaginationPage = isAllTab?then(pageable?has_content && pageable.currentPage == 1, true) />

                    <#assign hasPeopleLinks = peopleLinks?has_content />
                    <#assign hasDocumentsLinks = pageable.items?has_content/>
                    <#assign hasErrorMessages = accessTokenRequired?? || searchTermErrorMessage?? || apiErrorMessage?? />

                    <#if isAllOrPeopleTab && hasErrorMessages && isFirstPaginationPage>
                        <span class="intra-info-tag intra-info-tag--txt-black intra-info-tag--bg-flat intra-info-tag--block">
                            <#if accessTokenRequired??>
                                <@fmt.message key="text.accessTokenRequired"/>
                            <#elseif searchTermErrorMessage??>
                                <@fmt.message key="text.searchTermErrorMessage"/>
                            <#elseif apiErrorMessage??>
                                <@fmt.message key="text.apiErrorMessage"/>
                            </#if>
                        </span>
                        <#if loginRequired>
                            <div class="ctabtn--div">
                                <a class="ctabtn--nhs-digital-button" href="${authorizationUrl}"><@fmt.message key="labels.peopleSearchBtn"/></a>
                            </div>
                        </#if>
                    </#if>

                    <#-- Documents -->
                    <#if hasDocumentsLinks>
                        <#assign peopleNumbers = 0 />
                        <#if isAllTab && (pageable?has_content && pageable.currentPage != 1) >
                            <#assign peopleNumbers = peopleLinks?size />
                        </#if>
                        <#assign indexId += pageable.startOffset + peopleNumbers/>
                        <#list pageable.items as item>
                            <div class="tile-panel-group">
                                <@tabTileSection item indexId/>
                            </div>
                            <#assign indexId++ />
                        </#list>
                    <#else>
                        <p class="nhsd-t-body">Your search didn't return any results.</p>

                        <p class="nhsd-t-body">You could try:</p>

                        <ul class="nhsd-t-list nhsd-t-list--bullet">
                            <li>Checking your spelling</li>
                            <li>Removing filters</li>
                            <li>Using fewer keywords</li>
                            <li>Using different keywords</li>
                            <li>Generalising your keywords</li>
                        </ul>
                    </#if>

                    <#-- No result message -->
                    <#if (isAllTab && !hasPeopleLinks && !hasDocumentsLinks) || (isPeopleTab && !hasPeopleLinks) || (!isAllOrPeopleTab && !hasDocumentsLinks)>
                        <span class="intra-info-tag intra-info-tag--txt-black intra-info-tag--bg-flat intra-info-tag--block">${noSearchResults}</span>
                    </#if>
                </div>

                <div>
                    <#if pageable.totalPages gt 1>
                        <#include "../include/pagination.ftl">
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>
</form>
