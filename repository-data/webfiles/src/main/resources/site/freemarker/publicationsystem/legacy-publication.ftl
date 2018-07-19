<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../common/macro/sectionNav.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign hasSummary = legacyPublication.summary.content?has_content>
<#assign hasAdministrativeSources = legacyPublication.administrativeSources?has_content>
<#assign hasAttachments = legacyPublication.getAttachments()?size gt 0>
<#assign hasKeyFacts = legacyPublication.keyFacts.content?has_content>
<#assign hasResourceLinks = legacyPublication.resourceLinks?has_content>
<#assign hasDataSets = legacyPublication.datasets?has_content>
<#assign hasRelatedLinks = legacyPublication.relatedLinks?has_content>
<#assign hasResources = hasAttachments || hasResourceLinks || hasDataSets>

<#assign sectionCounter = 0 />
<#function shouldRenderNav>
    <#assign result = false />

    <#list [hasAdministrativeSources, hasAttachments, hasKeyFacts, hasResourceLinks, hasDataSets, hasRelatedLinks, hasResources] as section>
        <#if section>
            <#assign sectionCounter += 1 />
        </#if>
    </#list>
    <#return (hasSummary && sectionCounter gte 1 || sectionCounter gt 1)?then(true, false) />
</#function>

<#assign renderNav = shouldRenderNav() />


<#-- Define Article section headers, nav ids and titles -->
<@fmt.message key="headers.summary" var="summaryHeader" />
<@fmt.message key="headers.key-facts" var="keyFactsHeader" />
<@fmt.message key="headers.administrative-sources" var="adminSourcesHeader" />
<@fmt.message key="headers.resources" var="resourcesHeader" />
<@fmt.message key="headers.related-links" var="relatedLinksHeader" />
<#function getSectionNavLinks>
    <#assign links = [] />
    <#if hasSummary>
        <#assign links = [{ "url": "#" + slugify(summaryHeader), "title": summaryHeader }] />
    </#if>
    <#if hasKeyFacts>
        <#assign links += [{ "url": "#" + slugify(keyFactsHeader), "title": keyFactsHeader }] />
    </#if>
    <#if hasAdministrativeSources>
        <#assign links += [{ "url": "#" + slugify(adminSourcesHeader), "title": adminSourcesHeader }] />
    </#if>
    <#if hasResources>
        <#assign links += [{ "url": "#" + slugify(resourcesHeader), "title": resourcesHeader }] />
    </#if>
    <#if hasRelatedLinks>
        <#assign links += [{ "url": "#" + slugify(relatedLinksHeader), "title": relatedLinksHeader }] />
    </#if>

    <#return links />
</#function>

<#macro nationalStatsStamp>
    <#list legacyPublication.informationType as type>
        <#if type == "National statistics">
            <div class="article-header__stamp">
                <img src="<@hst.webfile path="images/national-statistics-logo@2x.png"/>" data-uipath="ps.publication.national-statistics" alt="National Statistics" title="National Statistics" class="image-icon image-icon--large" />
            </div>
            <#break>
        </#if>
    </#list>
</#macro>

<#macro restrictedContentOfUpcomingPublication>
<div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
    <div class="local-header article-header article-header--detailed">
        <div class="grid-wrapper">
            <div class="article-header__inner">

                <@nationalStatsStamp/>

                <h1 class="local-header__title" data-uipath="document.title">${legacyPublication.title}</h1>

                <hr class="hr hr--short hr--light">

                <div class="detail-list-grid">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="headers.publication-date"/></dt>
                                <dd class="detail-list__value" data-uipath="ps.publication.nominal-publication-date">
                                    <@formatRestrictableDate value=legacyPublication.nominalPublicationDate/>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <p data-uipath="ps.publication.upcoming-disclaimer" class="strong">(Upcoming, not yet published)</p>
                </div>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro fullContentOfPubliclyAvailablePublication>
<div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
    <div class="local-header article-header article-header--detailed">
        <div class="grid-wrapper">
            <div class="article-header__inner">

                <@nationalStatsStamp/>

                <span class="article-header__label"><@fmt.message key="labels.publication"/></span>
                <h1 class="local-header__title" data-uipath="document.title">${legacyPublication.title}</h1>
                <#if legacyPublication.parentDocument??>
                    <p class="article-header__subtitle">
                        This is part of
                        <a href="<@hst.link hippobean=legacyPublication.parentDocument.selfLinkBean/>"
                            title="${legacyPublication.parentDocument.title}">
                            ${legacyPublication.parentDocument.title}
                        </a>
                    </p>
                </#if>

                <span data-uipath="ps.publication.information-types">
                    <#if legacyPublication.informationType?has_content>
                        <#list legacyPublication.informationType as type>${type}<#sep>, </#list>
                    </#if>
                </span>

                <hr class="hr hr--short hr--light">

                <div class="detail-list-grid">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="headers.publication-date"/></dt>
                                <dd class="detail-list__value" data-uipath="ps.publication.nominal-publication-date">
                                    <@formatRestrictableDate value=legacyPublication.nominalPublicationDate/>
                                </dd>
                            </dl>
                        </div>
                    </div>

                    <#if legacyPublication.geographicCoverage?has_content>
                    <div class="grid-row">
                        <div class="column column--reset">
                            <dl class="detail-list">
                                <dt class="detail-list__key" id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                                <dd class="detail-list__value" data-uipath="ps.publication.geographic-coverage">
                                    <#list legacyPublication.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    </#if>

                    <#if legacyPublication.granularity?has_content >
                    <div class="grid-row">
                        <div class="column column--reset">
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="headers.geographical-granularity"/></dt>
                                <dd class="detail-list__value" data-uipath="ps.publication.granularity">
                                    <#list legacyPublication.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                                </dd>
                            </dl>
                        </div>
                    </div>
                    </#if>

                    <#if legacyPublication.coverageStart?? >
                    <div class="grid-row">
                        <div class="column column--reset">
                            <dl class="detail-list">
                                <dt class="detail-list__key"><@fmt.message key="headers.date-range"/></dt>
                                <dd class="detail-list__value" data-uipath="ps.publication.date-range">
                                    <#if legacyPublication.coverageStart?? && legacyPublication.coverageEnd??>
                                        <@formatCoverageDates start=legacyPublication.coverageStart.time end=legacyPublication.coverageEnd.time/>
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

