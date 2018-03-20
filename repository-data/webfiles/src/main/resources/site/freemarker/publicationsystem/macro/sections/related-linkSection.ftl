<#ftl output_format="HTML">

<#macro relatedLinkSection section>
    <div data-uipath="ps.publication.body.relatedlink-section">
        <a data-uipath="ps.publication.body.relatedlink-section.text" target="_blank"
           href="${section.linkUrl}" onClick="logGoogleAnalyticsEvent('Link click','Publication','${section.linkUrl}');">${section.linkText}</a>
    </div>
</#macro>
