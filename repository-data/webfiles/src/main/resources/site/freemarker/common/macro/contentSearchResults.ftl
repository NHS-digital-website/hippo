<#ftl output_format="HTML">

<#-- @ftlvariable name="queryResponse" type="com.onehippo.search.integration.api.QueryResponse" -->


<#macro contentSearchResults queryResponse>
    <#assign searchResult = queryResponse.searchResult />
    <#assign totalResults = searchResult.numFound />
    <#assign documents = searchResult.documents />

    ${searchResult}
    <#assign sortLink = searchLink + query???then('?query=${query}&', '?') +'area=${area}&' + 'sort='>
    ${query}

    <h1> sort = ${sort}</h1>
    <h1> area = ${area}</h1>
    <h1> query = ${query}</h1>
    <h1> SortLiLink = ${searchLink}</h1>
    <h1> SortLink = ${sortLink}</h1>

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
            </div>


<#--            <#if cparam.showPagination && pageable.totalPages gt 1>-->
<#--                <#include "../../include/pagination.ftl">-->
<#--            </#if>-->
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
