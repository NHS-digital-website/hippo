<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Role" -->

<#include "../../include/imports.ftl">
<#include "otherrole.ftl">

  <#macro role section>
    <div class="role--div">
      <h3>${section.primaryrole}</h3>
    </div>
  </#macro>
