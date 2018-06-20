<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/searchTabsComponent.ftl">
<#include "macro/siteHeader.ftl">
<#include "macro/metaTags.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "app-layout-head.ftl">

<body class="debugs">
    <#-- Add site header with the search bar -->
    <@siteHeader false></@siteHeader>

    <#-- Add breadcrumbs -->
    <#include "macro/pubsBreadcrumb.ftl">
    <@pubsBreadcrumb "Search results"></@pubsBreadcrumb>

    <@hst.include ref="top"/>

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
                    <@hst.include ref="main"/>
                </div>
            </div>
        </div>
    </div>

    <#include "site-footer.ftl"/>

    <#include "scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
