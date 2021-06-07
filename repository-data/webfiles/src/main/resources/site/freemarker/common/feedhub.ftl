<#ftl output_format="HTML">


<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/personitem.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<#assign hasLatestFeed = document.latestFeed?? && document.latestFeed?has_content />

<article itemscope itemtype="http://schema.org/Blog">
    <#if document.title?has_content>
        <div class="is-hidden" itemprop="publisher" itemscope itemtype="http://schema.org/Organization"><span itemprop="name">${document.title}</span></div>
    </#if>
    <#if document.seosummary?has_content><div class="is-hidden" itemprop="description">${document.seosummary.content?replace('<[^>]+>','','r')}</div></#if>
    ${document.feedType}<br/>
    ${document.hubType}
    <#if hasLatestFeed>
        <#list document.latestFeed as latest>

            ${latest.title}
        </#list>
    </#if>
</article>
