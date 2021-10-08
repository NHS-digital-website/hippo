<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/siteHeader.ftl">
<#include "../../common/macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "master-head.ftl">

<body class="debugs">
    <!-- Google Tag Manager (noscript) -->
    <noscript>
        <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-W6GJCR9"
                height="0" width="0" style="display:none;visibility:hidden">
        </iframe>
    </noscript>
    <!-- End Google Tag Manager (noscript) -->

    <@skipLink />

    <#-- Add IE banner -->
    <@hst.include ref="ie-banner"/>

    <#-- Add site header with the search bar -->
    <@siteHeader true></@siteHeader>

    <@hst.include ref="intra-breadcrumb"/>

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>

    <@hst.include ref="footer-menu"/>

</body>

</html>
