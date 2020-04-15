<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/tabTileHeadings.ftl">
<#include "./macro/tabTileSection.ftl">

<#assign queryResultsString = "containing '" + query + "'" />
<#assign documentTitle = "Search results" />
<@hst.link siteMapItemRefId="person" var="personLink" />

<#--<#function getPeopleLinks people>-->
<#--    <#assign links = [{}]/>-->
<#--    <#if people?? && people?has_content>-->
<#--        <#list people as person>-->
<#--            <#assign links += [{-->
<#--            "title": person.displayName,-->
<#--            "linkType":"external",-->
<#--            "link": person.personLink,-->
<#--            "type": "People",-->
<#--            "dept": person.department-->
<#--            }] />-->
<#--        </#list>-->
<#--    </#if>-->
<#--    <#return links />-->
<#--</#function>-->

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

<#assign blogLinks = [] />
<#list pageable.items as item>
    <#assign blogLinks += [{
    "title": item.searchResultTitle,
    "linkType":"external",
    "link": "#",
    "type": item.searchResultType
    }] />
</#list>

<#assign newsCount = 0 />
<#assign tasksCount = 0 />
<#assign teamsCount = 0 />
<#assign peopleCountChecked = peopleCount?has_content?then(peopleCount, 0) />
<#assign totalCount = peopleCountChecked + blogLinks?size />

<#assign tabTiles=[{
    "tileSectionHeading": "All",
    "tileSectionLinks": peopleLinks + blogLinks,
    "tileCount": totalCount
}, {
    "tileSectionHeading": "People",
    "tileSectionLinks": peopleLinks,
    "tileCount": peopleCountChecked
}, {
    "tileSectionHeading": "News",
    "tileSectionLinks": blogLinks,
    "tileCount": blogLinks?size
}, {
    "tileSectionHeading": "Tasks",
    "tileSectionLinks": blogLinks,
    "tileCount": blogLinks?size
}, {
    "tileSectionHeading": "Teams",
    "tileSectionLinks": blogLinks,
    "tileCount": blogLinks?size
}]/>
<#assign hasTabTiles=true/>

<article class="article article--intranet-home">

    <#if tabTiles?? && tabTiles?has_content>
        <#-- get param 'area' -->
        <#--<#assign param = hstRequest.request.getParameter("area") />-->
        <#-- assign area the param value else, the first section in the list -->
        <#--<#assign area = param???then(param, slugify(document.tileSections[0].tileSectionHeading)) />-->

        <div class="grid-wrapper">
            <div class="grid-row">
                <@tabTileHeadings tabTiles "service" area query />
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
                    <#if accessTokenRequired??>
                        <div>
                            <h3>Your session has expired due to inactivity. Please login again.</h3>
                            <a class="button" href="<@hst.link siteMapItemRefId='root'/>">Home</a>
                        </div>
                    <#else>
                        <#list tabTiles as tileSection>
                            <#if slugify(tileSection.tileSectionHeading) == area>
                                <span class="result-text result-text--pull-right">${tileSection.tileSectionLinks?size} results ${queryResultsString}</span>
                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <div class="tile-panel" role="tabpanel">
                                            <#if tileSection.tileSectionLinks?has_content>
                                                <#list tileSection.tileSectionLinks as links>
                                                    <div class="tile-panel-group">
                                                        <@tabTileSection links/>
                                                    </div>
                                                </#list>
                                                <#if morePeople??>
                                                    <p>
                                                        <a href="${searchLink}?area=people&query=${query}">Click here for more results</a>
                                                    </p>
                                                </#if>
                                            <#else>
                                                <#if query??>
                                                    <p>No results for: ${query}</p>
                                                </#if>
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        </#list>
                    </#if>


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
