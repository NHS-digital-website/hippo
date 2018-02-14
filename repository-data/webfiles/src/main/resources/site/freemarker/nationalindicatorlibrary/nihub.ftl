<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<section class="document-content">
    <#if document??>
        <h1 data-uipath="ps.document.title">${document.title}</h1>
        <p>${document.summary}</p>      
    </#if>
</section>
