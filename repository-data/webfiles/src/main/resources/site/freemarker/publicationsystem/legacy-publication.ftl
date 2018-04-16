<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() />
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>
<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#macro nationalStatsStamp>
    <div class="article-header__stamp">
        <img src="<@hst.webfile path="images/national-statistics-logo@2x.png"/>" data-uipath="ps.publication.national-statistics" title="National Statistics" class="image-icon image-icon--large" />
    </div>
</#macro>

<#macro restrictedContentOfUpcomingPublication>
<div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
    <div class="local-header article-header article-header--detailed">
        <div class="grid-wrapper">
            <div class="article-header__inner">
                <h1 class="local-header__title" data-uipath="document.title">${legacyPublication.title}</h1>

                <hr class="hr hr--short hr--light">

                <div class="article-header__detail-lines">
                    <div class="article-header__detail-line">
                        <dl class="article-header__detail-list">
                            <dt class="article-header__detail-list-key"><@fmt.message key="headers.publication-date"/></dt>
                            <dd class="article-header__detail-list-value" data-uipath="ps.publication.nominal-publication-date">
                                <@formatRestrictableDate value=legacyPublication.nominalPublicationDate/>
                            </dd>
                        </dl>
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

            <div class="article-header__detail-lines">
                <div class="article-header__detail-line">
                    <dl class="article-header__detail-list">
                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.publication-date"/></dt>
                        <dd class="article-header__detail-list-value" data-uipath="ps.publication.nominal-publication-date">
                            <@formatRestrictableDate value=legacyPublication.nominalPublicationDate/>
                        </dd>
                    </dl>
                </div>

                <#if legacyPublication.geographicCoverage?has_content>
                <div class="article-header__detail-line">
                    <dl class="article-header__detail-list">
                        <dt class="article-header__detail-list-key" id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                        <dd class="article-header__detail-list-value" data-uipath="ps.publication.geographic-coverage">
                            <#list legacyPublication.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                        </dd>
                    </dl>
                </div>
                </#if>

                <#if legacyPublication.granularity?has_content >
                <div class="article-header__detail-line">
                    <dl class="article-header__detail-list">
                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.geographical-granularity"/></dt>
                        <dd class="article-header__detail-list-value" data-uipath="ps.publication.granularity">
                            <#list legacyPublication.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                        </dd>
                    </dl>
                </div>
                </#if>

                <#if legacyPublication.coverageStart?? >
                <div class="article-header__detail-line">
                    <dl class="article-header__detail-list">
                        <dt class="article-header__detail-list-key"><@fmt.message key="headers.date-range"/></dt>
                        <dd class="article-header__detail-list-value" data-uipath="ps.publication.date-range">
                            <#if legacyPublication.coverageStart?? && legacyPublication.coverageEnd??>
                                <@formatCoverageDates start=legacyPublication.coverageStart.time end=legacyPublication.coverageEnd.time/>
                            <#else>
                                (Not specified)
                            </#if>
                        </dd>
                    </dl>
                </div>
                </#if>
            </div>
        </div>
    </div>
</div>

<#assign renderNav = false />
<#if legacyPublication.getExtAttachments()?size gt 0 || legacyPublication.resourceLinks?has_content || legacyPublication.datasets?has_content || legacyPublication.relatedLinks?has_content>
    <#assign renderNav = true />
</#if>

<div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
    <div class="grid-row">
        <#if renderNav>
        <div class="column column--one-third page-block page-block--sidebar page-block--sticky-top">
            <div class="article-section-nav">
                <#-- [FTL-BEGIN] Resources, attachments and datasets navigation section -->
                <#if legacyPublication.getExtAttachments()?size gt 0 || legacyPublication.resourceLinks?has_content || legacyPublication.datasets?has_content>
                <div class="article-section-nav__section">
                    <h3 class="article-section-nav__title">
                        <@fmt.message key="headers.resources"/>
                    </h3>
                    <hr>
                    <nav role="navigation">
                        <ul data-uipath="ps.publication.resources" class="article-section-nav__list">
                        <#list legacyPublication.getExtAttachments() as attachment>
                            <li class="attachment">
                                <@externalstorageLink attachment.resource; url>
                                <a title="${attachment.text}" href="${url}" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');">${attachment.text}</a>;
                                </@externalstorageLink>
                                <span class="fileSize">size: <@formatFileSize bytesCount=attachment.resource.length/></span>
                            </li>
                        </#list>
                        <#list legacyPublication.resourceLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');">${link.linkText}</a>
                            </li>
                        </#list>
                        <#list legacyPublication.datasets as dataset>
                            <li>
                                <a href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}">${dataset.title}</a>
                            </li>
                        </#list>
                        </ul>
                    </nav>
                </div>
                </#if>
                <#-- [FTL-END] Resources, attachments and datasets navigation section -->

                <#-- [FTL-BEGIN] Related links navigation section -->
                <#if legacyPublication.relatedLinks?has_content>
                <div class="article-section-nav__section">
                    <h3 class="article-section-nav__title"><@fmt.message key="headers.related-links"/></h3>
                    <hr>
                    <nav role="navigation">
                        <ul data-uipath="ps.publication.related-links" class="article-section-nav__list">
                            <#list legacyPublication.relatedLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');">${link.linkText}</a>
                            </li>
                            </#list>
                        </ul>
                    </nav>
                </div>
                </#if>
                <#-- [FTL-END] Related links navigation section -->
            </div>
        </div>
        </#if>

        <div class="column column--two-thirds page-block page-block--main">
            <#-- [FTL-BEGIN] mandatory 'summary section' -->
            <#if legacyPublication.keyFacts.content?has_content>
                <#assign summarySectionClassName = "article-section article-section--summary no-border">
            <#else>
                <#assign summarySectionClassName = "article-section article-section--summary">
            </#if>
            <#-- [FTL-END] mandatory 'Summary' section -->

            <div id="section-summary" class="${summarySectionClassName}">
                <h2><@fmt.message key="headers.summary"/></h2>
                <div class="rich-text-content">
                    <@hst.html hippohtml=legacyPublication.summary contentRewriter=gaContentRewriter/>
                </div>
            </div>
            <#-- [FTL-END] mandatory 'Summary' section -->

            <#-- [FTL-BEGIN] optional list of 'Key facts' section -->
            <#if legacyPublication.keyFacts.content?has_content>
            <div class="article-section article-section--highlighted">
                <div class="callout callout--attention">
                    <h2><@fmt.message key="headers.key-facts"/></h2>
                    <div class="rich-text-content">
                        <@hst.html hippohtml=legacyPublication.keyFacts contentRewriter=gaContentRewriter/>
                    </div>
                </div>
            </div>
            </#if>
            <#-- [FTL-END] optional list of 'Key facts' section -->

            <#-- [FTL-BEGIN] 'Administrative sources' section -->
            <div class="article-section">
                <#if legacyPublication.administrativeSources?has_content>
                    <h2><@fmt.message key="headers.administrative-sources"/></h2>
                    <p data-uipath="ps.publication.administrative-sources">
                        ${legacyPublication.administrativeSources}
                    </p>
                </#if>
            </div>
            <#-- [FTL-END] 'Administrative sources' section -->
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
