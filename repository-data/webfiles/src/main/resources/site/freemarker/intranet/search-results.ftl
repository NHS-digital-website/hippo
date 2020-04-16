<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#assign documentTitle = "Search results" />
<@hst.link siteMapItemRefId="person" var="personLink" />
<#assign newsCount = 0 />
<#assign tasksCount = 0 />
<#assign teamsCount = 0 />
<#assign totalCount = peopleCount + newsCount + tasksCount + teamsCount />

<article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-header">
            <h1 data-uipath="ps.document.title">${documentTitle}</h1>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <#if errorMessage??>
                        <h4>${errorMessage}</h4>
                    </#if>

                    <br />
                    <ul>
                        <li><a href="${searchLink}?area=all&query=${query}">All (${totalCount})</a></li>
                        <li><a href="${searchLink}?area=people&query=${query}">People (${peopleCount})</a></li>
                        <li><a href="${searchLink}?area=news&query=${query}">News (${newsCount})</a></li>
                        <li><a href="${searchLink}?area=tasks&query=${query}">Tasks (${tasksCount})</a></li>
                        <li><a href="${searchLink}?area=teams&query=${query}">Teams (${teamsCount})</a></li>
                    </ul>
                    <br />

                    <#switch area>
                        <#case 'all'>
                            <div>
                                <h2>People</h2>
                                <#if accessTokenRequired??>
                                    <div>
                                        <h3>Your session has expired due to inactivity. Please login again.</h3>
                                        <a class="button" href="<@hst.link siteMapItemRefId='root'/>">Home</a>
                                    </div>
                                <#else>
                                    <#if peopleResults?? && peopleResults?has_content>
                                        <#list peopleResults as person>
                                            <p><strong><a href="${personLink}/${person.id}">${person.displayName}</a></strong></p>
                                        </#list>
                                        <br />
                                        <#if morePeople??>
                                            <p><a href="${searchLink}?area=people&query=${query}">Click here for more results</a></p>
                                        </#if>
                                    <#else>
                                        <p>No results for: ${query}</p>
                                    </#if>
                                </#if>
                                <h2>Documents</h2>
                                <#if pageable??>
                                    <#list pageable.items as item>
                                        <p>${item.searchResultTitle}</p>
                                        <p>${item.searchResultType}</p>
                                    </#list>
                                <#else>
                                    <p>No results for: ${query}</p>
                                </#if>
                            </div>
                            <#break>
                        <#case 'people'>
                            <div>
                                <h2>People</h2>
                                <#if accessTokenRequired??>
                                    <div>
                                        <h3>Your session has expired due to inactivity. Please login again.</h3>
                                        <a class="button" href="<@hst.link siteMapItemRefId='root'/>">Home</a>
                                    </div>
                                <#else>
                                    <#if peopleResults?? && peopleResults?has_content>
                                        <#list peopleResults as person>
                                            <p><strong><a href="${personLink}/${person.id}">${person.displayName}</a></strong></p>
                                        </#list>
                                        <br />
                                        <#if morePeople??>
                                            <p><a href="${searchLink}?area=people&query=${query}">Click here for more results</a></p>
                                        </#if>
                                    <#else>
                                        <p>No results for: ${query}</p>
                                    </#if>
                                </#if>
                            </div>
                            <#break>
                        <#default>
                            <div>
                                <h2>Documents</h2>
                                <#if pageable??>
                                    <#list pageable.items as item>
                                        <p>${item.searchResultTitle}</p>
                                        <p>${item.searchResultType}</p>
                                    </#list>
                                <#else>
                                    <p>No results for: ${query}</p>
                                </#if>
                            </div>
                    </#switch>
                </div>
            </div>
        </div>
    </div>
</article>
