<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/metaTags.ftl">
<#include "../macro/contentPixel.ftl">
<#include "scrollable-filter-nav.ftl">
<#include "api-catalogue-entries.ftl">
<#include "../macro/svgIcons.ftl">
<#include "../../nhsd-common/macros/header-banner.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->
<#-- @ftlvariable name="filtersModel" type="uk.nhs.digital.common.components.apicatalogue.filters.Filters" -->

<#-- Add meta tags -->
<@metaTags></@metaTags>
<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#assign alphabetical_hash = group_blocks(flat_blocks(apiCatalogueLinks true))/>

<@headerBanner document />

<div class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-api-catalogue">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-12">
            <#if document.body?has_content??>
                <@hst.renderURL var="showAlllink">
                    <@hst.param name="showAll" value="${showAll?string('false', 'true')}" />
                    <@hst.param name="filters" value="${filtersModel.selectedFiltersKeysMinusDeprecatedAndRetiredSpecs(['deprecated-api', 'retired-api'])?join(',')}" />
                </@hst.renderURL>
                <@hst.html hippohtml=document.body contentRewriter=brContentRewriter/>
            </#if>
        </div>
    </div>


    <#if alphabetical_hash??>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2 nhsd-api-catalogue__scrollable-component">
                    <@scrollableFilterNav alphabetical_hash filtersModel false></@scrollableFilterNav>
                </div>
            </div>

            <div class="nhsd-t-col-l-9 nhsd-t-col-m-12">
                <div class="nhsd-!t-display-l-hide">
                    <@scrollableFilterNav alphabetical_hash filtersModel true></@scrollableFilterNav>
                </div>
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-3">
                        <div class="nhsd-t-float-left">
                            <h6 class="nhsd-t-heading-xs">${apiCatalogueLinks?size} results</h6>
                        </div>
                    </div>
                    <div class="nhsd-t-col-8 nhsd-t-col-l-8">
                        <div class="nhsd-t-float-right nhsd-!t-margin-top-1">
                            <p class="nhsd-t-body-s">Include deprecated and retired APIs</p>
                        </div>
                    </div>
                    <div class="nhsd-t-col-1 nhsd-t-col-l-1">
                        <label class="nhsd-a-selector-toggle nhsd-a-selector-toggle--cancel" aria-label="This is a toggle">
                            <a href="${showAlllink?no_esc}" class="nhsd-a-checkbox__label nhsd-t-body-s">
                                <input type="checkbox"  <#if showAll>checked</#if> />
                                <span class="slider"></span>
                            </a>
                        </label>
                    </div>
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xs"/>
                </div>

                <@apiCatalogueEntries alphabetical_hash filtersModel></@apiCatalogueEntries>
            </div>
        </div>

    </#if>
</div>
