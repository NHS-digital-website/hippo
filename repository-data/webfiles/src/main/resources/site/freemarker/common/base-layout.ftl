<!doctype html>
<#include "../include/imports.ftl">
<html lang="en">
  <head>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="<@hst.webfile  path="/css/demo.css"/>" type="text/css"/>
    <#if hstRequest.requestContext.cmsRequest>
      <link rel="stylesheet" href="<@hst.webfile  path="/css/cms-request.css"/>" type="text/css"/>
    </#if>
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" xhtml=true/>
  </head>
  <body>
    <section class="container container-top">
        <div class="in-line-container" style="width: 120px">
            <a href="/" class="logo logo-nhs-digital">NHS Digital</a>
        </div>
        <div class="in-line-container">
            <@hst.include ref="top"/>
        </div>
        <ul class="container-top__menu">
            <li><a href="#" title="">Data and information</a></li>
            <li><a href="#" title="">Systems and services</a></li>
            <li><a href="#" title="">News and events</a></li>
            <li><a href="#" title="">What is NHS Digital?</a></li>
            <li><a href="#" title="">How we look after your information</a></li>
        </ul>
    </section>

    <section class="container">
        <#if hstResponseChildContentNames?seq_contains("left") >
        <div class="container container-side">
            <@hst.include ref="left" />
        </div>
        </#if>
        <div class="container container-main">
            <@hst.include ref="main"/>
        </div>
    </section>

    <footer>
        <@hst.include ref="footer"/>
    </footer>

    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
  </body>
</html>
