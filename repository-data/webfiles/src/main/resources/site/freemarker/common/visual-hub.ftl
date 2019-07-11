<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.VisualHub" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/visualhubBox.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<@hst.setBundle basename="site.website.labels"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasBannerImage = document.image?has_content />
<#assign hasTopicIcon = document.icon?has_content />
<#assign hasAdditionalInformation = document.additionalInformation.content?has_content />
<#assign hasLinks = document.links?? && document.links?size gt 0 />

<article class="article" aria-label="Document Header">
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
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="local-header article-header article-header--with-icon">
                <div class="grid-wrapper">
                    <div class="article-header__inner">
                        <div class="grid-row">
                            <div class="column--two-thirds column--reset">
                                <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
                                <span class="article-header__subtitle" data-uipath="website.hub.summary">
                                    <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                                </span>
                            </div>
                            <#if hasTopicIcon>
                                <div class="column--one-third column--reset small-none">
                                    <@hst.link hippobean=document.icon.original fullyQualified=true var="iconImage" />
                                    <#if iconImage?ends_with("svg")>
                                        <img src="${iconImage?replace("/binaries", "/svg-magic/binaries")}?colour=ffcd60" alt="${document.title}" />
                                    <#else>
                                        <img src="${iconImage}" alt="${document.title}" />
                                    </#if>
                                </div>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
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
