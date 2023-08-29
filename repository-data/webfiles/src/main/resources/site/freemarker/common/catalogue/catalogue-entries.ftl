<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/fileMetaAppendix.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->

<#assign lettersOfTheAlphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]/>

<#assign matchesFound = 0/>

<#macro apiCatalogueEntries blockGroups filtersModel>
    <#if blockGroups?has_content>
        <div id="list-page-results-list" class="nhsd-!t-margin-bottom-9">
            <#list lettersOfTheAlphabet as letter>
                <#if blockGroups[letter]??>
                    <#assign matchesFound++ />
                    <div data-letter-section>
                        <#if matchesFound gt 1>
                            <hr class="nhsd-a-horizontal-rule"/>
                        </#if>
                        <div id="${slugify(letter)}" class="nhsd-t-flex" data-uipath="website.glossary.list">
                            <div class="nhsd-!t-margin-right-5">
                                <span class="nhsd-a-character-block nhsd-a-character-block--large nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">${letter}</span>
                            </div>
                            <div class="nhsd-t-flex-item--grow">
                                <#list blockGroups[letter] as block>
                                <#-- Glossery list links -->
                                    <#assign link = ""/>
                                    <#if block.external??>
                                        <#if block.internal?has_content || block.external?has_content>
                                            <#if block.internal?has_content>
                                                <@hst.link var="link" hippobean=block.internal/>
                                            <#else>
                                                <#assign link=block.external/>
                                            </#if>
                                        </#if>
                                    <#-- Link list links -->
                                    <#else>
                                        <#if block.linkType??>
                                            <#if block.linkType == "external">
                                                <#assign link = block.link/>
                                            <#elseif block.linkType == "asset">
                                                <@hst.link hippobean=block.link var="assetLink"/>
                                                <#assign link = assetLink/>
                                            </#if>
                                        <#else>
                                            <#if block.type?? && block.type?has_content && block.type == "alternativeTask">
                                                <@hst.link hippobean=block.task var="link"/>
                                            <#else>
                                                <@hst.link hippobean=block var="link1"/>
                                                <#assign link=link1>
                                            </#if>
                                        </#if>
                                    </#if>
                                    <div data-api-catalogue-entry="">
                                    <#list get_unique_sorted_tags(block filtersModel) as taxonomyTag>
                                        <#assign isSelected = filtersModel.selectedFiltersContain(taxonomyTag.key)/>
                                        <#if filtersModel.isHighlighted(taxonomyTag.getDisplayName())>
                                            <a title="${filter_title(taxonomyTag, isSelected)}"
                                               href="<@renderUrl baseUrl=baseUrl retiredFilterEnabled=retiredFilterEnabled showRetired=showRetired filters=filter_for_tag(filtersModel, taxonomyTag, isSelected)/>"
                                               style="line-height:1; text-decoration:none"
                                               class="nhsd-a-tag nhsd-a-tag--bg-${filtersModel.getHighlight(taxonomyTag.getDisplayName())} nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${taxonomyTag.getDisplayName()}</a>
                                        </#if>
                                    </#list>
                                    <h2 class="nhsd-t-heading-xs nhsd-!t-margin-top-1 nhsd-!t-margin-bottom-1" id="${block.title?lower_case?replace(" ", "-")}">
                                    <#if link?has_content>
                                        <a href="${link}" class="nhsd-a-link" data-filterable>${block.title}</a>
                                    <#else>
                                        ${block.title}
                                    </#if>
                                    </h2>

                                    <#if block.shortsummary??>
                                        <p class="nhsd-t-body" data-filterable>${block.shortsummary}</p>
                                    <#else>
                                        <div class="nhsd-t-grid nhsd-!t-no-gutters">
                                            <@hst.html hippohtml=block.definition contentRewriter=brContentRewriter/>
                                        </div>
                                    </#if>
                                        <#list get_unique_sorted_tags(block filtersModel) as taxonomyTag>
                                            <#assign isSelected = filtersModel.selectedFiltersContain(taxonomyTag.key)/>
                                            <#if !filtersModel.isHighlighted(taxonomyTag.getDisplayName())>
                                                <a title="${filter_title(taxonomyTag, isSelected)}"
                                                   href="<@renderUrl baseUrl=baseUrl retiredFilterEnabled=retiredFilterEnabled showRetired=showRetired filters=filter_for_tag(filtersModel, taxonomyTag, isSelected)/>"
                                                   style="line-height:1; text-decoration:none"
                                                   class="nhsd-a-tag <#if isSelected>filter-tag-yellow-highlight<#else>nhsd-a-tag--bg-light-grey</#if> nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-1">${taxonomyTag.getDisplayName()}</a>
                                        </#if>
                                    </#list>
                                    <#if block?index lt blockGroups[letter]?size - 1>
                                        <hr class="nhsd-a-horizontal-rule"/>
                                    </#if>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>
                </#if>
            </#list>
        </div>
    </#if>
</#macro>

<#function filter_for_tag filtersModel taxonomyTag isSelected>
    <#if isSelected>
        <#return filtersModel.selectedFiltersKeysMinus(taxonomyTag.key)>
    <#else>
        <#return filtersModel.selectedFiltersKeysPlus(taxonomyTag.key)>
    </#if>
</#function>

<#function filter_title taxonomyTag isSelected>
    <#if isSelected>
        <#return "Remove ${taxonomyTag.getDisplayName()} filter">
    <#else>
        <#return "Filter by ${taxonomyTag.getDisplayName()}">
    </#if>
</#function>

<#function get_unique_sorted_tags block filtersModel>
    <#local documentTags = block.getMultipleProperty("hippotaxonomy:keys")![] />
    <#local uniqueSortedTags = [] />
    <#list filtersModel.sectionsInOrderOfDeclaration() as filterSection>
        <#if documentTags?? && documentTags?seq_contains(filterSection.getKey()) && uniqueSortedTags?filter(x -> x.getDisplayName() == filterSection.getDisplayName())?size == 0>
            <#local uniqueSortedTags = uniqueSortedTags + [ filterSection ] />
        </#if>
    </#list>
    <#return uniqueSortedTags>
</#function>
