<#ftl output_format="HTML">
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Roadmap" -->

<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/roadmapItemBox.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/metaTags.ftl">


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

<#list document.item as item>
    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy-MM" var="monthYear" timeZone="${getTimeZone()}" />
    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="yyyy" var="year" timeZone="${getTimeZone()}" />
    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MM" var="month" timeZone="${getTimeZone()}" />
    <#assign quarter = year + "Q" + (month?number / 3)?int />

    <#assign monthYearGroupHash = monthYearGroupHash + {  monthYear : (monthYearGroupHash[monthYear]![]) + [ item ] } />
    <#assign yearGroupHash = yearGroupHash + {  year : (yearGroupHash[year]![]) + [ item ] } />
    <#assign quarterGroupHash = quarterGroupHash + {  quarter : (quarterGroupHash[quarter]![]) + [ item ] } />
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

<#-- Return the section navigation links -->
<#function getSectionNavLinks>
    <#assign links = [] />
    <#list groupedDatesHash?keys?sort as k>
        <#assign displayDate= getDisplayDate(k) />
        <#assign links += [{ "url": "#" + slugify(displayDate), "title": displayDate }] />
    </#list>
    <#return links />
</#function>


<#--Group the events by type  -->
<#assign typeGroupHash = {} />
<#list document.item as item>
   <#if item.markers?? >
    <#list item.markers as type>
        <#assign typeGroupHash = typeGroupHash + {  type : (typeGroupHash[type]![]) + [ item ] } />
    </#list>
   </#if>
</#list>

<#-- Return the filter navigation links for the type -->
<#function getFilterNavLinks>
    <#assign links = [] />

    <#list typeGroupHash?keys as key>
        <#assign typeCount = typeGroupHash[key]?size />
        <#assign links += [{ "key": "${key}" , "title": roadmapcategories[key] + " (${typeCount})"}] />
    </#list>

    <#return links />
</#function>


<#assign sectionTitlesFound = countSectionTitles(document.item) />
<@fmt.message key="headers.about-this-publication" var="aboutThisPublicationHeader" />

<article class="article article--latest-events" aria-label="Document Header">

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>

                    <hr class="hr hr--short hr--light">

                    <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--reset" data-uipath="website.roadmap.summary">
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
                    <#--<#assign links = getSectionNavLinks() />-->
                    <#--<@sectionNav links=links ></@sectionNav>-->
                    <#assign sections = [] />
                    <#list groupedDatesHash?keys?sort as k>
                        <#assign displayDate= getDisplayDate(k) />
                        <#assign sections += [{ "url": "#" + slugify(displayDate), "title": displayDate, "aria-label": "Jump to items starting in ${displayDate}" }] />
                    </#list>
                    <@stickyNavSections getStickySectionNavLinks({  "sections": sections, "includeTopLink": false})></@stickyNavSections>

                    <#if getFilterNavLinks()?size gt 0>
                        <@stickyNavTags getFilterNavLinks() "" "Filter by type" "type" selectedTypes></@stickyNavTags>
                    </#if>
                </div>
                <!-- end sticky-nav -->
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#list groupedDatesHash?keys?sort as key>
                        <div id="${slugify(getDisplayDate(key))}" class="article-section article-section--letter-group--highlighted">
                            <div class="grid-row">
                                <h2>${getDisplayDate(key)?keep_before(" ")}</h2>
                                <#list groupedDatesHash[key] as item>

                                    <#assign itemData = { "title": item.title, "text": item.shortsummary } />

                                    <@hst.link hippobean=item var="itemLink" />
                                    <#assign itemData += { "link": itemLink } />

                                    <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MMM-yyyy" var="startdate" timeZone="${getTimeZone()}" />
                                    <@fmt.formatDate value=item.effectiveDate.endDate.time type="Date" pattern="MMM-yyyy" var="enddate" timeZone="${getTimeZone()}" />
                                    <#assign itemData += { "category": item.categoryLink } />
                                    <#assign itemData += { "monthsDuration": item.effectiveDate.getMethodNames(startdate, enddate)} />
                                    <@roadmapItemBox itemData></@roadmapItemBox>
                                </#list>

                            </div>
                        </div>
                </#list>
            </div>
        </div>
    </div>
</article>
