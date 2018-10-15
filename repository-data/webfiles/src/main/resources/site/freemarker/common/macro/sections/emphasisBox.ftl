<#ftl output_format="HTML">

<#macro emphasisBox section>

    <div class="emphasis-box emphasis-box-${slugify(section.emphasisType)}">
        <#if section.heading?has_content>
            <h3 id="${slugify(section.heading)}">${section.heading}</h3>
        </#if>
        <#if section.body.content?has_content>
            <div><@hst.html hippohtml=section.body contentRewriter=gaContentRewriter /></div>
        </#if>
    </div>

</#macro>
