<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers,publicationsystem.labels"/>

<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() >
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() >

<#if dataset??>

<section class="document-header" aria-label="Document Header">
    <div class="document-header__inner">
        <h3 class="flush--bottom push-half--top"><@fmt.message key="labels.dataset"/></h3>
        <h1 class="layout-5-6" data-uipath="ps.document.title">${dataset.title}</h1>
        <p class="flush--top">
            This data set is part of
            <a class="label label--parent-document"
                href="<@hst.link hippobean=dataset.parentPublication.selfLinkBean/>"
                title="${dataset.parentPublication.title}"
            >${dataset.parentPublication.title}</a>
        </p>

        <div class="flex push--top push--bottom">
            <div class="flex__item">
                <div class="media">
                    <div class="media__icon media__icon--date-range"></div>
                    <dl class="media__body">
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
            </div><!--

            --><#if dataset.geographicCoverage??><div class="flex__item">
                <div class="media">
                    <div class="media__icon media__icon--granularity"></div>
                    <dl class="media__body">
                        <dt id="geographic-coverage"><@fmt.message key="headers.geographical-coverage"/></dt>
                        <dd data-uipath="ps.dataset.geographic-coverage">
                            ${dataset.geographicCoverage}
                        </dd>
                    </dl>
                </div>
            </div></#if><!--

            --><#if dataset.granularity?has_content ><div class="flex__item">
                <div class="media">
                    <div class="media__icon media__icon--geographic-coverage"></div>
                    <dl class="media__body">
                        <dt><@fmt.message key="headers.geographical-granularity"/></dt>
                        <dd data-uipath="ps.dataset.granularity">
                            <#list dataset.granularity as granularityItem><#if granularityItem?index != 0>, </#if>${granularityItem}</#list>
                        </dd>
                    </dl>
                </div>
            </div></#if><!--

            --><#if dataset.nominalDate?has_content ><div class="flex__item">
                <div class="media">
                    <div class="media__icon media__icon--calendar"></div>
                    <dl class="media__body">
                        <dt><@fmt.message key="headers.publication-date"/></dt>
                        <dd data-uipath="ps.dataset.nominal-date">
                            ${dataset.nominalDate.time?string[dateFormat]}
                        </dd>
                    </dl>
                </div>
            </div></#if><!--

            --><#if dataset.nextPublicationDate?has_content ><div class="flex__item">
                <div class="media">
                    <div class="media__icon media__icon--calendar"></div>
                    <dl class="media__body">
                        <dt><@fmt.message key="headers.next-publication-date"/></dt>
                        <dd data-uipath="ps.dataset.next-publication-date">
                            ${dataset.nextPublicationDate.time?string[dateFormat]}
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
            <@structuredText item=dataset.summary uipath="ps.dataset.summary" />

        </div><!--

        --><div class="layout__item layout-1-3">
            <div class="panel panel--grey push-half--bottom">
                <h3><@fmt.message key="headers.resources"/></h3>
                <ul data-uipath="ps.dataset.resources">
                    <#list dataset.files as attachment>
                        <li class="attachment">
                            <a title="${attachment.text}" href="<@hst.link hippobean=attachment.resource/>">${attachment.text}</a>;
                            <span class="fileSize">size: <@formatFileSize bytesCount=attachment.resource.length/></span>
                        </li>
                    </#list>
                    <#list dataset.resourceLinks as link>
                        <li>
                            <a href="${link.linkUrl}">${link.linkText}</a>
                        </li>
                    </#list>
                </ul>
            </div>
            <#if dataset.taxonomyList?has_content>
                <div class="panel panel--grey push-half--bottom">
                    <h3><@fmt.message key="headers.taxonomy"/></h3>
                    <div data-uipath="ps.dataset.taxonomy">
                        <#list dataset.taxonomyList as taxonomyChain>
                            <div>${taxonomyChain?join(" => ")}</div>
                        </#list>
                    </div>
                </div>
            </#if>
        </div>
    </div>
</section>

<#else>
    <span>${error}</span>
</#if>
