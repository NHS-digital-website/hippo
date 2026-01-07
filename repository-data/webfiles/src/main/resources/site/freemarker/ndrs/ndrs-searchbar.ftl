<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="ndrs.website.labels"/>
<@fmt.message key="search-banner.placeholder" var="placeholderText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>

<section class="search-banner search-banner--short search-banner--ndrs" aria-label="Search form">
    <div class="nhsd-t-grid">
        <div class="grid-row">
            <div class="column column--reset">
                <form role="search"
                      method="get"
                      action="${searchLink}"
                      class="search-banner__form"
                      id="ndrs-search"
                      aria-label="${searchTitle}">
                    <div class="search-banner__input-wrapper">
                        <input type="search"
                               class="search-banner__input"
                               id="ndrs-search-query"
                               name="query"
                               aria-label="${buttonLabel}"
                               placeholder="${placeholderText}"
                               value="${query!""}">
                        <label for="ndrs-search-query" class="visually-hidden">${buttonLabel}</label>
                    </div>
                    <div class="search-banner__button-wrapper">
                        <button class="search-banner__button"
                                data-uipath="search.button"
                                type="submit"
                                value="Search"
                                id="ndrs-search-button">${buttonLabel}</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <#-- Search Event Pixel -->
    <script>
        document.getElementById("ndrs-search-button").addEventListener("click", ndrsSearchEvent);
        document.getElementById("ndrs-search-query").addEventListener("click", ndrsSearchEvent);
        function ndrsSearchEvent() {
            var searchData = {};
            searchData["q"] = document.querySelector("#ndrs-search-query").value;
            searchData["catalogs"] = [{ "name" : "content_en" }];
            BrTrk.getTracker().logEvent("suggest", "submit", searchData, {}, true);
        }
    </script>
</section>
