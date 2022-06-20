<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../macro/globalHeader.ftl">
<#include "../macro/ndrsFooterBanner.ftl">
<#include "../macro/component/skipLink.ftl">
<#include "../breadcrumb.ftl">

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
    <#-- <@hst.include ref="intra-breadcrumb"/>
    <@hst.include ref="breadcrumb-ci"/>
    <@hst.include ref="nhsd-breadcrumb"/>-->
    <@hst.include ref="breadcrumb"/>
    <main id="main-content">
        <@hst.include ref="main"/>
    </main>
    <@ndrsFooterBanner></@ndrsFooterBanner>
    <@hst.include ref="footer-menu"/>
        <#include "../scripts/nhsd-footer-scripts.ftl" />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>

</body>
</html>
