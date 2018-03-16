<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/siteHeader.ftl">

<#include "app-layout-head.ftl">

<body class="debugs">
    <#include "cookie-banner.ftl"/>
    
    <#include "scripts/live-engage-chat.ftl"/>

    <@siteHeader true></@siteHeader>

    <@hst.include ref="breadcrumb"/>

    <@hst.include ref="main"/>

    <#include "site-footer.ftl"/>

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
