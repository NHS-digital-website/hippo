<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="nationalindicatorlibrary.headers"/>

<section class="document-content">
    <#if document??>
        <h1 data-uipath="ps.document.title">${document.title}</h1>
         <@hst.html hippohtml=document.content />
    </#if>
</section>

