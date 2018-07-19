<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.labels,homepage.website.labels"/>
<@fmt.message key="labels.search-ghost-text" var="ghostText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>

<section class="common-search" aria-label="Search form">
    <form class="navbar-form" role="search" action="${searchLink}" method="get" id="search" aria-label="${searchTitle}">
        <div class="common-search__inner">
            <span class="common-search__input">
                <input type="text" class="common-search__input__field" id="query" name="query" aria-label="${buttonLabel}" placeholder="${ghostText}" value="${query!""}">
                <label for="query" class="visually-hidden">${buttonLabel}</label>
            </span>
            <input type="submit" class="common-search__submit" data-uipath="search.button" value="${buttonLabel}">
        </div>
    </form>
</section>
