<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">


<main>
    <div class="page-block">
        <div class="grid-wrapper grid-wrapper--article" data-uipath="ps.search-results" data-totalresults="${(pageable.total)!0}">

            <p>template: searchresult.ftl (in common)</p>

            <#if pageable?? && pageable.total gt 0>
                <h1>Search results</h1>

                <div class="layout layout--flush">
                    <div class="layout__item layout-2-3" data-uipath="ps.search-results.description">
                        <span data-uipath="ps.search-results.count">${pageable.total}</span> result<#if pageable.total gt 1>s</#if><#if query?has_content> containing '<strong>${query}</strong>',</#if> sorted by <strong>${sort}</strong>.
                    </div><!--
                    --><div class="layout__item layout-1-3" style="text-align:right">
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

                <div class="push--top">
                    <@searchResults items=pageable.items/>
                </div>

                <#if cparam.showPagination>
                    <#include "../include/pagination.ftl">
                </#if>
                <#elseif query?has_content>
                    <h1 data-uipath="ps.search-results.description">No results for: ${query}</h1>
                <#else>
                    <h1 data-uipath="ps.search-results.description">No results for filters</h1>
                </#if>

        </div>
    </div>
</main>
