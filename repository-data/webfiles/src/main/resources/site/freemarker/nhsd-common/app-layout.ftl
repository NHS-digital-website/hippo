<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/globalHeader.ftl">
<#include "macro/component/skipLink.ftl">
<#include "macro/mergerBanner.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <#include "./app-layout-head.ftl">
<body>
<!-- Google Tag Manager (noscript) -->
<noscript>
    <iframe src="https://www.googletagmanager.com/ns.html?id=UA-76954916-2"
            height="0" width="0" style="display:none;visibility:hidden"></iframe>
</noscript>
<!-- End Google Tag Manager (noscript) -->
    <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">

        <@skipLink />

        <#-- Add IE banner -->
        <@hst.include ref="ie-banner"/>

        <@mergerBanner />

        <@globalHeader true/>

        <@hst.include ref="breadcrumb"/>

        <main id="main-content">
            <@hst.include ref= "main"/>
        </main>

        <@hst.include ref= "footer"/>

        <#include "scripts/nhsd-footer-scripts.ftl" />
    </div>
    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>
</html>
