<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/siteHeader.ftl">
<#include "macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "app-layout-head.ftl">

<body class="debugs">
    <@skipLink />
    <#-- Add site header with the search bar -->
    <@siteHeader true></@siteHeader>

    <#include "coronavirus-banner.ftl"/>

    <@hst.include ref="breadcrumb"/>

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>

    <#include "site-footer.ftl"/>

    <#include "scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
