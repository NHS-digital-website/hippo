<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/roadmapItemBox.ftl">
<#include "macro/tagNav.ftl">
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

<#list roadmapitems.hippoBeans as item>
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
        <#assign links += [{ "url": "#" + slugify(displayDate), "title": displayDate, "aria-label": "Jump to items starting in ${displayDate}" }] />
    </#list>
    <#return links />
</#function>


<#--Group the events by type  -->
<#assign typeGroupHash = {} />
<#list roadmapitems.hippoBeans as item>
    <#list item.markers as type>
        <#assign typeGroupHash = typeGroupHash + {  type : (typeGroupHash[type]![]) + [ item ] } />
    </#list>
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


<#assign sectionTitlesFound = countSectionTitles(roadmapitems.hippoBeans) />
<@fmt.message key="headers.about-this-publication" var="aboutThisPublicationHeader" />

<article class="article article--latest-events">

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>

                    <hr class="hr hr--short hr--light">

                    <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--reset">
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
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks()></@sectionNav>

                    <#if getFilterNavLinks()?size gt 0>
                        <@tagNav getFilterNavLinks()></@tagNav>
                    </#if>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#list groupedDatesHash?keys?sort as key>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="cta-list hub-box-list">
                                <div class="article-section article-section--letter-group no-border">
                                    <div class="grid-row" id="${slugify(getDisplayDate(key))}">
                                        <h2>${getDisplayDate(key)}</h2>

                                        <#list groupedDatesHash[key] as item>

                                            <#assign itemData = { "title": item.title, "text": item.shortsummary } />

                                            <@hst.link hippobean=item var="itemLink" />
                                            <#assign itemData += { "link": itemLink } />

                                            <@fmt.formatDate value=item.effectiveDate.startDate.time type="Date" pattern="MMMM yyyy" var="startdate" timeZone="${getTimeZone()}" />
                                            <#assign itemData += { "status": effectivedatestatus[item.effectiveDate.status], "date": startdate } />
                                            <#assign itemData += { "standards": item.standards } />

                                            <#assign markers = [] />
                                            <#list item.markers as marker>
                                                <#assign markers += [roadmapcategories[marker]] />
                                            </#list>
                                            <#assign itemData += { "markers": markers } />

                                            <@roadmapItemBox itemData></@roadmapItemBox>
                                        </#list>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>

        </div>
    </div>
</article>
