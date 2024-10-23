<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign markdownDescription = "uk.nhs.digital.common.components.catalogue.FilterDescriptionDirective"?new() />

<@hst.setBundle basename="month-names"/>

<div class="nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">
    <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-margin-bottom-5 nhsd-api-catalogue__scrollable-component">
        <div style="padding-bottom: 2.5rem">
            <div style="justify-content: space-between" class="nhsd-t-row">
                <h2 class="nhsd-t-heading-xs">
                    <span class="filter-head__title">Filters</span>
                </h2>
                <@hst.renderURL fullyQualified=true var="link" />
                <#assign baseURL = getBaseURL(link) />
                <span class="nhsd-t-body">
                    <a class="nhsd-a-link nhsd-!t-padding-0" href="${baseURL}"
                       title="Reset filters">
                        Reset filters
                    </a>
                </span>
            </div>

            <nav>
                <#if filtersModel?? && facets1?has_content>
                    <#list filtersModel.sections as section>
                        <#if section.displayed>
                            <div> <#-- the div separates sections so that CSS sibling selectors '~' only target elements within one section. -->
                                <input
                                    id="<#if responsive>responsive_</#if>toggler_${section.key}"
                                    class="section-folder"
                                    type="checkbox"
                                    aria-hidden="true"
                                    <#if section.expanded>checked</#if>/>
                                <div
                                    class="nhsd-m-filter-menu-section__menu-button section-label-container">
                                    <label
                                        for="<#if responsive>responsive_</#if>toggler_${section.key}"
                                        class="filter-category-label <#if section.description()??>filter-category-label__described</#if> nhsd-m-filter-menu-section__heading-text">
                                        <span
                                            class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                            <svg xmlns="http://www.w3.org/2000/svg"
                                                 preserveAspectRatio="xMidYMid meet"
                                                 aria-hidden="true"
                                                 focusable="false"
                                                 viewBox="0 0 16 16" width="100%"
                                                 height="100%">
                                                <path
                                                    d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                            </svg>
                                        </span>
                                        <span
                                            class="<#if !responsive>filter-category-label__text</#if>nhsd-t-body-s <#if section.expanded>selected</#if>">${section.displayName}</span>
                                    </label>
                                </div>
                                <div class="section-entries nhsd-!t-margin-top-2">
                                    <#list section.entries as filter>
                                        <@filterTemplate filter filtersModel facets1 section 0 responsive></@filterTemplate>
                                    </#list>
                                    <#if section.showMoreIndc>
                                        <div class="nhsd-m-filter-menu-section"
                                             id="show-more">
                                            <a class="nhsd-a-link--col-dark-grey">show
                                                more...</a>
                                        </div>
                                    </#if>
                                </div>
                            </div>
                            <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s">
                        </#if>
                    </#list>
                </#if>
            </nav>

        </div>
    </div>
    <div class="nhsd-t-body nhsd-!t-display-hide nhsd-!t-display-l-show"><a
            class="nhsd-a-link" href="#">Back to top</a></div>
</div>

<#macro filterTemplate filter filtersModel facets1 section indentationLevel=0 responsive=false>
    <@hst.link var="baseUrl"/>

    <#if (facets1[filter.taxonomyKey]?has_content && facets1[filter.taxonomyKey]?exists) || (filter.taxonomyKey == "apis_1" || filter.taxonomyKey == "api-standards" || filter.taxonomyKey == "medication-management")>

        <div class="section-label-container <#if !filter.isDisplayed()>nhsd-!t-display-hide</#if>"><#-- This div is needed to add vertical spacing between checkboxes -->

            <span class="nhsd-a-checkbox">
                <label class="filter-label <#if filter.description()??>filter-label__described</#if>">
                    <@hst.renderURL fullyQualified=true var="link" />
                    <#assign taxonomyPath = "/Taxonomies/${filter.taxonomyKey}">
                    <#assign updatedLink = updateOrRemoveLinkWithTaxonomyPath(link, taxonomyPath) />
                    <input onclick="window.location='${updatedLink}'" type="checkbox" <#if facets1[filter.taxonomyKey][1]>checked</#if> <#if filter.selectable>disabled</#if> >
                     <#if !filter.selectable >
                         <a aria-label="Filter by ${filter.displayName}"
                            href="${updatedLink}"
                            class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if filter.selected>selected</#if> <#if filter.entries?has_content>nhsd-!t-font-weight-bold</#if>">
                            <input type="checkbox">
                                <span class="<#if !responsive>filter-label__text</#if>">${filter.displayName} (${facets1[filter.taxonomyKey][2]}) </span>
                        </a>
                    <#else>
                         <a class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if filter.entries?has_content>nhsd-!t-font-weight-bold</#if>">
                            <input type="checkbox" disabled>
                            <span class="<#if !responsive>filter-label__text</#if>">${filter.displayName} <#if facets1[filter.taxonomyKey][2] != "0">(${facets1[filter.taxonomyKey][2]})</#if></span>
                        </a>
                     </#if>
                    <div class="checkmark"></div>
                </label>
                <@filterDescription filter.description()!"" responsive/>
            </span>
        </div>

        <#local nextLevel = indentationLevel + 1>
        <#if filter.entries?has_content>
            <div class="nhsd-m-filter-menu-section__option-group">
                <#list filter.entries as filter>
                    <@filterTemplate filter filtersModel facets1 section nextLevel responsive></@filterTemplate>
                </#list>
            </div>
        </#if>
    </#if>

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
    <#assign updatedLink = updatedBaseURL + (queryParams?has_content?then("?" + queryParams, ""))>

<#-- Output the updated link without escaping HTML special characters -->
    <#return updatedLink?no_esc>
</#function>

<#function getBaseURL url>
    <#assign index = url?index_of("Taxonomies") />
    <#assign index1 = url?index_of("?") />
    <#if index != -1>
        <#return url[0..(index - 2)] />
    <#elseif index1 != -1>
        <#return url[0..(index1 - 1)] />
    <#else>
        <#return url />
    </#if>
</#function>