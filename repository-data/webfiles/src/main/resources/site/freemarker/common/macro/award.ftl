<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Award" -->

<#include "../../include/imports.ftl">

  <#macro award section>
    <div class="award--div">
      <h3>${section.awardname}</h3>
    </div>
  </#macro>
