<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/articleSections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,rb.generic.headers"/>

<#assign hasSummaryContent = document.summary?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasChapters = linkeddocuments?? && linkeddocuments.hippoBeans?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />

<#assign renderNav = (hasSummaryContent && sectionTitlesFound gte 1) || sectionTitlesFound gt 1 />

<#if hasChapters>
    <#assign documents = [] />
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

<article class="article article--published-work-chapter">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <span>${documents[0].title}</span>
                    <h1 class="local-header__title">${document.title}</h1>

                    <hr class="hr hr--short hr--light">
                </div>
            </div>
        </div>
    </div>

    <#if hasChapters>
        <#assign splitChapters = splitHash(documents) />
    
        <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide">
            <div class="chapter-nav">
                <div class="grid-wrapper">
                    <div class="grid-row chapter-nav__skip">
                        <div class="column column--reset">
                            <a href="#document-content"><@fmt.message key="labels.skip-to-content" /></a>
                        </div>
                    </div>

                    <div class="grid-row">
                        <div class="column column--reset">
                            <h2 class="chapter-nav__title"><@fmt.message key="headers.publication-chapters" /></h2>  
                        </div>
                    </div>

                    <div class="grid-row">
                        <div class="column column--one-half column--left">
                            <ul class="list list--reset cta-list">
                            <#list splitChapters.left as chapter>
                                <li itemprop="hasPart" itemscope itemtype="http://schema.org/WebPage">
                                    <a itemprop="url" href="${chapter.link}" title="${chapter.title}"><span itemprop="name">${chapter.title}</span></a>
                                </li>
                            </#list>
                            </ul>
                        </div>

                        <#if splitChapters.right?size gte 1>
                            <div class="column column--one-half column--right">
                            <ul class="list list--reset cta-list">
                            <#list splitChapters.right as chapter>
                                <li itemprop="hasPart" itemscope itemtype="http://schema.org/WebPage">
                                    <a itemprop="url" href="${chapter.link}" title="${chapter.title}"><span itemprop="name">${chapter.title}</span></a>
                                </li>
                            </#list>
                            </ul>
                        </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </#if>

    <div class="grid-wrapper grid-wrapper--article" id="document-content">
        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <@sectionNav getSectionNavLinks({ "document": document, "ignoreSummary": hasSummaryContent })></@sectionNav>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSummaryContent>
                <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                    <h2><@fmt.message key="headers.summary" /></h2>
                    <p>${document.summary}</p>
                </div>
                </#if>

                <#if hasSectionContent>
                <@articleSections document.sections></@articleSections>
                </#if>
            </div>
        </div>
    </div>

    <#-- <#if hasChapters>
        <a itemprop="url" href="<@hst.link var=link hippobean=chapter />" title="${chapter.title}"><span itemprop="name">${chapter.title}</span></a>
    </#if> -->
</article>
