<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<div data-uipath="ps.search-results" data-totalresults="${(pageable.total)!0}">
    <#if pageable?? && pageable.total gt 0>
        <div class="search-results-heading">
            <h1 class="search-results-heading__title">Search results</h1>
            
            <div class="search-results-heading__details">
                <div class="search-results-heading__subcopy">
                    <div data-uipath="ps.search-results.description">
                        <span data-uipath="ps.search-results.count">${pageable.total}</span> result<#if pageable.total gt 1>s</#if><#if query?has_content> containing '<strong>${query}</strong>',</#if> sorted by <strong>${sort}</strong>.
                    </div>
                </div>
                <div class="search-results-heading__sort">
                    <label for="sortBy">Sort by:</label>
                    <select id="sortBy" onChange="window.location.href=this.value" data-uipath="ps.search-results.sort-selector">
                        <#assign sortLink = searchLink + query???then('?query=${query}&', '?') +'area=${area}&' + 'sort='>
                        <option title="Order by date" value="${sortLink}date"
                            <#if sort?? && sort == 'date'>selected</#if>>Date</option>
                        <option title="Order by relevance" value="${sortLink}relevance"
                            <#if sort?? && sort == 'relevance'>selected</#if>>Relevance</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="search-results">
            <@searchResults items=pageable.items/>
        </div>

        <#if cparam.showPagination && pageable.totalPages gt 1>
            <#include "../include/pagination.ftl">
        </#if>
    <#elseif query?has_content>
        <div class="search-results-heading no-border">
            <h1 class="search-results-heading__title" data-uipath="ps.search-results.description">No results for: ${query}</h1>
        </div>
    <#else>
        <div class="search-results-heading no-border">
            <h1 class="search-results-heading__title" data-uipath="ps.search-results.description">No results for filters</h1>
        </div>
    </#if>
</div>
