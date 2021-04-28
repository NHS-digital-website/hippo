<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/globalHeader.ftl">
<#include "../nhsd-common/macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <#include "./nhsd-homepage-head.ftl">
<body>
    <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
        <@skipLink />

        <@globalHeader true/>

        <@hst.include ref="breadcrumb"/>

        <main id="main-content">
            <@hst.include ref= "main"/>
        </main>

        <@hst.include ref= "footer"/>

        <#include "scripts/nhsd-footer-scripts.ftl" />
    </div>
</body>
</html>

