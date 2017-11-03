<#include "../include/imports.ftl">
<@hst.setBundle basename="month-names"/>
<@hst.setBundle basename="document-types"/>

<#if facets??>
<div class="hst-container">
    <div class="hst-container-item">
        <#list facets.folders as facet>
            <#if facet.folders?has_content>
                <div class="sidebar-block">
                    <h4 class="h3-sidebar-title sidebar-title">${facet.name?html}</h4>
                    <div class="sidebar-content tags">
                        <#list facet.folders as value>
                            <#if value.count &gt; 0>
                                <#if facet.name="MONTH">
                                    <@fmt.message key=value.name var="monthName"/>
                                    <#assign valueName=monthName/>
                                <#elseif facet.name="CATEGORY">
                                    <#assign valueName=taxonomy.getCategoryByKey(value.name).getInfo("en").name/>
                                <#elseif facet.name="DOCUMENT TYPE">
                                    <@fmt.message key=value.name var="documentType"/>
                                    <#assign valueName=documentType/>
                                <#else>
                                    <#assign valueName=value.name?html/>
                                </#if>
                                <#if value.leaf>
                                    <@hst.facetnavigationlink var="link" remove=value current=facets />
                                    <a href="${link}" class="remove">${valueName?html} x<i class="fa fa-times"> </i></a>
                                <#else>
                                    <@hst.link var="link" hippobean=value />
                                    <a href="${link}?query=${query}">${valueName}&nbsp;(${value.count})</a>
                                </#if>
                            </#if>
                        </#list>
                    </div>
                </div>
            </#if>
        </#list>
    </div>
</div>
</#if>
