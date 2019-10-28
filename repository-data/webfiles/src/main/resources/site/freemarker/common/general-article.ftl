<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.General" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
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
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChildPages = childPages?has_content />
<#assign hasHtmlCode = document.htmlCode?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = ((hasSummaryContent || hasChildPages) && sectionTitlesFound gte 1) || sectionTitlesFound gt 1 || (hasSummaryContent && hasChildPages) />

<article class="article article--general">

    <@documentHeader document 'general'></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <@stickyNavSections getStickySectionNavLinks({ "document": document, "childPages": childPages, "includeTopLink": true})></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
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
