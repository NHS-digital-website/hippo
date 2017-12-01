<#include "../include/imports.ftl">
<#assign dateFormat="dd/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() >

<#if dataset??>

<div class="content-section content-section--highlight">
    <h2 class="doc-title" data-uipath="ps.dataset.title">${dataset.title}</h2>

    This dataset is part of <a class="label label--parent-document"
        href="<@hst.link hippobean=dataset.parentPublication.selfLinkBean/>"
        title="${dataset.parentPublication.title}"
    >${dataset.parentPublication.title}</a>

    <dl class="doc-metadata">
        <dt>Date Range</dt>
        <dd data-uipath="ps.dataset.date-range">
            <#if dataset.coverageStart?? >
                <@fmt.formatDate value=dataset.coverageStart.time type="Date" pattern=dateFormat />
            <#else>
                (Not specified)
            </#if>
            to
            <#if dataset.coverageEnd?? >
                <@fmt.formatDate value=dataset.coverageEnd.time type="Date" pattern=dateFormat />
            <#else>
                (Not specified)
            </#if>
        </dd>

        <#if dataset.geographicCoverage??>
        <dt id="geographic-coverage">Geographic coverage</dt>
        <dd data-uipath="ps.dataset.geographic-coverage">
            ${dataset.geographicCoverage?html}
        </dd>
        </#if>

        <#if dataset.granularity?has_content >
        <dt>Geographical granularity</dt>
        <dd data-uipath="ps.dataset.granularity">
            <#list dataset.granularity as granularityItem><#if granularityItem?index != 0>, </#if>${granularityItem?html}</#list>
        </dd>
        </#if>
    </dl>

</div>

<div class="content-section">
    <p class="doc-summary" data-uipath="ps.dataset.summary">${dataset.summary}</p>
    <p class="doc-summary" data-uipath="ps.dataset.purpose">${dataset.purpose}</p>
</div>

<div class="content-section">
    <h3>Resources</h3>
    <#list dataset.files as attachment>
        <li class="attachment">
            <a title="${attachment.filename}" href="<@hst.link hippobean=attachment/>">${attachment.filename}</a>;
            <span class="fileSize">size: <@formatFileSize bytesCount=attachment.length/></span>
        </li>
    </#list>
</div>

<#else>
    <span>${error}</span>
</#if>
