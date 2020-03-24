<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <h2>${document.title}</h2>
        <h2>${document.reviewDate}</h2>
        <#if document.children?has_content>
            <#list document.children as child>
                ${child.title} <br>
            </#list>
        </#if>
        <#if document.parents?has_content>
            <#list document.parents as parent>
                ${parent.title} <br>
            </#list>
        </#if>
    </div>
</article>
