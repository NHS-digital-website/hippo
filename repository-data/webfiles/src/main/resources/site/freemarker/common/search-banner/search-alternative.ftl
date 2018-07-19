<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="website.labels"/>
<@fmt.message key="labels.search-placeholder" var="placeholderText"/>
<@fmt.message key="labels.search-button" var="buttonText"/>
<@fmt.message key="labels.search-title" var="searchTitle"/>
<@fmt.message key="labels.search-banner-text" var="searchBannerText"/>

<div class="grid-row">
  <div class="column">
      <section class="" aria-label="Search form">
          <div class="article-wrapper article-wrapper--collapse">
              <p class="search-banner__headline">${searchBannerText}</p>
              <form role="search" method="get" action="${searchLink}" class="search-banner__form" aria-label="${searchTitle}">
                  <div>
                      <input type="text" name="query" id="query" class="search-banner__input" placeholder="${placeholderText}" value="${query!""}">
                      <label for="query" class="visually-hidden">${buttonText}</label>
                  </div>
                  <div>
                      <button class="button button--tertiary search-banner__button">${buttonText}</button>
                  </div>
              </form>
          </div>
      </section>
  </div>
</div>
