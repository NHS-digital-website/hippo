<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/siteHeader.ftl">
<#include "../macro/siteFooter.ftl">
<#include "../macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "../app-layout-head.ftl">

<body class="debugs static-ui">
    <@skipLink />
    <#include "shared/nav.ftl"/>

    <@siteHeader true></@siteHeader>

    <@hst.include ref="coronavirus-banner"/>

    <#include "shared/breadcrumb.ftl"/>

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>

    <@siteFooter />

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>

    <#include "../scripts/footer-scripts.ftl" />
</body>

</html>
