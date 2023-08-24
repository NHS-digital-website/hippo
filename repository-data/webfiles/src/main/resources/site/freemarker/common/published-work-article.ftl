<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Publishedwork" -->

<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#include "../common/macro/component/pagination.ftl">
<#include "../common/macro/component/chapter-pagination.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<#include "../common/macro/fileIconByMimeType.ftl">
<#include "../common/macro/published-work-banners/text-banner.ftl">
<#include "../common/macro/published-work-banners/hero-module.ftl">
<#include "../common/macro/published-work-banners/slim-picture.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,publicationsystem.headers"/>

<#-- Fall back to Blue Banner if no publication style is defined -->
<#if document.publicationStyle??>
    <#assign publicationStyle = document.publicationStyle />
<#else>
    <#assign publicationStyle = "bluebanner" />
</#if>

<#-- Don't use the summary in the content when the hero module is active -->
<#--also if the publication system style is blueBanner or slimpicture do not use the summary in the content-->
<#if publicationStyle = 'heromodule' || publicationStyle = 'bluebanner' || publicationStyle = 'slimpicture'>
    <#assign hasSummaryContent = false />
<#else>
    <#assign hasSummaryContent = document.summary?? && document.summary.content?has_content />
</#if>

<#assign hasSectionContent = document.sections?has_content />
<#assign hasAboutThisSection = document.isbn?has_content || document.issn?has_content />
<#assign childPages = document.links />
<#assign hasChildPages = childPages?? && childPages?size gt 0 />
<#assign hasHighlightsContent = document.highlightsContent.content?? && document.highlightsContent.content?has_content />
<#assign hasResources = document.resources?has_content />

<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<@fmt.message key="headers.about-this-publication" var="aboutThisPublicationHeader" />

