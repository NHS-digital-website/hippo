<#ftl output_format="HTML">

<#include "../macro/metaTags.ftl">
<@metaTags />

<head>
    <script id="Cookiebot" src="https://consent.cookiebot.com/uc.js" data-cbid="8a16fbff-6ab2-4087-ae02-65267c376ba1" data-blockingmode="auto" async></script>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Generic meta tags -->
    <@hst.headContributions categoryIncludes="intranetMeta" xhtml=true/>

    <#assign toolkitVersion = 'v0.130.0' />

    <#-- Preload fonts to improve performance -->
    <link href="https://design-system.digital.nhs.uk/" rel="preconnect" crossorigin>
    <link type="font/woff2" href="https://design-system.digital.nhs.uk/cdn/${toolkitVersion}/fonts/FrutigerLTW01-55Roman.woff2" rel="preload" as="font" crossorigin>
    <link type="font/woff2" href="https://design-system.digital.nhs.uk/cdn/${toolkitVersion}/fonts/FrutigerLTW01-65Bold.woff2" rel="preload" as="font" crossorigin>
    <link type="font/woff2" href="https://design-system.digital.nhs.uk/cdn/${toolkitVersion}/fonts/FrutigerLTW01-45Light.woff2" rel="preload" as="font" crossorigin>

    <#-- Preconnect to 3rd parties to improve proformance -->
    <link rel="preconnect" href="https://in.hotjar.com" crossorigin>
    <link rel="preconnect" href="https://vars.hotjar.com" crossorigin>
    <link rel="preconnect" href="https://accdn.lpsnmedia.net" crossorigin>
    <link rel="preconnect" href="https://lpcdn.lpsnmedia.net" crossorigin>
    <link rel="preconnect" href="https://lptag.liveperson.net" crossorigin>

    <!--[if IE]><link rel="shortcut icon" href="<@hst.webfile path="icons/favicon.ico"/>"><![endif]-->
    <link rel="apple-touch-icon" sizes="180x180" href="<@hst.webfile path="icons/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<@hst.webfile path="icons/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<@hst.webfile path="icons/favicon-16x16.png"/>">
    <link rel="manifest" href="<@hst.webfile path="icons/manifest.json"/>">
    <link rel="mask-icon" href="<@hst.webfile path="icons/safari-pinned-tab.svg"/>">
    <meta name="theme-color" content="#ffffff">

    <link rel="stylesheet" href="https://design-system.digital.nhs.uk/cdn/${toolkitVersion}/stylesheets/nhsd-frontend.css" />
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsd-intranet-edge-cases.css" />" />

    <script src="https://design-system.digital.nhs.uk/cdn/${toolkitVersion}/scripts/nhsd-frontend.js"></script>
    <script src="<@hst.webfile path="/dist/nhsd-intranet-priority-scripts.bundle.js"/>"></script>

    <#include "../scripts/intranet-header-scripts.ftl" />
</head>
