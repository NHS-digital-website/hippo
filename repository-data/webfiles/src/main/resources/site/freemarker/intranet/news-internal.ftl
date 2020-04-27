<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/scripts/line-clamp-polyfill.js.ftl">
<#include "macro/metaTags.ftl">
<#include "../common/macro/headerMetadata.ftl">
<#include "../common/macro/documentHeader.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "macro/googleTags.ftl">

<@googleTags documentBean=document pageSubject='Services, teams, tasks, or things this news is about'/>
<#-- Add meta tags -->
<#assign pageShortSummary = document.shortsummary />
<@metaTags></@metaTags>

<@hst.setBundle basename="intranet.news" />

<#assign hasLeadImage = document.leadImageSection?has_content && document.leadImageSection.leadImage?has_content/>
<#assign hasLeadImageAltText = hasLeadImage && document.leadImageSection.alttext?has_content/>
<#assign hasLeadImageCaption = hasLeadImage && document.leadImageSection.imagecaption.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasRelatedDocuments = document.relatedDocuments?has_content />
<#assign hasLatestNews = document.latestNews?? && document.latestNews?has_content />
<#assign hasIntroductionContent = document.optionalIntroductoryText?? && document.optionalIntroductoryText?has_content />

<#assign metaData = [] />
<#assign metaData += [{"key": "Date","value": document.publicationDate.time,"uipath": "website.news.dateofpublication","type": "date","schemaOrgTag": "" }] />
<#if document.keys?has_content>
    <#assign metaData += [{"key":"Topics", "value": document.keys?join(", "), "uipath": "website.news.taxonomy", "schemaOrgTag": "", "type": ""}] />
</#if>

<article class="article article--news">
    <@documentHeader document 'news' "" "" "" "" false metaData true></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">

                <#if hasIntroductionContent>
                    <div class="article-section no-border article-section--introduction">
                        <div class="rich-text-content">
                            <@hst.html hippohtml=document.optionalIntroductoryText contentRewriter=gaContentRewriter/>
                        </div>
                    </div>
                </#if>

                <#if hasLeadImage>
                    <div class="lead-image-container" >
                        <div class="lead-image">
                            <@hst.link hippobean=document.leadImageSection.leadImage.original fullyQualified=true var="leadImage" />
                            <img src="${leadImage}" alt="<#if hasLeadImageAltText>${document.leadImageSection.alttext}</#if>" />
                        </div>
                        <#if hasLeadImageCaption>
                            <div class="lead-image-caption" data-uipath="website.blog.leadimagecaption">
                                <@hst.html hippohtml=document.leadImageSection.imagecaption contentRewriter=gaContentRewriter/>
                            </div>
                        </#if>
                    </div>
                </#if>

                <#if hasSectionContent>
                    <div class="article-section">
                        <@sections document.sections />
                    </div>
                </#if>

                <#if hasRelatedDocuments>
                    <div class="article-section">
                        <h2><@fmt.message key="headers.relatedPages" /></h2>
                        <#list document.relatedDocuments as child>
                            <div class="article-section-top-margin">
                                <@downloadBlock child />
                            </div>
                        </#list>
                    </div>
                </#if>
            </div>

            <#if hasLatestNews>
                <div class="column column--one-third">
                    <div class="news-latest-news">
                        <div class="news-latest-news-title bottom-margin-20"><@fmt.message key="headers.latestNews" /></div>
                        <ul class="intra-home-article-grid">
                            <#list document.latestNews as news>
                                <#assign hasShortSummary = news.shortsummary?? && news.shortsummary?has_content />

                                <li class="intra-home-article-grid-item intra-home-article-grid-item--full">
                                    <article class="intra-home-article-grid-article">
                                        <div class="intra-home-article-grid-article__header">
                                            <span class="intra-info-tag">News</span>
                                            <#if news.leadImageSection?has_content && news.leadImageSection.leadImage?has_content>
                                                <@hst.link hippobean=news.leadImageSection.leadImage.original fullyQualified=true var="leadImage" />
                                                <img src="${leadImage}" alt="<#if hasLeadImageAltText>${news.leadImageSection.alttext}</#if>" />
                                            <#else>
                                                <img src="<@hst.webfile path="/images/fibre_57101102_med.jpg"/>" alt="Lead image for ${news.title}" aria-hidden="true">
                                            </#if>
                                        </div>
                                        <div class="intra-home-article-grid-article__contents">
                                            <h1 class="intra-home-article-grid-article__title">
                                                <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, news.title) />
                                                <a href="<@hst.link hippobean=news/>" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${news.title}</a>
                                            </h1>

                                            <#if hasShortSummary>
                                                <div id="latest-article-${news?index}" class="intra-home-article-grid-article__summary">
                                                    ${news.shortsummary}
                                                </div>
                                                <#-- call function webkitLineClamp(element, lineCount, colour) to implement multiline ellipsis -->
                                                <script>
                                                    webkitLineClamp(document.getElementById("latest-article-${news?index}"), 3, "black");
                                                </script>
                                            </#if>

                                            <span class="intra-home-article-grid-article__date">
                                                <@fmt.formatDate value=news.publicationDate.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                                            </span>
                                        </div>
                                    </article>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </div>
            </#if>
        </div>
    </div>
</article>
<#assign useIntraLogo = true />
<#include "../common/scripts/print-pdf.js.ftl">
