<#ftl output_format="HTML">

<#include "macro/documentHeader.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiSpecification" -->

<#if document?? >
    <article class="article article--apispecification" itemscope>
        <@documentHeader document 'general'></@documentHeader>

        <div class="grid-wrapper grid-wrapper--article">
            <div class="grid-row">

                ${document.html?no_esc}

            </div>
        </div>
    </article>
</#if>
