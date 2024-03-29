<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/metaTags.ftl">
<@hst.setBundle basename="design-system"/>

<@fmt.message key="design-system.url" var="designSystemUrl" />

<head>
    <!-- Google tag (gtag.js) -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-WZ8GJKH035"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'G-WZ8GJKH035');
    </script>
    <script id="Cookiebot" src="https://consent.cookiebot.com/uc.js" data-cbid="5f472a90-947a-46c9-974b-88076109c473" async></script>
    <!-- Begin Constant Contact Active Forms -->
    <script> var _ctct_m = "93d2e771bce26a23dadb40486223f483"; </script>
    <script id="signupScript" src="//static.ctctcdn.com/js/signup-form-widget/current/signup-form-widget.min.js" async defer></script>
    <!-- End Constant Contact Active Forms -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="width=device-width, initial-scale=1">
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

    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsd-old-frontend.css"/>" media="screen" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/ndrs-frontend.css"/>" media="screen" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/ndrsuk.css"/>" media="screen" type="text/css"/>
    <#include "scripts/header-scripts.ftl" />
</head>
