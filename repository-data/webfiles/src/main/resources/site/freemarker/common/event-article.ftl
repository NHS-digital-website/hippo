<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>
<#assign timeZone = "Europe/London" />

<article class="article article--event">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>

                    <hr class="hr hr--short hr--light">

                    <div class="tabbed-detail-list">
                        <#-- [FTL-BEGIN] List of date ranges -->
                        <#list document.events as event>
                        
                        <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableStartDate"/>
                        <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableEndDate"/>
                        <#assign validDate = (comparableStartDate?? && comparableEndDate??) />

                        <#if document.events?size gt 1 && validDate>
                        <h4 class="tabbed-detail-title">Session ${event?counter}</h4>
                        </#if>   

                        <#if validDate>
                        <div class="tabbed-detail">                            
                            <#if comparableStartDate == comparableEndDate>
                            <dl class="tabbed-detail__wrapper">
                                <dt class="tabbed-detail__key">Date:</dt>
                                <dd class="tabbed-detail__value" data-uipath="">
                                    <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy" /></span>
                                </dd>
                            </dl>
                            <#else>
                            <dl class="tabbed-detail__wrapper">
                                <dt class="tabbed-detail__key">Date Range:</dt>
                                <dd class="tabbed-detail__value" data-uipath="">
                                    <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy" /> - <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="EEEE d MMMM yyyy" /></span>
                                </dd>
                            </dl>
                            </#if>
                        </div>

                        <div class="tabbed-detail">
                            <dl class="tabbed-detail__wrapper">
                                <dt class="tabbed-detail__key">Time:</dt>
                                <dd class="tabbed-detail__value" data-uipath="">
                                    <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="h:mm a" timeZone="${timeZone}"/> to <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="h:mm a" timeZone="${timeZone}"/></span>
                                </dd>
                            </dl>
                        </div>
                        </#if>
                        </#list>
                        <#-- [FTL-END] List of date ranges -->


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
                                            <a href="${document.maplocation}" onClick="${onClickMethodCall}">View Map</a>
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
                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>

                <div class="article-section">
                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.booking) />
                    <a class="button" href="${document.booking}" onClick="${onClickMethodCall}">Book Now</a>
                </div>

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

            </div>
        </div>
    </div>
</article>
