<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<#include "./macro/sections/sections.ftl">
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
            <@formatRestrictableDate value=publication.nominalPublicationDate/>
        </dd>
    </dl>
</#macro>

<#macro restrictedContentOfUpcomingPublication>
    <section class="document-header">
        <div class="document-header__inner">
            <h1 data-uipath="ps.document.title">${publication.title}</h1>
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

            <#if publication.informationType?has_content>
                <#list publication.informationType as type>
                    <#if type == "National statistics">
                        <div class="media__icon--national-statistics" data-uipath="ps.publication.national-statistics" title="National statistics"></div>
                        <#break>
                    </#if>
                </#list>
            </#if>

            <h3 class="flush--bottom push-half--top"><@fmt.message key="labels.publication"/></h3>

            <h3 data-uipath="ps.publication.information-types" class="flush--bottom push-half--top">
                <#if publication.informationType?has_content>
                    <#list publication.informationType as type>${type}<#sep>, </#list>
                </#if>
            </h3>

            <h1 class="layout-5-6" data-uipath="ps.document.title">${publication.title}</h1>

            <#if publication.parentDocument??>
                <p class="flush--top">
                    This is part of
                    <a class="label label--parent-document" href="<@hst.link hippobean=publication.parentDocument.selfLinkBean/>"
                        title="${publication.parentDocument.title}">
                        ${publication.parentDocument.title}
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

                --><#if publication.geographicCoverage?has_content><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--geographic-coverage"></div>
                        <dl class="media__body">
                            <dt id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                            <dd data-uipath="ps.publication.geographic-coverage">
                                <#list publication.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                            </dd>
                        </dl>
                    </div>
                </div></#if><!--

                --><#if publication.granularity?has_content ><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--granularity"></div>
                        <dl class="media__body">
                            <dt><@fmt.message key="headers.geographical-granularity"/></dt>
                            <dd data-uipath="ps.publication.granularity">
                                <#list publication.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                            </dd>
                        </dl>
                    </div>
                </div></#if><!--

                --><#if publication.coverageStart?? ><div class="flex__item">
                    <div class="media">
                        <div class="media__icon media__icon--date-range"></div>
                        <dl class="media__body">
                            <dt><@fmt.message key="headers.date-range"/></dt>
                            <dd data-uipath="ps.publication.date-range">
                                <#if publication.coverageStart?? && publication.coverageEnd??>
                                    <@formatCoverageDates start=publication.coverageStart.time end=publication.coverageEnd.time/>
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
        <div>
            <h2><@fmt.message key="headers.summary"/></h2>
            <@structuredText item=publication.summary uipath="ps.publication.summary" />

            <#if publication.keyFacts.elements?has_content>
                <h2><@fmt.message key="headers.key-facts"/></h2>
                <@structuredText item=publication.keyFacts uipath="ps.publication.key-facts" />
            </#if>

            <#if publication.administrativeSources?has_content>
                <h2><@fmt.message key="headers.administrative-sources"/></h2>
                <p data-uipath="ps.publication.administrative-sources">
                    ${publication.administrativeSources}
                </p>
            </#if>

            <#if publication.bodySections?has_content>
                <@sections sections=publication.bodySections />
            </#if>

            <#if publication.datasets?has_content>
                <h2><@fmt.message key="headers.datasets"/></h2>
                <ul data-uipath="ps.publication.datasets">
                    <#list publication.datasets as dataset>
                        <li>
                            <a href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}">${dataset.title}</a>
                        </li>
                    </#list>
                </ul>
            </#if>

            <#if publication.attachments?has_content || publication.resourceLinks?has_content>
                <h2><@fmt.message key="headers.resources"/></h2>
                <ul data-uipath="ps.publication.resources">
                    <#list publication.attachments as attachment>
                        <li class="attachment">
                            <a title="${attachment.text}" href="<@hst.link hippobean=attachment.resource/>" onClick="logGoogleAnalyticsEvent('Download attachment','Publication','${attachment.resource.filename}');">${attachment.text}</a>;
                            <span class="fileSize">size: <@formatFileSize bytesCount=attachment.resource.length/></span>
                        </li>
                    </#list>
                    <#list publication.resourceLinks as link>
                        <li>
                            <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');">${link.linkText}</a>
                        </li>
                    </#list>
                </ul>
            </#if>

            <#if publication.relatedLinks?has_content>
                <h3><@fmt.message key="headers.related-links"/></h3>
                <ul data-uipath="ps.publication.related-links">
                    <#list publication.relatedLinks as link>
                    <li>
                        <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${link.linkUrl}');">${link.linkText}</a>
                    </li>
                    </#list>
                </ul>
            </#if>
        </div>
    </section>
</#macro>

<#-- ACTUAL TEMPLATE -->
<#if publication?? >
    <#if publication.publiclyAccessible>
        <@fullContentOfPubliclyAvailablePublication/>
    <#else>
        <@restrictedContentOfUpcomingPublication/>
    </#if>
</#if>
