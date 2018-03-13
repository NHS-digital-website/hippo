<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/siteHeader.ftl">

<#include "app-layout-head.ftl">

<body class="debugs">
    <#include "scripts/live-engage-chat.ftl"/>

    <@siteHeader false></@siteHeader>

    <@hst.include ref="main"/>

    <footer role="contentinfo"></footer>

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
