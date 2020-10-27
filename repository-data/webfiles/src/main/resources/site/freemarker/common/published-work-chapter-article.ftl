<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Publishedworkchapter" -->

<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#include "../common/macro/component/pagination.ftl">
<#include "../common/macro/component/chapter-pagination.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "../common/macro/published-work-banners/text-banner.ftl">
<#include "../common/macro/published-work-banners/hero-module.ftl">
<#include "../common/macro/published-work-banners/slim-picture.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>

<#-- Define empty variables, just in case  -->
<#assign parentWork = document.publishedWork />
<#assign bannerImageAltText = "" />
<#-- Default to blue banners if nothing else works -->
<#assign publicationStyle = "bluebanner" />

<#-- Overly Complicated set of nested If Statements -->
<#-- If parent style is chosen, or no publication style is set, use the parent style -->
<#if parentWork.publicationStyle?? && (!(document.publicationStyle)?? || document.publicationStyle == 'parent')>
    <#-- If an image has been set in this document, use that -->
    <#if document.bannerImage??>
        <@hst.link hippobean=document.bannerImage.original fullyQualified=true var="bannerImage" />
        <#assign bannerImageAltText = document.bannerImageAltText />
    <#-- Otherwise, use the parent document's image too -->
    <#else>
        <@hst.link hippobean=parentWork.bannerImage.original fullyQualified=true var="bannerImage" />
        <#assign bannerImageAltText = parentWork.bannerImageAltText />
    </#if>
    <#if document.button??>
        <#assign button = document.button />
    <#-- Otherwise, use the parent document's image too -->
    <#else>
        <#assign button = parentWork.button />
    </#if>
    <#assign publicationStyle = parentWork.publicationStyle />
<#-- If style is set in the current document, use that -->
<#elseif document.publicationStyle?? && document.publicationStyle?length>
    <@hst.link hippobean=document.bannerImage.original fullyQualified=true var="bannerImage" />
    <#assign publicationStyle = document.publicationStyle />
    <#assign bannerImageAltText = document.bannerImageAltText />
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

<#-- this loop only runs ones -->
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

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article class="article article--published-work-chapter"
         aria-label="Document Header">
    <#if publicationStyle == 'bluebanner'>
        <#assign parentTitle = documents[0].title />
        <@textBanner document parentTitle/>
    </#if>
    <#if publicationStyle == 'heromodule' && bannerImage??>
        <@hst.link hippobean=parentWork var="parentLink" />

        <@heroModule {
            "document": document,
            "bannerImage": bannerImage,
            "bannerImageAltText": bannerImageAltText,
            "button": button,
            "buttonText": "Jump to content",
            "showTime": false,
            "topText": documents[0].title,
            "topTextLink": parentLink
        } />
    </#if>

    <#if publicationStyle == 'slimpicture'>
        <@slimPicture document bannerImage bannerImageAltText />
    </#if>
    <#if hasChapters>

        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide grid-wrapper--chapter-pagination">
            <div class="chapter-pagination-wrapper">
                <div class="grid-wrapper">
                    <div class="grid-row chapter-pagination-wrapper__skip visually-hidden">
                        <div class="column column--reset">
                            <a href="#document-content"><@fmt.message key="labels.skip-to-content" /></a>
                        </div>
                    </div>

                    <@chapterNav document "Current chapter – " />
                </div>
            </div>
        </div>
    </#if>

    <#assign gridWrapperClasses = ['grid-wrapper', 'grid-wrapper--article'] />
    <#if renderNav>
        <#assign gridWrapperClasses = gridWrapperClasses + ['grid-wrapper--has-nav'] />
    </#if>
    <div class="${gridWrapperClasses?join(" ")}" id="document-content">
        <div class="grid-row">
            <#if renderNav>
                <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
                    <!-- start sticky-nav -->
                    <div id="sticky-nav">
                        <@stickyNavSections getStickySectionNavLinks({ "document": document, "includeSummary": hasSummaryContent })></@stickyNavSections>
                    </div>
                    <!-- end sticky-nav -->
                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>
                </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
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

        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide"
             id="chapter-index">
            <div class="chapter-nav">
                <div class="grid-wrapper">

                    <div class="grid-row">
                        <div class="column column--reset">
                            <h2 class="chapter-nav__title"><@fmt.message key="headers.publication-chapters" /></h2>
                        </div>
                    </div>

                    <div class="grid-row">
                        <div class="column column--one-half column--left">
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
                            <div class="column column--one-half column--right">
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
        </div>
    </#if>


    <#-- <#if hasChapters>
        <a href="<@hst.link var=link hippobean=chapter />" title="${chapter.title}">${chapter.title}</a>
    </#if> -->
</article>
