<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Event" -->
<#-- @ftlvariable name="event" type="uk.nhs.digital.website.beans.Interval" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<#include "../common/macro/fileIconByMimeType.ftl">
<#include "macro/relatedarticles.ftl">
<#include "macro/contentPixel.ftl">
<#include "./macro/stickyNavSections.ftl">

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>
<@metaTags></@metaTags>

<#macro schemaMeta title startTimeData = '' endTimeData = ''>
    <#-- [BEGIN] Schema microdata -->
    <meta itemprop="name" content="${title}">
    <link itemprop="url" href="${getDocumentUrl()}" />
    <meta itemprop="description" content="${document.seosummary.content?replace('<[^>]+>','','r')}">
    <meta itemprop="location" content="${document.location}">

    <#if startTimeData?has_content && startTimeData?is_date>
        <@fmt.formatDate value=startTimeData type="date" pattern="yyyy-MM-dd" var="startDate" timeZone="${getTimeZone()}" />
        <@fmt.formatDate value=startTimeData type="time" pattern="HH:mm:ss" var="startDateTime" timeZone="${getTimeZone()}" />
        <meta itemprop="startDate" content="${startDate}T${startDateTime}">
    </#if>

    <#if endTimeData?has_content && endTimeData?is_date>
        <@fmt.formatDate value=endTimeData type="date" pattern="yyyy-MM-dd" var="endDate" timeZone="${getTimeZone()}" />
        <@fmt.formatDate value=endTimeData type="time" pattern="HH:mm:ss" var="endDateTime" timeZone="${getTimeZone()}" />
        <meta itemprop="endDate" content="${endDate}T${endDateTime}">
    </#if>

    <#if summaryImage??>
        <meta itemprop="image" content="${summaryImage}">
    </#if>
    <#-- [END] Schema microdata -->
</#macro>

<#assign hasSessions = document.events?size gte 1 />
<#assign hasFutureEvent = false>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#if !hasSessions>
<article class="article article--event" itemscope itemtype="http://schema.org/Event" aria-label="Document Header">
<#else>
<article class="article article--event" aria-label="Document Header">
</#if>
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
                    <hr class="hr hr--short hr--light">
                    <div class="tabbed-detail-list">
                        <#if hasSessions>
                            <#-- [FTL-BEGIN] List of date ranges -->
                            <#list document.events as event>
                                <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableStartDate" timeZone="${getTimeZone()}" />
                                <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableEndDate"  timeZone="${getTimeZone()}"/>
                                <#assign validDate = (comparableStartDate?? && comparableEndDate??) />
                                <#if event.enddatetime.time?date gt .now?date>
                                    <#assign hasFutureEvent = true>
                                </#if>
                                <div itemscope itemtype="http://schema.org/Event" class="tabbed-detail-wrapper">
                                    <#if document.events?size gt 1 && validDate>
                                        <span class="tabbed-detail-title list-title">Session ${event?counter}</span>
                                    </#if>
                                    <#if validDate>
                                        <div class="tabbed-detail">
                                            <#if comparableStartDate == comparableEndDate>
                                                <dl class="tabbed-detail__wrapper">
                                                    <dt class="tabbed-detail__key">Date:</dt>
                                                    <dd class="tabbed-detail__value" data-uipath="">
                                                        <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" /></span>
                                                    </dd>
                                                </dl>
                                            <#else>
                                                <dl class="tabbed-detail__wrapper">
                                                    <dt class="tabbed-detail__key">Date:</dt>
                                                    <dd class="tabbed-detail__value" data-uipath="">
                                                        <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" /> - <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" /></span>
                                                    </dd>
                                                </dl>
                                            </#if>
                                        </div>
                                        <div class="tabbed-detail">
                                            <dl class="tabbed-detail__wrapper">
                                                <dt class="tabbed-detail__key">Time:</dt>
                                                <dd class="tabbed-detail__value" data-uipath="">
                                                    <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="h:mm a" timeZone="${getTimeZone()}" /> to <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="h:mm a" timeZone="${getTimeZone()}" /></span>
                                                </dd>
                                            </dl>
                                        </div>
                                        <@schemaMeta "${document.title}" event.startdatetime.time?date event.enddatetime.time?date_if_unknown />
                                    </#if>
                                </div>
                            </#list>
                            <#-- [FTL-END] List of date ranges -->
                        <#else>
                            <@schemaMeta "${document.title}" />
                        </#if>
                        <#-- [FTL-BEGIN] Location -->
                        <#if document.events?size gt 1>
                            <div class="tabbed-detail tabbed-detail--pushed">
                        <#else>
                        <div class="tabbed-detail">
                        </#if>
                            <dl class="tabbed-detail__wrapper">
                                <dt class="tabbed-detail__key">Location:</dt>
                                <dd class="tabbed-detail__value" data-uipath="">
                                    <span>${document.location}</span>
                                    <#if document.maplocation?has_content>
                                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.maplocation) />
                                        <span>
                                            <a href="${document.maplocation}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">View Map</a>
                                        </span>
                                    </#if>
                                </dd>
                            </dl>
                        </div>
                        <#-- [FTL-END] Location -->
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSessions && hasFutureEvent && document.booking?has_content>
                    <div class="article-section">
                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.booking) />
                        <a class="button" href="${document.booking}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">Book Now</a>
                    </div>
                </#if>
                <#if document.summaryimage?? && document.summaryimage.original??>
                    <div class="article-section no-border no-top-margin">
                        <@hst.link hippobean=document.summaryimage.original fullyQualified=true var="summaryImage" />
                        <img src="${summaryImage}" alt="${document.title}" />
                    </div>
                </#if>
            </div>
        </div>
    </div>
