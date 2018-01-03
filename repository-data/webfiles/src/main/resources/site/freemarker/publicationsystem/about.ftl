<#ftl output_format="HTML">
<#include "../include/imports.ftlh">
<section class="document-content">
    <#if document??>
        <h2 data-uipath="ps.document.title">${document.title}</h2>
        <div>
            <@hst.html hippohtml=document.content />
        </div>
    </#if>
</section>
