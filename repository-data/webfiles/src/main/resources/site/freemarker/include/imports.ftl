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
    <#return "Europe/London" />
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

        <@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="yyyy-MM-dd" var="comparableStartDate" timeZone="${getTimeZone()}" />
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
                    <#if section.title?has_content && section.sectionType == 'website-section'>
                        <#assign isNumberedList = false />
                        <#if section.isNumberedList??>
                            <#assign isNumberedList = section.isNumberedList />
                        </#if>
                        <#assign links += [{ "url": "#" + slugify(section.title), "title": section.title, "isNumberedList": isNumberedList?c }] />
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
        <#if options.links??>
            <#assign links += options.links />
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

<#-- Months of the year -->
<#function monthsOfTheYear>
    <#return ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"] />
</#function>

<#-- Return the canonical document URL -->
<#function getDocumentUrl>
    <#if hstRequestContext?? && hstRequestContext.getContentBean()??>
        <@hst.link hippobean=hstRequestContext.getContentBean() fullyQualified=true var="documentUrl" />
    <#else>
        <@hst.link fullyQualified=true var="documentUrl" />
    </#if>
    <#return documentUrl />
</#function>

<#function getFormatByMimeType mimeType>
    <#local mimeTypes = {
        "image/jpeg": "jpg",
        "image/png": "png",
        "image/pdf": "pdf",
        "application/pdf": "pdf",
        "text/csv": "csv",
        "text/plain": "txt",
        "application/x-rar-compressed": "rar",
        "application/zip": "zip",

        "application/vnd.ms-powerpoint": "ppt",
        "application/vnd.ms-powerpoint.presentation.macroenabled.12": "ppt",
        "application/vnd.ms-powerpoint.addin.macroEnabled.12": "ppt",
        "application/vnd.ms-powerpoint.presentation.macroEnabled.12": "ppt",
        "application/vnd.ms-powerpoint.template.macroEnabled.12": "ppt",
        "application/vnd.ms-powerpoint.slideshow.macroEnabled.12": "ppt",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation": "ppt",
        "application/vnd.openxmlformats-officedocument.presentationml.template": "ppt",
        "application/vnd.openxmlformats-officedocument.presentationml.slideshow": "ppt",

        "application/vnd.ms-excel": "xls",
        "application/x-tika-msoffice": "xls",
        "application/vnd.ms-excel.sheet.macroEnabled.12": "xls",
        "application/vnd.ms-excel.addin.macroEnabled.12": "xls",
        "application/vnd.ms-excel.template.macroEnabled.12": "xls",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": "xls",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.template": "xls",

        "application/msword": "doc",
        "application/vnd.ms-word.document.macroenabled.12": "doc",
        "application/vnd.ms-word.template.macroEnabled.12": "doc",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document": "doc",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.template": "doc"
    }/>

    <#return (mimeTypes[mimeType]??)?then(mimeTypes[mimeType], "") />
</#function>

<#-- Split a hash into 2 hashes -->
<#function splitHash hash>
    <#local minRows = 3 />
    <#local rowCount = (hash?size / 2)?ceiling/>

    <#local leftHash = [] />
    <#local rightHash = [] />
    <#if rowCount < minRows>
        <#local leftHash = hash />
    <#else>
        <#list hash as hashItem>
            <#if hashItem?counter <= rowCount>
                <#local leftHash += [hashItem] />
            <#else>
                <#local rightHash += [hashItem] />
            </#if>
        </#list>
    </#if>

    <#return { "left": leftHash, "right": rightHash } />
</#function>

