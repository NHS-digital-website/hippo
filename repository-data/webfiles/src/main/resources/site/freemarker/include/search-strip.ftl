<#ftl output_format="HTML">
<#include "imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="search-banner.placeholder" var="placeholderText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>

<#assign actionProp = "" />
<#if searchLink?has_content>
    <#assign actionProp = "action=${searchLink}" />
</#if>

<form role="search" method="get" ${actionProp} class="search-strip" aria-label="${searchTitle}" id="${searchFormId?has_content?then(searchFormId, 'search')}" autocomplete="off">
    <div class="search-strip__contents">
        <div class="search-strip__table-cell">
            <input type="text" name="query" id="${searchId?has_content?then(searchId, 'query')}" class="search-strip__input" placeholder="${buttonLabel}" value="${query!""}" aria-label="${buttonLabel}">
            <label for="${searchId?has_content?then(searchId, 'query')}" class="visually-hidden">${buttonLabel}</label>
        </div>
        <div class="search-strip__table-cell search-strip__table-cell--button">
            <button data-uipath="search.button" class="search-strip__button" aria-label="${buttonLabel}">
                <img src="<@hst.webfile path="/images/icon-search-white.png"/>" alt="Magnifying glass" class="search-strip__icon" aria-hidden="true" />

                <img src="<@hst.webfile  path="images/icon-loading.svg"/>" alt="Spinning circle graphic" class="search-strip__icon search-strip__icon--loading" aria-hidden="true" />
            </button>
        </div>
    </div>
</form>
