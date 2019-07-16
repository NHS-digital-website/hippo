<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

  <#macro personrole role idprefix firstname>
    <#if role?has_content && role.primaryrole?has_content>
      <div id="role-${slugify(idsuffix)}"class="role--div article-section no-border">
        <div class="callout callout--attention">
          <h2>Role</h2> 
          <p data-uipath="person.role.primaryrole">
          <#if primaryroleorg?has_content> 
            ${firstname} is the <span itemprop="jobTitle">${role.primaryrole}</span> of <span itemprop="worksFor" itemscope itemtype="https://schema.org/Organization"><span itemprop="name">${role.primaryroleorg}</span></span>.
          <#else>
            ${firstname} is the <span itemprop="jobTitle">${role.primaryrole}</span>.
          </#if>
          </p>
        </div>
      </div>
    </#if>
  </#macro>
