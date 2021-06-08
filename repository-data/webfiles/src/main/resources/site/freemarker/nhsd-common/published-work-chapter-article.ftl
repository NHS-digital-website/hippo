<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Publishedworkchapter" -->

<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#include "../common/macro/component/pagination.ftl">
<#include "../common/macro/component/chapter-pagination.ftl">
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

<article class="article article--published-work-chapter"
    aria-label="Document Header">

    <#if publicationStyle == 'bluebanner'>
        <@textBanner document topText/>
    </#if>

    <#if publicationStyle == 'heromodule'>
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
        <#if document.bannerImage??>
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

        <div class="nhsd-t-grid nhsd-t-grid--full-width">
            <div class="chapter-pagination-wrapper">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-12" id="document-content">
                        <a href="#document-content"><@fmt.message key="labels.skip-to-content" /></a>
                    </div>
                </div>

                <@chapterNav document "Current chapter – " />
            </div>
        </div>
    </#if>

    <#assign gridWrapperClasses = ['grid-wrapper', 'grid-wrapper--article'] />
    <#if renderNav>
        <#assign gridWrapperClasses = gridWrapperClasses + ['grid-wrapper--has-nav'] />
    </#if>
    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <#if renderNav>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4">
                    <!-- start sticky-nav -->
                    <div id="sticky-nav">
                        <@stickyNavSections getStickySectionNavLinks({ "document": document, "includeSummary": hasSummaryContent })></@stickyNavSections>
                    </div>
                    <!-- end sticky-nav -->
                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>
                </div>
            </#if>

            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}"
                         class="article-section article-section--summary article-section--reset-top">
                        <h2><@fmt.message key="headers.summary" /></h2>
                        <div data-uipath="website.publishedworkchapter.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                    </div>
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>

                <div class="article-section no-border no-top-margin">
                    <@pagination document />
                </div>
            </div>
        </div>
    </div>

    <#if hasChapters>
        <#assign splitChapters = splitHash(documents) />

        <div class="nhsd-t-grid nhsd-t-grid--full-width"
             id="chapter-index">
            <div class="chapter-nav">

                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-12">
                            <h2 class="chapter-nav__title"><@fmt.message key="headers.publication-chapters" /></h2>
                        </div>
                    </div>

                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6">
                        <ol class="list list--reset cta-list chapter-index">
                            <#list splitChapters.left as chapter>
                                <#if chapter.id == document.identifier>
                                    <li class="chapter-index__item chapter-index__item--current">
                                        <p class="chapter-index__current-item">${chapter.title}</p>
                                    </li>
                                <#else>
                                    <li class="chapter-index__item">
                                        <a class="chapter-index__link"
                                            href="${chapter.link}"
                                            title="${chapter.title}">${chapter.title}</a>
                                    </li>
                                </#if>
                            </#list>
                        </ol>
                    </div>

                    <#if splitChapters.right?size gte 1>
                        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6">
                            <ol class="list list--reset cta-list chapter-index"
                                start="${splitChapters.left?size + 1}">
                                <#list splitChapters.right as chapter>
                                    <#if chapter.id == document.identifier>
                                        <li class="chapter-index__item chapter-index__item--current">
                                            <p class="chapter-index__current-item">${chapter.title}</p>
                                        </li>
                                    <#else>
                                        <li class="chapter-index__item">
                                            <a class="chapter-index__link"
                                                href="${chapter.link}"
                                                title="${chapter.title}">${chapter.title}</a>
                                        </li>
                                    </#if>
                                </#list>
                            </ol>
                        </div>
                    </#if>
                </div>
            </div>
        </div>
    </#if>


    <#-- <#if hasChapters>
        <a href="<@hst.link var=link hippobean=chapter />" title="${chapter.title}">${chapter.title}</a>
    </#if> -->
</article>
