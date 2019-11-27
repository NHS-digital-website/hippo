<#ftl output_format="HTML">
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Roadmap" -->

<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/roadmapItemBox.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/metaTags.ftl">

<#assign inArray="uk.nhs.digital.freemarker.InArray"?new() />

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="publicationsystem.headers"/>
<#assign interval = document.granularity />
<#assign monthsOfTheYear = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"] />
<#assign quartersOfTheYear = ["January - March", "April - June", "July - September", "October - December"] />

<#assign groupedDatesHash = {} />
<#assign quarterGroupHash = {} />
<#assign monthYearGroupHash = {} />
<#assign yearGroupHash = {} />

<#assign filters = {} />

<#list document.item as item>
    <#if !item.roadmapItemStatuses?? || (item.roadmapItemStatuses?? && item.roadmapItemStatuses.status?lower_case != 'archive')>
        <#if item.categoryLink?? &&  item.categoryLink?size == 1>
            <#assign filters = filters + {  item.categoryLink[0].name : (filters[item.categoryLink[0].name]![]) + [item.categoryLink[0]] } />
        </#if>
            <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy-MM" var="monthYear" timeZone="${getTimeZone()}" />
            <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy" var="year" timeZone="${getTimeZone()}" />
            <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MM" var="month" timeZone="${getTimeZone()}" />
            <#assign quarter = year + "Q" + (month?number / 3)?int />

            <#if selectedTypes?size != 0>
                <#list selectedTypes as i>
                    <#if item.categoryLink[0].name == i>
                        <#assign monthYearGroupHash = monthYearGroupHash + {  monthYear : (monthYearGroupHash[monthYear]![]) + [ item ] } />
                        <#assign yearGroupHash = yearGroupHash + {  year : (yearGroupHash[year]![]) + [ item ] } />
                        <#assign quarterGroupHash = quarterGroupHash + {  quarter : (quarterGroupHash[quarter]![]) + [ item ] } />
                    </#if>
                </#list>
            <#else>
                <#assign monthYearGroupHash = monthYearGroupHash + {  monthYear : (monthYearGroupHash[monthYear]![]) + [ item ] } />
                <#assign yearGroupHash = yearGroupHash + {  year : (yearGroupHash[year]![]) + [ item ] } />
                <#assign quarterGroupHash = quarterGroupHash + {  quarter : (quarterGroupHash[quarter]![]) + [ item ] } />
            </#if>
        </#if>
</#list>

<#if interval == "Monthly">
    <#assign groupedDatesHash = monthYearGroupHash />
<#elseif interval == "Quarterly">
    <#assign groupedDatesHash = quarterGroupHash />
<#elseif interval == "Yearly">
    <#assign groupedDatesHash = yearGroupHash />
</#if>

<#function getCurrentQuarter>
    <@fmt.formatDate value=.now type="Date" pattern="yyyy-MM" var="currentMonthYear" timeZone="${getTimeZone()}" />
    <#assign year = currentMonthYear?keep_before("-") />
    <#assign month = currentMonthYear?keep_after("-") />
    <#assign quarter = (month?number / 3)?int />
    <#return quartersOfTheYear[quarter] + " " + year />
</#function>

<#function getCurrentMonthYear>
    <@fmt.formatDate value=.now type="Date" pattern="MMMM yyyy" var="currentMonthYear" timeZone="${getTimeZone()}" />
    <#return currentMonthYear />
</#function>

<#function getCurrentYear>
    <@fmt.formatDate value=.now type="Date" pattern="yyyy" var="currentYear" timeZone="${getTimeZone()}" />
    <#return currentYear />
</#function>

