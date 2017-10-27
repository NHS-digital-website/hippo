<#assign hst=JspTaglibs["http://www.hippoecm.org/jsp/hst/core"] />
<#assign fmt=JspTaglibs ["http://java.sun.com/jsp/jstl/fmt"] />
<#assign dateFormat="h:mm a d/MM/yyyy"/>
<#assign formatFileSize="uk.nhs.digital.ps.directives.FileSizeFormatterDirective"?new() >

<#if document??>
    <#if parentSeries??>
    This document is part of Series
    <a class="label label--series" href="<@hst.link hippobean=parentSeries.selfLink/>"
        title="${parentSeries.title}">
    ${parentSeries.title}
    </a>
    </#if>
    <h1 class="doc-title" id="title">${document.title?html}</h1>
    <p class="doc-summary" id="summary">${document.summary?html}</p>

    <div id="taxonomy">
        <h4>Taxonomy:</h4>
        <#if taxonomyList??>
            <#list taxonomyList as taxonomyChain>
                <div>${taxonomyChain?join(" => ")}</div>
            </#list>
        <#else>
            (None specified)
        </#if>
    </div>
    <div id="nominal-date">
        <h4>Nominal publication date:</h4>
        <#if document.nominalDate??>
            <@fmt.formatDate value=document.nominalDate.time type="Date" pattern=dateFormat />
        <#else>
            (Not specified)
        </#if>
    </div>
    <div id="information-types">
        <h4>Information types:</h4>
        <#list document.informationType as type><#if type?index != 0>, </#if>${type?html}</#list>
    </div>
    <div id="key-facts">
        <h4>Key facts:</h4>
        ${document.keyFacts?html}
    </div>
    <div id="coverage-start">
        <h4>Coverage start:</h4>
        <#if document.coverageStart??>
            <@fmt.formatDate value=document.coverageStart.time type="Date" pattern=dateFormat />
        <#else>
            (Not specified)
        </#if>
    </div>
    <div id="coverage-end">
        <h4>Coverage end:</h4>
        <#if document.coverageEnd??>
            <@fmt.formatDate value=document.coverageEnd.time type="Date" pattern=dateFormat />
            <#else>
                (Not specified)
        </#if>
    </div>
    <div id="geographic-coverage">
        <h4>Geographic coverage:</h4>
        <#if document.geographicCoverage??>
            ${document.geographicCoverage?html}
        <#else>
            (Not specified)
        </#if>
    </div>
    <div id="granularity">
        <h4>Granularity:</h4>
        <#if document.granularity?has_content>
            <#list document.granularity as granularityItem><#if granularityItem?index != 0>, </#if>${granularityItem?html}</#list>
        <#else>
            (Not specified)
        </#if>
    </div>
    <div id="attachments">
        <h4>Attachments:</h4>
        <#if document.attachments?has_content>
            <ul>
            <#list document.attachments as attachment>
                <li class="attachment">
                    <a class="attachment-hyperlink" href="<@hst.link hippobean=attachment/>">${attachment.filename}</a>;
                    <span class="fileSize">size: <@formatFileSize bytesCount=attachment.length/></span>
                </li>
            </#list>
            </ul>
        <#else>
            (No attachments)
        </#if>
    </div>
    <div id="related-links">
        <h4>Related Links:</h4>
        <#if document.relatedLinks?has_content >
            <ul>
                <#list document.relatedLinks as link>
                    <#if link.linkText?has_content>
                        <#assign linkText=link.linkText/>
                    <#else>
                        <#assign linkText=link.linkUrl/>
                    </#if>
                    <li><a href="${link.linkUrl}">${linkText}</a></li>
                </#list>
            </ul>
        <#else>
            <p>(None)</p>
        </#if>
    </div>
    <div id="administrative-sources">
        <h4>Administrative sources:</h4>
        ${document.administrativeSources?html}
    </div>
</#if>
</body>
</html>
