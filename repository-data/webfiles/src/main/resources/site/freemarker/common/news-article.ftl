<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="news-article.labels"/>

<article class="article article--news">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article__label">
                    <@fmt.message key="news-article.labels.type"/>
                </div>

                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>

                <div class="article__date">
                    <@fmt.formatDate value=document.publisheddatetime.time type="Date" pattern="d MMMM yyyy" var="publishedDateTimeString"/>
                    <@fmt.formatDate value=document.publisheddatetime.time type="Date" pattern="yyyy-MM-dd HH:mm:ss" var="publishedDateTime"/>
                    <time datetime="${publishedDateTime}">${publishedDateTimeString}</time>
                </div>

                <div class="article__warning">
                    <div class="callout callout--warning callout--muted">
                        <div class="grid-row">
                            <div class="column column--two-thirds column--reset">
                                <@fmt.message key="news-article.labels.copyright-warning"/>
                            </div>
                        </div>
                    </div>
                </div>

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
                    <h2><@fmt.message key="news-article.labels.related-documents"/></h2>
                    <ul class="list">
                        <#list document.relateddocuments as relatedDocument>
                        <li>
                            <a href="${relatedDocument.selfLink}" onClick="logGoogleAnalyticsEvent('document click','Event','${document.selfLink}');" title="${relatedDocument.title}">${relatedDocument.title}</a>
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
