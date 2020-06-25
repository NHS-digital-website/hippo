<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/searchTabsComponent.ftl">

<div class="article article--search-results" aria-label="Search Results">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div>
                    <div class="article-section-nav-wrapper">
                        <div class="article-section-nav article-section-nav--facets">
                            <!-- Facets -->
                            <@hst.include ref="left" />
                        </div>
                    </div>
                </div>
            </div>

            <div class="column column--two-thirds page-block page-block--main">
                <@searchTabsComponent contentNames=hstResponseChildContentNames></@searchTabsComponent>
                <#include "searchresults.ftl">
            </div>
        </div>
    </div>
</div>
