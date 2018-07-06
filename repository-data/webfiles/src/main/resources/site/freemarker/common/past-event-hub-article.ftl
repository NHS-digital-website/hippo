<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/tagNav.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">

<@hst.setBundle basename="rb.doctype.event-hub"/>
<@fmt.message key="headers.past-events" var="pastEventsHeader" />


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

    <#list monthsOfTheYear?reverse as month>
        <#if eventGroupHash[month]??>
            <#assign links += [{ "url": "#" + slugify(month), "title": month, "aria-label": "Jump to events starting in ${month}" }] />
        </#if>
    </#list>

    <#return links />
</#function>

<#-- Return the filter navigation links for the type -->
<#function getFilterNavLinks>
    <#assign links = [] />

    <#list typeGroupHash?keys as key>
        <#assign typeCount = typeGroupHash[key]?size />
        <#assign links += [{ "url": "?type=" + key, "title": eventstype[key] + " (${typeCount})" }] />
    </#list>

    <#return links />
</#function>

<article class="article article--latest-events">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--one-third column--reset">
                            <img src="<@hst.webfile path="images/icon-calendar.png" fullyQualified=true/>" alt="Calendar">
                        </div>
                        <div class="column--two-thirds column--reset">
                            <h1 class="local-header__title" data-uipath="document.title"><@fmt.message key="headers.past-events"/></h1>
                            <p class="article-header__subtitle"><@fmt.message key="texts.past-events"/></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <#if eventGroupHash?has_content && eventGroupHash?size gt 1>
                <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                    <@sectionNav getSectionNavLinks()></@sectionNav>

                    <@tagNav getFilterNavLinks()></@tagNav>
                </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#if eventGroupHash?has_content>
                    <#list monthsOfTheYear?reverse as month>
                        <#if eventGroupHash[month]??>
                            <div class="article-section article-section--letter-group no-border">
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
