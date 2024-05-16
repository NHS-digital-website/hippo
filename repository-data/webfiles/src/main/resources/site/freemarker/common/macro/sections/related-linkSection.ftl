<#ftl output_format="HTML">

<#macro relatedLinkSection section>
    <div data-uipath="ps.publication.relatedlink-section">
        <a data-uipath="ps.publication.relatedlink-section.text" target="_blank" href="${section.linkUrl}">${section.linkText}</a>
    </div>
</#macro>
