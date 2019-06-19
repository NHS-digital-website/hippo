<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Biography" -->

<#include "../../include/imports.ftl">

  <#macro biography section>
    <div class="biography--div">
      <h3>${section.profbiography}</h3>
      <h3>${section.prevpositions}</h3>
      <h3>${section.nonnhspostions}</h3>
      <h3>${section.additionalbiography}</h3>
      <h3>${section.personalbiography}</h3>
    </div>
  </#macro>
