<#ftl output_format="HTML">

<#-- @ftlvariable name="bloghub" type="uk.nhs.digital.website.beans.BlogHub" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/component/showAll.ftl">
<#include "macro/contentPixel.ftl">
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

    <!-- metadata schema.org data - START -->
    <#if document.publisher?has_content>
      <div class="is-hidden" itemprop="publisher" itemscope itemtype="http://schema.org/Organization"><span itemprop="name">${document.publisher.title}</span></div>
    </#if>
    <#if document.seosummary?has_content><div class="is-hidden" itemprop="description">${document.seosummary.content?replace('<[^>]+>','','r')}</div></#if>
    <#if document.taxonomyTags?has_content><span class="is-hidden" itemprop="keywords" ><#list document.taxonomyTags as tag>${tag} <#sep>, </#list></span></#if>

    <#if document.leadImage?has_content>
      <@hst.link hippobean=document.leadImage.original fullyQualified=true var="blogImage" />
      <div class="is-hidden" itemprop="image" itemscope itemtype="http://schema.org/ImageObject">
        <meta itemprop="url" content="${blogImage}">
        <span itemprop="contentUrl" >${blogImage}</span>
      </div>
    </#if>

    <#if document.subject?has_content><span class="is-hidden" itemprop="about" >${document.subject.title}</span></#if>
    <!-- metadata schema.org data - END   -->
        <@documentHeader document 'bloghub'></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <#if hasLatestFeatures>
                <@hubArticle document.latestFeatures></@hubArticle>
            </#if>
            <#if hasLatestBlogs>
                <@hubArticle document.latestBlogs></@hubArticle>
            </#if>
        </div>
        <#if document.issn?has_content>
          <div class="bloghub__issn">
            ISSN number: <span itemprop="issn">${document.issn}</span>
          </div>
        </#if>
    </div>
</article>
