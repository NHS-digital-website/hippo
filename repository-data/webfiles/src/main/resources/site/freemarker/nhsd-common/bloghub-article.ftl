<#ftl output_format="HTML">

<#-- @ftlvariable name="bloghub" type="uk.nhs.digital.website.beans.BlogHub" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/showAll.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/heroes/hero.ftl">
<#include "macro/hubArticle.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign itemsMaxCount=10/>

<#assign hasLatestBlogs = document.latestBlogs?? && document.latestBlogs?has_content />
<#assign hasLatestFeatures = document.latestFeatures?? && document.latestFeatures?has_content />
<#assign hasSummaryContent = document.summary?? && document.summary?has_content />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article itemscope itemtype="http://schema.org/Blog">
    <#if document.publisher?has_content>
        <div class="nhsd-!t-display-hide" itemprop="publisher" itemscope itemtype="http://schema.org/Organization"><span itemprop="name">${document.publisher.title}</span></div>
    </#if>
    <#if document.seosummary?has_content><div class="nhsd-!t-display-hide" itemprop="description">${document.seosummary.content?replace('<[^>]+>','','r')}</div></#if>
    <#if document.taxonomyTags?has_content><span class="nhsd-!t-display-hide" itemprop="keywords" ><#list document.taxonomyTags as tag>${tag} <#sep>, </#list></span></#if>

    <#if document.leadImage?has_content>
        <@hst.link hippobean=document.leadImage.original fullyQualified=true var="blogImage" />
        <div class="nhsd-!t-display-hide" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
            <meta itemprop="url" content="${blogImage}">
            <span itemprop="contentUrl" >${blogImage}</span>
        </div>
    </#if>

    <#if document.subject?has_content><span class="nhsd-!t-display-hide" itemprop="about">${document.subject.title}</span></#if>

    <#assign heroOptions = getHeroOptions(document) />
    <#assign heroType = heroOptions.image?has_content?then("image", "default")>
    <@hero heroOptions heroType />

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <#if hasLatestFeatures>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <@hubArticles document.latestFeatures />
                </div>
            </div>
        </#if>
        <#if hasLatestBlogs>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <@hubArticles document.latestBlogs />
                </div>
            </div>
        </#if>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col nhsd-!t-margin-bottom-6">
                <#if document.issn?has_content>
                    ISSN number: <span itemprop="issn">${document.issn}</span>
                </#if>
            </div>
        </div>
    </div>
</article>
