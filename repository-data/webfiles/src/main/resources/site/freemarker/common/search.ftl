<#include "../include/imports.ftl">

<form class="navbar-form" role="search" action="<@hst.link siteMapItemRefId="search" />" method="get">
    <div class="input-group">
        <input type="text" id="query" class="search-input" placeholder="Search NHS Digital" name="query" value="${query!""}">
        <input type="submit" class="search-submit" id="btnSearch" value="Search">
    </div>
</form>
