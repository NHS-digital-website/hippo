<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "component/icon.ftl">

<#macro fileIcon mimeType = "">
    <#if mimeType != "">
        <#assign fileFormat = getFormatByMimeType(mimeType?lower_case) />
    </#if>
    <@icon name="${fileFormat}" size="2x" />
</#macro>