<div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
    <div class="grid-row">
        <#if renderNav>
        <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
            <div id="sticky-nav">
                <@sectionNav getSectionNavLinks()></@sectionNav>
            </div>
        </div>
        </#if>

        <div class="column column--two-thirds page-block page-block--main">
            <#if hasKeyFacts>
                <#assign summarySectionClassName = "article-section article-section--summary no-border">
            <#else>
                <#assign summarySectionClassName = "article-section article-section--summary">
            </#if>

            <#if hasSummary>
            <div class="${summarySectionClassName}" id="${slugify(summaryHeader)}">
                <h2>${summaryHeader}</h2>
                <div class="rich-text-content">
                    <@hst.html hippohtml=legacyPublication.summary contentRewriter=gaContentRewriter/>
                </div>
            </div>
            </#if>
            <#-- [FTL-END] mandatory 'Summary' section -->

            <#-- [FTL-BEGIN] optional list of 'Key facts' section -->
            <#if hasKeyFacts>
            <div class="article-section article-section--highlighted" id="${slugify(keyFactsHeader)}">
                <div class="callout callout--attention">
                    <h2>${keyFactsHeader}</h2>
                    <div class="rich-text-content">
                        <@hst.html hippohtml=legacyPublication.keyFacts contentRewriter=gaContentRewriter/>
                    </div>
                </div>
            </div>
            </#if>
            <#-- [FTL-END] optional list of 'Key facts' section -->

            <#-- [FTL-BEGIN] 'Administrative sources' section -->
            <#if hasAdministrativeSources>
            <div class="article-section" id="${slugify(adminSourcesHeader)}">
                <h2>${adminSourcesHeader}</h2>
                <p data-uipath="ps.publication.administrative-sources">
                    ${legacyPublication.administrativeSources}
                </p>
            </div>
            </#if>
            <#-- [FTL-END] 'Administrative sources' section -->

            <#-- [FTL-BEGIN] Optional 'Attachments' section -->
            <#if hasResources>
            <div class="article-section" id="${slugify(resourcesHeader)}">
                <h2>${resourcesHeader}</h2>
                <ul data-uipath="ps.publication.resources" class="list">
                <#list legacyPublication.attachments as attachment>
                    <li class="attachment">
                        <@externalstorageLink attachment.resource; url>
                        <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','LegacyPublication','${attachment.resource.filename}');" onKeyUp="return vjsu.onKeyUp(event)">${attachment.text}</a>;
                        </@externalstorageLink>
                        <span class="fileSize">[size: <@formatFileSize bytesCount=attachment.resource.length/>]</span>
                    </li>
                </#list>
                <#list legacyPublication.resourceLinks as link>
                    <li>
                        <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','LegacyPublication','${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)" title="${link.linkText}">${link.linkText}</a>
                    </li>
                </#list>
                <#list legacyPublication.datasets as dataset>
                    <li>
                        <a href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}">${dataset.title}</a>
                    </li>
                </#list>
                </ul>
            </div>
            </#if>
            <#-- [FTL-END] Optional 'Attachments' section -->

            <#-- [FTL-BEGIN] Optional 'Related links' section -->
            <#if hasRelatedLinks>
            <div class="article-section" id="${slugify(relatedLinksHeader)}">
                <h2>${relatedLinksHeader}</h2>
                <ul data-uipath="ps.publication.related-links" class="list">
                    <#list legacyPublication.relatedLinks as link>
                    <li>
                        <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','LegacyPublication','${link.linkUrl}');" onKeyUp="return vjsu.onKeyUp(event)" title="${link.linkText}">${link.linkText}</a>
                    </li>
                    </#list>
                </ul>
            </div>
            </#if>
            <#-- [FTL-END] Optional 'Related links' section -->
        </div>
    </div>
</div>
</#macro>

<#-- ACTUAL TEMPLATE -->
<#if legacyPublication?? >
<article class="article article--legacy-publication">
    <#if legacyPublication.publiclyAccessible>
        <@fullContentOfPubliclyAvailablePublication/>
    <#else>
        <@restrictedContentOfUpcomingPublication/>
    </#if>
</article>
</#if>
