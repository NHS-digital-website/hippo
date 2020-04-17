<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/tabTileHeadings.ftl">
<#include "./macro/tabTileSection.ftl">

<#assign overridePageTitle = "Search results" />
<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign queryResultsString = "containing '" + query + "'" />
<#assign documentTitle = "Search results" />
<@hst.link siteMapItemRefId="person" var="personLink" />

<#assign peopleLinks = []/>
<#if peopleResults?? && peopleResults?has_content>
    <#list peopleResults as person>
        <#assign peopleLinks += [{
        "title": person.displayName,
        "linkType":"external",
        "link": personLink + "/" + person.id,
        "type": "People",
        "dept": person.department
        }] />
    </#list>
</#if>

<#assign newsLinks = [] />
<#list pageable.items as item>
    <#assign newsLinks += [{
    "title": item.searchResultTitle,
    "linkType":"external",
    "link": "#",
    "type": item.searchResultTitle
    }] />
</#list>

<#assign taskLinks = [] />
<#list pageable.items as item>
    <#assign taskLinks += [{
    "title": item.searchResultTitle,
    "linkType":"external",
    "link": "#",
    "type": item.searchResultType
    }] />
</#list>

<#assign teamLinks = [] />
<#list pageable.items as item>
    <#assign teamLinks += [{
    "title": item.searchResultTitle,
    "linkType":"external",
    "link": "#",
    "type": "Teams"
    }] />
</#list>

<#assign newsCount = 0 />
<#assign tasksCount = 0 />
<#assign teamsCount = 0 />
<#assign peopleCountChecked = peopleCount?has_content?then(peopleCount, 0) />
<#assign totalCount = peopleCountChecked + newsLinks?size + taskLinks?size + teamLinks?size />

<#assign tabTiles=[{
    "tileSectionHeading": "News",
    "tileSectionLinks": newsLinks,
    "tileCount": newsLinks?size
}, {
    "tileSectionHeading": "Tasks",
    "tileSectionLinks": taskLinks,
    "tileCount": taskLinks?size
}, {
    "tileSectionHeading": "Teams",
    "tileSectionLinks": teamLinks,
    "tileCount": teamLinks?size
}]/>

<#assign peopleTabTiles=[{
    "tileSectionHeading": "People",
    "tileSectionLinks": peopleLinks,
    "tileCount": peopleCountChecked
}]/>

<#assign allTabTiles=[{
    "tileSectionHeading": "All",
    "tileSectionLinks": [],
    "tileCount": totalCount
}]/>

<article class="article article--intranet-home">

    <#if tabTiles?? && tabTiles?has_content>
        <#-- get param 'area' -->
        <#--<#assign param = hstRequest.request.getParameter("area") />-->
        <#-- assign area the param value else, the first section in the list -->
        <#--<#assign area = param???then(param, slugify(document.tileSections[0].tileSectionHeading)) />-->

        <div class="grid-wrapper">
            <div class="grid-row top-margin-20">
                <form role="search" method="get" action="${searchLink}" class="search-banner__form" id="search" aria-label="${searchTitle}">
                    <div>
                        <input type="text" name="query" id="query" class="search-banner__input" placeholder="${ghostText}" value="${query!""}" aria-label="${buttonLabel}" style="background-color: white">
                        <label for="query" class="visually-hidden">${buttonLabel}</label>
                    </div>
                </form>
            </div>
            <div class="grid-row">
                <@tabTileHeadings allTabTiles + peopleTabTiles + tabTiles "service" area query />
            </div>
        </div>
    </#if>
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-quarter page-block page-block--sidebar article-section-nav-outer-wrapper">
                <#--INSERT FILTERS HERE-->
            </div>

            <div class="column column--three-quarters page-block page-block--main">
                <div class="article-section">
                    <span class="intra-info-tag intra-info-tag--bg-flat intra-info-tag--txt-grey intra-info-tag--right-block">${totalCount} results ${queryResultsString}</span>

                    <#assign isPeopleSearch = area == 'people' />
                    <#if isPeopleSearch && accessTokenRequired??>
                        <h3>Your session has expired due to inactivity. Please login again.</h3>
                        <a class="button" href="<@hst.link siteMapItemRefId='root'/>">Home</a>
                    </#if>

                    <#list allTabTiles as tileSection>
                        <#if slugify(tileSection.tileSectionHeading) == area>
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <div class="tile-panel" role="tabpanel">

                                        <#if peopleLinks?has_content>
                                            <#list peopleLinks as links>
                                                <#if links?is_first>
                                                    <span class="intra-info-tag intra-info-tag--txt-grey intra-info-tag--bg-flat">People</span>
                                                </#if>
                                                <div class="tile-panel-group">
                                                    <@tabTileSection links/>
                                                </div>
                                                <#if morePeople?? && links?index == 1>
                                                    <p class="intra-info-tag intra-info-tag--bg-flat intra-info-tag--right-block">
                                                        <a href="${searchLink}?area=people&query=${query}">Click here for more results</a>
                                                    </p>
                                                </#if>
                                            </#list>
                                        </#if>

                                        <#assign documentLinks = newsLinks + taskLinks + teamLinks />
                                        <#if documentLinks?has_content>
                                            <#list documentLinks as links>
                                                <#if links?is_first>
                                                    <span class="intra-info-tag intra-info-tag--txt-grey intra-info-tag--bg-flat">Documents</span>
                                                </#if>
                                                <div class="tile-panel-group">
                                                    <@tabTileSection links/>
                                                </div>
                                            </#list>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </#list>

                    <#assign allOtherTiles = peopleTabTiles + tabTiles />
                    <#list allOtherTiles as tileSection>
                        <#if slugify(tileSection.tileSectionHeading) == area>
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <div class="tile-panel" role="tabpanel">
                                        <#if tileSection?has_content>
                                            <#list tileSection.tileSectionLinks as links>
                                                <div class="tile-panel-group">
                                                    <@tabTileSection links/>
                                                </div>
                                            </#list>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </#list>



                    <#if errorMessage??>
                        <h4>${errorMessage}???</h4>
                    </#if>

