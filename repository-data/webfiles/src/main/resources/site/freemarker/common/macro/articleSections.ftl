<#ftl output_format="HTML">
<#macro articleSections sections>
<#if sections?has_content>
<#list sections as section>
<section id="section-${section?index+1}" class="article-section">
    <h2>${section.title}</h2>
    <div class="rich-text-content">
        <@hst.html hippohtml=section.html contentRewriter=gaContentRewriter/>
    </div>
</section>
</#list>
</#if>
</#macro>