<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/searchTabsComponent.ftl">
<#include "../macro/globalHeader.ftl">
<#include "../macro/siteFooter.ftl">
<#include "../macro/metaTags.ftl">
<#include "../macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#-- <#include "app-layout-head.ftl"> -->
<#include "../nhsd-homepage-head.ftl">

<body class="debugs">
    <@skipLink />

    <#-- Add IE banner -->
    <@hst.include ref="ie-banner"/>

    <#-- Add site header with the search bar -->
    <@globalHeader false></@globalHeader>

    <@hst.include ref="coronavirus-banner"/>

    <#-- Add breadcrumbs -->
    <#include "../macro/pubsBreadcrumb.ftl">
    <@pubsBreadcrumb "Search results"></@pubsBreadcrumb>

    <@hst.include ref="top"/>

    <@hst.include ref="main"/>


    <@hst.include ref="footer-menu"/>

    <#include "../../intranet/scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
