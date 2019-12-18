<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.VisualHub" -->

<#include "../include/imports.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/visualhubBox.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<@hst.setBundle basename="site.website.labels"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasBannerImage = document.image?has_content />
<#assign hasTopicIcon = document.pageIcon?has_content />
<#assign hasAdditionalInformation = document.additionalInformation.content?has_content />
<#assign hasLinks = document.links?? && document.links?size gt 0 />

<article class="article">
    <#if hasBannerImage>
        <@hst.link hippobean=document.image.original fullyQualified=true var="bannerImage" />
        <div class="banner-image" aria-label="Document Header" style="background-image: url(${bannerImage});">
            <div class="grid-wrapper">
                <div class="grid-row">
                    <div class="column column--reset banner-image-title">
                        <div class="banner-image-title-background">
                            <h1 data-uipath="document.title">${document.title}</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="grid-wrapper banner-image-summary">
            <div class="article-header__inner">
                <div class="grid-row">
                    <div class="column column--reset column--81-25" data-uipath="website.hub.summary">
                        <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                    </div>
                </div>
            </div>
        </div>
    <#else>
      <@documentHeader document 'hub' document.pageIcon?has_content?then(document.pageIcon, '')></@documentHeader>
    </#if>

    <#if document.introduction?has_content>
        <div class="grid-wrapper visual-hub-introduction">
            <div class="grid-row">
                <div class="column column--reset">
                    <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter />
                </div>
            </div>
        </div>
    </#if>

    <#if hasLinks>
        <div class="grid-wrapper">
            <div class="grid-row visual-hub-grid-row">
                <div class="column column--reset">
                    <#list document.links as link>
                        <#if link?is_odd_item || link?is_first>
                            <div class="visual-hub-group">
                        </#if>
                        <@visualhubBox link />
                        <#if link?is_even_item || link?is_last>
                            </div>
                        </#if>
                    </#list>
                </div>
            </div>
        </div>
    </#if>

    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                    <@hst.html hippohtml=document.additionalInformation contentRewriter=gaContentRewriter />


                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>

</article>
