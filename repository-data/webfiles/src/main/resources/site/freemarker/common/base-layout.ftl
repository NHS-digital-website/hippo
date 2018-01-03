<#ftl output_format="HTML">
<!DOCTYPE html>
<#include "../include/imports.ftl">
<html lang="en">
  <head>

    <title>NHS - Replacement Publication System - Dataset</title>

    <meta charset="utf-8"/>
    <meta name="title" content="NHS - Replacement Publication System" />
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0" />

    <link rel="stylesheet" href="<@hst.webfile  path="/css/style.css"/>" type="text/css"/>
    <#if hstRequest.requestContext.cmsRequest>
      <link rel="stylesheet" href="<@hst.webfile  path="/css/cms-request.css"/>" type="text/css"/>
    </#if>
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" xhtml=true/>
  </head>
  <body>


    <header class="top-header">
      <div class="top-header__col1">
        <img class="top-header__logo" src="https://digital.nhs.uk/media/89/NHSDigital/variant1/NHS-Digital-logo_WEB_LEFT-100x855" alt="NHS Digital">
      </div><!--
      --><div class="top-header__col2">
        <div class="top-header__nav">
          <ul class="top-header__nav__list">
            <li class="top-header__nav__list__item"><a href="#">Data and information</a></li>
            <li class="top-header__nav__list__item"><a href="#">Systems and services</a></li>
            <li class="top-header__nav__list__item"><a href="#">News and events</a></li>
            <li class="top-header__nav__list__item"><a href="#">About NHS Digital</a></li>
          </ul>
        </div>
      </div>
    </header>

    <@hst.include ref="top"/>

    <#if hstResponseChildContentNames?seq_contains("left") >
    <section class="document-content" aria-label="Search Results">
      <div class="layout layout--large">
        <div class="layout__item layout-1-3">
          <div class="panel panel--grey">
            <h3>Filter by:</h3>
            <@hst.include ref="left" />
          </div>
        </div><!--
        --><div class="layout__item layout-2-3">
          <@hst.include ref="main"/>
        </div>
      </div>
    </section>
    <#else>

      <@hst.include ref="main"/>

    </#if>


    <@hst.include ref="footer"/>


    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
  </body>
</html>
