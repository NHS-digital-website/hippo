<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/results.ftl">
<#include "./macro/contentSearchResults.ftl">

<#-- Add meta tags -->
<#include "macro/metaTags.ftl">
<#assign overridePageTitle>Search Results</#assign>
<@metaTags></@metaTags>

<#-- Search Page Pixel -->
<script>
    if ("${query}"){
        var br_data = br_data || {};
        br_data.ptype = "search";
        br_data.search_term = "${query}";
    }
</script>

<#if isContentSearch?? && isContentSearch>
    <@contentSearch queryResponse />
<#else>
    <div data-uipath="ps.search-results" data-totalresults="${(pageable.total)!0}">
        <#if pageable?? && pageable.total gt 0>
            <div class="search-results-heading">
                <h1 class="search-results-heading__title">Search results</h1>

                <div class="search-results-heading__details">
                    <div class="search-results-heading__subcopy">
                        <div data-uipath="ps.search-results.description">
                            <span data-uipath="ps.search-results.count">${totalResults}</span> result<#if pageable.total gt 1>s</#if><#if query?has_content> containing '<strong>${query}</strong>',</#if> sorted by <strong>${sort}</strong>.
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
                <@searchResults items=pageable.items/>
            </div>

            <#if cparam.showPagination && pageable.totalPages gt 1>
                <#include "../include/pagination.ftl">
            </#if>
        <#elseif query?has_content>
            <div class="search-results-heading no-border">
                <h1 class="search-results-heading__title" data-uipath="ps.search-results.description">No results for: ${query}</h1>
            </div>
        <#else>
            <div class="search-results-heading no-border">
                <h1 class="search-results-heading__title" data-uipath="ps.search-results.description">No results for filters</h1>
            </div>
        </#if>
    </div>
</#if>
