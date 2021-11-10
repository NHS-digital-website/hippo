<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.VisualHub" -->

<#include "../include/imports.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/visualhubBox.ftl">
<#include "macro/tabTileHeadings.ftl">
<#include "macro/tabTiles.ftl">
<#include "macro/contentPixel.ftl">
<#include 'macro/published-work-banners/slim-picture.ftl'>

<#-- Add meta tags -->
<@metaTags></@metaTags>
<@hst.setBundle basename="site.website.labels"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasBannerImage = document.image?has_content />
<#assign hasTopicIcon = document.pageIcon?has_content />
<#assign hasAdditionalInformation = document.additionalInformation.content?has_content />
<#assign hasPrimaryLinks = document.primarySections?? && document.primarySections?size gt 0 />
<#assign hasTabTileLinks = document.tileSections?? && document.tileSections?size gt 0 && !hasPrimaryLinks/>

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article class="article">
    <#if hasBannerImage && hasPrimaryLinks>
        <@hst.link hippobean=document.image.pageHeaderSlimBanner fullyQualified=true var="bannerImage" />
        <@hst.link hippobean=document.image.pageHeaderSlimBanner2x fullyQualified=true var="bannerImage2x" />
        <@hst.link hippobean=document.image.pageHeaderSlimBannerSmall fullyQualified=true var="bannerImageSmall" />
        <@hst.link hippobean=document.image.pageHeaderSlimBannerSmall2x fullyQualified=true var="bannerImageSmall2x" />
        <#assign slimPictureConfig = {
            "document": document,
            "bannerImageSmall": bannerImageSmall,
            "bannerImageSmall2x": bannerImageSmall2x,
            "bannerImage": bannerImage,
            "bannerImage2x": bannerImage2x,
            "bannerImageAltText": "Document Header"
        } />
        <@slimPicture slimPictureConfig />
        <div class="grid-wrapper banner-image-summary">
            <div class="article-header__inner">
                <div class="grid-row">
                    <div class="column column--reset column--81-25" data-uipath="website.hub.summary">
                        <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                    </div>
                </div>
            </div>
        </div>
    <#elseif hasBannerImage && hasTabTileLinks>
        <@hst.link hippobean=document.image fullyQualified=true var="bannerImage" />
        <div class="banner-image banner-image--short banner-image--tint"
             style="background-image: linear-gradient(0deg, rgba(0,94,184,0.8), rgba(0,94,184,0.8)), url(${bannerImage});">
            <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
                <div class="article-header--with-icon">
                    <div class="grid-wrapper">
                        <div class="article-header__inner">
                            <div class="grid-row">
                                <div class="column column--reset">
                                    <h1 class="local-header__title"
                                        data-uipath="document.title">${document.title}</h1>
                                    <div class="article-header__subtitle">
                                        <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                                    </div>
                                </div>
                            </div>
                        </div>
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

    <#if hasTabTileLinks>
        <#-- get param 'area' -->
        <#assign param = hstRequest.request.getParameter("area") />
        <#-- assign area the param value else, the first section in the list -->
        <#assign area = param???then(param, slugify(document.tileSections[0].tileSectionHeading)) />

        <div class="grid-wrapper">
            <div class="grid-row">
                <@tabTileHeadings document.tileSections "service" area />
            </div>
        </div>

        <div class="grid-wrapper">
            <#list document.tileSections as tileSection>
                <#if slugify(tileSection.tileSectionHeading) == area>
                    <div class="grid-row">
                        <div class="tile-panel" role="tabpanel">
                            <#list tileSection.tileSectionLinks as links>
                                <#if links?is_odd_item || links?is_first>
                                    <div class="tile-panel-group">
                                </#if>
                                <@tabTiles links/>
                                <#if links?is_even_item || links?is_last>
                                    </div>
                                </#if>
                            </#list>
                        </div>
                    </div>
                </#if>
            </#list>
        </div>
    </#if>

    <#if hasPrimaryLinks>
        <div class="grid-wrapper">
            <div class="grid-row visual-hub-grid-row">
                <div class="column column--reset">
                    <#list document.primarySections as primarySection>
                        <#list primarySection.primarySectionsTiles as link>
                            <#if link?is_odd_item || link?is_first>
                                <div class="visual-hub-group">
                            </#if>
                            <@visualhubBox link />
                            <#if link?is_even_item || link?is_last>
                                </div>
                            </#if>
                        </#list>
                    </#list>
                </div>
            </div>
        </div>
    </#if>

    <#if !hasTabTileLinks>
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
    </#if>

</article>
