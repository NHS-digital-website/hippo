<#ftl output_format="HTML">

<#-- @ftlvariable name="queryResponse" type="com.onehippo.search.integration.api.QueryResponse" -->


<#macro contentSearch queryResponse>
    <#assign searchResult = queryResponse.searchResult />
    <#assign totalResults = searchResult.numFound />
    <#assign documents = searchResult.documents />

    <#assign sortLink = searchLink + query???then('?query=${query}&', '?') +'area=${area}&' + 'sort='>


    <div data-uipath="ps.search-results" data-totalresults="${(totalResults)!0}">
        <#if totalResults?? && totalResults gt 0>
            <div class="search-results-heading">
                <h1 class="search-results-heading__title">Search results</h1>

                <div class="search-results-heading__details">
                    <div class="search-results-heading__subcopy">
                        <div data-uipath="ps.search-results.description">
                            <span data-uipath="ps.search-results.count">${totalResults}</span>
                            result<#if totalResults gt 1>s</#if><#if query?has_content> containing '
                                <strong>${query}</strong>',</#if> sorted by
                            <strong>${sort}</strong>.
                        </div>
                    </div>

                    <div class="search-results-heading__sort">
                        <label for="sortBy">Sort by:</label>
                        <select id="sortBy"
                                onChange="window.location.href=this.value"
                                data-uipath="ps.search-results.sort-selector">
                            <#assign sortLink = searchLink + query???then('?query=${query}&', '?') +'area=${area}&' + 'sort='>
                            <option title="Order by date" value="${sortLink}date"
                                    <#if sort?? && sort == 'date'>selected</#if>>
                                Date
                            </option>
                            <option title="Order by relevance"
                                    value="${sortLink}relevance"
                                    <#if sort?? && sort == 'relevance'>selected</#if>>
                                Relevance
                            </option>
                        </select>
                    </div>
                </div>
            </div>

            <div class="search-results">
                <!-- replace with macro to render each search result -->
                <@contentSearchResults documents />
            </div>


            <#if cparam.showPagination && pageable.totalPages gt 1>
                <#include "../../include/pagination.ftl">
            </#if>
        <#elseif query?has_content>
            <div class="search-results-heading no-border">
                <h1 class="search-results-heading__title"
                    data-uipath="ps.search-results.description">No results
                    for: ${query}</h1>
            </div>
        <#else>
            <div class="search-results-heading no-border">
                <h1 class="search-results-heading__title"
                    data-uipath="ps.search-results.description">No results for
                    filters</h1>
            </div>
        </#if>
    </div>
</#macro>


<#macro contentSearchResults documents>

    <#list documents as document>

        <#assign properties = document.residualProperties />
        <#assign title = properties.title />
        <#assign url = properties.xmUrl />
        <#assign shortsummary = properties.shortsummary />

        <div class="cta cta--detailed" data-uipath="ps.search-results.result">

            <a class="cta__title cta__button" href="${url}" title="${title}" data-uipath="ps.search-results.result.title">
                ${title}
            </a>
<#--            <@fmt.formatDate value=publicationDate type="Date" pattern="dd MMMM yyyy" timeZone="${getTimeZone()}" var="datePosted"/>-->
<#--                <span class="cta__meta" data-uipath="ps.search-results.result.date"><@formatDate date=datePosted /></span>-->

            <p class="cta__text" data-uipath="ps.search-results.result.summary"><@truncate text=shortsummary size="300"/></p>
        </div>

    </#list>

</#macro>
