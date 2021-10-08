<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/tabTileHeadings.ftl">
<#include "./macro/tabTileSection.ftl">

<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/personitem.ftl">
<#include "./macro/filter-menu.ftl">
<#include "../nhsd-common/macros/az-link-nav.ftl">


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

<#assign queryResultsString = (query?? && query?has_content)?then(textContaining + " '" + query + "'", "") />

<#assign title = (area == "PEOPLE")?then("People", "Document") />

<@hst.link siteMapItemRefId="search" var="searchLink" />
<form id="nhsd-filter-menu-form" action="${searchLink}">
    <div id="intranet-search-hero">
    <@hero {
        "title": title + ' Search'
    }>
        <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
            <div class="nhsd-t-col-m-12 nhsd-t-col-l-8 nhsd-!t-no-gutters">
                <div class="nhsd-o-dropdown nhsd-o-dropdown--full-width" id="heroSearchDropdown" data-live-search-url="<@hst.link siteMapItemRefId="search-json" />" data-search-url="${searchLink}" style="--dropdown-height: 360px">
                    <div class="nhsd-o-dropdown__input">
                        <div class="nhsd-m-search-bar nhsd-m-search-bar__full-width nhsd-!t-padding-0 nhsd-!t-margin-top-4">
                            <div class="nhsd-t-form-control">
                                <input
                                    class="nhsd-t-form-input"
                                    type="text"
                                    id="heroSearchQuery"
                                    name="query"
                                    autocomplete="off"
                                    placeholder="What are you looking for today?"
                                    aria-label="Keywords"
                                    value="${query}"
                                />
                                <input name="area" value="${area}" type="hidden" />
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
                    <div class="nhsd-o-dropdown__dropdown">
                        <ul role="listbox"></ul>
                    </div>
                </div>
            </div>
        </div>
    </@hero>
    </div>

<div id="search-content">
    <div class="nhsd-t-grid nhsd-!t-padding-top-8 nhsd-!t-margin-bottom-8">
        <#if area == "PEOPLE">
            <div class="nhsd-t-row">
                <div id="people-search-nav" class="nhsd-t-col-l-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                    <#assign characters = {} />
                    <#list pageable.pagedItems as pagedPeopleResult>
                        <#assign page = pagedPeopleResult?index + 1 />
                        <#list pagedPeopleResult as peopleResult>
                            <#assign letter = peopleResult.displayName?cap_first[0] />
                            <#if !characters[letter]??>
                                <@hst.renderURL var="pageUrl">
                                    <@hst.param name="page" value="${page}"/>
                                </@hst.renderURL>
                                <#assign characters += { peopleResult.displayName?cap_first[0]: pageUrl } />
                            </#if>
                        </#list>
                    </#list>

                    <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">
                        <span class="nhsd-t-heading-xs">Search A-Z</span>
                        <@azLinkNav characters />
                    </div>
                </div>

                <div id="search-results" class="nhsd-t-col-l-8 nhsd-t-off-l-1">
                    <#if searchTermErrorMessage?has_content>
                        <h2 class="nhsd-t-heading-xs">Please enter a search query</h2>
                    <#else>
                        <h2 class="nhsd-t-heading-s nhsd-!t-margin-bottom-0">${pageable.total} ${resultsLabel} ${queryResultsString}</h2>
                        <div class="nhsd-!t-margin-bottom-2">
                            Showing results for people. <a href="?query=${query}" class="nhsd-a-link">Switch to documents</a>
                        </div>
                        <div class="js-search-loading nhsd-!t-display-hide nhsd-!t-margin-bottom-2 nhsd-!t-col-black nhsd-!t-font-weight-bold">Loading...</div>
                    </#if>
                    <hr class="nhsd-a-horizontal-rule" />

                    <#if loginRequired?has_content && loginRequired>
                        <h2 class="nhsd-t-heading-xl">Login required</h2>
                        <a href="${authorizationUrl}" class="nhsd-a-button">Login</a>
                    <#else>
                        <#if pageable.items?has_content>
                            <#assign groupedResults = {} />
                            <#list pageable.items as peopleResult>
                                <#assign resultsLetter = peopleResult.displayName[0]?cap_first />
                                <#if !groupedResults[resultsLetter]?has_content>
                                    <#assign groupedResults += { resultsLetter: [] } />
                                </#if>
                                <#assign letterResults = groupedResults[resultsLetter] />
                                <#assign letterResults += [peopleResult] />
                                <#assign groupedResults += { resultsLetter: letterResults } />
                            </#list>
                            <#list groupedResults?keys as groupedResultsLetter>
                                <div id="${groupedResultsLetter?lower_case}">
                                    <div class="nhsd-!t-margin-bottom-6">
                                        <span class="nhsd-a-character-block nhsd-a-character-block--large nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">${groupedResultsLetter}</span>
                                    </div>
                                    <div class="nhsd-t-flex-item--grow">
                                        <div class="nhsd-t-grid--nested">
                                            <div class="nhsd-t-row">
                                                <#list groupedResults[groupedResultsLetter] as peopleResult>
                                                    <div class="nhsd-t-col-6 nhsd-!t-margin-bottom-4">
                                                        <@personitem peopleResult />
                                                    </div>
                                                </#list>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <#if !groupedResultsLetter?is_last>
                                    <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-top-2" />
                                </#if>
                            </#list>
                            <div>
                                <#if pageable.totalPages gt 1>
                                    <#include "./include/pagination.ftl">
                                </#if>
                            </div>
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
                    </#if>
                </div>
            </div>
        <#else>
            <div class="nhsd-t-row">
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
                                                    <span>
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
                    <#if searchTermErrorMessage?has_content>
                        <h2 class="nhsd-t-heading-xs">Please enter a search query</h2>
                    <#else>
                        <div class="nhsd-!t-margin-bottom-2">
                            <h2 class="nhsd-t-heading-s nhsd-!t-margin-bottom-0">${pageable.total} ${resultsLabel} ${queryResultsString}</h2>
                            <div class="nhsd-!t-margin-bottom-2">
                                Showing results for documents. <a href="?query=${query}&area=PEOPLE" class="nhsd-a-link">Switch to people</a>
                            </div>

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
                                </div>
                            </div>
                        </div>
                    </#if>

                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s"/>

                    <div>
                        <#if pageable.items?has_content>
                            <#list pageable.items as item>
                                <div>
                                    <@tabTileSection item pageable.startOffset/>
                                </div>
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
                    </div>

                    <div>
                        <#if pageable.totalPages gt 1>
                            <#include "./include/pagination.ftl">
                        </#if>
                    </div>
                </div>
            </div>
        </#if>
    </div>
</div>
</form>
