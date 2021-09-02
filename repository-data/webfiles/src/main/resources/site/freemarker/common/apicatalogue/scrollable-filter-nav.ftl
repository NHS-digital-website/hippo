<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../nhsd-common/macros/az-nav.ftl">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#macro scrollableFilterNav blockGroups filtersModel responsive>
    <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-margin-bottom-5">
    <#if responsive>
        <h2 id="side-az-nav-heading-responsive" class="nhsd-t-heading-xs">Search A-Z</h2>
        <@azList blockGroups "side-az-nav-heading-responsive"/>
    <#else>
        <h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Refine results</h2>
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
                    <#if section.displayed>
                        <div>
                            <input id="<#if responsive>responsive_</#if>toggler_${section.key}" class="section-folder" style="display:none;" type="checkbox" aria-hidden="true"/>
                            <label for="<#if responsive>responsive_</#if>toggler_${section.key}">
                        <span class="icon">
                            <span class="nhsd-a-icon nhsd-a-icon--size-xxs">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16" width="100%" height="100%">
                                    <path d="M8,12L1,5.5L2.5,4L8,9.2L13.5,4L15,5.5L8,12z"></path>
                                </svg>
                            </span>
                        </span>
                                <span class="nhsd-m-filter-menu-section__heading-text nhsd-t-body-s nhsd-!t-margin-bottom-0 nhsd-!t-padding-0 <#if section.expanded>selected</#if>">
                            ${section.displayName}
                        </span>
                            </label>
                            <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s">
                            <div class="section-entries">
                                <#list section.entries as filter>
                                    <@filterTemplate filter filtersModel 0></@filterTemplate>
                                </#list>
                            </div>
                            <#if section.entries??>
                                <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s">
                            </#if>
                        </div>
                    </#if>
                </#list>
            </nav>
        </div>
        </div>
        <div class="nhsd-t-body nhsd-!t-display-hide nhsd-!t-display-l-show"><a class="nhsd-a-link" href="#">Back to top</a></div>
        </div>
    </#if>
</#macro>

<#macro filterTemplate filter filtersModel indentationLevel=0>
    <#if filter.displayed>
        <@hst.link var="baseUrl"/>
        <#if filter.selected>
            <#assign filtersParam = filtersModel.selectedFiltersKeysMinus(filter.key) />
        <#else>
            <#assign filtersParam = filtersModel.selectedFiltersKeysPlus(filter.key) />
        </#if>
        <span class="nhsd-a-checkbox">
            <label>
            <#if filter.selectable>
                <a title="Filter by ${filter.displayName}"
                   href="<@renderUrl baseUrl=baseUrl showDeprecatedAndRetired=showDeprecatedAndRetired filters=filtersParam />"
                   class="nhsd-a-checkbox__label nhsd-t-body-s <#if filter.selected>selected</#if>">
                    <input type="checkbox">${filter.displayName}
                </a>
                <span class="checkmark <#if filter.selected>selected</#if>"></span>
            <#else>
                <a href="" class="nhsd-a-checkbox__label nhsd-t-body-s nhsd-api-catalogue__filter-unavailable">
                    <input type="checkbox" disabled>${filter.displayName}
                </a>
                <span class="checkmark <#if filter.selected>selected</#if>"></span>
            </#if>
                <p class="nhsd-a-checkbox__hint"></p>
            </label>
        </span>
        <#local nextLevel = indentationLevel + 1>
        <#list filter.entries as filter>
            <div style="margin-left:${nextLevel}em;">
                <@filterTemplate filter filtersModel nextLevel></@filterTemplate>
            </div>
        </#list>
    </#if>
</#macro>
