<#ftl output_format="HTML">
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.News" -->
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.news"/>

<article class="article article--news">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <#if document.type?has_content>
                <div class="article__label">
                    <#-- ${newsTypeMap[document.type]} -->
                </div>
                </#if>

                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>

                <div class="article__date">
                    <@fmt.formatDate value=document.publisheddatetime.time type="Date" pattern="d MMMM yyyy" var="publishedDateTimeString" timeZone="${getTimeZone()}" />
                    <@fmt.formatDate value=document.publisheddatetime.time type="Date" pattern="yyyy-MM-dd HH:mm:ss" var="publishedDateTime" timeZone="${getTimeZone()}" />
                    <time datetime="${publishedDateTime}">${publishedDateTimeString}</time>
                </div>

                <#if document.creditBanner?has_content>
                <div class="article__warning" role="alert">
                    <div class="callout callout--warning callout--muted">
                        <div class="grid-row">
                            <div class="column column--reset">
                                ${creditbanner[document.creditBanner]}
                            </div>
                        </div>
                    </div>
                </div>
                </#if>

                <#-- [FTL-BEGIN] 'Body' section -->
                <div class="article-section article-section--reset-top">
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

                <#-- [FTL-BEGIN] Optional 'Related links' section -->
                <#if document.relateddocuments?has_content>
                <div class="article-section" id="section-related-links">
                    <h2><@fmt.message key="headers.related-documents"/></h2>
                    <ul class="list">
                        <#list document.relateddocuments as relatedDocument>
                        <li>
                            <@hst.link hippobean=relatedDocument var="newslink"/>
                            <a href="${newslink}" onClick="logGoogleAnalyticsEvent('document click','Event','${newslink}');" onKeyUp="return vjsu.onKeyUp(event)" title="${relatedDocument.title}">${relatedDocument.title}</a>
                        </li>
                        </#list>
                    </ul>
                </div>
                </#if>
                <#-- [FTL-END] Optional 'Related links' section -->

            </div>
        </div>
    </div>
</article>
