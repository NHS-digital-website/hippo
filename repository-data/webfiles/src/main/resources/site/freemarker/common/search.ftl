<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.link siteMapItemRefId="search" mount="common-context" var="searchLink"/>


<section class="common-search" aria-label="Search Bar">
  <form class="navbar-form" role="search" action="${searchLink}" method="get">
    <div class="common-search__inner">
      <span class="common-search__input">
        <!--Need to strip escape characters from quotes as they are present in the query variable given to us by hippo-->
        <input type="text" class="common-search__input__field" id="query" name="query" aria-labelledby="btnSearch" placeholder="What are you looking for today?" value="${(query!"")?replace("\\\"", "\"")}">
      </span>
      <input type="submit" class="common-search__submit" id="btnSearch" value="Search">
    </div>
  </form>
</section>
