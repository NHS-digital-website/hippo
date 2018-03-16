<#ftl output_format="HTML">
<!DOCTYPE html>
<#include "../include/imports.ftl">
<@hst.setBundle basename="emails"/>

<html lang="en">
  <head>

    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script async src="https://www.googletagmanager.com/gtag/js?id=UA-76954916-2"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', 'UA-76954916-2');

        function logGoogleAnalyticsEvent(action,category,label) {
            gtag('event', action, {
                    'event_category': category,
                    'event_label': label
                });
        }
    </script>

    <title>NHS Digital Website</title>

    <meta charset="utf-8"/>
    <meta name="title" content="NHS Digital Website" />
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0" />

    <link rel="stylesheet" href="<@hst.webfile  path="/css/style.css"/>" type="text/css"/>
    <#if hstRequest.requestContext.cmsRequest>
      <link rel="stylesheet" href="<@hst.webfile  path="/css/cms-request.css"/>" type="text/css"/>
    </#if>
    <@hst.headContributions categoryExcludes="htmlBodyEnd, scripts" xhtml=true/>
  </head>
  <body>

    <div class="beta-banner">
        <div class="beta-banner__inner">
            You are trying the new Beta Clinical Indicator pages. Please send any feedback to <a href="mailto:<@fmt.message key="email.feedback"/>"><@fmt.message key="email.feedback"/></a>.
        </div>
    </div>

    <header class="top-header">
      <div class="top-header__col1">
        <a href="https://digital.nhs.uk/home">
          <img class="top-header__logo" src="https://digital.nhs.uk/media/89/NHSDigital/variant1/NHS-Digital-logo_WEB_LEFT-100x855" alt="NHS Digital">
        </a>
      </div><!--
      --><div class="top-header__col2">
        <div class="top-header__nav">
          <ul class="top-header__nav__list">
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/article/190/Data-and-information">Data and information</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/article/196/Systems-and-services">Systems and services</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/news-and-events">News and events</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/about-nhs-digital">About NHS Digital</a></li>
            <li class="top-header__nav__list__item"><a href="https://digital.nhs.uk/article/1153/How-we-look-after-your-information">How we look after your information</a></li>
          </ul>
        </div>
      </div>
    </header>

    <div class="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb__crumb">
                <span class="label">You are here:</span>
            </li>
            <li class="breadcrumb__crumb">
                <a href="https://digital.nhs.uk">Home</a>
            </li>
            <li class="separator">/</li>
            <li class="breadcrumb__crumb">
                <@hst.link var="rootLink" path="/"/>
                <a href="${rootLink}">Clinical Indicators</a>
            </li>
        </ol>
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
