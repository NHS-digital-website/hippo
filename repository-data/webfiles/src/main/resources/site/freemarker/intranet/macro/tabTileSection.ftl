<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro tabTileSection item indexId>
    <#assign options = {
        "title": item.searchResultTitle,
        "linkType": "internal",
        "link": item,
        "modified": item.lastModified,
        "summary": item.shortsummary,
        "type": item.searchResultType
    } />

    <#assign title = options.title???then(options.title, "") />
    <#assign modified = options.modified???then(options.modified, "NOMODIFIED") />
    <#assign summary = options.summary???then(options.summary, "") />

    <div>
        <#assign mainTag = ""/>
        <#if options.dept?has_content>
            <#assign mainTag = options.dept/>
        </#if>

        <#if mainTag?has_content>
            <span class="nhsd-a-tag nhsd-a-tag--bg-dark-grey">${mainTag?upper_case}</span>
        </#if>

        <#if options.type?has_content>
            <span class="nhsd-a-tag nhsd-a-tag--bg-dark-grey">${options.type}</span>
        </#if>

        <div class="nhsd-!t-margin-top-2">
            <#if options.linkType == "internal">
                <#assign link><@hst.link hippobean=options.link/></#assign>
            <#elseif options.linkType == "external">
                <#assign link = options.link/>
            </#if>

            <#if options.linkType??>
                <a href="${link}" class="nhsd-a-link">
            </#if>
                ${title}
            <#if options.linkType??>
                </a>
            </#if>
        </div>

         <#if summary?has_content>
            <p class="nhsd-t-body nhsd-!t-margin-bottom-1 nhsd-!t-margin-top-2">${summary}</p>
        </#if>

        <#local documentTags = item.getMultipleProperty("common:SearchableTags")![] />

        <ul class="nhsd-m-tag-list">
            <#list documentTags as tag>
                <li><span class="nhsd-a-tag nhsd-a-tag--bg-light-grey">${tag}</span></li>
            </#list>
        </ul>
    </div>
    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s">
</#macro>
