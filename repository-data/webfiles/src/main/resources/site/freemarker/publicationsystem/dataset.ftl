<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers,publicationsystem.labels"/>
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />

<#assign dateFormat="dd MMM yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() >
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() >


<main>
    <div class="page-block">
        <div class="grid-wrapper grid-wrapper--article">

            <p>template: dataset.ftl</p>

            <#if dataset??>

            <div class="local-header article-header">
                <div class="document-header__inner">
                    <h3><@fmt.message key="labels.dataset"/></h3>
                    <h1 data-uipath="ps.document.title">${dataset.title}</h1>
                    <p>This data set is part of <a href="<@hst.link hippobean=dataset.parentPublication.selfLinkBean/>" title="${dataset.parentPublication.title}">${dataset.parentPublication.title}</a></p>

                    <div class="grid-row">

                        <div class="column--one-quarter">
                            <div class="ico-block">
                                <div class="ico-block__icon ico-block__icon--date-range"></div>
                                <dl class="ico-block__body">
                                    <dt><@fmt.message key="headers.date-range"/></dt>
                                    <dd data-uipath="ps.dataset.date-range">
                                        <#if dataset.coverageStart?? && dataset.coverageEnd??>
                                            <@formatCoverageDates start=dataset.coverageStart.time end=dataset.coverageEnd.time/>
                                        <#else>
                                            (Not specified)
                                        </#if>
                                    </dd>
                                </dl>
                            </div>
                        </div>

                        <#if dataset.geographicCoverage?has_content>
                        <div class="column--one-quarter">
                            <div class="ico-block">
                                <div class="ico-block__icon ico-block__icon--granularity"></div>
                                <dl class="ico-block__body">
                                    <dt id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                                    <dd data-uipath="ps.dataset.geographic-coverage">
                                        <#list dataset.geographicCoverage as geographicCoverageItem>${geographicCoverageItem}<#sep>, </#list>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>

                        <#if dataset.granularity?has_content >
                        <div class="column--one-quarter">
                            <div class="ico-block">
                                <div class="ico-block__icon ico-block__icon--geographic-coverage"></div>
                                <dl class="ico-block__body">
                                    <dt><@fmt.message key="headers.geographical-granularity"/></dt>
                                    <dd data-uipath="ps.dataset.granularity">
                                        <#list dataset.granularity as granularityItem>${granularityItem}<#sep>, </#list>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>

                        <#if dataset.nominalDate?has_content >
                        <div class="column--one-quarter">
                            <div class="ico-block">
                                <div class="ico-block__icon ico-block__icon--calendar"></div>
                                <dl class="ico-block__body">
                                    <dt><@fmt.message key="headers.publication-date"/></dt>
                                    <dd data-uipath="ps.dataset.nominal-date">
                                        <@formatRestrictableDate value=dataset.nominalDate/>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>

                        <#if dataset.nextPublicationDate?has_content >
                        <div class="column--one-quarter">
                            <div class="ico-block">
                                <div class="ico-block__icon ico-block__icon--calendar"></div>
                                <dl class="ico-block__body">
                                    <dt><@fmt.message key="headers.next-publication-date"/></dt>
                                    <dd data-uipath="ps.dataset.next-publication-date">
                                        ${dataset.nextPublicationDate.time?string[dateFormat]}
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        </#if>

                    </div>
                </div>
            </div>

            <section class="article-section">
                <div>
                    <h2><@fmt.message key="headers.summary"/></h2>
                    <@structuredText item=dataset.summary uipath="ps.dataset.summary" />

                    <h2><@fmt.message key="headers.resources"/></h2>
                    <ul data-uipath="ps.dataset.resources">
                        <#list dataset.files as attachment>
                            <li class="attachment">
                                <a title="${attachment.text}" href="<@hst.link hippobean=attachment.resource/>" onClick="logGoogleAnalyticsEvent('Download attachment','Data set','${attachment.resource.filename}');">${attachment.text}</a>;
                                <span class="fileSize">size: <@formatFileSize bytesCount=attachment.resource.length/></span>
                            </li>
                        </#list>
                        <#list dataset.resourceLinks as link>
                            <li>
                                <a href="${link.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Data set','${link.linkUrl}');">${link.linkText}</a>
                            </li>
                        </#list>
                    </ul>
                </div>
            </section>

            <#else>
                <span>${error}</span>
            </#if>

        </div>
    </div>
</main>
