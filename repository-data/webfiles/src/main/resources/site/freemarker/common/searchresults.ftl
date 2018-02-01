<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">

<div data-uipath="ps.search-results" data-totalresults="${(pageable.total)!0}">
    <#if pageable?? && pageable.total gt 0>
        <h1 data-uipath="ps.search-results.count">${pageable.total} result<#if pageable.total gt 1>s</#if> found</h1>
        <h4 class="push-double--bottom">Displaying  ${pageable.startOffset +1 } to ${pageable.endOffset} of ${pageable.total} results${query???then(" for '${query}'", "")}</h4>

        <@searchResults items=pageable.items/>

        <#if cparam.showPagination>
            <#include "../include/pagination.ftl">
        </#if>
    <#elseif query?has_content>
        <h1 data-uipath="ps.search-results.count">No results for: ${query}</h1>
    <#else>
        <h1 data-uipath="ps.search-results.count">No results for filters</h1>
    </#if>
</div>
