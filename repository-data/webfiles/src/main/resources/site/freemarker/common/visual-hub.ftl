<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.VisualHub" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/visualhubBox.ftl">
<#include "macro/component/inlineSVG-v2.ftl">


<#-- Add meta tags -->
<@metaTags></@metaTags>
<@hst.setBundle basename="site.website.labels"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasBannerImage = document.image?has_content />
<#assign hasTopicIcon = document.icon?has_content />
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
                            <h1 style="">${document.title}</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="grid-wrapper banner-image-summary">
            <div class="article-header__inner">
                <div class="grid-row">
                    <div class="column column--reset column--81-25">
                        <p class="article-header__subtitle"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></p>
                    </div>
                </div>
            </div>
        </div>
    <#elseif hasTopicIcon>
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
            <div class="local-header article-header article-header--with-icon">
                <div class="grid-wrapper">
                    <div class="article-header__inner">
                        <div class="grid-row">
                            <div class="column--two-thirds column--reset">
                                <h1 class="local-header__title" data-uipath="document.title">${document.title}
                                    <p class="article-header__subtitle"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></p>
                            </div>
                            <div class="column--one-third column--reset small-none">
                                <@hst.link hippobean=document.icon.original fullyQualified=true var="iconImage" />
                                <@svg iconImage "fill:none;stroke:#ffcd60;" document.title "" />
                            </div>
                        </div>
                    </div>
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
