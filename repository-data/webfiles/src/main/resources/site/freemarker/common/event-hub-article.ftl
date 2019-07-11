<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/yearNav.ftl">
<#include "macro/tagNav.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">

<@hst.setBundle basename="rb.component.latest-events"/>
<@fmt.message key="headers.past-events" var="pastEventsHeader" />

<#assign overridePageTitle><@fmt.message key="headers.hub-title" /></#assign>
<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign monthsOfTheYear = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"] />

<#--Group the events by earliest start month  -->
<#assign eventGroupHash = {} />
<#list pageable.items as item>
    <#assign dateRangeData = getEventDateRangeData(item.events) />
        <#if dateRangeData?size gt 0>
            <@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="MMMM" timeZone="${getTimeZone()}" var="key" />
            <#assign eventGroupHash = eventGroupHash + {  key : (eventGroupHash[key]![]) + [ item ] } />
        </#if>
</#list>

<#--Group the events by type  -->
<#assign typeGroupHash = {} />
<#list pageable.items as item>
    <#list item.type as type>
        <#assign typeGroupHash = typeGroupHash + {  type : (typeGroupHash[type]![]) + [ item ] } />
    </#list>
</#list>

<#-- Return the section navigation links for the months -->
<#function getSectionNavLinks>
    <#assign links = [] />
    
    <#list monthsOfTheYear as month>
        <#if eventGroupHash[month]??>
            <#assign links += [{ "url": "#" + slugify(month), "title": month, "aria-label": "Jump to events starting in ${month}" }] />
        </#if>
    </#list>  
    
    <#return links />
</#function>

<#-- Return the filter navigation links for the type -->
<#function getFilterNavLinks>
    <#assign links = [] />
    <#if typeGroupHash?has_content>
        <#list typeGroupHash?keys as key>
            <#assign typeCount = typeGroupHash[key]?size />
            <#assign links += [{ "key" : key, "title": eventstype[key] + " (${typeCount})" }] />
        </#list>
        <#else>
            <#list selectedTypes as key>
                <#assign links += [{ "key" : key, "title": key?cap_first?replace("-", " ") + " (0)" }] />
            </#list>
    </#if>

    <#return links />
</#function>

<#-- Return the filter navigation links for the year -->
<#function getFilterYearLinks>
    <#assign links = [] />

    <#list years?keys as key>
        <#assign typeCount = years?size />
        <#assign links += [{ "key" : key, "title": key }] />
    </#list>

    <#return links />
</#function>

<article class="article article--latest-events" aria-label="Document Header">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/icon-calendar.png" fullyQualified=true/>" alt="Calendar">
                        </div>
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title"><@fmt.message key="headers.hub-title"/></h1>
                            <p class="article-header__subtitle"><@fmt.message key="texts.hub-intro"/></p>  
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


                    <#if getFilterYearLinks()?size gt 0>
                        <#assign affix = selectedTypes?has_content?then("&type=" + selectedTypes?join("&type="), "") />
                        <@yearNav getFilterYearLinks() affix></@yearNav>
                    </#if>

                    <@sectionNav getSectionNavLinks()></@sectionNav>

                    <#assign affix = "&year=" + selectedYear />
                    <@tagNav getFilterNavLinks() affix></@tagNav>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <#if eventGroupHash?has_content>
                    <#list monthsOfTheYear as month>
                        <#if eventGroupHash[month]??>
                            <div class="article-section article-section--letter-group no-border" id="${slugify(month)}">
                                <@stickyGroupBlockHeader month></@stickyGroupBlockHeader>

                                <div class="grid-row">
                                    <div class="column column--reset">
                                        <div class="cta-list hub-box-list">
                                            <#list eventGroupHash[month] as item>
                                                <#assign eventData = { "title": item.title, "text": item.shortsummary } />
                                                <#if item.summaryimage??>
                                                    <@hst.link hippobean=item.summaryimage.original var="backgroundImage" />
                                                    <#assign eventData += { "background": backgroundImage } />
                                                </#if>

                                                <@hst.link hippobean=item var="eventLink" />
                                                <#assign eventData += { "link": eventLink } />

                                                <#assign dateRangeData = getEventDateRangeData(item.events) />
                                                <#if dateRangeData?size gt 0>
                                                    <#if dateRangeData.comparableStartDate == dateRangeData.comparableEndDate>
                                                        <@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="eventFullDate" />
                                                        <#assign eventDate = eventFullDate>
                                                    <#else>
                                                        <@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="eventStartDate" />
                                                        <@fmt.formatDate value=dateRangeData.maxEndTime type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" var="eventEndDate" />
                                                        <#assign eventDate = eventStartDate + " - " + eventEndDate />
                                                    </#if>
                                                    <#assign eventData += { "date": eventDate } />
                                                </#if>

                                                <#assign types = [] />
                                                <#list item.type as type>
                                                    <#assign types += [eventstype[type]] />
                                                </#list>
                                                <#assign eventData += { "types": types } />

                                                <@hubBox eventData></@hubBox>
                                            </#list>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </#list>
                    <#else>
                        <h2 class="article-header__title">No Results</h2>
                        Would you like to <a href="${getDocumentUrl()}" aria-label="Clear filters" title="Clear filters">clear the filters</a>?
                </#if>

                <#if pageable.totalPages gt 1>
                    <div class="article-section no-border">
                        <#include "../include/pagination.ftl">
                    </div>
                </#if>
            </div>
        </div>
    </div>
</article>
