<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Publishedwork" -->

<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/sections/sections.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">
<#include "../nhsd-common/macro/component/pagination.ftl">
<#include "../nhsd-common/macro/component/chapter-pagination.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "../common/macro/fileMetaAppendix.ftl">
<#include "../common/macro/fileIconByMimeType.ftl">
<#include "../nhsd-common/macro/component/downloadBlockAsset.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/documentIcon.ftl">
<#import "app-layout-head.ftl" as alh>

<@hst.headContribution category="metadata">
    <meta name="robots" content="${document.noIndexControl?then("noindex","index")}"/>
</@hst.headContribution>

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,publicationsystem.headers"/>

<#--  Define variables  -->
<#assign bannerImage = "" />
<#assign bannerImageAltText = "" />
<#assign button = "" />
<#assign buttonText = "" />

<#assign earlyAccessKey = hstRequest.request.getParameter("key")!"">

<#-- Fall back to Blue Banner if no publication style is defined -->
<#if document.publicationStyle??>
    <#assign publicationStyle = document.publicationStyle />
<#else>
    <#assign publicationStyle = "bluebanner" />
</#if>

<#-- Don't use the summary in the content when the hero module is active -->
<#if publicationStyle = 'heromodule'>
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

<div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-display-chapters"
     aria-label="Document Header">

    <@hst.headContributions categoryIncludes="testscripts"/>

    <#assign heroOptions = getHeroOptionsWithMetaData(document)/>

    <#if document.button?? && document.button == "jumptocontent">
        <#assign heroButtons = [{
            "text": "Jump to overview",
            "src": "#document-content",
            "type": "invert"
        }]/>

        <#assign heroOptions += {
            "buttons": heroButtons
        }/>
    </#if>

    <#if publicationStyle == 'bluebanner' || !heroOptions.image?has_content>
        <#assign heroOptions += {
            "colour": "darkBlue"
        }/>
        <@hero heroOptions />
    <#elseif publicationStyle == 'heromodule'>
        <@hero heroOptions "image" />
    <#elseif publicationStyle == 'slimpicture'>
        <@hero getHeroOptions(document) "image" />
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

        <@chapterNav document />
    </#if>
    <div class="nhsd-t-grid nhsd-!t-margin-top-8 nhsd-!t-margin-bottom-6" id="document-content">
        <div class="nhsd-t-row">
            <#if renderNav>
                <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3">
                    <!-- start sticky-nav -->
                    <@fmt.message key="headers.page-contents" var="pageContentsHeader" />
                    <#assign links = [] />
                    <#if hasAboutThisSection>
                        <#assign links = links + [{"url": "#about-this-publication", "title": aboutThisPublicationHeader}] />
                    </#if>
                    <@stickyNavSections getStickySectionNavLinks({ "document": document, "sections": links, "includeSummary": hasSummaryContent}), pageContentsHeader></@stickyNavSections>

                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.doctype.published-work,publicationsystem.headers"/>
                    <!-- end sticky-nav -->
                </div>
            </#if>

            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 ${renderNav?then('nhsd-t-off-s-1', '')}">
                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}">
                        <p class="nhsd-t-heading-xl"><@fmt.message key="headers.summary"/></p>
                        <div data-uipath="website.publishedwork.summary"><@hst.html hippohtml=document.summary contentRewriter=brContentRewriter/></div>
                    </div>
                </#if>

                <#if hasHighlightsContent>
                    <#if hasSummaryContent>
                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <p class="nhsd-t-heading-xl">${document.highlightsTitle}</p>
                    <@hst.html hippohtml=document.highlightsContent contentRewriter=brContentRewriter/>
                </#if>

                <#if hasSectionContent>
                    <#if hasHighlightsContent || (hasSummaryContent && !hasHighlightsContent)>
                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <@sections document.sections></@sections>
                </#if>

                <#if hasAboutThisSection>
                    <#if hasSectionContent || (hasHighlightsContent && !hasSectionContent) || (hasSummaryContent && !hasSectionContent && !hasHighlightsContent)>
                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <div class="nhsd-!t-margin-bottom-6"
                         id="about-this-publication">
                        <p class="nhsd-t-heading-xl">${aboutThisPublicationHeader}</p>

                        <#if document.isbn?has_content>
                            <p class="nhsd-t-heading-s"><@fmt.message key="labels.isbn" /></p>
                            <p class="nhsd-t-body">${document.isbn}</p>
                        </#if>

                        <#if document.issn?has_content>
                            <p class="nhsd-t-heading-s"><@fmt.message key="labels.issn" /></p>
                            <p class="nhsd-t-body">${document.issn}</p>
                        </#if>
                    </div>
                </#if>

                <#if hasResources>
                    <#if hasAboutThisSection || (hasSectionContent && !hasAboutThisSection) || (hasHighlightsContent && !hasAboutThisSection && !hasSectionContent) || (hasSummaryContent && !hasAboutThisSection && !hasSectionContent && !hasHighlightsContent)>
                        <hr class="nhsd-a-horizontal-rule"/>
                    </#if>

                    <div class="nhsd-!t-margin-bottom-6" id="resources">
                        <p class="nhsd-t-heading-xl"><@fmt.message key="labels.resources"/></p>
                        <div class="nhsd-t-grid">
                            <div class="nhsd-t-row">
                                <div class="nhsd-t-col">
                                    <#list document.resources as attachment>
                                    <#--  Download macro cannot be used due to different yaml config -->
                                        <#assign iconTypeFromMime = getFormatByMimeType("${attachment.resource.mimeType?lower_case}") />
                                        <#assign fileName = attachment.resource.filename />
                                        <#assign fileSize = sizeToDisplay(attachment.resource.length) />
                                        <div class="attachment" itemprop="hasPart"
                                             itemscope
                                             itemtype="http://schema.org/MediaObject">
                                            <@externalstorageLink attachment.resource; url>
                                                <div class="nhsd-m-download-card nhsd-!t-margin-bottom-6">
                                                    <a class="nhsd-a-box-link"
                                                       title="${attachment.text}"
                                                       href="${url}"
                                                       onClick="logGoogleAnalyticsEvent('Download attachment','Published work','${url}');"
                                                       onKeyUp="return vjsu.onKeyUp(event)"
                                                       itemprop="contentUrl"
                                                    >
                                                        <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                                            <div class="nhsd-m-download-card__image-box">
                                                                <@documentIcon "${iconTypeFromMime}"/>
                                                            </div>

                                                            <div class="nhsd-m-download-card__content-box">
                                                                <#if attachment.text?has_content>
                                                                    <p class="nhsd-t-heading-s"
                                                                       itemprop="name">${attachment.text}</p>
                                                                </#if>

                                                                <div class="nhsd-m-download-card__meta-tags">
                                                                    <#assign fileFormat = iconTypeFromMime />
                                                                    <#if fileName != "">
                                                                        <#assign fileFormat = getFileExtension(fileName?lower_case) />
                                                                    </#if>
                                                                    <span class="nhsd-a-tag nhsd-a-tag--meta">${fileFormat}</span>
                                                                    <span class="nhsd-a-tag nhsd-a-tag--meta-light">${fileSize}</span>
                                                                </div>

                                                                <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-arrow--down nhsd-a-icon--size-s">
                                                                <svg xmlns="http://www.w3.org/2000/svg"
                                                                     preserveAspectRatio="xMidYMid meet"
                                                                     aria-hidden="true"
                                                                     focusable="false"
                                                                     viewBox="0 0 16 16"
                                                                     width="100%"
                                                                     height="100%">
                                                                    <path d="M15,8.5L8,15L1,8.5L2.5,7L7,11.2L7,1l2,0l0,10.2L13.5,7L15,8.5z"/>
                                                                </svg>
                                                            </span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </div>
                                            </@externalstorageLink>
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>
                </#if>

                <div class="nhsd-!t-margin-bottom-6">
                    <hr class="nhsd-a-horizontal-rule"/>
                    <@lastModified document.lastModified false></@lastModified>
                </div>

                <div class="nhsd-!t-margin-bottom-6">
                    <@pagination document />
                </div>

                <#if hasChildPages>
                    <#assign splitChapters = splitHash(documents) />
                    <div class="nhsd-m-publication-chapter-navigation nhsd-m-publication-chapter-navigation--split nhsd-!t-margin-1"
                         id="chapter-index"
                    >
                        <hr class="nhsd-a-horizontal-rule"/>
                        <h2 class="nhsd-t-heading-m">Chapters</h2>

                        <ol class="nhsd-t-list nhsd-t-list--number nhsd-t-list--loose">
                            <#list splitChapters.left as chapter>
                                <#if chapter.id == document.identifier>
                                    <li class="nhsd-m-publication-chapter-navigation--active">
                                <#else>
                                    <li class="">
                                </#if>
                                <a class="nhsd-a-link"
                                   href="${chapter.link + earlyAccessKey?has_content?then('?key=' + earlyAccessKey, '')}"
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
                                       href="${chapter.link + earlyAccessKey?has_content?then('?key=' + earlyAccessKey, '')}"
                                       onClick="${getOnClickMethodCall(document.class.name, chapter.link)}"
                                       title="${chapter.title}"
                                    >
                                        ${chapter.title}
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
</div>
