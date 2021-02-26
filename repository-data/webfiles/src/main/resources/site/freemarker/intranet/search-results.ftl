<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/tabTileHeadings.ftl">
<#include "./macro/tabTileSection.ftl">

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

<#-- PARAMS related -->
<#assign PARAM_ALL = 'all'/>
<#assign PARAM_PEOPLE = 'people'/>
<#assign area = area?lower_case />
<#assign isAllTab = area = PARAM_ALL/>
<#assign isPeopleTab = area = PARAM_PEOPLE/>

<#-- Build tabs -->
<#assign tabs = [] />
<#assign totalCount = 0 />
<#list searchTabs as tab>
    <#if tab.tabName == 'All'>
        <#assign totalCount = tab.numberOfResults />
    </#if>

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

<#--Build 'news' link tiles-->
<#assign documentLinks = [] />
<#list pageable.items as item>
    <#assign documentLinks += [{
    "title": item.searchResultTitle,
    "linkType": "internal",
    "link": item,
    "type": item.searchResultType
    }] />
</#list>

<article class="article article--intranet-home">
    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3 nhsd-t-col-m-3 nhsd-t-col-l-3 nhsd-t-col-xl-3">
                <#--INSERT FILTERS HERE-->
            </div>
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-9 nhsd-t-col-m-9 nhsd-t-col-l-6 nhsd-t-col-xl-6">
                <#if searchTabs?has_content>
                    <div class="nhsd-t-row top-margin-20">
                        <div class="nhsd-t-heading-xl">Search results for:</div>
                        <div class="nhsd-m-search-bar nhsd-!t-padding-0">
                            <form role="search" method="get" class="nhsd-t-form" novalidate autocomplete="off" aria-label="Search">
                                <div class="nhsd-t-form-group">
                                    <div class="nhsd-t-form-control">
                                        <input class="nhsd-t-form-input" type="text" id="query" name="query" autocomplete="off" value="${query!""}" placeholder="What are you looking for today?" aria-label="Keywords" />
                                        <span class="nhsd-t-form-control__button">
                                            <button class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="Perform search">
                                                <span class="nhsd-a-icon nhsd-a-icon--size-s">
                                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                        <path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/>
                                                    </svg>
                                                </span>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <#assign queryResultsString = (query?? && query?has_content)?then(textContaining + " '" + query + "'", "") />
                    <span class="intra-info-tag intra-info-tag--bg-flat intra-info-tag--txt-grey">${totalCount} ${resultsLabel} ${queryResultsString}</span>
                    <a class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s nhsd-!t-margin-bottom-6"></a>
                    <div class="nhsd-t-row">
                        <@tabTileHeadings tabs "search" area query />
                    </div>
                    <a class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s nhsd-!t-margin-bottom-1"></a>
                </#if>
                <div class="article-section no-border no-top-margin no-top-padding">
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-12 nhsd-!t-padding-0">
                            <div class="tile-panel" role="tabpanel">
                                <#assign indexId = 1 />
                                <#assign isAllOrPeopleTab = isAllTab || isPeopleTab />
                                <#assign isFirstPaginationPage = isAllTab?then(pageable?has_content && pageable.currentPage == 1, true) />
                                <#assign hasPeopleLinks = peopleLinks?has_content />
                                <#assign hasDocumentsLinks = documentLinks?has_content/>
                                <#assign hasErrorMessages = accessTokenRequired?? || searchTermErrorMessage?? || apiErrorMessage?? />
                                <#assign displayHeaders = isAllTab && isFirstPaginationPage && hasDocumentsLinks && (hasPeopleLinks || hasErrorMessages) />
                                <#-- People -->
                                <#if displayHeaders>
                                    <span class="intra-info-tag intra-info-tag--txt-grey intra-info-tag--bg-flat intra-info-tag--block">${peopleHeader}</span>
                                </#if>
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
                                <#if isAllOrPeopleTab && hasPeopleLinks && isFirstPaginationPage>
                                    <#list peopleLinks as links>
                                        <div class="tile-panel-group">
                                            <@tabTileSection links indexId/>
                                        </div>
                                        <#assign indexId++ />
                                        <a class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s"></a>
                                    </#list>
                                    <#if morePeople?? && isAllTab>
                                        <p class="intra-info-tag intra-info-tag--bg-flat intra-info-tag--block-right">
                                            <a href="${searchLink}?area=people&query=${query}">${peopleMoreResults}</a>
                                        </p>
                                    </#if>
                                </#if>
                                <#-- Documents -->
                                <#if displayHeaders>
                                    <span class="intra-info-tag intra-info-tag--txt-grey intra-info-tag--bg-flat intra-info-tag--block">${documentHeader}</span>
                                </#if>
                                <#if hasDocumentsLinks>
                                    <#assign peopleNumbers = 0 />
                                    <#if isAllTab && (pageable?has_content && pageable.currentPage != 1) >
                                        <#assign peopleNumbers = peopleLinks?size />
                                    </#if>
                                    <#assign indexId += pageable.startOffset + peopleNumbers/>
                                    <#list documentLinks as links>
                                        <div class="tile-panel-group">
                                            <@tabTileSection links indexId/>
                                        </div>
                                        <#assign indexId++ />
                                        <a class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s"></a>
                                    </#list>
                                </#if>
                                <#-- No result message -->
                                <#if (isAllTab && !hasPeopleLinks && !hasDocumentsLinks) || (isPeopleTab && !hasPeopleLinks) || (!isAllOrPeopleTab && !hasDocumentsLinks)>
                                    <span class="intra-info-tag intra-info-tag--txt-black intra-info-tag--bg-flat intra-info-tag--block">${noSearchResults}</span>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="article-section no-border no-top-margin" id="intra-search-pagination">
                    <#if pageable.totalPages gt 1>
                        <#include "../include/pagination.ftl">
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>