<#assign renderNav = (hasSummaryContent && sectionTitlesFound gte 1) || (sectionTitlesFound gt 1) />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article class="article article--published-work" aria-label="Document Header">

    <@hst.headContributions categoryIncludes="testscripts"/>

    <#if publicationStyle == 'bluebanner'>
        <@textBanner document />
    </#if>

    <#if publicationStyle == 'heromodule'>
        <@hst.link hippobean=document.bannerImage.pageHeaderHeroModuleSmall2x fullyQualified=true var="bannerImageSmall2x" />
        <@hst.link hippobean=document.bannerImage.pageHeaderHeroModuleSmall fullyQualified=true var="bannerImageSmall" />
        <@hst.link hippobean=document.bannerImage.pageHeaderHeroModule2x fullyQualified=true var="bannerImage2x" />
        <@hst.link hippobean=document.bannerImage.pageHeaderHeroModule fullyQualified=true var="bannerImage" />
        <#assign heroConfig = {
            "document": document,
            "bannerImageSmall": bannerImageSmall,
            "bannerImageSmall2x": bannerImageSmall2x,
            "bannerImage": bannerImage,
            "bannerImage2x": bannerImage2x,
            "bannerImageAltText": document.bannerImageAltText,
            "button": document.button,
            "buttonText": "Jump to overview",
            "showTime": true
        } />
        <@heroModule heroConfig />
    </#if>
    <#if publicationStyle == 'slimpicture'>
        <@hst.link hippobean=document.bannerImage.pageHeaderSlimBanner fullyQualified=true var="bannerImage" />
        <@hst.link hippobean=document.bannerImage.pageHeaderSlimBanner2x fullyQualified=true var="bannerImage2x" />
        <@hst.link hippobean=document.bannerImage.pageHeaderSlimBannerSmall fullyQualified=true var="bannerImageSmall" />
        <@hst.link hippobean=document.bannerImage.pageHeaderSlimBannerSmall2x fullyQualified=true var="bannerImageSmall2x" />
        <#assign slimPictureConfig = {
            "document": document,
            "bannerImageSmall": bannerImageSmall,
            "bannerImageSmall2x": bannerImageSmall2x,
            "bannerImage": bannerImage,
            "bannerImage2x": bannerImage2x,
            "bannerImageAltText": document.bannerImageAltText
        } />
        <@slimPicture slimPictureConfig />
    </#if>

    <#if hasChildPages>

        <#assign documents = [] />

    <#-- Cache the parent document's details -->
        <@hst.link hippobean=document var="link" />
        <#assign documents = [{ "index": 0, "id": document.identifier, "title": document.title, "link": link }] />

    <#-- Cache the chapter details -->
        <#list childPages as chapter>
            <@hst.link hippobean=chapter var="link" />
            <#assign documents += [{ "index": chapter?counter, "id": chapter.identifier, "title": chapter.title, "link": link }] />
        </#list>


        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide grid-wrapper--chapter-pagination">
            <div class="chapter-pagination-wrapper">
                <div class="grid-wrapper">
                    <div class="grid-row chapter-pagination-wrapper__skip visually-hidden">
                        <div class="column column--reset">
                            <a href="#document-content"><@fmt.message key="labels.skip-to-content" /></a>
                        </div>
                    </div>

                    <@chapterNav document />
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
                        <@fmt.message key="headers.page-contents" var="pageContentsHeader" />
                        <#assign links = [] />
                        <#if hasAboutThisSection>
                            <#assign links = links + [{"url": "#about-this-publication", "title": aboutThisPublicationHeader}] />
                        </#if>
                        <@stickyNavSections getStickySectionNavLinks({ "document": document, "sections": links, "includeSummary": hasSummaryContent}), pageContentsHeader></@stickyNavSections>

                        <#-- Restore the bundle -->
                        <@hst.setBundle basename="rb.doctype.published-work,publicationsystem.headers"/>
                    </div>
                    <!-- end sticky-nav -->
                </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}"
                         class="article-section article-section--summary article-section--reset-top">
                        <h2><@fmt.message key="headers.summary" /></h2>
                        <div data-uipath="website.publishedwork.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                    </div>
                </#if>

                <#if hasHighlightsContent>
                    <div class="article-section article-section--highlighted">
                        <div class="callout callout--attention">
                            <h2>${document.highlightsTitle}</h2>
                            <@hst.html hippohtml=document.highlightsContent contentRewriter=gaContentRewriter/>
                        </div>
                    </div>
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <#if hasAboutThisSection>
                    <div id="about-this-publication" class="article-section">
                        <h2>${aboutThisPublicationHeader}</h2>

                        <div class="cta-list">
                            <#if document.isbn?has_content>
                                <div class="cta">
                                    <h3 class="cta-title"><@fmt.message key="labels.isbn" /></h3>
                                    <p class="cta__text">${document.isbn}</p>
                                </div>
                            </#if>

                            <#if document.issn?has_content>
                                <div class="cta">
                                    <h3 class="cta-title"><@fmt.message key="labels.issn" /></h3>
                                    <p class="cta__text">${document.issn}</p>
                                </div>
                            </#if>
                        </div>
                    </div>
                </#if>

                <#if hasResources>
                    <div class="article-section" id="resources">
                        <h2><@fmt.message key="labels.resources"/></h2>
                        <ul class="list list--reset">
                            <#list document.resources as attachment>
                                <li class="attachment" itemprop="hasPart" itemscope
                                    itemtype="http://schema.org/MediaObject">
                                    <@externalstorageLink attachment.resource; url>
                                        <a title="${attachment.text}"
                                           href="${url}"
                                           class="block-link"
                                           itemprop="contentUrl">
                                            <div class="block-link__header">
                                                <@fileIconByMimeType attachment.resource.mimeType></@fileIconByMimeType>
                                            </div>
                                            <div class="block-link__body">
                                                <span class="block-link__title"
                                                      itemprop="name">${attachment.text}</span>
                                                <@fileMetaAppendix attachment.resource.length, attachment.resource.mimeType></@fileMetaAppendix>
                                            </div>
                                        </a>
                                    </@externalstorageLink>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>
                <div class="article-section muted">
                    <p><@lastModified document.lastModified false></@lastModified></p>
                </div>

                <div class="article-section no-border no-top-margin">
                    <@pagination document />
                </div>
            </div>
        </div>
    </div>
    <#if hasChildPages>
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

</article>
