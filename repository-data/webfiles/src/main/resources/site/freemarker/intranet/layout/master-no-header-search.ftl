<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/siteHeader.ftl">
<#include "../intranet-footer.ftl">
<#include "../../common/macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "master-head.ftl">

<body class="debugs">
    <@skipLink />

    <#-- Add IE banner -->
    <@hst.include ref="ie-banner"/>

    <#-- Add site header without the search bar -->
    <@siteHeader false></@siteHeader>

    <#-- Add breadcrumbs -->
    <#include "../macro/intraSingleBreadcrumb.ftl">
    <@intraSingleBreadcrumb "Search results"></@intraSingleBreadcrumb>

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>

    <@hst.include ref="footer"/>

    <#include "../scripts/intranet-footer-scripts.ftl" />
</body>

</html>
