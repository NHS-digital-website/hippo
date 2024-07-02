<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign markdownDescription = "uk.nhs.digital.common.components.catalogue.FilterDescriptionDirective"?new() />

<@hst.setBundle basename="month-names"/>

<#if facets??>
    <@hst.link var="baseLink" hippobean=facets />
    <#list facets.folders as facet>
        <div class="article-section-nav-wrapper faceted-nav-facet">
            <div class="js-filter-list">
                <#assign filterKey = facet.name />
                <#assign filterName = facet.displayName?cap_first />
                <div class="nhsd-m-filter-menu-section ${facet.folders[0].leaf?then('nhsd-m-filter-menu-section--active', '')}" data-active-menu="${filterKey}">
                    <div class="nhsd-m-filter-menu-section__accordion-heading">
                        <button class="nhsd-m-filter-menu-section__menu-button" type="button">
                                <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s">
                                    <span>
                                        <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"/>
                                            </svg>
                                        </span>
                                    </span>
                                    ${filterName}
                                </span>
                        </button>
                        <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-xxs" />

                        <div class="nhsd-m-filter-menu-section__accordion-panel">
                            <#list facet.folders as value>
                                <#assign valueName = value.displayName />
                                <div class="nhsd-m-filter-menu-section__option-row">
                                    <span class="nhsd-a-checkbox">
                                        <#assign filterName = facet.name?replace(' ', '+')/>
                                        <#assign filterValue = valueName?replace(' ', '+')/>
                                        <#assign newLink = baseLink + "/" + filterName + "/" + filterValue />
                                        <#if value.leaf>
                                            <#assign newLink = newLink?replace('/${filterName}/${filterValue}', '', 'i')>
                                        </#if>
                                        <label>
                                            <#assign newLink = newLink + query?has_content?then("?query=" + query, "") />

                                            <#if filterKey="year">
                                                <#assign redirect = newLink?replace('month/(?:[0-9]|1[0-1])/?', '', 'ir')/>
                                            <#else>
                                                <#assign redirect = newLink />
                                            </#if>
                                            ${valueName} (${value.count})
                                            <#if facet.name="month">
                                                <@fmt.message key=value.name var="monthName"/>
                                                <#assign valueName=monthName/>
                                            </#if>
                                            <input name="${filterKey}" type="checkbox"
                                                   data-input-id="${filterKey}-${valueName}"
                                                   class="js-filter-checkbox"
                                                   value="${valueName}"
                                                ${value.leaf?then('checked=checked', '')}
                                                onClick="window.location='${redirect}'"
                                            />
                                            <span class="nhsd-a-checkbox__label nhsd-t-body-s">${valueName} (${value.count})</span>
                                            <span class="checkmark"></span>

                                        </label>
                                    </span>
                                </div>
                            </#list>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#list>
</#if>

<div class="nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">
    <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-margin-bottom-5 nhsd-api-catalogue__scrollable-component">
        <div style="padding-bottom: 2.5rem">
            <div style="justify-content: space-between" class="nhsd-t-row">
                <h2 class="nhsd-t-heading-xs">
                    <span class="filter-head__title">Filters</span>
                </h2>
                <span class="nhsd-t-body">
                        <a class="nhsd-a-link nhsd-!t-padding-0" href="<@hst.link/>" title="Reset filters">
                            Reset filters
                        </a>
                    </span>
            </div>

            <nav>
             <#if filtersModel?? && facets1?has_content>
               <#list filtersModel.sections as section>
                  <#--<#if !section.displayed>-->
                        <div> <#-- the div separates sections so that CSS sibling selectors '~' only target elements within one section. -->
                            <input id="<#if responsive>responsive_</#if>toggler_${section.key}"
                                   class="section-folder"
                                   type="checkbox"
                                   aria-hidden="true"
                                   <#if section.expanded>checked</#if>/>
                            <div class="nhsd-m-filter-menu-section__menu-button section-label-container">
                                <label for="<#if responsive>responsive_</#if>toggler_${section.key}"
                                       class="filter-category-label <#if section.description()??>filter-category-label__described</#if> nhsd-m-filter-menu-section__heading-text">
                                        <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                            </svg>
                                        </span>
                                    <span class="<#if !responsive>filter-category-label__text</#if>nhsd-t-body-s <#if section.expanded>selected</#if>">${section.displayName}</span>
                                        </label>
                                <#--<@filterDescription section.description()!"" responsive/>-->
                                </div>
                            <div class="section-entries nhsd-!t-margin-top-2">
                                <#list section.entries as filter>
                                    <@filterTemplate filter filtersModel facets1 0 responsive></@filterTemplate>
                            </#list>
                               <#-- <#if section.hasHiddenSubsections()>-->
                                    <div class="nhsd-m-filter-menu-section" id="show-more">
                                        <a class="nhsd-a-link--col-dark-grey">show more...</a>
                                    </div>
                               <#-- </#if>-->
                        </div>
                    </div>
                        <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s">
                  <#--</#if>-->
               </#list>
             </#if>
            </nav>
                </div>
            </div>
    <div class="nhsd-t-body nhsd-!t-display-hide nhsd-!t-display-l-show"><a class="nhsd-a-link" href="#">Back to top</a></div>
