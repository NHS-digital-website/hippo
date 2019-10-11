<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

  <#macro personrole role idprefix>
    <#if role?has_content && role.primaryrole?has_content>
        <div class="article-header__label">
          <#if role.primaryroleorg?has_content>
            <span itemprop="jobTitle">${role.primaryrole}</span> at <span itemprop="worksFor" itemscope itemtype="https://schema.org/Organization"><span itemprop="name">${role.primaryroleorg}</span></span>
          <#else>
            <span itemprop="jobTitle">${role.primaryrole}</span>
          </#if>
        </div>
    </#if>
  </#macro>
