<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/siteHeader.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "../app-layout-head.ftl">

<body class="debugs static-ui">    
    <#include "shared/nav.ftl"/>
    
    <@siteHeader true></@siteHeader>

    <#include "shared/breadcrumb.ftl"/>

    <main>
        <@hst.include ref="main"/>
    </main>

    <#include "../site-footer.ftl"/>

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>

    <#include "../scripts/footer-scripts.ftl" />
</body>

</html>
