<#ftl output_format="HTML">

<#macro textSection section>
    <h1>textSection.ftl</h1>
    <div data-uipath="ps.publication.text-section">
        <#if section.heading?has_content>
            <h1>section has heading:</h1>
            <h2 data-uipath="ps.publication.text-section.heading" id="${slugify(section.heading)}">${section.heading}</h2>
        </#if>
        <#if section.text.content?has_content>
            <h1>section has text:</h1>
            <div data-uipath="ps.publication.text-section.text"><@hst.html hippohtml=section.text /></div>
        </#if>
    </div>
</#macro>
