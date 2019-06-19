<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.PersonImage" -->

<#include "../../include/imports.ftl">

  <#macro personimage section>
    <div class="personimage--div">
      <h3>${section.imagesourcepermission}</h3>
    </div>
  </#macro>
