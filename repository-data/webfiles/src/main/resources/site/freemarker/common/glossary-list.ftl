<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.GlossaryList" -->

<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary.content?has_content />


<#-- TODO: Complete frontend display

<article class="article article--general">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--reset">
                <div class="local-header article-header">
                    <h1 class="local-header__title" data-uipath="document.title">${document.title}</h1>
                </div>
            </div>
        </div>

        <div class="grid-row">

            <div class="column column--two-thirds page-block page-block--main">
                <#if hasSummaryContent>
                    <div id="${slugify('Summary')}" class="article-section article-section--summary article-section--reset-top">
                        <h2><@fmt.message key="headers.summary" /></h2>
                        <div data-uipath="website.general.summary" class="article-section--summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                    </div>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>

-->