<#--                    <ul>-->
<#--                        <li><a href="${searchLink}?area=all&query=${query}">All (${totalCount})</a></li>-->
<#--                        <li><a href="${searchLink}?area=people&query=${query}">People (${peopleCount})</a></li>-->
<#--                        <li><a href="${searchLink}?area=news&query=${query}">News (${newsCount})</a></li>-->
<#--                        <li><a href="${searchLink}?area=tasks&query=${query}">Tasks (${tasksCount})</a></li>-->
<#--                        <li><a href="${searchLink}?area=teams&query=${query}">Teams (${teamsCount})</a></li>-->
<#--                    </ul>-->

<#--                    <#switch area>-->
<#--                        <#case 'all'>-->
<#--                            <div>-->
<#--                                <h2>People</h2>-->
<#--                                <#if accessTokenRequired??>-->
<#--                                    <div>-->
<#--                                        <h3>Your session has expired due to inactivity. Please login again.</h3>-->
<#--                                        <a class="button" href="<@hst.link siteMapItemRefId='root'/>">Home</a>-->
<#--                                    </div>-->
<#--                                <#else>-->
<#--                                    <#if peopleResults?? && peopleResults?has_content>-->
<#--                                        <#list peopleResults as person>-->
<#--                                            <p><strong><a href="${personLink}/${person.id}">${person.displayName}</a></strong></p>-->
<#--                                        </#list>-->
<#--                                        <br />-->
<#--                                        <#if morePeople??>-->
<#--                                            <p><a href="${searchLink}?area=people&query=${query}">Click here for more results</a></p>-->
<#--                                        </#if>-->
<#--                                    <#else>-->
<#--                                        <p>No results for: ${query}</p>-->
<#--                                    </#if>-->
<#--                                </#if>-->
<#--                                <h2>Documents</h2>-->
<#--                                <#if pageable??>-->
<#--                                    <#list pageable.items as item>-->
<#--                                        <p>${item.searchResultTitle}</p>-->
<#--                                        <p>${item.searchResultType}</p>-->
<#--                                    </#list>-->
<#--                                <#else>-->
<#--                                    <p>No results for: ${query}</p>-->
<#--                                </#if>-->
<#--                            </div>-->
<#--                            <#break>-->
<#--                        <#case 'people'>-->
<#--                            <div>-->
<#--                                <h2>People</h2>-->
<#--                                <#if accessTokenRequired??>-->
<#--                                    <div>-->
<#--                                        <h3>Your session has expired due to inactivity. Please login again.</h3>-->
<#--                                        <a class="button" href="<@hst.link siteMapItemRefId='root'/>">Home</a>-->
<#--                                    </div>-->
<#--                                <#else>-->
<#--                                    <#if peopleResults?? && peopleResults?has_content>-->
<#--                                        <#list peopleResults as person>-->
<#--                                            <p><strong><a href="${personLink}/${person.id}">${person.displayName}</a></strong></p>-->
<#--                                        </#list>-->
<#--                                        <br />-->
<#--                                        <#if morePeople??>-->
<#--                                            <p><a href="${searchLink}?area=people&query=${query}">Click here for more results</a></p>-->
<#--                                        </#if>-->
<#--                                    <#else>-->
<#--                                        <p>No results for: ${query}</p>-->
<#--                                    </#if>-->
<#--                                </#if>-->
<#--                            </div>-->
<#--                            <#break>-->
<#--                        <#default>-->
<#--                            <div>-->
<#--                                <h2>Documents</h2>-->
<#--                                <#if pageable??>-->
<#--                                    <#list pageable.items as item>-->
<#--                                        <p>${item.searchResultTitle}</p>-->
<#--                                        <p>${item.searchResultType}</p>-->
<#--                                    </#list>-->
<#--                                <#else>-->
<#--                                    <p>No results for: ${query}</p>-->
<#--                                </#if>-->
<#--                            </div>-->
<#--                    </#switch>-->
                </div>
            </div>
        </div>
    </div>
</article>
