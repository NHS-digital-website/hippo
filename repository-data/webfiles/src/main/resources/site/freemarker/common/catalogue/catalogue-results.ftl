<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../nhsd-common/macro/pagination.ftl">

<div class="nhsd-t-row nhsd-!t-padding-top-1">
    <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0" id="search-results-count">
        <#if totalAvailable?has_content>${totalAvailable} <#else>0</#if> results
    </h6>
</div>

<div class="nhsd-!t-padding-top-4"></div>

<#if totalAvailable?has_content>

    <div id="list-page-results-list" class="nhsd-!t-margin-bottom-9">
        <#assign currentLetter = "">

        <#list pageable.items as document>
        <#-- Extract the first letter of the current document title -->
            <#assign firstLetter = document.title?substring(0, 1)?upper_case>

        <#-- Check if this first letter is different from the currentLetter -->
            <#if firstLetter != currentLetter>
            <#-- Update currentLetter and add the letter marker -->
                <#assign currentLetter = firstLetter>
                <div id="${currentLetter?lower_case}" class="nhsd-t-flex" data-uipath="website.glossary.list">
                <div class="nhsd-!t-margin-right-5">
                        <span class="nhsd-a-character-block nhsd-a-character-block--large nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">
                            ${currentLetter}
                        </span>
                </div>
                <div class="nhsd-t-flex-item--grow">
            </#if>

            <div class="nhsd-t-flex-item--grow" data-api-catalogue-entry>

                <#if sectionEntries?? && sectionEntries?has_content>
                    <@hst.renderURL fullyQualified=true var="link" />
                    <#if document.keys?has_content>
                        <#list document.keys as key>
                            <#if sectionEntries[key]?has_content && sectionEntries[key].highlight?has_content>
                                <#assign isFacetSelected = (facets1[key]?has_content && facets1[key][1])>
                                <#if isFacetSelected>
                                    <@hst.facetnavigationlink var="facetLink" current=facets remove=facets1[key][0] />
                                    <a title="Remove ${sectionEntries[key].displayName} filter"
                                       href="${facetLink}"
                                       style="line-height:1; text-decoration:none"
                                       class="nhsd-a-tag nhsd-a-tag--bg-${sectionEntries[key].highlight} nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                                <#else>
                                    <@hst.link var="facetLink" hippobean=facets1[key][0] navigationStateful=true />
                                    <a title="Filter by ${sectionEntries[key].displayName}"
                                       href="${facetLink}"
                                       style="line-height:1; text-decoration:none"
                                       class="nhsd-a-tag nhsd-a-tag--bg-${sectionEntries[key].highlight} nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                                </#if>
                            </#if>
                        </#list>
                    </#if>
                </#if>

                <@hst.link hippobean=document var="link1"/>

                <h2 class="nhsd-t-heading-xs nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-1"
                    id="${document.title?lower_case?replace(" ", "-")}">
                    <#if link1?has_content>
                        <a href="${link1}" class="nhsd-a-link"
                           data-filterable>${document.title}</a>
                    <#else>
                        ${document.title}
                    </#if>
                </h2>

                <p class="nhsd-t-body" data-filterable="">${document.shortsummary}</p>

                <#if document.keys?has_content>
                    <#list document.keys as key>
                        <#if sectionEntries[key]?has_content && !sectionEntries[key].highlight?has_content>
                            <#assign isFacetSelected = (facets1[key]?has_content && facets1[key][1])>
                            <#if isFacetSelected>
                                <@hst.facetnavigationlink var="facetLink" current=facets remove=facets1[key][0] />
                                <a title="Remove ${key} filter"
                                   href="${facetLink}"
                                   style="line-height:1; text-decoration:none"
                                   class="nhsd-a-tag filter-tag-yellow-highlight nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                            <#else>
                                <@hst.link var="facetLink" hippobean=facets1[key][0] navigationStateful=true />
                                <a title="Filter by key ${key}"
                                   href="${facetLink}"
                                   style="line-height:1; text-decoration:none"
                                   class="nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                            </#if>
                        </#if>
                    </#list>
                </#if>
            </div>

            <hr class="nhsd-a-horizontal-rule">

        <#-- Add pagination only after the last item, but with the correct possion relative to the letter flex -->
            <#if document?is_last>
                <#if totalAvailable?has_content && pageable?? && pageable.total gt 0>
                    <div class="pagination-container nhsd-!t-margin-top-9">
                        <@pagination />
                    </div>
                </#if>
            </#if>

        <#-- Check if the next item has a different starting letter, or if this is the last item -->
            <#if document?is_last || pageable.items[document?index + 1].title?substring(0, 1)?upper_case != currentLetter>
                </div> <!-- Close nhsd-t-flex-item--grow -->
                </div> <!-- Close nhsd-t-flex -->
            </#if>
        </#list>
    </div>

</#if>