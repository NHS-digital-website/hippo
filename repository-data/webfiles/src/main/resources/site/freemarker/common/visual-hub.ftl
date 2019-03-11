<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.VisualHub" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>
<@hst.setBundle basename="site.website.labels"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasBannerImage = document.image?has_content />
<#assign hasTopicIcon = document.icon?has_content />
<#assign hasAdditionalInformation = document.additionalInformation.content?has_content />
<#assign hasLinks = document.links?? && document.links?size gt 0 />

<#-- TODO: implement frontend

    ${document.title}
    <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
    ${document.shortsummary}
    ${document.seosummary}
    other fields...

-->

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <div class="column column--reset">
                <div class="local-header article-header">
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">

                <#-- [FTL-BEGIN] 'Summary' section -->
                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                        <h2><@fmt.message key="headers.summary" /></h2>
                        <div data-uipath="website.hub.summary" class="article-section--summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                    </div>
                </#if>
                <#-- [FTL-END] 'Summary' section -->


                <#-- [FTL-BEGIN] 'Visual links' section -->
                <#if hasLinks>

                </#if>
                <#-- [FTL-END] 'Visual links' section -->

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
           </div>
        </div>
    </div>
</article>
