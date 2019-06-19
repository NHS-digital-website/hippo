<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Responsibility" -->

<#include "../../include/imports.ftl">

  <#macro biography section>
    <div class="responsibility--div">
      <h3>${section.responsible}</h3>
      <h3>${section.responsibleforservice}</h3>
    </div>
  </#macro>
