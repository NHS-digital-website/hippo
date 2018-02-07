<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.labels"/>
<@fmt.message key="labels.search-ghost-text" var="ghostText"/>

<section class="common-search" aria-label="Search Bar">
    <form class="navbar-form" role="search" action="${searchLink}" method="get">
        <div class="common-search__inner">
            <span class="common-search__input">
                <input type="text" class="common-search__input__field" id="query" name="query" aria-label="Search box" placeholder="${ghostText}" value="${query!""}">
            </span>
            <input type="submit" class="common-search__submit" id="btnSearch" value="Search">
        </div>
    </form>
</section>
