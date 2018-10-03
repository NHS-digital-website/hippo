<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="month-names,facet-headers,facet-labels,searchtab-labels"/>

<div data-uipath="ps.search-tabs" class="js-tabs">
    <#assign tabLink = searchLink + query???then('?query=${query}&', '?') + 'sort=${sort}&' +'area=' >

    <ul class="tabs-nav" role="tablist">
        <#list tabs as tab>
            <li class="tabs-nav__item ${(area=='${tab}')?string("tabs-nav__item--active", "")}">
                <a tabindex="${tab?index}" href="${tabLink}${tab}" class="tabs-link" title="<@fmt.message key="searchtab.${tab}" /> results" role="tab"><@fmt.message key="searchtab.${tab}"/></a>
            </li>
        </#list>
    </ul>
</div>
