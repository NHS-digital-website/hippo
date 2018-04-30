<#ftl output_format="HTML">

<#macro externalstorageLink item>
    <#if item.class.name == "org.hippoecm.hst.content.beans.standard.HippoResource">
        <@hst.link var="url" hippobean=item/>
    <#elseif hstRequestContext.preview>
        <@hst.link siteMapItemRefId="S3CONNECTOR-PIPELINE-ID" var="s3EndpointBaseUrl"/>
        <#assign url=s3EndpointBaseUrl + "/?s3Reference=" + item.referenceEncoded + "&fileName=" + item.filename/>
    <#else>
        <#assign url=item.url/>
    </#if>
    <#nested url>
</#macro>
