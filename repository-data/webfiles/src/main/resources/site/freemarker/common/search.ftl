<#include "../include/imports.ftl">

<@hst.link siteMapItemRefId="search" mount="common-context" var="searchLink"/>


<section class="common-search">
  <form class="navbar-form" role="search" action="${searchLink}" method="get">
    <div class="common-search__inner">
      <span class="common-search__input">
        <input type="text" class="common-search__input__field" id="query" name="query" placeholder="What are you looking for today?" value="${query!""}">
      </span>
      <input type="submit" class="common-search__submit" id="btnSearch" value="Search">
    </div>
  </form>
</section>
