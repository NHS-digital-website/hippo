<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "app-example-layout-head.ftl">
<#include "scripts/header-scripts.ftl" />

<body class="app-example-page">
    <@skipLink />
    <#include "static-ui/shared/nav.ftl"/>
    <main id="main-content" role="main">
        <#include "static-ui/shared/back.ftl"/>
        <@hst.include ref="main"/>
    </main>
    <#include "../../src/js/sticky-nav.js.ftl" />
</body>
</html>
