<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/metaTags.ftl">
<#include "../macro/contentPixel.ftl">
<#include "scrollable-filter-nav.ftl">
<#include "catalogue-entries.ftl">
<#include "../macro/svgIcons.ftl">
<#include "../../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../../nhsd-common/macro/heroes/hero.ftl">
<#include "../../nhsd-common/macros/az-nav.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->
<#-- @ftlvariable name="filtersModel" type="uk.nhs.digital.common.components.catalogue.filters.Filters" -->
<#assign renderUrl = "uk.nhs.digital.common.components.catalogue.UrlGeneratorDirective"?new() />

<#-- Add meta tags -->
<@metaTags></@metaTags>
<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#assign alphabetical_hash = group_blocks(flat_blocks(catalogueLinks true))/>

<@hero getHeroOptions(document) />

<div
    class="nhsd-t-grid nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-7 nhsd-api-catalogue">

    <div class="nhsd-t-row">
        <div class="nhsd-t-col-12">
            <#if document.body?has_content??>
                <@hst.html hippohtml=document.body contentRewriter=brContentRewriter/>
            </#if>
        </div>
    </div>
    <#if alphabetical_hash??>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12 nhsd-!t-margin-bottom-5">
                <h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Quick
                    navigation</h2>
                <@azList alphabetical_hash "side-az-nav-heading"/>
            </div>

            <div class="nhsd-t-col-3 nhsd-!t-display-hide nhsd-!t-display-l-show">
                <@scrollableFilterNav filtersModel false></@scrollableFilterNav>
            </div>

            <div class="nhsd-t-col-l-9 nhsd-t-col-m-12">
                <div class="nhsd-!t-display-l-hide">
                    <@scrollableFilterNav filtersModel true></@scrollableFilterNav>
                </div>
                <div class="nhsd-t-row nhsd-!t-display-hide"
                     id="catalogue-search-bar">
                    <div class="nhsd-t-col-l-8 nhsd-!t-padding-left-0">
                        <div class="nhsd-t-float-left">
                            <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-2">
                                Search</h6>
                        </div>
                        <div class=" nhsd-!t-padding-0 nhsd-!t-margin-bottom-2">
                            <div class="nhsd-t-form-control">
                                <input
                                    class="nhsd-t-form-input"
                                    type="text"
                                    id="catalogue-search-bar-input"
                                    name="query"
                                    autocomplete="off"
                                    placeholder="What are you looking for today?"
                                    aria-label="Keywords"
                                />
                                <span class="nhsd-t-form-control__button">
                                    <button
                                        class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent nhsd-!t-display-hide"
                                        type="submit" data-clear-button
                                        aria-label="Clear">
                                        <span
                                            class="nhsd-a-icon nhsd-a-icon--size-s">
                                        <svg xmlns="http://www.w3.org/2000/svg"
                                             preserveAspectRatio="xMidYMid meet"
                                             focusable="false" viewBox="0 0 16 16"><polygon
                                                points="13.9,1 8,6.9 2.1,1 1,2.1 6.9,8 1,13.9 2.1,15 8,9.1 13.9,15 15,13.9 9.1,8 15,2.1 "/></svg>
                                        </span>
                                    </button>
                                </span>
                                <script
                                    src="<@hst.webfile path="/apicatalogue/apicatalogue.js"/>"></script>
                            </div>
                        </div>
                    </div>
                    <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xs"/>
                </div>
                <div class="nhsd-t-row nhsd-!t-padding-top-1">
                    <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0"
                        id="search-results-count">${catalogueLinks?size}
                        results</h6>
                </div>
                <#if retiredFilterEnabled>
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-6 nhsd-!t-padding-left-0">
                            <label aria-label="Include retired APIs and API standards"
                                   class="nhsd-m-selector-toggle-card nhsd-!t-padding-left-0">
                                <div class="nhsd-a-box nhsd-!t-padding-left-0">
                                    <span class="nhsd-m-selector-toggle-card__toggle">
                                        <div class="nhsd-a-selector-toggle">
                                        <@hst.link var="baseUrl"/>
                                            <a href="<@renderUrl baseUrl=baseUrl retiredFilterEnabled=retiredFilterEnabled showRetired=!showRetired filters=filtersModel.selectedFiltersKeysMinusCollection(["retired-api","retired","retired-standard"]) />"
                                               class="nhsd-a-checkbox__label nhsd-t-body-s">
                                                <input type="checkbox"
                                                       <#if showRetired>checked</#if> />
                                                <span class="slider"></span>
                                            </a>
                                        </div>
                                    </span>

                                    <p class="nhsd-t-body-s">Include retired APIs and
                                        API standards</p>
                                </div>
                            </label>
                        </div>
                    </div>
                <#else>
                    <div class="nhsd-!t-padding-top-4"/>
                </#if>

                <@apiCatalogueEntries alphabetical_hash filtersModel></@apiCatalogueEntries>
            </div>
        </div>
    </#if>

</div>
