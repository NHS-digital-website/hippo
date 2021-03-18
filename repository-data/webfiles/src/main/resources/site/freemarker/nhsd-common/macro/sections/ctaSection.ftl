<#ftl output_format="HTML">

<#include "../../../include/imports.ftl">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.CtaButton" -->

  <#macro ctaSection section>
    <#list section.items as ctalink>

        <#if slugify(section.buttonType) == "nhs-digital-button">
          <#assign typeOfButton = ""/>
        <#elseif slugify(section.buttonType) == "journey-start-button">
          <#assign typeOfButton = "--start"/>
        <#elseif slugify(section.buttonType) == "secondary-button">
          <#assign typeOfButton = "--outline"/>
        </#if>

        <#if ctalink.linkType == "internal">
        <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, ctalink.link.title) />
          <nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed nhsd-!t-text-align-${slugify(section.alignment)} nhsd-!t-margin-bottom-6">
            <a class="nhsd-a-button nhsd-a-button${typeOfButton}" 
                href="<@hst.link hippobean=ctalink.link />"
                id="nhsd-a-button-${slugify(section.title)}" 
                onClick="${onClickMethodCall}"
                onKeyUp="return vjsu.onKeyUp(event)">
              <span class="nhsd-a-button__label">${section.title}</span>
            </a>
          </nav>
        <#elseif ctalink.linkType == "external">
          <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, ctalink.link) />
          <nav class="nhsd-m-button-nav nhsd-!t-text-align-${slugify(section.alignment)} nhsd-!t-margin-bottom-6">
            <a class="nhsd-a-button nhsd-a-button${typeOfButton}" 
                href="${ctalink.link}" 
                id="nhsd-a-button-${slugify(section.title)}" 
                onClick="${onClickMethodCall}"
                onKeyUp="return vjsu.onKeyUp(event)"
                target="_blank" 
                rel="external">
              <span class="nhsd-a-button__label">${section.title}</span>
              <span class="nhsd-t-sr-only">(external link, opens in a new tab)</span>
            </a>
          </nav>
        </#if>
    </#list>
  </#macro>