</div>

<#macro filterTemplate filter filtersModel facets1 indentationLevel=0 responsive=false>
<#-- @ftlvariable name="filter" type="uk.nhs.digital.common.components.catalogue.filters.Subsection" -->
   <#-- <#if !filter.displayed>-->
        <@hst.link var="baseUrl"/>
       <#-- <#if !filter.selected>
            <#assign filtersParam = filtersModel.selectedFiltersKeysMinus(filter.key) />
        <#else>
            <#assign filtersParam = filtersModel.selectedFiltersKeysPlus(filter.key) />
        </#if>-->

    <#if facets1[filter.taxonomyKey]?has_content && facets1[filter.taxonomyKey]?exists>

        <div class="section-label-container <#if filter.isHidden()>nhsd-!t-display-hide</#if>"><#-- This div is needed to add vertical spacing between checkboxes -->
            <span class="nhsd-a-checkbox">
                <label class="filter-label <#if filter.description()??>filter-label__described</#if>">
                    <@hst.renderURL fullyQualified=true var="link" />
                    <#assign taxonomyPath = "/Taxonomies/${filter.taxonomyKey}">
                    <#assign updatedLink = updateOrRemoveLinkWithTaxonomyPath(link, taxonomyPath) />
                    <input onclick="window.location='${updatedLink}'" type="checkbox" <#if filter.selected>checked</#if> <#if filter.selectable>disabled</#if>>
                    <#-- <#if !filter.selectable>-->
                        <a aria-label="Filter by ${filter.displayName}"
                           <#--href="&lt;#&ndash;<@renderUrl baseUrl=baseUrl retiredFilterEnabled=retiredFilterEnabled showRetired=showRetired filters=filtersParam />&ndash;&gt;"-->

                           href="${updatedLink}"
                           class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if filter.selected>selected</#if> <#if filter.entries?has_content>nhsd-!t-font-weight-bold</#if>">
                            <input type="checkbox">
                            <#--<span class="<#if !responsive>filter-label__text</#if>">${filter.displayName} <#if filter.count() != "0">(${filter.count()})</#if></span>-->
                            <span class="<#if !responsive>filter-label__text</#if>">${filter.displayName} (${facets1[filter.taxonomyKey]}) </span>

                        </a>
                   <#-- <#else>
                        <a class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if filter.entries?has_content>nhsd-!t-font-weight-bold</#if>">
                            <input type="checkbox" disabled>
                            <span class="<#if !responsive>filter-label__text</#if>">${filter.displayName} <#if filter.count() != "0">(${filter.count()})</#if></span>
                        </a>
                    </#if>-->
                    <div class="checkmark"></div>
                </label>
                <@filterDescription filter.description()!"" responsive/>
            </span>
        </div>
        <#local nextLevel = indentationLevel + 1>
        <#if filter.entries?has_content>
            <div class="nhsd-m-filter-menu-section__option-group">
                <#list filter.entries as filter>
                    <@filterTemplate filter filtersModel facets1 nextLevel responsive></@filterTemplate>
    </#list>
            </div>
        </#if>
</#if>
    <#--</#if>-->
</#macro>


<#macro filterDescription description responsive>
    <#if description?? && !responsive>
        <div class="section-label-description">
            <@markdownDescription description=description/>
        </div>
    </#if>
</#macro>

<#function updateOrRemoveLinkWithTaxonomyPath link taxonomyPath>
<#-- Split the link into base URL and query parameters -->
    <#assign parts = link?split("?", 2)>
    <#assign baseURL = parts[0]>
    <#assign queryParams = parts[1]?if_exists>

<#-- Check if the base URL contains the taxonomy path -->
    <#if baseURL?contains(taxonomyPath)>
    <#-- Remove the taxonomy path from the base URL -->
        <#assign updatedBaseURL = baseURL?replace(taxonomyPath, "")>
    <#else>
    <#-- Append the taxonomy path to the base URL -->
        <#assign updatedBaseURL = baseURL + taxonomyPath>
    </#if>

<#-- Reassemble the updated link with query parameters if they exist -->
    <#assign updatedLink = updatedBaseURL + (queryParams?has_content?then("?", "") + queryParams)>

<#-- Output the updated link without escaping HTML special characters -->
    <#return updatedLink?no_esc>
</#function>