<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../common/macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "master-head.ftl">

<body class="debugs">
    <@skipLink />

    <main id="main-content">
        <@hst.include ref="main"/>
    </main>
</body>

</html>
