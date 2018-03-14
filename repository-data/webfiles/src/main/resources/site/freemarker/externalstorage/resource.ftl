<#ftl output_format="HTML">

<#macro externalstorageLink item>
    <#if hstRequestContext.preview>
        <#assign url="/?s3Reference="+item.reference+"&fileName="+item.filename/>
    <#else>
        <#assign url=item.url/>
    </#if>
    <#nested url>
</#macro>
