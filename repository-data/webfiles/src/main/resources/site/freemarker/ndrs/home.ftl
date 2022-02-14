<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js" data-test="ttttttt">
    <#include "./nhsd-homepage-head.ftl">
<body>
    <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
        <@skipLink />

        <#-- Add IE banner -->
        <@hst.include ref="ie-banner"/>

        <@hst.include ref="breadcrumb"/>

        <main id="main-content">
            <@hst.include ref= "main"/>
        </main>

        <@hst.include ref= "footer"/>
        <#-- <#include "scripts/nhsd-header-scripts.ftl" /> -->
        <#include "scripts/nhsd-footer-scripts.ftl" />

        <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
    </div>
</body>
</html>