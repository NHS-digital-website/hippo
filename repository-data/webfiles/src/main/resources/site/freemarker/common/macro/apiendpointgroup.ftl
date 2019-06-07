<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.ApiEndpointGroup" -->

<#include "../../include/imports.ftl">

  <#macro apiendpointgroup section>
    <div class="apiendpointgroup--div">
      <h3>${section.title}</h3>
    </div>
  </#macro>
