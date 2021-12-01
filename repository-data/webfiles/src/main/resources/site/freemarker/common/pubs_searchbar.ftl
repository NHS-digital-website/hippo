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
        <form role="search" method="get" action="${searchLink}" class="search-banner__form" id="search" aria-label="${searchTitle}">
            <div>
                <input type="search" name="query" id="query" class="search-banner__input" placeholder="${ghostText}" value="${query!""}" aria-label="${buttonLabel}">
                <label for="query" class="visually-hidden">${buttonLabel}</label>
            </div>
            <div>
                <button class="search-banner__button" data-uipath="search.button" value="Search" id="search_button_bar">${buttonLabel}</button>
            </div>
        </form>
    </div>
    <#-- Search Event Pixel -->
     <script>
        document.getElementById("search_button_bar").addEventListener("click", myFunction);
        document.getElementById("query").addEventListener("click", myFunction);
        function myFunction() {
            var searchData = {};
            searchData["q"] = document.querySelector("#query").value;;
            searchData["catalogs"] = [{ "name" : "content_en" }];
            BrTrk.getTracker().logEvent("suggest", "submit",searchData,{},true);
        }
     </script>
</section>
