<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro fileMetaAppendix size = 0 mimeType = "" meetpdfa = false>
    <#local format = ""/>
    <#if mimeType != "">
        <meta itemprop="encodingFormat" content="${mimeType}" />
        <#assign fileFormat = getFormatByMimeType(mimeType?lower_case) />
        <#if fileFormat != "">
          <#if fileFormat == "pdf" && meetpdfa >
            <#assign fileFormat = "pdf/a" />
          </#if>
          <#local format = fileFormat + ", " />
        </#if>
    </#if>
    <span class="block-link__meta regular">[${format}<span class="fileSize">size: <span itemprop="contentSize"><@formatFileSize bytesCount=size/></span></span>]</span>

</#macro>
