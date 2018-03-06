<#ftl output_format="HTML">

<#macro textSection section>
    <div data-uipath="ps.publication.body.text-section">
        <#if section.heading?has_content>
            <h2 data-uipath="ps.publication.body.text-section.heading">${section.heading}</h2>
        </#if>
        <#if section.text?has_content>
            <p data-uipath="ps.publication.body.text-section.text">${section.text}</p>
        </#if>
    </div>
</#macro>
