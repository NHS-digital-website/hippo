<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <head>
        <title>Test page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <@hst.setBundle basename="design-system"/>
        <@fmt.message key="design-system.url" var="designSystemUrl" />
        <#assign toolkitVersion = 'v0.167.0' />

        <link href="${designSystemUrl}" rel="preconnect" crossorigin>
        <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-55Roman.woff2" rel="preload" as="font" crossorigin>
        <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-65Bold.woff2" rel="preload" as="font" crossorigin>
        <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-45Light.woff2" rel="preload" as="font" crossorigin>

        <link rel="stylesheet" href="${designSystemUrl}/cdn/${toolkitVersion}/stylesheets/nhsd-frontend.css" media="screen" type="text/css"/>
    </head>
    <body>
        <@hst.include ref= "main"/>
        <script defer src="${designSystemUrl}/cdn/${toolkitVersion}/scripts/nhsd-frontend.js"></script>
    </body>
</html>

