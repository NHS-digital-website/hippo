<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary?? && document.summary?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChildPages = childPages?has_content />

<#assign sectionTitlesFound = countSectionTitles(document.sections) />

<#assign renderNav = ((hasSummaryContent || hasChildPages) && sectionTitlesFound gte 1) || sectionTitlesFound gt 1 || (hasSummaryContent && hasChildPages) />

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
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks({ "document": document, "childPages": childPages, "ignoreSummary": hasSummaryContent })></@sectionNav>
                </div>
                <#-- Restore the bundle -->
                <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSummaryContent>
                <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                    <h2><@fmt.message key="headers.summary" /></h2>
                    <div data-uipath="website.general.summary" class="article-section--summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                </div>
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <#if hasChildPages>
                    <@furtherInformationSection childPages></@furtherInformationSection>
                </#if>
            </div>
        </div>
    </div>
</article>
