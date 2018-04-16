<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/searchTabsComponent.ftl">
<#include "macro/siteHeader.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "app-layout-head.ftl">

<body class="debugs">
    <#-- Add site header with the search bar -->
    <@siteHeader true></@siteHeader>

    <@hst.include ref="breadcrumb-ci"/>

    <@hst.include ref="main" />

    <#include "site-footer.ftl"/>

    <#include "cookie-banner.ftl"/>

    <#include "scripts/live-engage-chat.ftl"/>

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
