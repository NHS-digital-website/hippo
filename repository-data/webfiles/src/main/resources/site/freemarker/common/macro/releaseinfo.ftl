<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.ReleaseInfo" -->

<#include "../../include/imports.ftl">

  <#macro releaseinfo section>
    <div class="releaseinfo--div">
      <h3>${section.version}</h3>
    </div>
  </#macro>
