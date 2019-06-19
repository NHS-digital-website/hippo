<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.ContactDetail" -->

<#include "../../include/imports.ftl">

  <#macro contactdetail section>
    <div class="contactdetail--div">
      <h3>${section.email}</h3>
      <h3>${section.phone}</h3>
    </div>
  </#macro>
