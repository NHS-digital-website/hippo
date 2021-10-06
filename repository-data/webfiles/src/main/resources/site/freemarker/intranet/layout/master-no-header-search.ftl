<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/siteHeader.ftl">
<#include "../../common/macro/siteFooter.ftl">
<#include "../macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "master-head.ftl">

<body class="debugs">
    <@skipLink />

    <#-- Add site header without the search bar -->
    <@siteHeader false></@siteHeader>

    <#-- Add breadcrumbs -->
    <#include "../macro/intraSingleBreadcrumb.ftl">
    <@intraSingleBreadcrumb "Search results"></@intraSingleBreadcrumb>

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>

    <@siteFooter narrowLayout=true />

    <#include "../../common/scripts/footer-scripts.ftl" />
</body>

</html>
