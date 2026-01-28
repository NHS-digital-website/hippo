<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<div class="article article--search-results" aria-label="Search Results" role="search">
    <div class="nhsd-t-grid">
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
                <#include "searchresults.ftl">
            </div>
        </div>
    </div>
</div>
