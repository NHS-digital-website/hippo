<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/globalHeader.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <#include "./app-layout-head.ftl">
<body>
    <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
        <#-- Add IE banner -->
        <@hst.include ref="ie-banner"/>

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
