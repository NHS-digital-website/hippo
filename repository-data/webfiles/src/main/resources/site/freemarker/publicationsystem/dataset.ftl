<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers"/>

<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() >
<#assign formatCoverageDates="uk.nhs.digital.ps.directives.CoverageDatesFormatterDirective"?new() >

<#if dataset??>

<section class="document-header">
    <div class="document-header__inner">
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
                    <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
                    <dl class="media__body">
                        <dt>Date Range</dt>
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
                    <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
                    <dl class="media__body">
                        <dt id="geographic-coverage">Geographic coverage</dt>
                        <dd data-uipath="ps.dataset.geographic-coverage">
                            ${dataset.geographicCoverage?html}
                        </dd>
                    </dl>
                </div>
            </div></#if><!--

            --><#if dataset.granularity?has_content ><div class="flex__item">
                <div class="media">
                    <img class="media__img" src="http://via.placeholder.com/52x52" alt="">
                    <dl class="media__body">
                        <dt>Geographical granularity</dt>
                        <dd data-uipath="ps.dataset.granularity">
                            <#list dataset.granularity as granularityItem><#if granularityItem?index != 0>, </#if>${granularityItem?html}</#list>
                        </dd>
                    </dl>
                </div>
            </div></#if>

        </div>
    </div>
</section>

<section class="document-content">
    <div class="layout layout--large">
        <div class="layout__item layout-2-3">

            <h2><@fmt.message key="headers.summary"/></h2>
            <@structuredText item=dataset.summary uipath="ps.dataset.summary" />

        </div><!--

        --><div class="layout__item layout-1-3">
            <div class="panel panel--grey">
                <h3><@fmt.message key="headers.resources"/></h3>
                <ul data-uipath="ps.dataset.resources">
                    <#list dataset.files as attachment>
                        <li class="attachment">
                            <a title="${attachment.filename}" href="<@hst.link hippobean=attachment/>">${attachment.filename}</a>;
                            <span class="fileSize">size: <@formatFileSize bytesCount=attachment.length/></span>
                        </li>
                    </#list>
                    <#list dataset.resourceLinks as link>
                        <li>
                            <a href="${link.linkUrl}">${link.linkText}</a>
                        </li>
                    </#list>
                </ul>
            </div>
        </div>
    </div>
</section>

<#else>
    <span>${error}</span>
</#if>
