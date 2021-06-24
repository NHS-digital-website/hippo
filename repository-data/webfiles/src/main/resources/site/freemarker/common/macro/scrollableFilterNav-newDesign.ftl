<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "alphabeticalFilterNav-newDesign.ftl">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#macro scrollableFilterNav blockGroups filtersModel responsive>
<h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Refine results</h2>
<#if responsive == true>
    <div class="nhsd-!t-margin-bottom-4">
        <@alphabeticalFilterNav blockGroups "top-az-nav-heading"></@alphabeticalFilterNav>
        <hr class="nhsd-a-horizontal-rule" />
    </div>
<#else>
    <@alphabeticalFilterNav blockGroups></@alphabeticalFilterNav>
</#if>
<#if filtersModel?? && !filtersModel.isEmpty()>
    <div id="nhsd-filter-menu">

        <h2 class="nhsd-t-heading-xs">
            <span class="filter-head__title">Filters</span>
            <@hst.renderURL var="resetUrl"/>
            <a class="nhsd-a-button" href="${resetUrl}" title="Reset">
            <span class="nhsd-a-button__label">Reset</span>
            </a>
        </h2>

        <nav>
                <#list filtersModel.sections as section>
                    <#if section.displayed>
                    <div id="${section.key}-section">
                        <input id="<#if responsive == true>responsive_</#if>toggler_${section.key}" class="section-folder" style="display:none;" type="checkbox" aria-hidden="true"/>
                        <label for="<#if responsive == true>responsive_</#if>toggler_${section.key}">
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
</#if>
</#macro>

<#macro filterTemplate filter filtersModel indentationLevel=0>
    <#if filter.displayed>
        <@hst.renderURL var="filterLink">
            <#if filter.selected>
                <@hst.param name="filters" value="${filtersModel.selectedFiltersKeysMinus(filter.key)?join(',')}" />
            <#else>
                <@hst.param name="filters" value="${filtersModel.selectedFiltersKeys()?join(',', '', ',')}${filter.key}" />
            </#if>
        </@hst.renderURL>
        <span class="nhsd-a-checkbox">
        <label>
        <#if filter.selectable>
            <a title="Filter by ${filter.displayName}" href="${filterLink}" class="nhsd-a-checkbox__label nhsd-t-body-s <#if filter.selected>selected</#if>">

            <input type="checkbox">${filter.displayName}
            </a>
        <#else>
            <a href="" class="nhsd-a-checkbox__label nhsd-t-body-s filter-label-unavailable">
            <input type="checkbox">${filter.displayName}</a>
        </#if>
        <span class="checkmark <#if filter.selected>selected</#if>"></span>
        </span>
        <p class="nhsd-a-checkbox__hint"></p>
        </label>
        <#local nextLevel = indentationLevel + 1>
        <#list filter.entries as filter>
            <div style="margin-left:${nextLevel}em;">
            <@filterTemplate filter filtersModel nextLevel></@filterTemplate>
            </div>
        </#list>
    </#if>
</#macro>
