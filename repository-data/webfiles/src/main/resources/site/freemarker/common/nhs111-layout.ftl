<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<!DOCTYPE html>
<html lang="en" class="no-js">

<#include "nhs111-layout-head.ftl">

<body class="debugs">
    <a class="nhsuk-skip-link" href="#maincontent">Skip to main content</a>

    <div class="nhsuk-width-container">
        <main class="nhsuk-main-wrapper app-main-wrapper" id="maincontent">
            <div class="nhsuk-grid-row">
            <div class="nhsuk-grid-column-full app-component-reading-width">
                <@hst.include ref="main"/>
            </div>
        </main>
    </div>
</body>

</html>
