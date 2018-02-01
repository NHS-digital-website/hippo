<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<!--Need to have a single setBundle call as subsequent ones will overwrite the previous values-->
<@hst.setBundle basename="document-types,month-names,publicationsystem.labels"/>

<div class="layout layout--flush">
    <div class="layout__item layout-1-2">
        <h3>Filter by:</h3>
    </div><!--
    --><div class="layout__item layout-1-2" style="text-align:right">
        <@hst.link siteMapItemRefId="search" var="searchLink" navigationStateful=true/>
        <a href="${searchLink}" title="Clear all">Clear all</a>
    </div>
</div>
<#if facets??>
    <#list facets.folders as facet>
        <#if facet.folders?has_content>
            <h3>${facet.name}</h3>
            <ul class="filter-list" title="${facet.name}">
                <#list facet.folders as value>
                    <#if value.count &gt; 0>
                        <#if facet.name="MONTH">
                            <@fmt.message key=value.name var="monthName"/>
                            <#assign valueName=monthName/>
                        <#elseif facet.name="CATEGORY">
                            <#assign valueName=taxonomy.getValueName(value.name)/>
                        <#elseif facet.name="DOCUMENT TYPE">
                            <@fmt.message key=value.name var="documentTypeKey"/>
                            <@fmt.message key=documentTypeKey var="valueName"/>
                        <#else>
                            <#assign valueName=value.name/>
                        </#if>
                        <#if value.leaf>
                            <@hst.facetnavigationlink var="link" remove=value current=facets />
                            <li class="filter-list__item"><a href="${link}" title="${valueName}" class="filter-list__item__link">${valueName} x<i class="fa fa-times"> </i></a></li>
                        <#else>
                            <@hst.link var="link" hippobean=value navigationStateful=true/>
                            <li class="filter-list__item"><a href="${link}" title="${valueName}" class="filter-list__item__link">${valueName}&nbsp;(${value.count})</a></li>
                        </#if>
                    </#if>
                </#list>
            </ul>
        </#if>
    </#list>
</#if>
