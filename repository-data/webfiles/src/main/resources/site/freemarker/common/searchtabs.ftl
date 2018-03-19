<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<@hst.setBundle basename="month-names,facet-headers,facet-labels,searchtab-labels"/>

<div data-uipath="ps.search-tabs">
    <#assign tabLink = searchLink + query???then('?query=${query}&', '?') + 'sort=${sort}&' +'area=' >
    
    <ul class="tabs-nav" role="tablist">
        <#list tabs as tab>    
            <li class="${(area=='${tab}')?string("active", "")}"><a tabindex="${tab?index}" href="${tabLink}${tab}" title="<@fmt.message key="searchtab.${tab}"/>"><@fmt.message key="searchtab.${tab}"/></a></li>
        </#list>
    </ul>
</div>