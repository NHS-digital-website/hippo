<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() />
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>
<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->

<#macro nominalPublicationDate>
    <dl class="media__body">
        <dt><@fmt.message key="headers.publication-date"/></dt>
        <dd class="zeta" data-uipath="ps.publication.nominal-publication-date">
            <@formatRestrictableDate value=legacyPublication.nominalPublicationDate/>
        </dd>
    </dl>
</#macro>

<#macro restrictedContentOfUpcomingPublication>
    <section class="document-header">
        <div class="document-header__inner">
            <h1 data-uipath="ps.document.title">${legacyPublication.title}</h1>
            <div class="media push--top push--bottom">
                <div class="media__icon media__icon--calendar"></div>
                <@nominalPublicationDate/>
            </div>
        </div>
    </section>
    <section class="document-content">
        <div class="layout layout--large">
            <div class="layout__item layout-2-3">
                <p data-uipath="ps.publication.upcoming-disclaimer">(Upcoming, not yet published)</p>
            </div>
        </div>
    </section>
</#macro>

<#macro fullContentOfPubliclyAvailablePublication>
    <section class="document-header" aria-label="Document Header">
        <div class="document-header__inner">
            <h3 class="flush--bottom push-half--top"><@fmt.message key="labels.publication"/></h3>

            <h3 data-uipath="ps.publication.information-types" class="flush--bottom push-half--top">
                <#if legacyPublication.informationType?has_content>
                    <#list legacyPublication.informationType as type>${type}<#sep>, </#list>
                </#if>
            </h3>

            <h1 class="layout-5-6" data-uipath="ps.document.title">${legacyPublication.title}</h1>

            <#if legacyPublication.parentDocument??>
                <p class="flush--top">
                    This is part of
                    <a class="label label--parent-document" href="<@hst.link hippobean=legacyPublication.parentDocument.selfLinkBean/>"
                        title="${legacyPublication.parentDocument.title}">
                        ${legacyPublication.parentDocument.title}
                    </a>
                </p>
            </#if>

            <div class="flex push--top push--bottom">
                <div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--calendar"></div>
                        <@nominalPublicationDate/>
                    </div>
                </div><!--

                --><#if legacyPublication.geographicCoverage?has_content><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--geographic-coverage"></div>
                        <dl class="media__body">
                            <dt id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                            <dd data-uipath="ps.publication.geographic-coverage">
                                <#list legacyPublication.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                            </dd>
                        </dl>
                    </div>
                </div></#if><!--

                --><#if legacyPublication.granularity?has_content ><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--granularity"></div>
                        <dl class="media__body">
                            <dt><@fmt.message key="headers.geographical-granularity"/></dt>
                            <dd data-uipath="ps.publication.granularity">
                                <#list legacyPublication.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                            </dd>
                        </dl>
                    </div>
                </div></#if><!--

                --><#if legacyPublication.coverageStart?? ><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--date-range"></div>
                        <dl class="media__body">
                            <dt><@fmt.message key="headers.date-range"/></dt>
                            <dd data-uipath="ps.publication.date-range">
                                <#if legacyPublication.coverageStart?? && legacyPublication.coverageEnd??>
                                    <@formatCoverageDates start=legacyPublication.coverageStart.time end=legacyPublication.coverageEnd.time/>
                                <#else>
                                    (Not specified)
                                </#if>
                            </dd>
                        </dl>
                    </div>
                </div></#if>

            </div>
        </div>
    </section>

    <section class="document-content" aria-label="Document Content">
        <div class="layout layout--large">
            <div class="layout__item layout-2-3">
                <h2><@fmt.message key="headers.summary"/></h2>
                <@hst.html hippohtml=legacyPublication.summary />

                <#if legacyPublication.keyFacts.elements?has_content>
                    <h2><@fmt.message key="headers.key-facts"/></h2>
                    <@hst.html hippohtml=legacyPublication.keyFacts />
                </#if>

                <#if legacyPublication.administrativeSources?has_content>
                    <h2><@fmt.message key="headers.administrative-sources"/></h2>
                    <p data-uipath="ps.publication.administrative-sources">
                        ${legacyPublication.administrativeSources}
                    </p>
                </#if>
            </div><!--

            --><div class="layout__item layout-1-3">
                <div class="panel panel--grey push-half--bottom">
                    <h3><@fmt.message key="headers.resources"/></h3>
                    <ul data-uipath="ps.publication.resources">
                        <#list legacyPublication.attachments as attachment>
                            <li class="attachment">
                                <a title="${attachment.text}" href="<@hst.link hippobean=attachment.resource/>" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');">${attachment.text}</a>;
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
                    <#if legacyPublication.relatedLinks?has_content>
                        <h3><@fmt.message key="headers.related-links"/></h3>
                        <ul data-uipath="ps.publication.related-links">
                            <#list legacyPublication.relatedLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');">${link.linkText}</a>
                            </li>
                            </#list>
                        </ul>
                    </#if>
                </div>
            </div>
        </div>
    </section>
</#macro>

<#-- ACTUAL TEMPLATE -->
<#if legacyPublication?? >
    <#if legacyPublication.publiclyAccessible>
        <@fullContentOfPubliclyAvailablePublication/>
    <#else>
        <@restrictedContentOfUpcomingPublication/>
    </#if>
</#if>
