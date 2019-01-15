<#ftl output_format="HTML">
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Event" -->
<#include "../include/imports.ftl">
<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<#macro schemaMeta title startTimeData = '' endTimeData = ''>
    <#-- [BEGIN] Schema microdata -->
    <meta itemprop="name" content="${title}">
    <link itemprop="url" href="${getDocumentUrl()}" />
    <meta itemprop="description" content="${document.seosummary}">
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

<#if !hasSessions>
<article class="article article--event" itemscope itemtype="http://schema.org/Event">
<#else>
<article class="article article--event">
</#if>
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
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

                                        <@schemaMeta "${document.title}" event.startdatetime.time event.enddatetime.time />
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

                <#if document.booking?has_content>
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

                <#-- [FTL-BEGIN] 'Body' section -->
                <div class="article-section">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="rich-text-content">
                                <#if document.body?has_content??>
                                <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Body' section -->

                <#if document.extAttachments?has_content>
                    <div class="article-section" id="resources">
                        <h2>Resources</h2>
                        <ul class="list">
                            <#list document.extAttachments as attachment>
                                <li class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                                    <@externalstorageLink attachment.resource; url>
                                        <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)" itemprop="contentUrl"><span itemprop="name">${attachment.text}</span></a>;
                                    </@externalstorageLink>
                                    <span class="fileSize">[size: <span itemprop="contentSize"><@formatFileSize bytesCount=attachment.resource.length/></span>]</span>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>


                <#if document.relatedDocuments?has_content>
                    <div class="article-section" id="section-related-links">
                        <h2>Related Links</h2>
                        <ul class="list">
                            <#list document.relatedDocuments as relatedDocument>
                                <li>
                                    <@hst.link hippobean=relatedDocument var="newslink"/>
                                    <a href="${newslink}" onClick="logGoogleAnalyticsEvent('document click','Event','${newslink}');" onKeyUp="return vjsu.onKeyUp(event)" title="${relatedDocument.title}">${relatedDocument.title}</a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>

            </div>
        </div>
    </div>
</article>
