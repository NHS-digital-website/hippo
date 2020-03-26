<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#include "../common/macro/component/pagination.ftl">
<#include "../common/macro/component/chapter-pagination.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary?? && document.summary.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChapters = linkeddocuments?? && linkeddocuments.hippoBeans?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />

<#assign renderNav = (hasSummaryContent && sectionTitlesFound gte 1) || sectionTitlesFound gt 1 />

<#assign parentChapter = document.getPublishedWork() />
<#if parentChapter?? && parentChapter.publicationStyle??>
    <h1>Parent style: ${parentChapter.publicationStyle}</h1>
</#if>

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

<article class="article article--published-work-chapter" aria-label="Document Header">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <span>${documents[0].title}</span>
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>

                    <hr class="hr hr--short hr--light">
                </div>
            </div>
        </div>
    </div>

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

    <div class="grid-wrapper grid-wrapper--article" id="document-content">
        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
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
                <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
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

        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" id="chapter-index">
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
                                            <a class="chapter-index__link" href="${chapter.link}" title="${chapter.title}">${chapter.title}</a>
                                        </li>
                                    </#if>
                                </#list>
                            </ol>
                        </div>

                        <#if splitChapters.right?size gte 1>
                            <div class="column column--one-half column--right">
                                <ol class="list list--reset cta-list chapter-index" start="${splitChapters.left?size + 1}">
                                    <#list splitChapters.right as chapter>
                                        <#if chapter.id == document.identifier>
                                            <li class="chapter-index__item chapter-index__item--current">
                                                <p class="chapter-index__current-item">${chapter.title}</p>
                                            </li>
                                        <#else>
                                            <li class="chapter-index__item">
                                                <a class="chapter-index__link" href="${chapter.link}" title="${chapter.title}">${chapter.title}</a>
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
