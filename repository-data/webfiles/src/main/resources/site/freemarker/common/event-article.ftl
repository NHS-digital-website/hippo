<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="site.website.labels"/>
<@fmt.message key="child-pages-section.title" var="childPagesSectionTitle"/>

<article class="article article--event">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar page-block--sticky-top">
                <div class="article-section-nav">
                    <h2 class="article-section-nav__title">Side nav coming...</h2>
                    <hr>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>

                <div class="article-section">
                    <div>
                        <#if document.events?size gt 0>
                        <ul class="list list--reset">
                        <#list document.events as event>
                            <li>
                                <h3>Event #${event?counter}</h3>
                                <@fmt.formatDate value=event.startdatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableStartDate"/>
                                <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="yyyy-MM-dd" var="comparableEndDate"/>

                                <#if comparableStartDate == comparableEndDate>
                                <span>Date:</span> <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy"/></span>
                                <#else>
                                <span>Date Range:</span> <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="EEEE d MMMM yyyy"/> - <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="EEEE d MMMM yyyy"/></span>
                                </#if>
                                <br>
                                <span>Time:</span> <span><@fmt.formatDate value=event.startdatetime.time type="Date" pattern="hh:mm a"/> to <@fmt.formatDate value=event.enddatetime.time type="Date" pattern="hh:mm a"/></span>
                                <hr>
                            </li>
                            </#list>
                        </ul>
                        </#if>
                    </div>

                    <div>
                        <p>Location: ${document.location}
                        <#if document.maplocation?has_content>
                            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.maplocation) />
                            <a href="${document.maplocation}" onClick="${onClickMethodCall}">View Map</a>
                        </#if>
                        </p>
                    </div>

                    <div>
                        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, document.booking) />
                        <a class="button" href="${document.booking}" onClick="${onClickMethodCall}">Book Now</a>
                    </div>
                </div>

                <#-- [FTL-BEGIN] 'Body' section -->
                <div class="article-section article-section--reset-tops">
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
