<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/toolkit/organisms/global-header.ftl">
<#include "../nhsd-common/macro/component/skipLink.ftl">
<#include "../nhsd-common/macro/mergerBanner.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <#include "./nhsd-homepage-head.ftl">
<body>
    <div class="nhsd-t-grid nhsd-t-grid--full-width nhsd-!t-no-gutters">
        <@skipLink />

        <#-- Add IE banner -->
        <@hst.include ref="ie-banner"/>

        <@mergerBanner />

        <@globalHeader true/>

        <@hst.include ref="breadcrumb"/>

        <main id="main-content">
            <@hst.include ref= "main"/>
        </main>

        <@hst.include ref= "footer"/>

        <#include "scripts/nhsd-footer-scripts.ftl" />

        <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
    </div>
</body>
</html>

