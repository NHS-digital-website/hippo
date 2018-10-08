<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.labels"/>
<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="search-banner.placeholder" var="placeholderText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>
<@fmt.message key="search-banner.text" var="searchBannerText"/>

<section>


<article class="article article--service">
    <div class="grid-wrapper grid-wrapper--article">

        <form role="search" method="get" action="ods" id="search">
            <div>
                Name: <input type="text" name="query" id="query" value="${query!""}" aria-label="${buttonLabel}">
            </div>

            <div>
                <button class="search-banner__button" data-uipath="search.button" value="Search">${buttonLabel}</button>
            </div>
        </form>

    </div>
</article>


</section>
