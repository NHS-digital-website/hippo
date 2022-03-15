<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../nhsd-common/macros/az-nav.ftl">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>
<#assign markdownDescription = "uk.nhs.digital.common.components.apicatalogue.FilterDescriptionDirective"?new() />

<#macro scrollableFilterNav blockGroups filtersModel responsive>
    <#-- @ftlvariable name="filtersModel" type="uk.nhs.digital.common.components.apicatalogue.filters.Filters" -->
    <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-margin-bottom-5">
    <#if responsive>
        <h2 id="side-az-nav-heading-responsive" class="nhsd-t-heading-xs">Search A-Z</h2>
        <@azList blockGroups "side-az-nav-heading-responsive"/>
    <#else>
        <h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Search A-Z</h2>
        <@azList blockGroups "side-az-nav-heading"/>
    </#if>
    </div>
    <#if filtersModel?? && !filtersModel.isEmpty()>
        <div class="nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">
        <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-margin-bottom-5 nhsd-api-catalogue__scrollable-component">
        <div>
                <div class="nhsd-t-row">
                    <h2 class="nhsd-t-heading-xs">
                        <span class="filter-head__title">Filters</span>
                    </h2>
                    <span class="nhsd-t-body nhsd-!t-padding-left-6 nhsd-!t-margin-right-0">
                        <a class="nhsd-a-link nhsd-!t-padding-0" href="<@hst.link/>" title="Reset filters">
                            Reset filters
                        </a>
                    </span>
                </div>

            <nav>
                <#list filtersModel.sections as section>
                    <#-- @ftlvariable name="section" type="uk.nhs.digital.common.components.apicatalogue.filters.Section" -->
                    <#if section.displayed>
                        <div> <#-- the div separates sections so that CSS sibling selectors '~' only target elements within one section. -->
                            <input id="<#if responsive>responsive_</#if>toggler_${section.key}"
                                   class="section-folder"
                                   type="checkbox"
                                   aria-hidden="true"
                                   <#if section.expanded>checked</#if>/>
                            <div class="nhsd-m-filter-menu-section__menu-button">
                                <label for="<#if responsive>responsive_</#if>toggler_${section.key}"
                                       class="filter-category-label <#if section.description()??>filter-category-label__described</#if> nhsd-m-filter-menu-section__heading-text">
                                        <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                                <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                            </svg>
                                        </span>
                                        <span class="<#if !responsive>filter-category-label__text</#if> nhsd-t-body-s <#if section.expanded>selected</#if>">${section.displayName}</span>
                                </label>
                                <@filterDescription responsive section.description()/>
                            </div>
                            <div class="section-entries nhsd-!t-margin-top-2">
                                <#list section.entries as filter>
                                    <@filterTemplate filter filtersModel 0 responsive></@filterTemplate>
                                </#list>
                            </div>
                        </div>
                        <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s">
                    </#if>
                </#list>
            </nav>
        </div>
        </div>
        <div class="nhsd-t-body nhsd-!t-display-hide nhsd-!t-display-l-show"><a class="nhsd-a-link" href="#">Back to top</a></div>
        </div>
    </#if>
</#macro>

<#macro filterTemplate filter filtersModel indentationLevel=0 responsive=false>
    <#-- @ftlvariable name="filter" type="uk.nhs.digital.common.components.apicatalogue.filters.Subsection" -->
    <#if filter.displayed>
        <@hst.link var="baseUrl"/>
        <#if filter.selected>
            <#assign filtersParam = filtersModel.selectedFiltersKeysMinus(filter.key) />
        <#else>
            <#assign filtersParam = filtersModel.selectedFiltersKeysPlus(filter.key) />
        </#if>
        <div><#-- This div is needed to add vertical spacing between checkboxes -->
        <span class="nhsd-a-checkbox">
            <label class="filter-label <#if filter.description()??>filter-label__described</#if>">
                <input onclick="window.location = '<@renderUrl baseUrl=baseUrl showRetired=showRetired filters=filtersParam />'" type="checkbox" <#if filter.selected>checked</#if> <#if !filter.selectable>disabled</#if>>
                <#if filter.selectable>
                    <a title="Filter by ${filter.displayName}"
                       href="<@renderUrl baseUrl=baseUrl showRetired=showRetired filters=filtersParam />"
                       class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if filter.selected>selected</#if> <#if filter.entries?has_content>nhsd-!t-font-weight-bold</#if>">
                        <input type="checkbox">
                        <span class="<#if !responsive>filter-label__text</#if>">${filter.displayName}</span>
                    </a>
                <#else>
                    <a class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if filter.entries?has_content>nhsd-!t-font-weight-bold</#if>">
                        <input type="checkbox" disabled>
                        <span class="<#if !responsive>filter-label__text</#if>">${filter.displayName}</span>
                    </a>
                </#if>
                <div class="checkmark"></div>
            </label>
            <@filterDescription responsive filter.description()/>
        </span>
        </div>
        <#local nextLevel = indentationLevel + 1>
        <#if filter.entries?has_content>
            <div class="nhsd-m-filter-menu-section__option-group">
            <#list filter.entries as filter>
                <@filterTemplate filter filtersModel nextLevel responsive></@filterTemplate>
            </#list>
            </div>
        </#if>
    </#if>
</#macro>

<#macro filterDescription responsive description>
    <#if description?? && !responsive>
        <dfn class="filter-description nhsd-a-checkbox__hint"><@markdownDescription description=description/></dfn>
    </#if>
</#macro>
