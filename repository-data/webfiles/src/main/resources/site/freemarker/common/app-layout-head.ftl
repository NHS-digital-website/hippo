<#ftl output_format="HTML">
<head>
    <!-- Google Tag Manager -->
    <script>(function (w, d, s, l, i) {
            w[l] = w[l] || [];
            w[l].push({
                'gtm.start':
                    new Date().getTime(), event: 'gtm.js'
            });
            var f = d.getElementsByTagName(s)[0],
                j = d.createElement(s), dl = l != 'dataLayer' ? '&l=' + l : '';
            j.async = true;
            j.src =
                'https://www.googletagmanager.com/gtm.js?id=' + i + dl;
            f.parentNode.insertBefore(j, f);
        })(window, document, 'script', 'dataLayer', 'GTM-W6GJCR9');</script>
    <!-- End Google Tag Manager -->

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

    <#-- Preload fonts to improve performance -->
    <link rel="preload" href="<@hst.webfile path="fonts/FrutigerLTW01-55Roman.woff2" />" as="font" type="font/woff2" crossorigin>
    <link rel="preload" href="<@hst.webfile path="fonts/FrutigerLTW01-56Italic.woff2" />" as="font" type="font/woff2" crossorigin>
    <link rel="preload" href="<@hst.webfile path="fonts/FrutigerLTW01-65Bold.woff2" />" as="font" type="font/woff2" crossorigin>
    <link rel="preload" href="<@hst.webfile path="fonts/FrutigerLTW01-66BoldItalic.woff2" />" as="font" type="font/woff2" crossorigin>

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

    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsuk.css"/>" media="screen" type="text/css"/>

    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsuk-print.css"/>" media="print" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsuk-print-pdf-document.css"/>" media="print" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhse-global-menu.css"/>" type="text/css"/>

    <#include "scripts/header-scripts.ftl" />

    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts, genericMeta, facebookMeta, twitterMeta, metadata" xhtml=true />
</head>
