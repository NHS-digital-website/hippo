<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="search-banner.placeholder" var="placeholderText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>

<form role="search" method="get" action="${searchLink}" class="search-strip">
    <div class="search-strip__contents">
        <div class="search-strip__table-cell">
            <input type="text" name="query" id="query" class="search-strip__input" placeholder="${buttonLabel}" value="${query!""}" title="${buttonLabel}">
        </div>
        <div class="search-strip__table-cell search-strip__table-cell--button">
            <button class="search-strip__button">
                <img src="<@hst.webfile path="/images/icon-search-white.png"/>" alt="Magnifying glass" />
            </button>
        </div>
    </div>
</form>
