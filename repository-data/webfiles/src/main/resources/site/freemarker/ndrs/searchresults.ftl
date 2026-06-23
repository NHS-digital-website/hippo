<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/contentSearchResults.ftl">

<@hst.setBundle basename="publicationsystem.labels,nationalindicatorlibrary.headers,nationalindicatorlibrary.labels,website.labels,homepage.website.labels,rb.doctype.published-work,rb.doctype.cyberalerts,rb.generic.labels,ndrs.website.labels"/>

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<#assign overridePageTitle>Search Results</#assign>
<@metaTags></@metaTags>

<#-- Search Page Pixel -->
<#assign queryValue = query!''>
<#assign resultsCount = (totalResults)!((pageable.total)!0)>
<script>
    if ("${queryValue}"){
        var br_data = br_data || {};
        br_data.ptype = "search";
        br_data.search_term = "${queryValue}";
    }
</script>

<#if isContentSearch?? && isContentSearch>
    <@contentSearch queryResponse />
<#else>
    <div data-uipath="ps.search-results" data-totalresults="${resultsCount}">
        <#if pageable?? && pageable.total gt 0>
            <div class="search-results-heading">
                <h1 class="search-results-heading__title">Search results</h1>

                <div class="search-results-heading__details">
                    <div class="search-results-heading__subcopy">
                        <div data-uipath="ps.search-results.description">
                            <span data-uipath="ps.search-results.count">${resultsCount}</span> result<#if resultsCount gt 1>s</#if><#if queryValue?has_content> containing '<strong>${queryValue}</strong>',</#if> sorted by <strong>${sort}</strong>.
                        </div>
                    </div>
                    <div class="search-results-heading__sort">

                        <label for="sortBy">Sort by:</label>

                        <select id="sortBy" data-uipath="ps.search-results.sort-selector">
                            <option title="Order by date" value="date" <#if sort?? && sort == 'date'>selected</#if>>Date</option>
                            <option title="Order by relevance" value="relevance" <#if sort?? && sort == 'relevance'>selected</#if>>Relevance</option>
                        </select>

                        <script>
                            // Function to update the URL based on the selected sorting option
                            function updateSort() {

                                // Get the current URL without the query parameters
                                const currentUrl = new URL(window.location.href);

                                // Get the selected value from the dropdown
                                const sortBy = document.getElementById('sortBy').value;

                                // Update the 'sort' query parameter based on the selection
                                if (sortBy === 'relevance') {
                                    // Remove the 'sort' parameter for relevance (default)
                                    currentUrl.searchParams.delete('sort');
                                } else {
                                    // Set the 'sort' parameter for date sorting
                                    currentUrl.searchParams.set('sort', sortBy);
                                }

                                // Redirect to the updated URL
                                window.location.href = currentUrl.toString();
                            }

                            // Attach the function to the 'onChange' event of the dropdown
                            document.getElementById('sortBy').addEventListener('change', updateSort);
                        </script>
                    </div>
                </div>
            </div>

            <div class="search-results">
                <div class="cta-list">
                    <#list pageable.items as document>
                        <@genericSearchElement item=document />
                    </#list>
                </div>
            </div>

            <#if cparam.showPagination && pageable.totalPages gt 1>
                <#include "../include/pagination.ftl">
            </#if>
        <#elseif queryValue?has_content>
            <div class="search-results-heading no-border">
                <h1 class="search-results-heading__title" data-uipath="ps.search-results.description">No results for: ${queryValue}</h1>
            </div>
        <#else>
            <div class="search-results-heading no-border">
                <h1 class="search-results-heading__title" data-uipath="ps.search-results.description">No results for filters</h1>
            </div>
        </#if>
    </div>
</#if>

<#macro genericSearchElement item>

<#assign nameList = item.class.name?split(".")>
<#assign doctype = nameList[nameList?size-1]?lower_case>
<#assign doctypeSpit = doctype?split("$")>
<#assign doctypeToDisplay = doctypeSpit[0]?lower_case>
<#assign summaryText = ''>
<@fmt.message key="labels.${doctypeToDisplay}" var="typeLabel" />

<#if item.shortsummary?? && item.shortsummary?has_content>
    <#assign summaryText = item.shortsummary>
<#elseif item.summary?? && item.summary.firstParagraph?? && item.summary.firstParagraph?has_content>
    <#assign summaryText = item.summary.firstParagraph>
<#elseif item.seosummary?? && item.seosummary?has_content>
    <#assign summaryText = item.seosummary.content?replace('<[^>]+>', '', 'r')>
</#if>

<div class="cta cta--detailed" data-uipath="ps.search-results.result">
    <#if typeLabel?has_content && !(typeLabel?starts_with('???') && typeLabel?ends_with('???'))>
        <div>
            <span class="cta__label" data-uipath="ps.search-results.result.type">${typeLabel}</span>
        </div>
    <#else>
        <!-- ${typeLabel} -->
    </#if>
    <h3 class="cta__title">
        <a class="cta__title cta__button cta__title-link" href="<@hst.link hippobean=item/>" title="${item.title}" data-uipath="ps.search-results.result.title">
            ${item.title}
        </a>
    </h3>
    <#if summaryText?has_content>
        <p class="cta__text" data-uipath="ps.search-results.result.summary"><@truncate text=summaryText size="300"/></p>
    </#if>
</div>
</#macro>
