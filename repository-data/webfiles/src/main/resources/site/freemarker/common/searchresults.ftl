<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />

<#if pageable??>
    <div data-uipath="ps.search-results" data-totalresults="${pageable.total}">
        <#if pageable.total == 0>
            <h1 data-uipath="ps.search-results.count">No results for: ${query}</h1>
        <#else>

            <h1 data-uipath="ps.search-results.count">${pageable.total} result<#if pageable.total gt 1>s</#if> found</h1>
            <h4 class="push-double--bottom">Displaying  ${pageable.startOffset +1 } to ${pageable.endOffset} of ${pageable.total} results for '${query}'</h4>

            <#list pageable.items as publication>
                <div class="push-double--bottom" data-uipath="ps.search-results.result">
                    <p class="flush">
                        <a href="<@hst.link hippobean=publication.selfLinkBean/>" title="${publication.title}" data-uipath="ps.search-results.result.title">
                            ${publication.title}
                        </a>
                    </p>
                    <p class="flush zeta" data-uipath="ps.search-results.result.date"><@formatRestrictableDate value=publication.nominalPublicationDate/></p>
                    <p class="flush" data-uipath="ps.search-results.result.summary"><@truncate text=publication.summary.firstParagraph size="300"/></p>
                </div>
            </#list>

            <#if cparam.showPagination>
                <#include "../include/pagination.ftl">
            </#if>

        </#if>
    </div>
<#else>
    <h3>Please fill in a search term</h3>
</#if>
