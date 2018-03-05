<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/siteHeader.ftl">

<#include "app-layout-head.ftl">

<body>
    <@siteHeader true></@siteHeader>

    <@hst.include ref="breadcrumb"/>

    <@hst.include ref="main"/>

    <footer role="contentinfo"></footer>

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
</body>

</html>
