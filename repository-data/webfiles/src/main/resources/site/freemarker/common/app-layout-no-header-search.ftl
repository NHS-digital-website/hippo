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
    <#-- Add site header without the search bar -->
    <@siteHeader false></@siteHeader>

    <@hst.include ref="coronavirus-banner"/>

    <#-- No breadcrumbs here -->

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>

    <@siteFooter />

    <#include "scripts/footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
