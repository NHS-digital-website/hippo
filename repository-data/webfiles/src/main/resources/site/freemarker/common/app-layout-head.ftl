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
    <meta property="og:image" content="<@hst.webfile path="images/nhs-digital-logo-social.jpg" fullyQualified=true/>">
    <#if hstRequestContext?? && hstRequestContext.getContentBean()??>
        <meta property="og:url" content="<@hst.link hippobean=hstRequestContext.getContentBean() fullyQualified=true />">
    <#else>
        <meta property="og:url" content="<@hst.link fullyQualified=true />">
    </#if>
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="facebookMeta" xhtml=true/>
    
    <!-- Twitter meta tags -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@NHSDigital">
    <meta name="twitter:image" content="<@hst.webfile path="images/nhs-digital-logo-social.jpg" fullyQualified=true/>">
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" categoryIncludes="twitterMeta" xhtml=true/>
    
    <!--[if IE]><link rel="shortcut icon" href="<@hst.webfile path="icons/favicon.ico"/>"><![endif]-->
    <link rel="apple-touch-icon" sizes="180x180" href="<@hst.webfile path="icons/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" sizes="32x32" href="<@hst.webfile path="icons/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<@hst.webfile path="icons/favicon-16x16.png"/>">
    <link rel="manifest" href="<@hst.webfile path="icons/manifest.json"/>">
    <link rel="mask-icon" href="<@hst.webfile path="icons/safari-pinned-tab.svg"/>" color="#666666">
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
    <link rel="stylesheet" href="<@hst.webfile path="/css/nhsuk-print.css"/>" media="print" type="text/css"/>

    <#-- Add CSS class to mark JS enabled -->
    <#include "scripts/js-enabled.ftl"/>

    <#include "scripts/vanilla-js-utils.ftl"/>

    <#-- Cookiebot loading -->
    <#include "scripts/cookiebot-load.ftl"/>

    <#-- GA Tracking code -->
    <#include "scripts/google-analytics.ftl"/>

    <!--[if IE]>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
    <![endif]-->

    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts, genericMeta, facebookMeta, twitterMeta" xhtml=true/>
</head>
