<#include "../include/imports.ftl">
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />

<#if pageable??>
    <div id="searchResults" data-totalresults="${pageable.total}">
        <#if pageable.total == 0>
            <h3>No results for: ${query?html}</h3>
        <#else>
            <div>
                <h2>${pageable.total} result<#if pageable.total gt 1>s</#if> found</h2>
                <h4>Displaying  ${pageable.startOffset +1 } to ${pageable.endOffset} of ${pageable.total} results for '${query?html}'</h4>
            </div>
            <div>
                <ul id="resultsList">
                    <#list pageable.items as publication>
                        <li>
                            <a href="<@hst.link hippobean=publication.selfLinkBean/>"
                               title="${publication.title}"
                            >${publication.title}</a>
                            <@formatRestrictableDate value=publication.nominalPublicationDate/>
                            <p class="doc-summary"><@truncate text=publication.summary.firstParagraph size="300"/></p>
                        </li>
                    </#list>
                </ul>
                <#if cparam.showPagination>
                    <#include "../include/pagination.ftl">
                </#if>
            </div>
        </#if>
    </div>
<#else>
    <h3>Please fill in a search term</h3>
</#if>
