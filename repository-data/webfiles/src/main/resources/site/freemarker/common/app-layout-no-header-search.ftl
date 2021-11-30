<#ftl output_format="HTML">
<#include "../include/imports.ftl">
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

    <#-- Add site header without the search bar -->
    <@siteHeader false></@siteHeader>

    <@hst.include ref="coronavirus-banner"/>

    <#-- No breadcrumbs here -->

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>
    <#-- Home Page Pixel -->
    <script>
        var br_data = br_data || {};
        br_data.ptype = "homepage";
    </script>
    <@siteFooter />

    <#include "scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
