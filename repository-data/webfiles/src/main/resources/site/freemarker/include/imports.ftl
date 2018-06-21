<#ftl output_format="HTML">
<#include "../externalstorage/resource.ftl">

<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] >
<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] >

<#assign truncate="uk.nhs.digital.ps.directives.TruncateFormatterDirective"?new() >
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() >
<#assign formatDate="uk.nhs.digital.ps.directives.DateFormatterDirective"?new() >
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />

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
        "Service":                      "uk.nhs.digital.website.beans.Service",
        "General":                      "uk.nhs.digital.website.beans.General",
        "Hub":                          "uk.nhs.digital.website.beans.Hub",
        "Event":                        "uk.nhs.digital.website.beans.Event",
        "List":                         "uk.nhs.digital.website.beans.ComponentList",
        "Footer":                       "Footer",
        "GDPR Transparency":            "uk.nhs.digital.website.beans.Gdprtransparency",
        "GDPR Summary":                 "uk.nhs.digital.website.beans.Gdprsummary"
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



<#function flat_blocks blocks order>
    <#local flattened_blocks = [] />
    <#list blocks as block>
        <#if block.linkType == "internal">
            <#local flattened_blocks = flattened_blocks + [ block.link ] />
        <#elseif block.linkType == "external" || block.linkType == "asset">
            <#local flattened_blocks = flattened_blocks + [ block ] />
        </#if>
    </#list>
    <#return order?then(flattened_blocks?sort_by("title"), flattened_blocks)/>
</#function>

<#function group_blocks blocks>
    <#local alphabetical_hash = {} />
    <#list blocks as block>
        <#local key = block.title?cap_first[0]/>
        <#local alphabetical_hash = alphabetical_hash + {  key : (alphabetical_hash[key]![]) + [ block ] } />
    </#list>
    <#return alphabetical_hash/>
</#function>

<#-- HOW TO USE the group_blocks function
    <#assign alphabetical_hash = group_blocks(flat_blocks(document.blocks true))/>
    <#list alphabetical_hash?keys as key>
        key: ${key} - value ${alphabetical_hash[key]?size}:
        <#list alphabetical_hash[key] as item>
            ${item}
        </#list>
        <br/>
    </#list>
-->

<#-- TODO s
1) when using the flat_blocks function, use the 'anchor' value as second parameter
2) when printng the block field and using the 'flattened_list', remember that the type variable is only defined for the externallink

        <#if block.linkType?? && block.linkType == "external">
        <h2 class="cta__title"><a href="${block.link}">${block.title}</a></h2>
        <#else>
        <h2 class="cta__title"><a href="<@hst.link hippobean=block />">${block.title}</a></h2>
        </#if>
-->

<#function getTimeZone>
    <#return "Europe/London"/>
</#function>

<#-- Get earliest and latest date in a date range in events -->
<#function getEventDateRangeData events = {}>
    <#local dateRangeData = { "minStartTimeStamp": 0, "maxEndTimeStamp": 0 } />

    <#if events??>
        <#-- Gather the earliest start date and the latest end date for each event -->
        <#list events as event>
            <#-- Store the earliest start date values -->
            <#local startTimeStamp = event.startdatetime.time?long/>
            <#if dateRangeData.minStartTimeStamp == 0 || startTimeStamp lt dateRangeData.minStartTimeStamp>
                <#local dateRangeData = dateRangeData + { "minStartTime": event.startdatetime.time, "minStartTimeStamp": startTimeStamp } />
            </#if>
            <#-- Store the latest end date values -->
            <#local endTimeStamp = event.enddatetime.time?long/>
            <#if dateRangeData.maxEndTimeStamp == 0 || endTimeStamp gt dateRangeData.maxEndTimeStamp>
                <#local dateRangeData = dateRangeData + { "maxEndTime": event.enddatetime.time, "maxEndTimeStamp": endTimeStamp } />
            </#if>
        </#list>

        <@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="yyyy-MM-dd" pattern="yyyy-MM-dd" var="comparableStartDate" timeZone="${getTimeZone()}" />
        <@fmt.formatDate value=dateRangeData.maxEndTime type="Date" pattern="yyyy-MM-dd" var="comparableEndDate" timeZone="${getTimeZone()}" />

        <#local dateRangeData = dateRangeData + { "comparableStartDate": comparableStartDate, "comparableEndDate": comparableEndDate } />
    </#if>

    <#return dateRangeData />
</#function>

<#function slugify string>
    <#return  string?lower_case?replace("\\W+", "-", "r") />
</#function>

<#-- Gather section nav links in a hash -->
<#function getSectionNavLinks options>
    <@hst.setBundle basename="rb.generic.headers"/>
    
    <#assign links = [] />
    <#if options??>
        <#if options.ignoreSummary?? && !options.ignoreSummary>
        <#else>
            <@fmt.message key="headers.summary" var="summaryHeader" />
            <#assign links = [{ "url": "#summary", "title": summaryHeader }] />
        </#if>

        <#if options.document??>
            <#if options.document.sections?has_content>
                <#list options.document.sections as section>
                    <#if section.title?has_content>
                        <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title }] />
                    </#if>
                </#list>
            </#if>
            <#if options.document.contactdetails?? && options.document.contactdetails.content?has_content>
                <@fmt.message key="headers.contact-details" var="contactDetailsHeader" />
                <#assign links += [{ "url": "#contact-details", "title": contactDetailsHeader }] />
            </#if>
        </#if>
        <#if options.childPages?? && options.childPages?has_content>
            <@fmt.message key="headers.further-information" var="furtherInformationHeader" />
            <#assign links += [{ "url": "#further-information", "title": furtherInformationHeader }] />
        </#if>
    </#if>

    <#return links />
</#function>

<#-- Count the sections with titles available -->
<#function countSectionTitles sections>
    <#local titlesFound = 0 />
    <#if sections??>
        <#list sections as section>
            <#if section.title?has_content>
                <#local titlesFound += 1 />
            </#if>
        </#list>
    </#if>
    <#return titlesFound />
</#function>
