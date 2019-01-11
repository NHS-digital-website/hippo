<#ftl output_format="HTML">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Generic meta tags -->
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="genericMeta" xhtml=true/>

    <!-- Facebook meta tags -->
    <meta property="og:type" content="website" />
    <meta property="og:locale" content="en_GB" />
    <meta property="og:image:type" content="image/jpeg">
    <meta property="og:image" content="<@hst.webfile path="images/nhs-digital-logo-social.png" fullyQualified=true/>">
    <meta property="og:url" content="${getDocumentUrl()}">
    
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="facebookMeta" xhtml=true/>
    
    <!-- Twitter meta tags -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@NHSDigital">
    <meta name="twitter:image" content="<@hst.webfile path="images/nhs-digital-logo-social.png" fullyQualified=true/>">
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="twitterMeta" xhtml=true/>
    
    <!--[if IE]><link rel="shortcut icon" href="<@hst.webfile path="icons/favicon.ico"/>"><![endif]-->
    <link rel="apple-touch-icon" sizes="180x180" href="<@hst.webfile path="icons/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<@hst.webfile path="icons/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<@hst.webfile path="icons/favicon-16x16.png"/>">
    <link rel="manifest" href="<@hst.webfile path="icons/manifest.json"/>">
    <link rel="mask-icon" href="<@hst.webfile path="icons/safari-pinned-tab.svg"/>">
    <meta name="theme-color" content="#ffffff">

    <!--[if gt IE 8]><!-->
    <link rel="stylesheet" href="<@hst.webfile path="/css/nhsuk.css"/>" media="screen" type="text/css"/>
    <!--<![endif]-->
    <!--[if IE 6]>
    <link rel="stylesheet" href="<@hst.webfile path="/css/nhsuk-ie6.css"/>" media="screen" type="text/css"/>
    <![endif]-->
    <!--[if IE 7]>
    <link rel="stylesheet" href="<@hst.webfile path="/css/nhsuk-ie7.css"/>" media="screen" type="text/css"/>
    <![endif]-->
    <!--[if IE 8]>
    <link rel="stylesheet" href="<@hst.webfile path="/css/nhsuk-ie8.css"/>" media="screen" type="text/css"/>
    <![endif]-->
    <link rel="stylesheet" href="<@hst.webfile path="/css/eforms.css"/>" media="screen" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/css/nhsuk-print.css"/>" media="print" type="text/css"/>

    <#include "scripts/header-scripts.ftl" />

    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts, genericMeta, facebookMeta, twitterMeta" xhtml=true/>
</head>
