<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>
<@fmt.message key="search-banner.placeholder" var="placeholderText"/>
<@fmt.message key="search-banner.buttonLabel" var="buttonLabel"/>
<@fmt.message key="search-banner.title" var="searchTitle"/>

<#assign actionProp = "" />
<#if searchLink?has_content>
    <#assign actionProp = "action=${searchLink}" />
</#if>

<form role="search" method="get" ${actionProp} class="search-strip" aria-label="${searchTitle}" id="${searchFormId?has_content?then(searchFormId, 'search')}" autocomplete="off">
    <div class="nhsd-m-search-bar nhsd-m-search-bar__small nhsd-!t-padding-0">
        <div class="nhsd-t-form-control">
            <input
                class="nhsd-t-form-input"
                type="text"
                id="query"
                name="query"
                autocomplete="off"
                placeholder="${placeholderText}"
                aria-label="Keywords"
            />
            <span class="nhsd-t-form-control__button">
                <button id="search_button" class="nhsd-a-button nhsd-a-button--circle-condensed nhsd-a-button--transparent" type="submit" aria-label="${buttonLabel}">
                    <span class="nhsd-a-icon nhsd-a-icon--size-s">
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M7,10.9c-2.1,0-3.9-1.7-3.9-3.9c0-2.1,1.7-3.9,3.9-3.9c2.1,0,3.9,1.7,3.9,3.9C10.9,9.2,9.2,10.9,7,10.9zM13.4,14.8l1.4-1.4l-3-3c0.7-1,1.1-2.1,1.1-3.4c0-3.2-2.6-5.8-5.8-5.8C3.8,1.2,1.2,3.8,1.2,7c0,3.2,2.6,5.8,5.8,5.8c1.3,0,2.4-0.4,3.4-1.1L13.4,14.8z"/></svg>
                    </span>
                </button>
            </span>
        </div>
    </div>
</form>

<#-- Search Event Pixel -->
<script type="text/javascript">
    document.getElementById("search_button").addEventListener("click", myFunction);
    document.getElementsByName("query")[0].addEventListener("click", myFunction);
    function myFunction() {
        var searchData = {};
        searchData["q"] = document.querySelector("#query").value;
        searchData["catalogs"] = [{ "name" : "content_en" }];
        BrTrk.getTracker().logEvent("suggest", "submit",searchData,{},true);
    }
</script>
