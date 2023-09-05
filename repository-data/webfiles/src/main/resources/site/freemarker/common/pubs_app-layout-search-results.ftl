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
<!-- Google Tag Manager (noscript) -->
<noscript>
    <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-T4CTKLJ4"
            height="0" width="0" style="display:none;visibility:hidden"></iframe>
</noscript>
<!-- End Google Tag Manager (noscript) -->
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
