<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.General" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/latestblogs.ftl">
<#include "macro/component/calloutBox.ftl">
<#include "macro/contentPixel.ftl">
<#import "app-layout-head.ftl" as alh>
<#include "macro/heroes/hero.ftl">
<#include "macro/heroes/hero-options.ftl">

<@hst.headContribution category="metadata">
    <meta name="robots" content="${document.noIndexControl?then("noindex","index")}"/>
</@hst.headContribution>

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
<#assign idsuffix = slugify(document.title) />
<#assign navStatus = document.navigationController />
<#assign hasBannerImage = document.image?has_content />
<#assign custom_summary = document.summary />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article >
    <#assign heroType = "default" />
    <#if hasBannerImage>
        <#assign heroType = "image" />
    </#if>
    <@hero getHeroOptions(document) heroType />

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <#if document.updates?has_content>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-12">
                    <#assign item = {} />
                    <#list document.updates as update>
                        <#assign item += update />
                        <#assign item += {"calloutType":"update", "index":update?index} />
                        <@calloutBox item document.class.name />
                    </#list>
                </div>
            </div>
        </#if>
        <div class="nhsd-t-row">
            <#if navStatus == "withNav" && renderNav>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3">
                    <#assign links = [{ "url": "#top", "title": "Top of page" }] />
                    <#if document.latestNews?has_content >
                        <#assign links += [{ "url": "#related-articles-latest-news-${idsuffix}", "title": 'Latest news' }] />
                    </#if>
                    <#assign links += getStickySectionNavLinks({ "document": document, "childPages": childPages, "includeTopLink": false }) />
                    <#if document.relatedEvents?has_content >
                        <#assign links += [{ "url": "#related-articles-events-${idsuffix}", "title": 'Forthcoming events' }] />
                    </#if>
                    <@stickyNavSections links></@stickyNavSections>
                    <!-- end sticky-nav -->
                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
                </div>
            </#if>

            <div class="${(navStatus == "withNav" || navStatus == "withoutNav")?then("nhsd-t-col-xs-12 nhsd-t-off-s-1 nhsd-t-col-s-8", "nhsd-t-col-12")}">

                <@latestblogs document.latestNews 'General' 'latest-news-' + idsuffix 'Latest news' />

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <#if !document.latestNews?has_content && document.relatedNews?has_content>
                    <@fmt.message key="headers.related-news" var="relatedNewsHeader" />
                    <div id="${slugify(relatedNewsHeader)}">
                        <hr class="nhsd-a-horizontal-rule">
                        <h2 class="nhsd-t-heading-xl" data-uipath="website.contentblock.section.title">${relatedNewsHeader}</h2>
                        <@latestblogs document.relatedNews 'General' 'related-news-' + idsuffix "" />
                    </div>
                </#if>

                <#if hasChildPages>
                    <#if (hasSectionContent && (document.latestNews?has_content || !document.relatedNews?has_content) ) >
                        <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-bottom-6" />
                    </#if>
                    <@furtherInformationSection childPages></@furtherInformationSection>
                </#if>

                <@latestblogs document.relatedEvents 'General' 'events-' + idsuffix 'Forthcoming events' />

                <div class="nhsd-t-body nhsd-!t-margin-bottom-8">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>

<#if hasHtmlCode>
    ${document.htmlCode?no_esc}
</#if>
