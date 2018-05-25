<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro articleSections sections>
<#if sections?has_content>
<#list sections as section>
<div id="${slugify(section.title)}" class="article-section">
    <#if section.title?has_content>
    <h2>${section.title}</h2>
    </#if>
    <div class="rich-text-content">
        <@hst.html hippohtml=section.html contentRewriter=gaContentRewriter/>
    </div>
</div>
</#list>
</#if>
</#macro>