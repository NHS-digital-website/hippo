<#include "../include/imports.ftl">
<@hst.setBundle basename="month-names"/>
<@hst.setBundle basename="document-types"/>

<#if facets??>
  <#list facets.folders as facet>
    <#if facet.folders?has_content>

      <h3>${facet.name?html}</h3>
      <ul class="filter-list">
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
                      <li class="filter-list__item"><a href="${link}" class="filter-list__item__link">${valueName?html} x<i class="fa fa-times"> </i></a></li>
                  <#else>
                      <@hst.link var="link" hippobean=value />
                      <li class="filter-list__item"><a href="${link}?query=${query}" class="filter-list__item__link">${valueName}&nbsp;(${value.count})</a></li>
                  </#if>
              </#if>
          </#list>
      </ul>

    </#if>
  </#list>
</#if>
