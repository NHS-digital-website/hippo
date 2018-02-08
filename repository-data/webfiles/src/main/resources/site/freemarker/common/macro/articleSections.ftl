<#ftl output_format="HTML">
<#macro articleSections sections>
  <#if sections?has_content>
    <#list sections as section>
      <section id="section-${section?index+1}" class="article-section">
        <h2>${section.title}</h2>
        <@hst.html hippohtml=section.html/>
      </section>
    </#list>
  </#if>
</#macro>