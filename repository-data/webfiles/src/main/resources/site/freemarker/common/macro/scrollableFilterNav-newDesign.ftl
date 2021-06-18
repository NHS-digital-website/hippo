<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "alphabeticalFilterNav-newDesign.ftl">

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#macro scrollableFilterNav blockGroups filtersModel>
<div class="scrollable-component">
<h2 id="side-az-nav-heading" class="nhsd-t-heading-xs">Refine results</h2>
<@alphabeticalFilterNav blockGroups></@alphabeticalFilterNav>
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
</#macro>

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
