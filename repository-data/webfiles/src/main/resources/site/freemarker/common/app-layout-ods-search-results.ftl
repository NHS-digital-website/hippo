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
    <@siteHeader true></@siteHeader>

    <#-- Add breadcrumbs -->
    <#include "macro/pubsBreadcrumb.ftl">
    <@pubsBreadcrumb "ODS Search results"></@pubsBreadcrumb>

    <@hst.include ref="top"/>

    <div class="article article--search-results" aria-label="Search Results">
        <div class="grid-wrapper grid-wrapper--article">
            <@hst.include ref="main"/>
        </div>
    </div>

    <#include "site-footer.ftl"/>

    <#include "scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
