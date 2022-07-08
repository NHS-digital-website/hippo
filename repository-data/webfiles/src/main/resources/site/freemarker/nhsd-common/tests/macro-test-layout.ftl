<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <head>
        <title>Test page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="https://design-system.digital.nhs.uk/" rel="preconnect" crossorigin>
        <link type="font/woff2" href="https://design-system.digital.nhs.uk/cdn/v0.156.0/fonts/FrutigerLTW01-55Roman.woff2" rel="preload" as="font" crossorigin>
        <link type="font/woff2" href="https://design-system.digital.nhs.uk/cdn/v0.156.0/fonts/FrutigerLTW01-65Bold.woff2" rel="preload" as="font" crossorigin>
        <link type="font/woff2" href="https://design-system.digital.nhs.uk/cdn/v0.156.0/fonts/FrutigerLTW01-45Light.woff2" rel="preload" as="font" crossorigin>

        <@hst.setBundle basename="design-system"/>
        <@fmt.message key="design-system.url" var="designSystemUrl" />
        <#assign toolkitVersion = 'v0.153.0' />
        <link rel="stylesheet" href="${designSystemUrl}/cdn/${toolkitVersion}/stylesheets/nhsd-frontend.css" media="screen" type="text/css"/>
        <style>
            .nav {
                color: #fff;
                background: #005bbb;
                width: 100%;
                padding: 5px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <@hst.include ref= "main"/>
    </body>
</html>

