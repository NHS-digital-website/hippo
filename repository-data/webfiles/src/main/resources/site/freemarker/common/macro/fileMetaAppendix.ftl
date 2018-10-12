<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro fileMetaAppendix size = 0 mimeType = "">
    <#local format = ""/>
    <#if mimeType != "">
        <meta itemprop="encodingFormat" content="${mimeType}" />
        <#assign fileFormat = getFormatByMimeType(mimeType?lower_case) />
        <#if fileFormat != "">
        <#local format = fileFormat + ", " />
        </#if>
    </#if>
    <span class="regular">[${format}<span class="fileSize">size: <span itemprop="contentSize"><@formatFileSize bytesCount=size/></span></span>]</span>

</#macro>
