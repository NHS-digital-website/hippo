<#ftl output_format="HTML">

<#macro externalstorageLink item>
    <#if hstRequestContext.preview>
        <@hst.link siteMapItemRefId="S3CONNECTOR-PIPELINE-ID" var="s3EndpointBaseUrl"/>
        <#assign url=s3EndpointBaseUrl + "/?s3Reference=" + item.reference + "&fileName=" + item.filename/>
    <#else>
        <#assign url=item.url/>
    </#if>
    <#nested url>
</#macro>
