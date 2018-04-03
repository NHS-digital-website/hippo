<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<section class="document-content" aria-label="Policy Content">
    <#if document??>
        <h1 data-uipath="document.title">${document.title}</h1>
        <div data-uipath="ps.document.content">
            <@hst.html hippohtml=document.content />
        </div>
    </#if>
</section>
