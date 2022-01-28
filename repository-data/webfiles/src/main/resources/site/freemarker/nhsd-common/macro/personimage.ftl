<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.PersonImage" -->

<#include "../../include/imports.ftl">
<#include "./responsiveImage.ftl">

  <#macro personimage image idsuffix personName="">
    <#if image?? && image?has_content && image.picture?has_content>
      <div id="personimage-${slugify(idsuffix)}" itemscope itemtype="http://schema.org/ImageObject">
        <@responsiveImage image.picture {'alt': personName} />
        <p class="nhsd-t-body" style="font-size: small;">${imagedistributiontagging[image.imagedistributiontagging]}</p>
      </div>
    </#if>
  </#macro>
