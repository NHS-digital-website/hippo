<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "nhs111-layout-head.ftl">

<body class="debugs">
    <@skipLink />

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>
</body>

</html>
