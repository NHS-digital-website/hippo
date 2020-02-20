<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/stickyNavYears.ftl">
<#include "macro/stickyNavTags.ftl">
<#include "macro/hubBox.ftl">
<#include "macro/stickyGroupBlockHeader.ftl">
<#include "macro/documentHeader.ftl">

<@hst.setBundle basename="rb.generic.headers,rb.component.latest-events"/>
<@fmt.message key="headers.past-events" var="pastEventsHeader" />

<#assign overridePageTitle><@fmt.message key="headers.hub-title" /></#assign>
<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

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

<#-- Return the filter navigation links for the year -->
<#function getFilterYearLinks>
    <#assign links = [] />

    <#list years?keys as key>
        <#assign typeCount = years?size />
        <#assign links += [{ "key" : key, "title": key }] />
    </#list>

    <#return links />
</#function>

<article class="article article--latest-events">


    <#assign header_title><@fmt.message key="headers.hub-title" /></#assign>
    <#assign header_summary><@fmt.message key="texts.hub-intro" /></#assign>
    <#assign header_icon = 'images/icon-calendar.png' />
    <#assign document = "simulating_doc" />

    <@documentHeader document 'events' header_icon header_title header_summary></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <div class="inner-wrapper-sticky">
                        <#-- Year filter component -->
                        <#if getFilterYearLinks()?size gt 0>
                            <#assign affix = selectedTypes?has_content?then("&type=" + selectedTypes?join("&type="), "") />
                            <div class="article-section-nav-wrapper" data-hub-filter-type="nhsd-hub-tag-filter" data-hub-filter-key="year">
                                <@stickyNavYears getFilterYearLinks() affix></@stickyNavYears>
                            </div>
                        </#if>

                        <#-- Type filter component -->
                        <#assign tags = [] />
                        <#if typeGroupHash?has_content>
                            <#list typeGroupHash?keys as key>
                                <#assign typeCount = typeGroupHash[key]?size />
                                <#assign tags += [{ "key" : key, "title": eventstype[key] + " (${typeCount})" }] />
                            </#list>
                        <#else>
                            <#list selectedTypes as key>
                                <#assign tags += [{ "key" : key, "title": key?cap_first?replace("-", " ") + " (0)" }] />
                            </#list>
                        </#if>
                        <#assign affix = "&year=" + selectedYear />
                        <div class="article-section-nav-wrapper" data-hub-filter-type="nhsd-hub-tag-filter" data-hub-filter-key="type">
                            <@stickyNavTags tags affix "Filter by type" "type" selectedTypes></@stickyNavTags>
                        </div>

                        <#-- Month anchor nav -->
                        <#assign links = [] />
                        <#list monthsOfTheYear() as month>
                            <#if eventGroupHash[month]??>
                                <#assign links += [{ "url": "#" + slugify(month), "title": month, "aria-label": "Jump to events starting in ${month}" }] />
                            </#if>
                        </#list>
                        <div class="article-section-nav-wrapper" id="hub-search-page-contents">
                            <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
                        </div>
                    </div>
                </div>

                <#-- Restore the bundle -->
                <@hst.setBundle basename="rb.generic.headers,rb.component.latest-events"/>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="hub-box-list" id="hub-search-results">
                <#if eventGroupHash?has_content>
                    <#list monthsOfTheYear() as month>
                        <#if eventGroupHash[month]??>
                            <div class="article-section article-section--letter-group no-border" id="${slugify(month)}">
                                <@stickyGroupBlockHeader month></@stickyGroupBlockHeader>
                                <div class="grid-row">
                                    <div class="column column--reset">
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
                        </#if>
                    </#list>
                <#else>
                    <div class="article-section">
                        <h2 class="article-header__title"><@fmt.message key="headers.no-results" /></h2>
                        <p>Would you like to <a href="${getDocumentUrl()}" aria-label="Clear filters" title="Clear filters">clear the filters</a>?</p>
                    </div>
                </#if>
                </div>

                <div class="article-section no-border no-top-margin" id="hub-search-pagination">
                    <#if pageable.totalPages gt 1>
                        <#include "../include/pagination.ftl">
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>
