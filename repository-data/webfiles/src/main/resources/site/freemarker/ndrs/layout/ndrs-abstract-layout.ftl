<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../macro/globalHeader.ftl">
<#include "../macro/ndrsFooterBanner.ftl">
<#include "../macro/component/skipLink.ftl">
<#include "../breadcrumb.ftl">
<#include "../breadcrumb-ci.ftl">
<#include "../nhsd-breadcrumb.ftl">
<#include "../intra-breadcrumb.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">
<#-- <#include "master-head.ftl"> -->
    <#include "../nhsd-homepage-head.ftl">
<body class="debugs">
    <!-- Google Tag Manager (noscript) -->
    <noscript>
        <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-W6GJCR9"
                height="0" width="0" style="display:none;visibility:hidden">
        </iframe>
    </noscript>
    <!-- End Google Tag Manager (noscript) -->
    <@skipLink />
    <#-- Add site header with the search bar -->
    <@globalHeader true></@globalHeader>
    <@hst.include ref="intra-breadcrumb"/>
    <@hst.include ref="breadcrumb"/>
    <@hst.include ref="breadcrumb-ci"/>
    <@hst.include ref="nhsd-breadcrumb"/>
    <main id="main-content">
        <@hst.include ref="main"/>
    </main>
    <@ndrsFooterBanner></@ndrsFooterBanner>
    <@hst.include ref="footer-menu"/>
    <#include "../../intranet/scripts/footer-scripts.ftl" />
</body>
</html>
