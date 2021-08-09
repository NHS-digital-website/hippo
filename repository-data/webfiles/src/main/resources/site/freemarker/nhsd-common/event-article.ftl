<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Event" -->
<#-- @ftlvariable name="event" type="uk.nhs.digital.website.beans.Interval" -->

<#include "../include/imports.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/documentIcon.ftl">
<#include "macro/component/downloadBlockAsset.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/relatedarticles.ftl">
<#include "macro/heroes/hero.ftl">

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
<#assign hasSummaryImage = document.summaryimage?? && document.summaryimage.original?? />
<#assign hasBodyContent = document.body?has_content?? />
<#assign hasExtAttachments = document.extAttachments?has_content />
<#assign hasRelatedDocuments = document.relatedDocuments?has_content />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#if !hasSessions>
<article itemscope itemtype="http://schema.org/Event" aria-label="Document Header">
<#else>
<article aria-label="Document Header">
</#if>
    <@hero {
        "title": document.title,
        "colour": "darkBlue"
    }>
        <div class="nhsd-!t-margin-top-6">
            <#if hasSessions>
                <#-- [FTL-BEGIN] List of date ranges -->
                <#list document.events as event>

                    <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableStartDate" timeZone="${getTimeZone()}" />
                    <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableEndDate"  timeZone="${getTimeZone()}"/>
                    <#assign validDate = (comparableStartDate?? && comparableEndDate??) />
                    <#if event.enddatetime.time?date gt .now?date>
                        <#assign hasFutureEvent = true>
                    </#if>

                    <div itemscope itemtype="http://schema.org/Event">
                        <#if document.events?size gt 1 && validDate>
                            <p class="nhsd-t-heading-s nhsd-!t-col-white">Session ${event?counter}</p>
                        </#if>

                        <#if validDate>
                            <div class="nhsd-o-hero__meta-data nhsd-!t-margin-bottom-6">
                                <div class="nhsd-o-hero__meta-data-item">
                                    <div class="nhsd-o-hero__meta-data-item-title">Date:</div>
                                    <div class="nhsd-o-hero__meta-data-item-description" data-uipath="">
                                        <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" />
                                        <#if comparableStartDate != comparableEndDate>
                                            - <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="EEEE d MMMM yyyy" timeZone="${getTimeZone()}" />
                                        </#if>
                                    </div>
                                </div>

                                <div class="nhsd-o-hero__meta-data-item">
                                    <div class="nhsd-o-hero__meta-data-item-title">Time:</div>
                                    <div class="nhsd-o-hero__meta-data-item-description" data-uipath="">
                                        <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="h:mm a" timeZone="${getTimeZone()}" /> to <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="h:mm a" timeZone="${getTimeZone()}" />
                                    </div>
                                </div>
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
            <div class="nhsd-o-hero__meta-data">
                <div class="nhsd-o-hero__meta-data-item">
                    <div class="nhsd-o-hero__meta-data-item-title">Location:</div>
                    <div class="nhsd-o-hero__meta-data-item-description" data-uipath="">
                        <span>${document.location}</span>
                        <#if document.maplocation?has_content>
                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.maplocation) />
                            <span>
                                <a class="nhsd-a-link nhsd-a-link nhsd-a-link--col-white" href="${document.maplocation}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">View Map</a>
                            </span>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </@hero>

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">

                <#if hasSessions && hasFutureEvent && document.booking?has_content>
                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.booking) />
                    <a class="nhsd-a-button" href="${document.booking}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)" rel="external">
                        <span class="nhsd-a-button__label">Book Now</span>
                    </a>
                </#if>

                <#if hasSummaryImage>
                    <div class="nhsd-a-image nhsd-!t-margin-bottom-6">
                        <picture class="nhsd-a-image__picture">
                            <@hst.link hippobean=document.summaryimage.original fullyQualified=true var="summaryImage" />
                            <img src="${summaryImage}" alt="${document.title}">
                        </picture>
                    </div>
                </#if>

                <#if hasBodyContent>
                    <#if hasSummaryImage>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <@hst.html hippohtml=document.body contentRewriter=brContentRewriter/>
                </#if>

                <#if hasExtAttachments>
                    <#if hasBodyContent || hasSummaryImage>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                    <div id="resources">
                        <p class="nhsd-t-heading-xl">Resources</p>
                        <#list document.extAttachments as attachment>
                            <#--  Download macro cannot be used due to different yaml config -->
                            <#assign iconTypeFromMime = getFormatByMimeType("${attachment.resource.mimeType?lower_case}") />
                            <#assign fileName = attachment.resource.filename />
                            <#assign fileSize = sizeToDisplay(attachment.resource.length) />

                            <div itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                                <@externalstorageLink attachment.resource; url>
                                    <div class="nhsd-m-download-card nhsd-!t-margin-bottom-6">
                                        <a class="nhsd-a-box-link"
                                            href="${url}"
                                            title="${attachment.text}"
                                            onClick="logGoogleAnalyticsEvent('Download attachment','Event','${url}');"
                                            onKeyUp="return vjsu.onKeyUp(event)"
                                            itemprop="contentUrl"
                                            aria-label="${attachment.text}"
                                        >
                                            <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                                <div class="nhsd-m-download-card__image-box">
                                                    <@documentIcon "${iconTypeFromMime}"/>
                                                </div>

                                                <div class="nhsd-m-download-card__content-box">
                                                    <p class="nhsd-t-heading-s nhsd-!t-margin-bottom-2" itemprop="name">${attachment.text}</p>

                                                    <div class="nhsd-m-download-card__meta-tags">
                                                        <#assign fileFormat = iconTypeFromMime />
                                                        <#if fileName != "">
                                                            <#assign fileFormat = getFileExtension(fileName?lower_case) />
                                                        </#if>
                                                        <span class="nhsd-a-tag nhsd-a-tag--meta">${fileFormat}</span>
                                                        <span class="nhsd-a-tag nhsd-a-tag--meta-light">${fileSize}</span>
                                                    </div>

                                                    <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-arrow--down nhsd-a-icon--size-s">
                                                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                            <path d="M15,8.5L8,15L1,8.5L2.5,7L7,11.2L7,1l2,0l0,10.2L13.5,7L15,8.5z"/>
                                                        </svg>
                                                    </span>
                                                </div>
                                            </div>
                                        </a>
                                    </div>
                                </@externalstorageLink>
                            </div>
                        </#list>
                    </div>
                </#if>

                <#if hasRelatedDocuments>
                    <#if hasExtAttachments || hasBodyContent || hasSummaryImage>
                        <hr class="nhsd-a-horizontal-rule" />
                    </#if>

                  <@relatedarticles document.relatedDocuments "Event" false></@relatedarticles>
                </#if>
            </div>
        </div>
    </div>
</article>
