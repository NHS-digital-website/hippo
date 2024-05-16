<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<!DOCTYPE html>
<html lang="en" class="nhsd-no-js">
    <head>
        <title>Test page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <@hst.setBundle basename="design-system"/>
        <@fmt.message key="design-system.url" var="designSystemUrl" />
        <#assign toolkitVersion = 'v0.176.0' />

        <link href="${designSystemUrl}" rel="preconnect" crossorigin>
        <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-55Roman.woff2" rel="preload" as="font" crossorigin>
        <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-65Bold.woff2" rel="preload" as="font" crossorigin>
        <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-45Light.woff2" rel="preload" as="font" crossorigin>

        <link rel="stylesheet" href="${designSystemUrl}/cdn/${toolkitVersion}/stylesheets/nhsd-frontend.css" media="screen" type="text/css"/>
        <link rel="stylesheet" href="/site/webfiles/1706127788592/dist/nhse-global-menu.css" type="text/css"/>
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
    <!-- Google Tag Manager (noscript) -->
    <noscript>
        <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-W6GJCR9"
                height="0" width="0" style="display:none;visibility:hidden"></iframe>
    </noscript>
    <!-- End Google Tag Manager (noscript) -->
        <@hst.include ref= "main"/>
    </body>
</html>

