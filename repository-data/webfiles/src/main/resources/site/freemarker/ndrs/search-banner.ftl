<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="search-banner.placeholder" var="placeholderText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>
<@fmt.message key="search-banner.text" var="searchBannerText"/>

<section class="search-banner" aria-label="Search form">
    <div class="grid-wrapper grid-wrapper--collapse">
        <h1 class="search-banner__headline">${searchBannerText}</h1>
        <form role="search" method="get" action="${searchLink}" class="search-banner__form" id="search" aria-label="${searchTitle}">
            <div>
                <input type="search" name="query" id="query" class="search-banner__input" placeholder="${placeholderText}" value="${query!""}" aria-label="${buttonLabel}">
                <label for="query" class="visually-hidden">${buttonLabel}</label>
            </div>
            <div>
                <button class="search-banner__button" id="search_button_banner" data-uipath="search.button">${buttonLabel}</button>
            </div>
        </form>
    </div>
    <#-- Search Event Pixel -->
     <script>
        document.getElementById("search_button_banner").addEventListener("click", myFunction);
        document.getElementById("query").addEventListener("click", myFunction);
        function myFunction() {
            var searchData = {};
            searchData["q"] = document.querySelector("#query").value;;
            searchData["catalogs"] = [{ "name" : "content_en" }];
            BrTrk.getTracker().logEvent("suggest", "submit",searchData,{},true);
        }
     </script>
</section>
