<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<!--Need to have a single setBundle call as subsequent ones will overwrite the previous values-->
<@hst.setBundle basename="document-types,month-names"/>

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
                      <#assign valueName=taxonomy.getCategoryByKey(value.name).getInfo("en").name/>
                  <#elseif facet.name="DOCUMENT TYPE">
                      <@fmt.message key=value.name var="documentType"/>
                      <#assign valueName=documentType/>
                  <#else>
                      <#assign valueName=value.name/>
                  </#if>
                  <#if value.leaf>
                      <@hst.facetnavigationlink var="link" remove=value current=facets />
                      <li class="filter-list__item"><a href="${link}" title="${valueName}" class="filter-list__item__link">${valueName} x<i class="fa fa-times"> </i></a></li>
                  <#else>
                      <@hst.link var="link" hippobean=value />
                      <li class="filter-list__item"><a href="${link}?query=${query}" title="${valueName}" class="filter-list__item__link">${valueName}&nbsp;(${value.count})</a></li>
                  </#if>
              </#if>
          </#list>
      </ul>

    </#if>
  </#list>
</#if>
