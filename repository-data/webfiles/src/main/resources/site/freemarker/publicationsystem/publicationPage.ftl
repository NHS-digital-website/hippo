<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/sections/sections.ftl">
<#include "./macro/pageIndex.ftl">
<#include "./macro/publicationHeader.ftl">
<#include "./publication.ftl">
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>

<@publicationHeader publication=page.publication/>

<@pageIndex index=index/>

<section class="document-content" aria-label="Document Content">
    <div>
        <h1 data-uipath="ps.publication.page-title" title="${page.title}">${page.title}</h1>
        <#if page.bodySections?has_content>
            <@sections sections=page.bodySections />
        </#if>
    </div>
</section>
