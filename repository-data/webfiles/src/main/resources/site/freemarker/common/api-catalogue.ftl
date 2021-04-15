<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/alphabeticalFilterNav.ftl">
<#include "macro/apiCatalogueEntries.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/svgIcons.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->
<#-- @ftlvariable name="filtersModel" type="uk.nhs.digital.common.components.apicatalogue.filters.Filters" -->
<#-- @ftlvariable name="section" type="uk.nhs.digital.common.components.apicatalogue.filters.Section" -->
<#-- @ftlvariable name="filter" type="uk.nhs.digital.common.components.apicatalogue.filters.Subsection" -->

<#-- Add meta tags -->
<@metaTags></@metaTags>
<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article class="article article--filtered-list api-catalogue">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <div class="article-header article-header--secondary">
                    <h1 data-uipath="document.title">${document.title}</h1>
                </div>
            </div>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds column--reset">
                <#-- [FTL-BEGIN] 'Summary and optional Body' sections -->
                <div id="section-summary" class="article-section article-section--summary  no-border">
                    <div class="grid-row">
                        <div class="column column--reset">
                            <div class="rich-text-content">
                                <div data-uipath="website.linkslist.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
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
                    <@alphabeticalFilterNav alphabetical_hash></@alphabeticalFilterNav>

                    <#if filtersModel?? && !filtersModel.isEmpty()>
                        <div class="article-section-nav-wrapper">
                            <div class="article-section-nav filters">

                                <h2 class="article-section-nav__title">
                                    <span class="filter-head__title">Filters</span>
                                    <@hst.renderURL var="resetUrl"/>
                                    <a class="filter-head__reset button button--tiny" href="${resetUrl}" title="Reset">Reset</a>
                                </h2>

                                <nav>
                                    <ul>
                                        <#list filtersModel.sections as section>
                                            <#if section.displayed>
                                            <li>
                                                <input id="toggler_${section.key}" type="checkbox" <#if section.expanded>checked</#if> aria-hidden="true"/>

                                                <#assign "expansionArrowClass" = "expansion-arrow " + section.expanded?string("selected", "") />
                                                <@svgIcon "expansionArrow" expansionArrowClass/>
                                                <label for="toggler_${section.key}"
                                                       class="section-heading <#if section.expanded>selected</#if>">
                                                    ${section.displayName}
                                                </label>
                                                <ul class="section-content">
                                                <#list section.entries as filter>
                                                    <@filterTemplate filter filtersModel></@filterTemplate>
                                                </#list>
                                                </ul>
                                            </li>
                                            </#if>
                                        </#list>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </#if>
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

<#macro filterTemplate filter filtersModel>
    <#if filter.displayed>
    <li>
        <@hst.renderURL var="filterLink">
            <#if filter.selected>
                <@hst.param name="filters" value="${filtersModel.selectedFiltersKeysMinus(filter.key)?join(',')}" />
            <#else>
                <@hst.param name="filters" value="${filtersModel.selectedFiltersKeys()?join(',', '', ',')}${filter.key}" />
            </#if>
        </@hst.renderURL>
        <#if filter.selectable>
            <a title="Filter by ${filter.displayName}" href="${filterLink}" class="filter-label <#if filter.selected>selected</#if>">${filter.displayName}</a>
        <#else>
            <div class="filter-label-unavailable">${filter.displayName}</div>
        </#if>
        <ul>
            <#list filter.entries as filter>
                <@filterTemplate filter filtersModel></@filterTemplate>
            </#list>
        </ul>
    </li>
    </#if>
</#macro>
