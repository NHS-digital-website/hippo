<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "macro/sectionNav.ftl">
<#include "macro/metaTags.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.doctype.published-work,publicationsystem.headers"/>
<#assign hasSummaryContent = document.summary?? && document.summary?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasPublicationDate = document.publicationDate?? />
<#assign hasGeographicCoverage = document.geographicCoverage?has_content />
<#assign hasGranularity = document.geographicGranularity?has_content />
<#assign hasAboutThisSection = document.isbn?has_content || document.issn?has_content />
<#assign childPages = document.links />
<#assign hasChildPages = childPages?? && childPages?size gt 0 />
<#assign hasHighlightsContent = document.highlightsContent.content?? && document.highlightsContent.content?has_content />
<#assign hasResources = document.resources?has_content />

<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<@fmt.message key="headers.about-this-publication" var="aboutThisPublicationHeader" />

<#assign renderNav = (hasSummaryContent && sectionTitlesFound gte 1) />

<#macro publicationDate>
<dl class="detail-list">
    <dt class="detail-list__key"><@fmt.message key="labels.publication-date"/>:</dt>
    <dd class="detail-list__value" data-uipath="ps.dataset.nominal-date" itemprop="datePublished">
        <@fmt.formatDate value=document.publicationDate.time?date type="date" pattern="d MMM yyyy" timeZone="${getTimeZone()}" />
    </dd>
</dl>
</#macro>

<#macro nationalStatsStamp>
    <#list document.informationType as type>
        <#if type == "National statistics">
            <div class="article-header__stamp">
                <img src="<@hst.webfile path="images/national-statistics-logo@2x.png"/>" title="National Statistics" class="image-icon image-icon--large" />
            </div>
            <#break>
        </#if>
    </#list>
</#macro>

<article class="article article--published-work">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--detailed">
            <div class="grid-wrapper">
                <div class="article-header__inner">

                    <@nationalStatsStamp />

                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>

                    <#-- TODO - Chapter title to be added  -->
                    <#-- <p class="article-header__subtitle">(Hardcoded) Chapter title</p> -->

                    <span data-uipath="ps.publication.information-types">
                        <#if document.informationType?has_content>
                            <#list document.informationType as type>${type}<#sep>, </#list>
                        </#if>
                    </span>

                    <hr class="hr hr--short hr--light">

                    <div class="detail-list-grid">
                        <div class="grid-row">
                            <div class="column column--reset">
                                <@publicationDate />
                            </div>
                        </div>

                        <#if hasGeographicCoverage>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key" id="geographic-coverage"><@fmt.message key="labels.geographic-coverage"/>:</dt>
                                    <dd class="detail-list__value">
                                        <#list document.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>

                        <#if hasGranularity>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key"><@fmt.message key="labels.geographic-granularity"/>:</dt>
                                    <dd class="detail-list__value">
                                        <#list document.geographicGranularity as granularityItem>${granularityItem}<#sep>, </#list>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>

                        <#if document.coverageStart??>
                        <div class="grid-row">
                            <div class="column column--reset">
                                <dl class="detail-list">
                                    <dt class="detail-list__key"><@fmt.message key="labels.date-range"/>:</dt>
                                    <dd class="detail-list__value">
                                        <#if document.coverageStart?? && document.coverageEnd??>
                                            <@formatCoverageDates start=document.coverageStart.time end=document.coverageEnd.time/>
                                        <#else>
                                            (Not specified)
                                        </#if>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <#if hasChildPages>
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

                <#assign splitChapters = splitHash(childPages) />

                <div class="grid-row">
                    <div class="column column--one-half column--left">
                        <ul class="list list--reset cta-list">
                        <#list splitChapters.left as childPage>
                            <li>
                                <a href="<@hst.link var=link hippobean=childPage />" title="${childPage.title}">${childPage.title}</a>
                            </li>
                        </#list>
                        </ul>
                    </div>

                    <#if splitChapters.right?size gte 1>
                        <div class="column column--one-half column--right">
                        <ul class="list list--reset cta-list">
                        <#list splitChapters.right as childPage>
                            <li>
                                <a href="<@hst.link var=link hippobean=childPage />" title="${childPage.title}">${childPage.title}</a>
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
                    <@fmt.message key="headers.page-contents" var="pageContentsHeader" />
                    <#assign links = [] />
                    <#if hasAboutThisSection>
                    <#assign links = links + [{"url": "#about-this-publication", "title": aboutThisPublicationHeader}] />
                    </#if>
                    <@sectionNav getSectionNavLinks({ "document": document, "links": links}), pageContentsHeader></@sectionNav>

                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="rb.doctype.published-work,publicationsystem.headers"/>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSummaryContent>
                <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
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
                    <ul class="list">
                    <#list document.resources as attachment>
                        <li class="attachment" itemprop="hasPart" itemscope itemtype="http://schema.org/MediaObject">
                            <@externalstorageLink attachment.resource; url>
                            <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)" itemprop="contentUrl"><span itemprop="name">${attachment.text}</span></a>;
                            </@externalstorageLink>
                            <span class="fileSize">[size: <span itemprop="contentSize"><@formatFileSize bytesCount=attachment.resource.length/></span>]</span>
                        </li>
                    </#list>
                    </ul>
                </div>
            </#if>

            </div>
        </div>
    </div>
</article>
