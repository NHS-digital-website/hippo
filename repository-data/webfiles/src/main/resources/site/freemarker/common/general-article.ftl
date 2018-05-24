<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasSummaryContent = document.summary?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChildPages = childPages?has_content />

<#assign sectionTitlesFound = countSectionTitles(document.sections) />

<#assign renderNav = (hasSummaryContent && (hasSectionContent && (sectionTitlesFound gte 1))) || (hasSectionContent && (sectionTitlesFound gt 1)) || (hasSectionContent && (sectionTitlesFound gte 1) && hasChildPages) />

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="local-header article-header">
            <h1 class="local-header__title">${document.title}</h1>
        </div>

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                <@sectionNav getSectionNavLinks({ "document": document, "childPages": childPages, "ignoreSummary": hasSummaryContent })></@sectionNav>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSummaryContent>
                <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                    <h2>Summary</h2>
                    <p>${document.summary}</p>
                </div>
                </#if>

                <#if hasSectionContent>
                <@articleSections document.sections></@articleSections>
                </#if>

                
                <@furtherInformationSection childPages></@furtherInformationSection>
            </div>
        </div>
    </div>
</article>
