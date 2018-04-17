<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<@metaTags></@metaTags>

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<article class="article article--event">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
    <div class="local-header article-header article-header--detailed">
        <div class="grid-wrapper">
            <div class="article-header__inner">
                <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>

                <hr class="hr hr--short hr--light">

                <#-- [FTL-BEGIN] List of date ranges -->
                <#if document.events?size gt 0>
                
                <div class="article-header__detail-lines">
                    <#list document.events as event>
                    
                    <div>
                        <h4 class="article-header__detail-line-title">#${event?counter}</h4>

                        <div class="article-header__detail-line">
                            <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableStartDate"/>
                            <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableEndDate"/>
                            
                            <#if comparableStartDate == comparableEndDate>
                            <dl class="article-header__detail-list">
                                <dt class="article-header__detail-list-key">Date:</dt>
                                <dd class="article-header__detail-list-value" data-uipath="">
                                    <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy"/></span>
                                </dd>
                            </dl>
                            <#else>
                            <dl class="article-header__detail-list">
                                <dt class="article-header__detail-list-key">Date Range:</dt>
                                <dd class="article-header__detail-list-value" data-uipath="">
                                    <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy"/> - <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="EEEE d MMMM yyyy"/></span>
                                </dd>
                            </dl>
                            </#if>
                        </div>

                        <div class="article-header__detail-line">
                            <dl class="article-header__detail-list">
                                <dt class="article-header__detail-list-key">Time:</dt>
                                <dd class="article-header__detail-list-value" data-uipath="">
                                    <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="hh:mm a"/> to <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="hh:mm a"/></span>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    </#list>
                </div>
                </#if>
                <#-- [FTL-END] List of date ranges -->

                <div class="article-header__detail-lines">
                    <div class="article-header__detail-line">
                        <dl class="article-header__detail-list">
                            <dt class="article-header__detail-list-key">Location:</dt>
                            <dd class="article-header__detail-list-value" data-uipath="">
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

                <div class="article-sections">
                    <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.booking) />
                    <a class="button" href="${document.booking}" onClick="${onClickMethodCall}">Book Now</a>
                </div>

                <#-- [FTL-BEGIN] 'Body' section -->
                <div class="article-section no-border">
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
