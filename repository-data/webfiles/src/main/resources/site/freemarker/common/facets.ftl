<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<!--Need to have a single setBundle call as subsequent ones will overwrite the previous values-->
<@hst.setBundle basename="month-names,facet-headers,facet-labels"/>
<#assign facetMaxCount=6/>

<div class="layout layout--flush">
    <div class="layout__item layout-1-2">
        <h3 class="flush">Filter by:</h3>
    </div><!--
    --><div class="layout__item layout-1-2" style="text-align:right">
        <@hst.link siteMapItemRefId="search" var="searchLink" navigationStateful=true/>
        <a href="${searchLink}" title="Reset">Reset</a>
    </div>
</div>
<#if facets??>
    <#list facets.folders as facet>
        <#if facet.folders?has_content>
            <#assign unselectedItems = [] />
            <#assign selectedItems = [] />
            <#list facet.folders as value>
                <#if value.count &gt; 0>
                    <#if value.leaf>
                        <#assign selectedItems = selectedItems + [ value ] />
                    <#else>
                        <#assign unselectedItems = unselectedItems + [ value ] />
                    </#if>
                </#if>
            </#list>
            <#assign facetItems = selectedItems + unselectedItems />

            <h3 class="filter-list-title"><@fmt.message key=facet.name /></h3>
            <ul class="filter-list" title="<@fmt.message key=facet.name />" data-max-count="${facetMaxCount}" data-state="short">
                <#list facetItems as value>
                    <#assign valueName="Not Defined"/>
                    <#if facet.name="month">
                        <@fmt.message key=value.name var="monthName"/>
                        <#assign valueName=monthName/>
                    <#elseif facet.name="category">
                        <#assign valueName=taxonomy.getValueName(value.name)/>
                    <#elseif facet.name="document-type">
                        <@fmt.message key="facet."+value.name var="docType"/>
                        <#assign valueName=docType/>
                    <#elseif facet.name="assuredStatus">
                        <#assign valueName=value.name?boolean?then('Yes','No')/>
                    <#elseif facet.name="publicationStatus">
                        <@fmt.message key="facet." + value.name?boolean?then("liveStatus", "upcomingStatus") var="publicationStatus"/>
                        <#assign valueName=publicationStatus/>
                    <#else>
                        <#assign valueName=value.name/>
                    </#if>

                    <#if value?index &gt;= facetMaxCount >
                        <#assign displayStyle="none"/>
                    <#else>
                        <#assign displayStyle="block"/>
                    </#if>

                    <#if value.leaf>
                        <@hst.facetnavigationlink var="link" remove=value current=facets />
                        <li class="filter-list__item" style="display: ${displayStyle}"><a href="${link}" title="${valueName}" class="filter-list__item__link filter-list__item__link--active">${valueName}</a></li>
                    <#else>
                        <@hst.link var="link" hippobean=value navigationStateful=true/>
                        <li class="filter-list__item" style="display: ${displayStyle}"><a href="${link}" title="${valueName}" class="filter-list__item__link">${valueName}&nbsp;(${value.count})</a></li>
                    </#if>
                </#list>

                <#if facetItems?size &gt; facetMaxCount >
                    <div>
                        <a href='#' class='filter-list__expand' onclick='toggleHandler(event)'>Show all (${facetItems?size})</a>
                        <a href='#' class='filter-list__expand' style="display: none" onclick='toggleHandler(event)'>Show less (${facetMaxCount})</a>
                    </div>
                </#if>
            </ul>
        </#if>
    </#list>
</#if>

<script>
function toggleUl(ulElement) {
    if ("full" == ulElement.dataset.state) {
        hideListItems(ulElement);
        ulElement.dataset.state = "short";
        ulElement.getElementsByClassName("filter-list__expand")[0].style.display = "block";
        ulElement.getElementsByClassName("filter-list__expand")[1].style.display = "none";
    } else {
        showListItems(ulElement);
        ulElement.dataset.state = "full"
        ulElement.getElementsByClassName("filter-list__expand")[0].style.display = "none";
        ulElement.getElementsByClassName("filter-list__expand")[1].style.display = "block";
    }
}

function hideListItems(ulElement) {
    setDisplayItems(ulElement, 'none');
}

function showListItems(ulElement) {
    setDisplayItems(ulElement, '');
}

function setDisplayItems(ulElement, display) {
    var listItems = ulElement.getElementsByClassName("filter-list__item");

    [].forEach.call(listItems, function(li, index) {
        if (index >= ulElement.dataset.maxCount) {
            li.style.display = display;
        }
    });
}

function toggleHandler(event) {
    var ulElement = event.target.parentNode.parentNode;

    toggleUl(ulElement);

    event.preventDefault();
    return true;
}
</script>
