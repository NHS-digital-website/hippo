<#ftl output_format="HTML">

<#-- This is the intranet's main layout. -->

<#include "../../include/imports.ftl">
<#include "../../common/macro/component/skipLink.ftl">

<!-- todo: move to java call -->

<#include "../../common/macro/siteFooter.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "master-head.ftl">

<body class="intranet debugs">
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
    <@hst.include ref="site-header"/>

    <@hst.include ref="intra-breadcrumb"/>

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>

    <@siteFooter narrowLayout=true />

    <#include "../../common/scripts/footer-scripts.ftl" />
</body>

</html>
