<!DOCTYPE html>
<#include "../include/imports.ftlh">
<html lang="en">
  <head>

    <title>NHS - Replacement Publication System - Page not found</title>

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

      <section class="common-search">
        <form class="" role="search" action="/search" method="get">
          <div class="common-search__inner">
            <span class="common-search__input">
              <input type="text" class="common-search__input__field" id="query" name="query" placeholder="What are you looking for today?" value="">
            </span>
            <input type="submit" class="common-search__submit" id="btnSearch" value="Search">
          </div>
        </form>
      </section>

      <section class="document-content">
        <h2>Page not found</h2>
      </section>

</body>
</html>
