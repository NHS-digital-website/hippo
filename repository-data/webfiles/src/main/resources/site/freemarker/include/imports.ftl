<#ftl output_format="HTML">
<#include "../externalstorage/resource.ftl">

<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >

<#assign truncate="uk.nhs.digital.ps.directives.TruncateFormatterDirective"?new() >
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() >
<#assign formatDate="uk.nhs.digital.ps.directives.DateFormatterDirective"?new() >

<@hst.defineObjects />

<#if facets??>
    <@hst.link var="searchLink" hippobean=facets />
<#else>
    <@hst.link siteMapItemRefId="search" var="searchLink"/>
</#if>

<#-- Doctype helper function -->
<#function getDocTypeName className>
    <#local docType = '' />
    <#local docTypes = {
        "Service":  "uk.nhs.digital.website.beans.Service",
        "General":  "uk.nhs.digital.website.beans.General",
        "Hub":      "uk.nhs.digital.website.beans.Hub",
        "Event":      "uk.nhs.digital.website.beans.Event",
        "List":     "uk.nhs.digital.website.beans.ComponentList",
        "Footer":   "Footer"
    }/>

    <#list docTypes?keys as key>
        <#if docTypes[key] == className>
            <#local docType = key/>
            <#break>
        </#if>
    </#list>

    <#return docType/>
</#function>

<#-- onClick attribute helper function -->
<#function getOnClickMethodCall className, link>
    <#if className?? && link??>
        <#local docType = getDocTypeName(className) />

        <#if docType?length gt 0>
            <#local onClickAttr="logGoogleAnalyticsEvent('Link click', '${docType}', '${link}')" />
            <#return onClickAttr/>
        </#if>
    </#if>
</#function>


<#function getPageTitle documentTitle = ''>
    <#local siteTitle = "NHS Digital"/>
    <#assign pageTitle = siteTitle/>
    <#assign delimeter = " - "/>

    <#if documentTitle != ''>
    <#assign pageTitle += delimeter + documentTitle />
    </#if>

    <#return pageTitle/>
</#function>
