<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<@hst.setBundle basename="design-system"/>
<@metaTags title summary></@metaTags>

<@fmt.message key="design-system.url" var="designSystemUrl" />

<head>
    <script id="Cookiebot" src="https://consent.cookiebot.com/uc.js" data-cbid="8a16fbff-6ab2-4087-ae02-65267c376ba1" async></script>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="robots" content="max-image-preview:large">
    <@hst.headContributions categoryIncludes="metadata" xhtml=true />

    <!-- Generic meta tags -->
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="genericMeta" xhtml=true/>

    <!-- Facebook meta tags -->
    <meta property="og:type" content="website" />
    <meta property="og:locale" content="en_GB" />
    <meta property="og:image:type" content="image/jpeg" />
    <meta property="og:url" content="${getDocumentUrl()}" />
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="facebookMeta" xhtml=true/>

    <!-- Twitter meta tags -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@NHSDigital">
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="twitterMeta" xhtml=true/>

    <#assign toolkitVersion = 'v0.153.0' />

    <link href="${designSystemUrl}/" rel="preconnect" crossorigin>
    <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-55Roman.woff2" rel="preload" as="font" crossorigin>
    <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-65Bold.woff2" rel="preload" as="font" crossorigin>
    <link type="font/woff2" href="${designSystemUrl}/cdn/${toolkitVersion}/fonts/FrutigerLTW01-45Light.woff2" rel="preload" as="font" crossorigin>

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

    <link rel="stylesheet" href="${designSystemUrl}/cdn/${toolkitVersion}/stylesheets/nhsd-frontend.css" media="screen" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsd-frontend-edge-cases.css"/>" media="screen" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsd-print.css"/>" media="print" type="text/css"/>

    <#include "scripts/header-scripts.ftl" />
</head>
