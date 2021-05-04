<#ftl output_format="HTML">

<#macro typeSpan type>
    <#if type?? && (type == "external" || type == "internal" || type == "asset") >
        <span class="nhsd-t-sr-only" data-type="${type}">${type}</span>
    </#if>
</#macro>