<#ftl output_format="HTML">
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() >

<#macro fileMetaAppendix size = 0 mimeType = ''>
    <#local format = ""/>
    <#if mimeType != ''>
    <#local format = "format: " + mimeType + ", " />
    </#if>
    <span class="regular">[${format}size: <@formatFileSize bytesCount=size/>]</span>
</#macro>