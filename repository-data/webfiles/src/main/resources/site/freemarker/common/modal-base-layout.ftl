<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "macro/siteHeader.ftl">
<#include "macro/siteFooter.ftl">
<#include "macro/component/skipLink.ftl">

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="format-detection" content="telephone=no">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <@hst.headContributions categoryIncludes="metadata" xhtml=true />
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsuk.css"/>" media="screen" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsuk-print.css"/>" media="print" type="text/css"/>
    <link rel="stylesheet" href="<@hst.webfile path="/dist/nhsuk-print-pdf-document.css"/>" media="print" type="text/css"/>

    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts, genericMeta, facebookMeta, twitterMeta, metadata" xhtml=true />
    <meta name="robots" content="noindex">
</head>

<body>
    <main id="main-content">
        <@hst.include ref="main"/>
    </main>
</body>

</html>
