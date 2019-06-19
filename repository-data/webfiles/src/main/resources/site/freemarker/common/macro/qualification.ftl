<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Qualification" -->

<#include "../../include/imports.ftl">

  <#macro qualification section>
    <div class="qualification--div">
      <h3>${section.qualname}</h3>
    </div>
  </#macro>
