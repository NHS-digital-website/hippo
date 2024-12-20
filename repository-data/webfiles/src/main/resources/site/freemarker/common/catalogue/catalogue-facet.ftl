<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign markdownDescription = "uk.nhs.digital.common.components.catalogue.FilterDescriptionDirective"?new() />

<#-- Helper fuction to determine if all items in the subsections and the nested subsections are skipped -->
<#function isSkipped(entries)>
    <#local result = true>
    <#list entries as entry>
        <#if facetFilterMap[entry.taxonomyKey]??>
        <#-- If we find something in facetFilterMap for this entry, it's not skipped -->
            <#local result = false>
            <#break>
        <#elseif entry.entries?has_content>
        <#-- If nested entries are not skipped, then this isn't skipped -->
            <#if !isSkipped(entry.entries)>
                <#local result = false>
                <#break>
            </#if>
        </#if>
    </#list>
    <#return result>
</#function>

<#-- Helper fuction to determine if any item in the subsections and nested subsections are checked -->
<#function hasChecked(entries)>
    <#list entries as entry>
    <#-- Check if facetFilterMap[entry.taxonomyKey] exists and has a truthy second element -->
        <#if (facetFilterMap[entry.taxonomyKey]?? && facetFilterMap[entry.taxonomyKey][1])>
            <#return true>
        <#elseif entry.entries?has_content>
        <#-- Recursively check nested entries -->
            <#if hasChecked(entry.entries)>
                <#return true>
            </#if>
        </#if>
    </#list>
    <#-- If no conditions matched, return false -->
    <#return false>
</#function>

<#-- Helper funciton to strip all filters from the URL -->
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

<#-- Helper function to decide if an element should be hidden -->
<#function shouldHide(childrenToShowCounter, section, checked)>
    <#if (!checked && childrenToShowCounter lte 0 && section.amountChildrenToShow?? && section.amountChildrenToShow gt 0)>
        <#return true>
    <#else>
        <#return false>
    </#if>
</#function>

