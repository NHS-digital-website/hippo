<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/scrollableFilterNav.ftl">
<#include "macro/apiCatalogueEntries.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/svgIcons.ftl">
<#include "../nhsd-common/macros/header-banner.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->
<#-- @ftlvariable name="filtersModel" type="uk.nhs.digital.common.components.apicatalogue.filters.Filters" -->
<#-- @ftlvariable name="section" type="uk.nhs.digital.common.components.apicatalogue.filters.Section" -->
<#-- @ftlvariable name="filter" type="uk.nhs.digital.common.components.apicatalogue.filters.Subsection" -->

<#-- Add meta tags -->
<@metaTags></@metaTags>
<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<@headerBanner document />
<article class="article article--filtered-list api-catalogue">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <#-- [FTL-BEGIN] 'Summary and optional Body' sections -->
                <div id="section-summary" class="article-section article-section--summary  no-border">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="rich-text-content">
                                <#if document.body?has_content??>
                                    <@hst.html hippohtml=document.body contentRewriter=gaContentRewriter/>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
                <#-- [FTL-END] 'Summary and optional Body' sections -->
            </div>
        </div>

        <#assign alphabetical_hash = group_blocks(flat_blocks(apiCatalogueLinks true))/>

        <#if alphabetical_hash??>
            <div class="grid-row">
                <div class="column column--one-third page-block page-block--sidebar sticky sticky--top">
                    <@scrollableFilterNav alphabetical_hash filtersModel></@scrollableFilterNav>
                </div>

                <div class="column column--two-thirds page-block page-block--main">
                    <@apiCatalogueEntries alphabetical_hash filtersModel></@apiCatalogueEntries>
                    <div class="article-section muted">
                        <@lastModified document.lastModified false></@lastModified>
                    </div>
                </div>
            </div>

        </#if>
    </div>
</article>
