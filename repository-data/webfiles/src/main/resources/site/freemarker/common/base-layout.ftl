<#ftl output_format="HTML">
<!DOCTYPE html>
<#include "../include/imports.ftl">
<#include "macro/searchTabsComponent.ftl">
<@hst.setBundle basename="emails, feature-toggles"/>
<#include "macro/component/skipLink.ftl">

<html lang="en">
  <head>
    <script id="Cookiebot" src="https://consent.cookiebot.com/uc.js" data-cbid="5f472a90-947a-46c9-974b-88076109c473" data-blockingmode="auto" async></script>

    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-76954916-2"></script>

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

    <title>NHS Digital Website</title>

    <meta charset="utf-8"/>
    <meta name="title" content="NHS Digital Website" />
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0" />

    <link rel="stylesheet" href="<@hst.webfile  path="/css/style.css"/>" type="text/css"/>
    <#if hstRequest.requestContext.channelManagerPreviewRequest>
      <link rel="stylesheet" href="<@hst.webfile  path="/css/cms-request.css"/>" type="text/css"/>
    </#if>
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" xhtml=true/>

    <link rel="icon" type="image/png" sizes="32x32" href="<@hst.webfile path="icons/favicon-32x32.png"/>">
    <link rel="icon" type="image/png" sizes="16x16" href="<@hst.webfile path="icons/favicon-16x16.png"/>">
  </head>
  <body>
  <!-- Google Tag Manager (noscript) -->
  <noscript>
      <iframe src="https://www.googletagmanager.com/ns.html?id=GTM-W6GJCR9"
              height="0" width="0" style="display:none;visibility:hidden"></iframe>
  </noscript>
  <!-- End Google Tag Manager (noscript) -->
  <@skipLink />
    <div class="beta-banner">
        <div class="beta-banner__inner">
            You are trying the new Beta Clinical Indicator pages. Please send any feedback to <a href="mailto:<@fmt.message key="email.feedback"/>"><@fmt.message key="email.feedback"/></a>.
        </div>
    </div>

    <header class="top-header">
      <div class="top-header__col1">
        <a href="https://digital.nhs.uk/home">
            <img alt="NHS Digital" width="110" src="<@hst.webfile path="/images/nhs-digital-logo.svg"/>">
        </a>
      </div><!--
      --><div class="top-header__col2">
        <div class="top-header__nav">
          <ul class="top-header__nav__list">
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/article/190/Data-and-information">Data and information</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/services">Systems and services</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/news-and-events">News and events</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/about-nhs-digital">About NHS Digital</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/article/1153/How-we-look-after-your-information">How we look after your information</a></li>
          </ul>
        </div>
      </div>
    </header>

    <div class="breadcrumb">
        <@hst.include ref="breadcrumb-ci-old-styling"/>
    </div>

    <@hst.include ref="top"/>

    <#if hstResponseChildContentNames?seq_contains("left") >
    <section class="document-content" aria-label="Search Results">
      <div class="layout layout--large">
        <div class="layout__item layout-1-3">
          <div class="panel panel--grey">
            <@hst.include ref="left" />
          </div>
        </div><!--
        --><div class="layout__item layout-2-3">
          <@searchTabsComponent contentNames=hstResponseChildContentNames></@searchTabsComponent>

          <@hst.include ref="main"/>
        </div>
      </div>
    </section>
    <#else>
      <@searchTabsComponent contentNames=hstResponseChildContentNames></@searchTabsComponent>

      <@hst.include ref="main"/>

    </#if>


    <@hst.include ref="footer"/>


    <@hst.headContributions categoryIncludes="htmlBodyEnd, scripts" xhtml=true/>
  </body>
</html>