<div class="nhsd-!t-display-sticky nhsd-!t-display-sticky--offset-2">
    <div class="nhsd-a-box nhsd-a-box--border-grey nhsd-!t-margin-right-3 nhsd-!t-margin-bottom-5 nhsd-api-catalogue__scrollable-component">
        <div style="padding-bottom: 2.5rem">
            <div style="justify-content: space-between" class="nhsd-t-row">

                <h2 class="nhsd-t-heading-xs">
                    <span class="filter-head__title">Filters</span>
                </h2>


                <span class="nhsd-t-body">
                    <@hst.renderURL fullyQualified=true var="link" />
                    <#assign baseURL = getBaseURL(link) />
                    <a class="nhsd-a-link nhsd-!t-padding-0" href="${baseURL}" title="Reset filters">
                        Reset filters
                    </a>
                </span>
            </div>

            <nav>
                <#if filters?has_content>
                    <#list filters.sections as section>
                        <#assign skipped = isSkipped(section.entries)>
                        <#if !skipped>
                            <div> <#-- the div separates sections so that CSS sibling selectors '~' only target elements within one section. -->
                                <#assign checked = hasChecked(section.entries)>
                                <input
                                    id="responsive_toggler_${section.key}"
                                    class="section-folder"
                                    type="checkbox"
                                    aria-hidden="true"
                                    <#if checked || (section.amountChildrenToShow?? && section.amountChildrenToShow gt 0)>checked</#if>
                                />
                                <div class="nhsd-m-filter-menu-section__menu-button section-label-container">
                                    <label
                                        for="responsive_toggler_${section.key}"
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
                                        <span class="filter-category-label__text nhsd-t-body-s">${section.displayName}</span>
                                    </label>
                                    <#if section.description()??>
                                        <div class="section-label-description">
                                           <@markdownDescription description=section.description() />
                                        </div>
                                    </#if>
                                </div>

                                <div class="section-entries nhsd-!t-margin-top-2">

                                    <#-- Initialize counters -->
                                    <#assign childrenToShowCounter = (section.amountChildrenToShow?? && section.amountChildrenToShow gt 0)?then(section.amountChildrenToShow, 0) />
                                    <#assign showMoreCounter = 0>

                                    <#-- Loop through subsections -->
                                    <#list section.entries as subsection>

                                        <#assign nestedSkipped = true />
                                        <#if subsection.entries?has_content>
                                            <#assign nestedSkipped = isSkipped(subsection.entries)>
                                        </#if>

                                        <#if facetFilterMap[subsection.taxonomyKey]?? || (subsection.entries?has_content && !nestedSkipped)>
                                            <#assign showMoreCounter = showMoreCounter + 1>
                                            <div class="section-label-container <#if shouldHide(childrenToShowCounter, section, checked)>nhsd-!t-display-hide</#if>">
                                                <span class="nhsd-a-checkbox">
                                                    <#-- Check if the facet is selected -->
                                                    <#if facetFilterMap[subsection.taxonomyKey]??>
                                                        <#assign facetBean = facetFilterMap[subsection.taxonomyKey][0]>
                                                        <#if facetFilterMap[subsection.taxonomyKey][1]>
                                                            <@hst.facetnavigationlink var="facetLink" remove=facetBean current=facetBean />
                                                        <#else>
                                                            <@hst.link var="facetLink" hippobean=facetBean>
                                                                <@hst.param name="query" value="${query}" />
                                                            </@hst.link>
                                                        </#if>
                                                        <label class="filter-label <#if subsection.description()??>filter-label__described</#if>">
                                                            <input
                                                                onclick="window.location = '${facetLink}'"
                                                                type="checkbox"
                                                                 <#if facetFilterMap[subsection.taxonomyKey][1]>checked</#if>
                                                            >
                                                            <a aria-label="Filter by ${subsection.displayName}"
                                                               href="${facetLink}"
                                                               class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if facetFilterMap[subsection.taxonomyKey][1]>selected</#if> <#if subsection.entries?has_content>nhsd-!t-font-weight-bold</#if>">
                                                                <input type="checkbox">
                                                                <span class="filter-label__text">${subsection.displayName} (${facetFilterMap[subsection.taxonomyKey][2]})</span>
                                                                <#assign childrenToShowCounter = childrenToShowCounter - 1 />
                                                            </a>
                                                            <div class="checkmark"></div>
                                                        </label>
                                                        <#if subsection.description()??>
                                                            <div class="section-label-description">
                                                                <@markdownDescription description=subsection.description() />
                                                            </div>
                                                        </#if>
                                                    <#else>
                                                        <div class="section-label-container ">
                                                            <label class="filter-label ">
                                                                <input type="checkbox" disabled>
                                                                     <span class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s nhsd-!t-font-weight-bold">
                                                                        <input type="checkbox" disabled>
                                                                        <span class="filter-label__text">${subsection.displayName}</span>
                                                                    </span>
                                                                <div class="checkmark"></div>
                                                            </label>
                                                            <#if subsection.description()??>
                                                                <div class="section-label-description">
                                                                    <@markdownDescription description=subsection.description() />
                                                                </div>
                                                            </#if>
                                                        </div>
                                                    </#if>
                                                </span>
                                            </div>

                                            <#-- If there are further nested entries, display them -->
                                            <#if subsection.entries?has_content>
                                                <div class="nhsd-m-filter-menu-section__option-group">
                                                    <#list subsection.entries as nestedSubsection>
                                                        <#if facetFilterMap[nestedSubsection.taxonomyKey]??>
                                                            <#assign showMoreCounter = showMoreCounter + 1>

                                                            <#-- Check if the facet is selected -->
                                                            <#assign facetBean = facetFilterMap[nestedSubsection.taxonomyKey][0]>
                                                            <#if facetFilterMap[nestedSubsection.taxonomyKey][1]>
                                                                <@hst.facetnavigationlink var="facetLink" remove=facetBean current=facetBean />
                                                            <#else>
                                                                <@hst.link var="facetLink" hippobean=facetBean>
                                                                    <@hst.param name="query" value="${query}" />
                                                                </@hst.link>
                                                            </#if>

                                                            <div class="section-label-container <#if shouldHide(childrenToShowCounter, section, checked)>nhsd-!t-display-hide</#if>">
                                                                <span class="nhsd-a-checkbox">
                                                                    <label class="filter-label ">
                                                                        <input
                                                                            onclick="window.location = '${facetLink}'"
                                                                            type="checkbox"
                                                                            <#if facetFilterMap[nestedSubsection.taxonomyKey][1]>checked</#if>
                                                                        >
                                                                            <a aria-label="Filter by ${nestedSubsection.displayName}"
                                                                               href="${facetLink}"
                                                                               class="nhsd-a-checkbox__label nhsd-!t-margin-bottom-0 nhsd-t-body-s <#if facetFilterMap[nestedSubsection.taxonomyKey][1]>selected</#if>">
                                                                                <input type="checkbox">
                                                                                <span class="filter-label__text">${nestedSubsection.displayName} (${facetFilterMap[nestedSubsection.taxonomyKey][2]})</span>
                                                                                <#assign childrenToShowCounter = childrenToShowCounter - 1 />
                                                                            </a>
                                                                        <div class="checkmark"></div>
                                                                    </label>
                                                                    <#if nestedSubsection.description()??>
                                                                        <div class="section-label-description">
                                                                           <@markdownDescription description=nestedSubsection.description() />
                                                                        </div>
                                                                    </#if>
                                                                </span>
                                                            </div>
                                                        </#if>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </#if>
                                        <#if subsection?is_last>
                                            <#if !checked &&  section.amountChildrenToShow?? && section.amountChildrenToShow gt 0 && section.amountChildrenToShow lt showMoreCounter>
                                                <div class="nhsd-m-filter-menu-section show-more">
                                                    <a class="nhsd-a-link--col-dark-grey">show more...</a>
                                                </div>
                                            </#if>
                                        </#if>
                                    </#list>
                                </div>
                            </div><!-- end new div -->
                            <hr class="nhsd-a-horizontal-rule nhsd-a-horizontal-rule--size-s">
                        </#if>
                    </#list>
                <#else>
                    <p>No filters available.</p>
                </#if>
            </nav>
        </div>
    </div>
</div>

<script src="<@hst.webfile path="/catalogue/catalogue.js"/>"></script>