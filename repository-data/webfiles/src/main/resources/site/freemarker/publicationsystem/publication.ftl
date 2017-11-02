<#include "../include/imports.ftl">
<#assign dateFormat="h:mm a d/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() />
<#assign formatRestrictableDate="uk.nhs.digital.ps.directives.RestrictableDateFormatterDirective"?new() />

<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.beans.Publication" -->

<#macro nominalPublicationDate>
<dt>Nominal Publication Date:</dt>
<dd id="nominal-date-value">
    <#if document.nominalPublicationDate??>
        <@formatRestrictableDate value=publication.nominalPublicationDate/>
    <#else>
        (Not specified)
    </#if>
</dd>
</#macro>

<#if publication?? >
    <#if parentSeries??>
    This is part of
    <a class="label label--parent-document" href="<@hst.link hippobean=parentSeries.selfLinkBean/>"
        title="${parentSeries.title}">
    ${parentSeries.title}
    </a>
    </#if>

    <#if publication.publiclyAccessible>
        <div class="content-section">
            <h2 class="doc-title" data-uipath="ps.publication.title">${publication.title?html}</h2>
            <dl class="doc-metadata">
                <@nominalPublicationDate/>

                <#if publication.geographicCoverage?has_content>
                <dt id="geographic-coverage">Geographic coverage</dt>
                <dd data-uipath="ps.publication.geographic-coverage">
                    ${publication.geographicCoverage?html}
                </dd>
                </#if>

                <#if publication.granularity?has_content >
                <dt>Geographical granularity</dt>
                <dd data-uipath="ps.publication.granularity">
                    <#list publication.granularity as granularityItem><#if granularityItem?index != 0>, </#if>${granularityItem?html}</#list>
                </dd>
                </#if>

                <dt>Date Range</dt>
                <dd>
                    <#if publication.coverageStart?? >
                        <@fmt.formatDate value=publication.coverageStart.time type="Date" pattern=dateFormat />
                    <#else>
                        (Not specified)
                    </#if>
                    to
                    <#if publication.coverageEnd?? >
                        <@fmt.formatDate value=publication.coverageEnd.time type="Date" pattern=dateFormat />
                    <#else>
                        (Not specified)
                    </#if>
                </dd>
                <#if taxonomyList??>
                <dt>Taxonomy</dt>
                <dd>
                    <#list taxonomyList as taxonomyChain>
                        <div>${taxonomyChain?join(" => ")}</div>
                    </#list>
                </dd>
                </#if>
            </dl>

            <div data-uipath="ps.publication.information-types" style="text-align: right; font-weight: 600; font-size: 1.4em;">
                <#list publication.informationType as type><#if type?index != 0>, </#if>${type?html}</#list>
            </div>

            <p class="doc-summary" data-uipath="ps.publication.summary">${publication.summary?html}</p>

            <div id="key-facts">
                <h4>Key facts:</h4>
                ${publication.keyFacts?html}
            </div>
        </div>

        <div class="content-section">
            <h3>Resources</h3>
            <ul>
                <#list publication.attachments as attachment>
                    <li class="attachment">
                        <i class="icon icon--xxx">[pdf]</i>
                        <a class="attachment-hyperlink" href="<@hst.link hippobean=attachment/>">${attachment.filename}</a>;
                        <span class="fileSize">size: <@formatFileSize bytesCount=attachment.length/></span>
                    </li>
                </#list>

                <#list publication.relatedLinks as link>
                    <li>
                        <i class="icon icon--link">[link]</i>
                        <a href="${link.linkUrl}">${link.linkText}</a>
                    </li>
                </#list>

                <#list datasets as dataset>
                    <li class="dataset">
                        <i class="icon icon--dataset">[dataset]</i>
                        <a href="<@hst.link hippobean=dataset.selfLinkBean/>" title="${dataset.title}">${dataset.title}</a>
                    </li>
                </#list>
            </ul>
        </div>

        <#if publication.administrativeSources?has_content >
        <div id="administrative-sources">
            <h4>Administrative sources:</h4>
            <#if document.administrativeSources?has_content >
                ${publication.administrativeSources?html}
            <#else>
                (No sources)
            </#if>
        </div>
        </#if>

        <#if (taxonomyList?size > 0) >
        <div id="taxonomy">
            <h4>Taxonomy:</h4>
            <#list taxonomyList as taxonomyChain>
                <div>${taxonomyChain?join(" => ")}</div>
            </#list>
        </div>
        </#if>
    <#else>
    <div class="content-section">
        <h2 class="doc-title" data-uipath="ps.publication.title">${publication.title?html}</h2>
        <dl class="doc-metadata">
            <@nominalPublicationDate/>
            <p class="empty-field-disclaimer">(Upcoming, not yet published)</p>
        </dl>
    </div>
    </#if>
</#if>
