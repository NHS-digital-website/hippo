<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "./catalogue-functions.ftl">

<div class="nhsd-t-row nhsd-!t-padding-top-1">
    <h6 class="nhsd-t-heading-xs nhsd-!t-margin-bottom-0"
        id="search-results-count"><#if totalAvailable?has_content> ${totalAvailable} <#else>0</#if>
        results </h6>
</div>

<div class="nhsd-!t-padding-top-4"/>

<#if totalAvailable?has_content>
    <div id="list-page-results-list" class="nhsd-!t-margin-bottom-9">
        <#list pageable.items as document>
            <div class="nhsd-t-flex-item--grow" data-api-catalogue-entry>

                <#if sectionEntries?? && sectionEntries?has_content>
                    <@hst.renderURL fullyQualified=true var="link" />
                    <#list document.keys as key>
                        <#if sectionEntries[key]?has_content && sectionEntries[key].highlight?has_content>
                            <#assign taxonomyPath = "/Taxonomies/${key}">
                            <#assign updatedLink = updateOrRemoveLinkWithTaxonomyPath(link, taxonomyPath) />
                            <#if link?contains(taxonomyPath)>
                                <a title="Remove ${sectionEntries[key].displayName} filter"
                                   href="${updatedLink}"
                                   style="line-height:1; text-decoration:none"
                                   class="nhsd-a-tag nhsd-a-tag--bg-${sectionEntries[key].highlight} nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                            <#else>
                                <a title="Filter by ${subEntry.displayName}"
                                   href="${updatedLink}"
                                   style="line-height:1; text-decoration:none"
                                   class="nhsd-a-tag nhsd-a-tag--bg-${sectionEntries[key].highlight} nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                            </#if>
                        </#if>
                    </#list>
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

                <#list document.keys as key>
                    <#if sectionEntries[key]?has_content && !sectionEntries[key].highlight?has_content>
                        <@hst.renderURL fullyQualified=true var="link" />
                        <#assign taxonomyPath = "/Taxonomies/${key}" />
                        <#assign updatedLink = updateOrRemoveLinkWithTaxonomyPath(link, taxonomyPath) />
                        <#if link?contains(taxonomyPath)>
                            <a title="Remove ${key} filter"
                               href="${updatedLink}"
                               style="line-height:1; text-decoration:none"
                               class="nhsd-a-tag filter-tag-yellow-highlight nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                        <#else>
                            <a title="Filter by key ${key}"
                               href="${updatedLink}"
                               style="line-height:1; text-decoration:none"
                               class="nhsd-a-tag nhsd-a-tag--bg-light-grey nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${sectionEntries[key].displayName}</a>
                        </#if>
                    </#if>
                </#list>
            </div>

            <hr class="nhsd-a-horizontal-rule">
        </#list>
    </div>

    <#if document.entriesFooterContentTitle?has_content?? && document.entriesFooterContentBody?has_content??>
        <@entriesFooter document.entriesFooterContentTitle document.entriesFooterContentBody/>
    </#if>
</#if>