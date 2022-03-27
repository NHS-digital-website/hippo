<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/searchTabsComponent.ftl">
<#include "macro/siteHeader.ftl">
<#include "macro/siteFooter.ftl">
<#include "macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "app-layout-head.ftl">

<body class="debugs">
    <@skipLink />

    <#-- Add IE banner -->
    <@hst.include ref="ie-banner"/>

    <#-- Add site header with the search bar -->
    <@siteHeader true></@siteHeader>

    <@hst.include ref="coronavirus-banner"/>

    <@hst.include ref="breadcrumb-ci"/>

    <main id="main-content">
        <@hst.include ref="main" />
    </main>

    <@siteFooter />

    <#include "scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
