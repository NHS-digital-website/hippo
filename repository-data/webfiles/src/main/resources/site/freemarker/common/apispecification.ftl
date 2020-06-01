<#ftl output_format="HTML">

<#include "macro/documentHeader.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiSpecification" -->

<#if document?? >
    <article class="article article--publication" itemscope>
        <@documentHeader document 'general'></@documentHeader>
        <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
           ${document.html?no_esc}
        </div>
    </article>
</#if>
