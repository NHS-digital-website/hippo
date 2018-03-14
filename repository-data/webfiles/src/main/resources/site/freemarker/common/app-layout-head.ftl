<#ftl output_format="HTML">

<#if document?? & document.title??>
    <#assign documentTitle = document.title>
<#else>
    <#assign documentTitle = "NHS Digital">
</#if>

<#if document?? & document.seosummary??>
    <#assign documentSEOSummary = document.seosummary>
<#else>
    <#assign documentSEOSummary = "Document SEO summary">
</#if>

<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    
    <title>${documentTitle}</title>
    <meta name="title" content="${documentTitle}" />
    <meta property="og:title" name="twitter:title" content="${documentTitle}">
    <meta property="og:locale" content="en_GB" />
    <meta property="og:type" content="website">
    <meta property="og:url" content="<@hst.link fullyQualified=true />">
    <meta name="description" content="${documentSEOSummary}" />
    <meta property="og:description" name="twitter:description" content="${documentSEOSummary}">
    <meta name="twitter:card" content="summary">


    <!--[if IE]><link rel="shortcut icon" href="<@hst.webfile  path="icons/favicon.ico"/>"><![endif]-->
    <link rel="apple-touch-icon" sizes="180x180" href="<@hst.webfile  path="icons/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<@hst.webfile  path="icons/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<@hst.webfile  path="icons/favicon-16x16.png"/>">
    <link rel="manifest" href="<@hst.webfile  path="icons/manifest.json"/>">
    <link rel="mask-icon" href="<@hst.webfile  path="icons/safari-pinned-tab.svg"/>" color="#666666">
    <meta name="theme-color" content="#ffffff">


    <!--[if gt IE 8]><!-->
    <link rel="stylesheet" href="<@hst.webfile  path="/css/nhsuk.css"/>" media="screen" type="text/css"/>
    <!--<![endif]-->
    <!--[if IE 6]>
    <link rel="stylesheet" href="<@hst.webfile  path="/css/nhsuk-ie6.css"/>" media="screen" type="text/css"/>
    <![endif]-->
    <!--[if IE 7]>
    <link rel="stylesheet" href="<@hst.webfile  path="/css/nhsuk-ie7.css"/>" media="screen" type="text/css"/>
    <![endif]-->
    <!--[if IE 8]>
    <link rel="stylesheet" href="<@hst.webfile  path="/css/nhsuk-ie8.css"/>" media="screen" type="text/css"/>
    <![endif]-->
    <link rel="stylesheet" href="<@hst.webfile  path="/css/nhsuk-print.css"/>" media="print" type="text/css"/>

    <#-- Add CSS class to mark JS enabled -->
    <#include "scripts/js-enabled.ftl"/>

    <#-- GA Tracking code -->
    <#include "scripts/google-analytics.ftl"/>

    <!--[if IE]>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
    <![endif]-->

    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" xhtml=true/>
</head>