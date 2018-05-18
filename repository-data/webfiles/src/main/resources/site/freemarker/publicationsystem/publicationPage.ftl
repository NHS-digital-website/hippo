<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/sections/sections.ftl">
<#include "./macro/pageIndex.ftl">
<#include "./macro/publicationHeader.ftl">
<#include "./publication.ftl">

<article class="article">
    <@publicationHeader publication=page.publication/>

    <div class="grid-wrapper grid-wrapper--article article-section" aria-label="Document Content">
        <div class="grid-row article-section">
            <@pageIndex index=index/>

            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <h1 data-uipath="ps.publication.page-title" title="${page.title}">${page.title}</h1>
                    <#if page.bodySections?has_content>
                        <@sections sections=page.bodySections />
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>
