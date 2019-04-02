<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.General" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#if document.metadata?has_content>
    <#list document.metadata as md>
        <@hst.headContribution category="metadata">
            ${md?no_esc}
        </@hst.headContribution>
    </#list>
</#if>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasPageIcon = document.pageIcon?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChildPages = childPages?has_content />
<#assign hasHtmlCode = document.htmlCode?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = ((hasSummaryContent || hasChildPages) && sectionTitlesFound gte 1) || sectionTitlesFound gt 1 || (hasSummaryContent && hasChildPages) />

<article class="article article--general">

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column<#if hasPageIcon>--three-quarters</#if> column--reset">
                            <h1 id="top" class="local-header__title" data-uipath="document.title">${document.title}</h1>
                            <#if hasSummaryContent>
                                <div class="article-header__subtitle" data-uipath="website.general.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                            </#if>
                        </div>
                        <#if hasPageIcon>
                            <div class="column--one-quarter column--reset">
                                <@hst.link hippobean=document.pageIcon.original fullyQualified=true var="image" />
                                <img src="${image?replace("/binaries", "/svg-magic/binaries")}?colour=ffcd60" alt="${document.title}" />
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks({ "document": document, "childPages": childPages, "includeTopLink": true, "ignoreSummary": true})></@sectionNav>
                </div>
                <#-- Restore the bundle -->
                <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <#if hasChildPages>
                    <@furtherInformationSection childPages></@furtherInformationSection>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>

<#if hasHtmlCode>
    ${document.htmlCode?no_esc}
</#if>
