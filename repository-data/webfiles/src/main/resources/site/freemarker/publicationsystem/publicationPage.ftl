<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/sections/sections.ftl">
<#include "./macro/pageIndex.ftl">
<#include "./macro/publicationHeader.ftl">
<#include "./publication.ftl">
<@hst.setBundle basename="publicationsystem.labels,publicationsystem.headers"/>



<main role="main">
    <div class="grid-wrapper grid-wrapper--article">

        <div class="breadcrumb list list--inline list--reset">
            <a href="/" class="breadcrumb__link">NHS Digital</a>
            <img src="/webfiles/1523882005071/images/icon-arrow.png" alt="" class="breadcrumb__sep">
            <span class="breadcrumb__link breadcrumb__link--secondary">${page.title}</span>
        </div>

        <@publicationHeader publication=page.publication/>

        <div class="grid-row article-section">
            <div class="column column--one-third page-block page-block--sidebar page-block--sticky-top">
                <@pageIndex index=index/>
            </div>

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
</main>
