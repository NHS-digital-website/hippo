<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.labels"/>
<@fmt.message key="labels.search-ghost-text" var="ghostText"/>
<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="search-banner.placeholder" var="placeholderText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>
<@fmt.message key="search-banner.text" var="searchBannerText"/>

<section class="search-banner search-banner--short" aria-label="Search Bar">
    <div class="grid-wrapper grid-wrapper--collapse">
        <form role="search" method="get" action="${searchLink}" class="search-banner__form">
            <div>
                <input type="text" name="query" id="query" class="search-banner__input" placeholder="${ghostText}" value="${query!""}" aria-label="Search box">
            </div>
            <div>
                <button class="search-banner__button" id="btnSearch" value="Search">${buttonLabel}</button>
            </div>
        </form>
    </div>
</section>
