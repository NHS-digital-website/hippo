<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Publishedworkchapter" -->

<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/sections/sections.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">
<#include "../nhsd-common/macro/component/pagination.ftl">
<#include "../nhsd-common/macro/component/chapter-pagination.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "../nhsd-common/macro/published-work-banners/text-banner.ftl">
<#include "../nhsd-common/macro/published-work-banners/hero-module.ftl">
<#include "../nhsd-common/macro/published-work-banners/slim-picture.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>

<#-- Define variables to prevent null values  -->
<#assign parentWork = document.publishedWork />
<#assign bannerImage = "" />
<#assign bannerImageAltText = "" />
<#assign button = "" />
<#assign parentText = "" />
<#assign parentLink = "" />

<#-- Default to blue banner if nothing else works -->
<#assign publicationStyle = "bluebanner" />

<#-- Overly Complicated set of nested If Statements -->
<#-- If parent style is chosen, or no publication style is set, use the parent style -->
<#if parentWork.publicationStyle?? && (!(document.publicationStyle)?? || document.publicationStyle == 'parent')>

    <#-- If an image has been set in this document, use that, else use the parent -->
    <#if document.bannerImage??>
        <#assign imageDocument = document />
    <#else>
        <#assign imageDocument = parentWork />
    </#if>

    <#assign publicationStyle = parentWork.publicationStyle />

<#-- If style is set in the current document, use that -->
<#elseif document.publicationStyle?? && document.publicationStyle?length gt 0>
    <#assign imageDocument = document />
    <#assign publicationStyle = document.publicationStyle />
</#if>

<#--  Use current values, else use parent values, if any  -->
<#if document.bannerAltText??>
    <#assign bannerImageAltText = document.bannerAltText />
<#elseif parentWork.bannerImageAltText??>
    <#assign bannerImageAltText = parentWork.bannerImageAltText />
</#if>

<#if document.button??>
    <#assign button = document.button />
<#elseif parentWork.button??>
    <#assign button = parentWork.button />
</#if>

<#-- Don't use the summary in the content when the hero module is active -->
<#if publicationStyle = 'heromodule'>
    <#assign hasSummaryContent = false />
<#else>
    <#assign hasSummaryContent = document.summary?? && document.summary.content?has_content />
</#if>

<#assign hasSectionContent = document.sections?has_content />
<#assign hasChapters = linkeddocuments?? && linkeddocuments.hippoBeans?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />

<#assign renderNav = (hasSummaryContent && sectionTitlesFound gte 1) || (sectionTitlesFound gt 1) />

<#if hasChapters>
    <#assign documents = [] />

<#-- this loop only runs once -->
    <#list linkeddocuments.hippoBeans as item>

    <#-- Cache the parent document's details -->
        <@hst.link hippobean=item var="link" />
        <#assign documents = [{ "index": 0, "id": item.identifier, "title": item.title, "link": link }] />

    <#-- Cache the chapter details -->
        <#list item.links as chapter>
            <@hst.link hippobean=chapter var="link" />
            <#assign documents += [{ "index": chapter?counter, "id": chapter.identifier, "title": chapter.title, "link": link }] />
        </#list>
    </#list>
</#if>

<#-- Assign title and link of parent document -->
<#if documents[0].title??>
    <#assign parentText = documents[0].title />
</#if>

