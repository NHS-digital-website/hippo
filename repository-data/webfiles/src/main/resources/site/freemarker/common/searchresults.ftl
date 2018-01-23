<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">

<#if pageable?? && pageable.total gt 0>
    <div data-uipath="ps.search-results" data-totalresults="${pageable.total}">
        <h1 data-uipath="ps.search-results.count">${pageable.total} result<#if pageable.total gt 1>s</#if> found</h1>
        <h4 class="push-double--bottom">Displaying  ${pageable.startOffset +1 } to ${pageable.endOffset} of ${pageable.total} results for '${query}'</h4>

        <@searchResutls items=pageable.items/>

        <#if cparam.showPagination>
            <#include "../include/pagination.ftl">
        </#if>
    </div>
<#elseif query?has_content>
    <div data-uipath="ps.search-results" data-totalresults="0">
        <h1 data-uipath="ps.search-results.count">No results for: ${query}</h1>
    </div>
<#else>
    <div data-uipath="ps.search-results">
        <h1 data-uipath="ps.search-results.count">Please fill in a search term</h1>
    </div>
</#if>