<#function getDisplayDate dateString>
    <#if interval == "Monthly">
        <#assign year= dateString?keep_before("-") />
        <#assign month = monthsOfTheYear[dateString?keep_after("-")?number-1] />
        <#assign monthYear = month + " " + year />
        <#return (monthYear == getCurrentMonthYear())?then("Current changes", monthYear) />

    <#elseif interval == "Quarterly">
        <#assign year= dateString?keep_before("Q") />
        <#assign quarterString = quartersOfTheYear[dateString?keep_after("Q")?number] + " " + year />
        <#return (quarterString == getCurrentQuarter())?then("Current changes", quarterString) />
    <#else>
        <#return (dateString == getCurrentYear())?then("Current changes", dateString) />
    </#if>
</#function>

<#assign sectionTitlesFound = countSectionTitles(document.item) />
<@fmt.message key="headers.about-this-publication" var="aboutThisPublicationHeader" />

<#macro roadmapItem item>
    <#assign itemData = { "title": item.title, "text": item.shortsummary } />

    <@hst.link hippobean=item var="itemLink" />
    <#assign itemData += { "link": itemLink } />

    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MMM-yyyy" var="startdate" timeZone="${getTimeZone()}" />
    <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="MMM-yyyy" var="enddate" timeZone="${getTimeZone()}" />
    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy-MM-dd" var="datetime" timeZone="${getTimeZone()}" />
    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MMMM yyyy" var="datelabel" timeZone="${getTimeZone()}" />
    <#assign itemData += { "category": item.categoryLink } />
    <#assign itemData += { "monthsDuration": item.effectiveDate.getMethodNames(startdate, enddate)} />
    <#assign itemData += { "datetime": datetime } />
    <#assign itemData += { "datelabel": datelabel} />
    <@roadmapItemBox itemData></@roadmapItemBox>
</#macro>

<article class="article article--latest-events" aria-label="Document Header">

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <h1 class="local-header__title"
                        data-uipath="document.title">${document.title}</h1>
                    <hr class="hr hr--short hr--light">
                    <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--reset"
                                 data-uipath="website.roadmap.summary">
                                <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <#assign sections = [] />
                    <#list groupedDatesHash?keys?sort as k>
                        <#assign displayDate= getDisplayDate(k) />
                        <#assign sections += [{ "url": "#" + slugify(displayDate), "title": displayDate, "aria-label": "Jump to items starting in ${displayDate}" }] />
                    </#list>
                    <#if sections?size != 0>
                        <@stickyNavSections getStickySectionNavLinks({  "sections": sections, "includeTopLink": false})></@stickyNavSections>
                    </#if>
                    <#if filters?has_content>
                        <#assign tags = [] />
                        <#list filters?keys as key>
                            <#assign typeCount = filters[key]?size />
                            <#assign tags += [{ "key" : key, "title": key?cap_first + " (${typeCount})" }] />
                        </#list>
                        <@stickyNavTags tags "" "Filter by type" "type" selectedTypes></@stickyNavTags>
                    </#if>
                </div>
                <!-- end sticky-nav -->
            </div>

             <div class="column column--two-thirds page-block page-block--main">
                <#list groupedDatesHash?keys?sort as key>

                    <div id="${slugify(getDisplayDate(key))}"
                         class="article-section article-section--letter-group--highlighted">
                        <div class="grid-row">
                            <h2>${getDisplayDate(key)?keep_before(" ")}</h2>
                            <#list groupedDatesHash[key] as item>
                                <#if item.roadmapItemStatuses?? && inArray(item.roadmapItemStatuses.status?lower_case, ['complete'])>
                                    <@roadmapItem item />
                                </#if>
                            </#list>
                            <#list groupedDatesHash[key] as item>
                                <#if !item.roadmapItemStatuses?? || (item.roadmapItemStatuses?? && !inArray(item.roadmapItemStatuses.status?lower_case, ['complete', 'cancelled', 'superseded']))>
                                    <@roadmapItem item />
                                </#if>
                            </#list>
                            <#list groupedDatesHash[key] as item>
                                <#if item.roadmapItemStatuses?? && inArray(item.roadmapItemStatuses.status?lower_case, ['cancelled', 'superseded'])>
                                    <@roadmapItem item />
                                </#if>
                            </#list>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </div>
</article>
