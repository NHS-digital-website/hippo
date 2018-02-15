<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#if pageable?? && pageable.items?has_content>
  <ul>
    <#list pageable.items as item>
      <li>
        <#if hstRequestContext.preview>
            <div style="position:relative">
                <@hst.cmseditlink hippobean=item />
            </div>
        </#if>
        <h3>${item.title}</h3>
        <p><@hst.html hippohtml=item.content/></p>
      </li>
    </#list>
  </ul>
</#if>
