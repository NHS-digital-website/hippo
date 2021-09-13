<#ftl output_format="HTML">

<#macro textSection section>
    <div class="nhsd-!t-margin-bottom-6" data-uipath="ps.publication.text-section">
        <#if section.heading?has_content>
            <h2 class="nhsd-t-heading-m" data-uipath="ps.publication.text-section.heading" id="${slugify(section.heading)}">${section.heading}</h2>
        </#if>
        <#if section.text.content?has_content>
            <div class="nhsd-t-body" data-uipath="ps.publication.text-section.text"><@hst.html hippohtml=section.text /></div>
        </#if>
    </div>
</#macro>
