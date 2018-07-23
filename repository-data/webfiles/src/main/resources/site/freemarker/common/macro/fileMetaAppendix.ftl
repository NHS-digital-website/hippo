<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro fileMetaAppendix size = 0 mimeType = "">
    <#local format = ""/>
    <#if mimeType != "">
        <#assign fileFormat = getFormatByMimeType(mimeType?lower_case) />
        <#if fileFormat != "">
        <#local format = fileFormat + ", " />
        </#if>
    </#if>
    <span class="regular">[${format}size: <@formatFileSize bytesCount=size/>]</span>
</#macro>