</article>
<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav" class="check">
                    <#assign links = [ {"title": "Top Of Page", "url": "#"}, {"title": "Over View", "url": "#overview"}, {"title": "Component", "url": "#component"}, {"title": "How we keep patient data safe", "url": "#"}, {"title": "How long we keep patient data", "url": "#"}, {"title": "How to access NDRS data", "url": "#"}, {"title": "Who we share patient data with", "url": "#"}] />
                    <@stickyNavSections getStickySectionNavLinks({"sections" : links}) />
                </div>
                <!-- end sticky-nav -->
            </div>
            <div class="column column--two-thirds page-block page-block--main">
                <#-- [FTL-BEGIN] 'Body' section -->
                <div class="article-section" id="how-we-use-patient-data">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="rich-text-content"><div id="overview">
                                <#if document.body?has_content??>
                                <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                            <div id="component">
                                <#if document.component?has_content??>
                                    <@hst.html hippohtml=document.component contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                       </div>
                    </div>
                </div>
            </div>
                <#-- [FTL-END] 'Body' section -->

                <#if document.extAttachments?has_content>
                    <div class="article-section" id="resources">
                        <h2>Resources</h2>
                        <ul class="list list--reset">
                            <#list document.extAttachments as attachment>
                                <li class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                                    <@externalstorageLink attachment.resource; url>
                                        <a title="${attachment.text}"
                                           href="${url}"
                                           class="block-link"
                                           onClick="logGoogleAnalyticsEvent('Download attachment','Event','${url}');"
                                           onKeyUp="return vjsu.onKeyUp(event)"
                                           itemprop="contentUrl">
                                            <div class="block-link__header">
                                                <@fileIconByMimeType attachment.resource.mimeType></@fileIconByMimeType>
                                            </div>
                                            <div class="block-link__body">
                                                <span class="block-link__title" itemprop="name">${attachment.text}</span>
                                                <@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType></@fileMetaAppendix>
                                            </div>
                                        </a>
                                    </@externalstorageLink>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>
                <#if document.relatedDocuments?has_content>
                  <@relatedarticles document.relatedDocuments "Event" false></@relatedarticles>
                </#if>
                <#include "shared/sections.ftl"/>
                <#include "shared/child-documents.ftl"/>
            </div>
        </div>
    </div>
</article>
