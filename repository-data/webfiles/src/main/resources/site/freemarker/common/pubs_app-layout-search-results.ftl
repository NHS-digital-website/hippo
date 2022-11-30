<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/searchTabsComponent.ftl">
<#include "macro/siteHeader.ftl">
<#include "macro/siteFooter.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/skipLink.ftl">
<#include "macro/mergerBanner.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "app-layout-head.ftl">

<body class="debugs">
    <@skipLink />

    <#-- Add IE banner -->
    <@hst.include ref="ie-banner"/>

    <@mergerBanner />

    <#-- Add site header with the search bar -->
    <@siteHeader false></@siteHeader>

    <@hst.include ref="coronavirus-banner"/>

    <#-- Add breadcrumbs -->
    <#include "macro/pubsBreadcrumb.ftl">
    <@pubsBreadcrumb "Search results"></@pubsBreadcrumb>

    <@hst.include ref="top"/>

    <@hst.include ref="main"/>

    <@siteFooter />

    <#include "scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