<@hst.link hippobean=parentWork var="parentLinkBean" />
<#assign parentLink = parentLinkBean />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-display-chapters" aria-label="Document Header">

    <#--  Commented out until blue banner ticket is unblocked. It will use hero until then  -->

    <#--  <#if publicationStyle == 'bluebanner'>
        <@textBanner document topText/>
    </#if>  -->

    <#if publicationStyle == 'heromodule' || publicationStyle == 'bluebanner'>
        <#if imageDocument.bannerImage.pageHeaderHeroModule??>
            <@hst.link hippobean=imageDocument.bannerImage.pageHeaderHeroModule fullyQualified=true var="selectedBannerImage" />
            <#assign bannerImage = selectedBannerImage />
        </#if>
        <#assign heroConfig = {
            "document": document,
            "bannerImage": bannerImage,
            "bannerImageAltText": bannerImageAltText,
            "button": button,
            "buttonText": "Jump to content",
            "showTime": false,
            "topText": parentText,
            "topTextLink": parentLink
            }
        />
        <@heroModule heroConfig />
    </#if>

    <#if publicationStyle == 'slimpicture'>
        <#if imageDocument.bannerImage.pageHeaderSlimBannerSmall2x??>
            <@hst.link hippobean=imageDocument.bannerImage.pageHeaderSlimBannerSmall2x fullyQualified=true var="selectedBannerImage" />
            <#assign bannerImage = selectedBannerImage />
        </#if>
        <#assign slimPictureConfig = {
            "document": document,
            "bannerImage": bannerImage,
            "bannerImageAltText": bannerImageAltText,
            "topText": parentText,
            "topTextLink": parentLink
        } />
        <@slimPicture slimPictureConfig />
    </#if>

    <#if hasChapters>
        <@chapterNav document "Current chapter – " />
    </#if>

    <div class="nhsd-t-row" id="document-content">
        <#if renderNav>
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4">
                <!-- start sticky-nav -->
                    <@stickyNavSections getStickySectionNavLinks({ "document": document, "includeSummary": hasSummaryContent })></@stickyNavSections>
                <!-- end sticky-nav -->
                <#-- Restore the bundle -->
                <@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>
            </div>
        </#if>

        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
            <#if hasSummaryContent>
                <div id="${slugify('Summary')}">
                    <p class="nhsd-t-heading-xl"><@fmt.message key="headers.summary"/></p>
                    <div data-uipath="website.publishedworkchapter.summary"><@hst.html hippohtml=document.summary contentRewriter=brContentRewriter/></div>
                </div>
            </#if>

            <#if hasSectionContent>
                <#if hasSummaryContent>
                    <hr class="nhsd-a-horizontal-rule" />
                </#if>

                <@sections document.sections></@sections>
            </#if>

            <div class="nhsd-!t-margin-bottom-6">
                <@lastModified document.lastModified false></@lastModified>
            </div>

            <div class="nhsd-!t-margin-bottom-8">
                <@pagination document />
            </div>

            <#if hasChapters>
                <#assign splitChapters = splitHash(documents) />

                <div class="nhsd-m-publication-chapter-navigation nhsd-m-publication-chapter-navigation--split nhsd-!t-margin-1"
                    id="chapter-index"
                >
                    <ol class="nhsd-t-list nhsd-t-list--number nhsd-t-list--loose">
                        <#list splitChapters.left as chapter>
                        <#if chapter.id == document.identifier>
                            <li class="nhsd-m-publication-chapter-navigation--active">
                        <#else>
                            <li class="">
                        </#if>
                                <a class="nhsd-a-link"
                                    href="${chapter.link}"
                                    onClick="${getOnClickMethodCall(document.class.name, chapter.link)}"
                                    title="${chapter.title}"
                                >
                                    ${chapter.title}
                                    <span class="nhsd-t-sr-only"></span>
                                </a>
                            </li>
                        </#list>

                        <#if splitChapters.right?size gte 1>
                            <#list splitChapters.right as chapter>
                            <#if chapter.id == document.identifier>
                                <li class="nhsd-m-publication-chapter-navigation--active">
                            <#else>
                                <li class="">
                            </#if>
                                    <a class="nhsd-a-link"
                                        href="${chapter.link}"
                                        onClick="${getOnClickMethodCall(document.class.name, chapter.link)}"
                                        title="${chapter.title}"
                                    >
                                        ${chapter.title}
                                        <span class="nhsd-t-sr-only"></span>
                                    </a>
                                </li>
                            </#list>
                        </#if>
                    </ol>
                </div>
            </#if>
        </div>
    </div>
</div>
