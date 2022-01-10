<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.OrgStructure" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/contentPixel.ftl">
<#include "macro/heroes/hero-options.ftl">
<#include "macro/heroes/hero.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasIntroduction = document.introduction.content?has_content />
<#assign isNavigationEmbedTrue = document.navigationembed />
<#assign hasDirectorates = document.directorates?has_content && document.directorates?size gt 0 />
<#assign renderNav = hasIntroduction || isNavigationEmbedTrue || hasDirectorates />

<#assign ourStructureText  = "Our structure" />
<#assign introductionText = "Introduction" />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article >
  <#assign pageIcon = '' />
  <#if document.bannercontrols?has_content && document.bannercontrols.icon?has_content>
    <#assign pageIcon = document.bannercontrols.icon />
  </#if>

  <@hero getHeroOptions(document) />

  <div class="nhsd-t-grid nhsd-!t-margin-top-8">
    <div class="nhsd-t-row">
      <#if renderNav>
      <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4">
        <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
        <#if hasIntroduction>
        <#assign links += [{ "url": "#introduction", "title": introductionText }] />
        </#if>
        <#if isNavigationEmbedTrue>
          <#assign links += [{ "url": "#our-structure", "title": ourStructureText }] />
        </#if>
        <#list document.directorates as directorate>
          <#if directorate.mainbusinessunit?has_content && directorate.mainbusinessunit.businessunit?has_content>
            <#assign links += [{ "url": "#" + slugify(directorate.mainbusinessunit.businessunit.title), "title": directorate.mainbusinessunit.businessunit.title }] />
          </#if>
        </#list>
        <@stickyNavSections links />
      </div>
      </#if>

      <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">

        <#if hasIntroduction>
          <div id="introduction" data-uipath="website.orgstructure.introduction">
            <h2 class="nhsd-t-heading-xl" >${introductionText}</h2>
            <@hst.html hippohtml=document.introduction contentRewriter=brContentRewriter />
          </div>
        </#if>

        <div id="our-structure" data-uipath="website.orgstructure.ourstructure">
          <h2 class="nhsd-t-heading-xl" >${ourStructureText}</h2>
        </div>

        <#if isNavigationEmbedTrue>

          <div class="nhsd-orgstructure-container-static nhsd-!t-margin-bottom-6">

            <!-- for now Owain suggested to implement this as fixed structure as on the picture of DW-1060 & DW-2392 -->
            <#assign i = 1 />

            <#if hasDirectorates>

              <!-- only first 8 items will be drawn as we have a fixed structure for now - see comment above -->
              <#list document.directorates as directorate>

                <#assign directorateStyle='' />
                <#if directorate.backgroundcolor?has_content>
                <#assign directorateStyle = 'style=background-color:${directorate.backgroundcolor}' />
                </#if>

                <#assign linkColour = "" >
                <#-- Mark has recommended which colours should have white or black links. See comment on DW-2392 -->
                <#if directorate.backgroundcolor == "#FA9200" || directorate.backgroundcolor == "#FAE100" >
                  <#assign linkColour = "nhsd-a-link--col-black">
                <#else >
                  <#assign linkColour = "nhsd-a-link--col-white">
                </#if>

                <#if directorate.fontcolor?has_content>
                  <#assign directorateStyle = '${directorateStyle};color:${directorate.fontcolor}' />
                </#if>

                <#if i <= 8 >
                  <#if i == 6 >
                    <div class="nhsd-orgstructure-custom-container" >
                  </#if>
                    <div class="nhsd-a-box nhsd-orgstructure-block nhsd-orgstructure-block-${i}" ${directorateStyle}>
                      <#if directorate.mainbusinessunit?has_content && directorate.mainbusinessunit.businessunit?has_content>
                        <span><a class="nhsd-a-link ${linkColour}" href="#${slugify(directorate.mainbusinessunit.businessunit.title)}" >
                          ${directorate.mainbusinessunit.businessunit.title}
                        </a></span>
                      <#else>
                        <span>&nhsp;</span>
                      </#if>
                    </div>
                  <#if i == 7 >
                    </div>
                  </#if>
                </#if>

                <#if i &gt; 8 >
                  <div class="nhsd-a-box nhsd-orgstructure-block nhsd-orgstructure-block-extra" ${directorateStyle}>
                    <#if directorate.mainbusinessunit?has_content && directorate.mainbusinessunit.businessunit?has_content>
                      <span><a class="nhsd-a-link ${linkColour}" href="#${slugify(directorate.mainbusinessunit.businessunit.title)}" >
                        ${directorate.mainbusinessunit.businessunit.title}
                      </a></span>
                    <#else>
                      <span>&nhsp;</span>
                    </#if>
                  </div>
                </#if>

                <#assign i = i+1 />
              </#list>
            </#if>

            <#if i <= 7 >
              <#list i..7 as blockNr>
                <div class="orgstructure-block orgstructure-block-${blockNr}">
                  <span>&nbsp;</span>
                </div>
              </#list>
            </#if>
            </div>

          </#if>

          <#if hasDirectorates>
            <#list document.directorates as directorate>
              <#assign directorateStyle='' />
              <#if directorate.backgroundcolor?has_content>
                <#assign directorateStyle = 'style=background-color:${directorate.backgroundcolor}' />
                <#assign linkColour = "" >
                <#if directorate.backgroundcolor == "#FA9200" || directorate.backgroundcolor == "#FAE100" >
                  <#assign linkColour = "nhsd-a-link--col-black">
                <#else >
                  <#assign linkColour = "nhsd-a-link--col-white">
                </#if>
              </#if>

              <#if directorate.fontcolor?has_content>
                <#assign directorateStyle = '${directorateStyle};color:${directorate.fontcolor}' />
              </#if>

              <div class="nhsd-a-box nhsd-!t-margin-bottom-6" ${directorateStyle}>
                <#if directorate.mainbusinessunit?has_content>

                  <#if directorate.mainbusinessunit.businessunit?has_content>
                    <h4 class="nhsd-t-heading-m" id="${slugify(directorate.mainbusinessunit.businessunit.title)}" ${directorateStyle} >${directorate.mainbusinessunit.businessunit.title}</h4>
                    <p class="nhsd-t-body nhsd-!t-margin-top-3" >${directorate.mainbusinessunit.businessunit.shortsummary}</p>
                    <a class="nhsd-a-button nhsd-a-button--invert ${(directorate.backgroundcolor == "#FAE100")?then("nhsd-a-button--custom","")}" style="max-width: 100%" href="<@hst.link hippobean=directorate.mainbusinessunit.businessunit/>">
                      <span class="nhsd-a-button__label">${directorate.mainbusinessunit.businessunit.title}</span>
                    </a>
                  </#if>
                  <div class="nhsd-!t-margin-bottom-3">
                  <#if directorate.mainbusinessunit.responsibleperson?has_content>
                    <#assign aiText = directorate.mainbusinessunit.interimappointment?then('(interim)', '') />
                      <article class="nhsd-m-card ${(directorate.mainbusinessunit.responsibleperson.personimages.picture.original?has_content)?then("nhsd-m-card--image-position-adjacent", "")}">
                      <a href="<@hst.link hippobean=directorate.mainbusinessunit.responsibleperson/>" class="nhsd-a-box-link nhsd-a-box-link--focus-orange" aria-label="${directorate.mainbusinessunit.responsibleperson.title}" >
                        <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                          <#if directorate.mainbusinessunit.responsibleperson.personimages.picture.original?has_content>
                           <div class="nhsd-m-card__image_container">
                           <@hst.link var="image" hippobean=directorate.mainbusinessunit.responsibleperson.personimages.picture.original fullyQualified=true />
                             <figure class="nhsd-a-image nhsd-a-image--cover">
                              <picture class="nhsd-a-image__picture ">
                                <img src="${image}" alt="${directorate.mainbusinessunit.responsibleperson.title}">
                              </picture>
                            </figure>
                          </div>
                          </#if>
                          <div class="nhsd-m-card__content_container">
                            <div class="nhsd-m-card__content-box">
                              <div class="nhsd-m-card__tag-list">
                                <span class="nhsd-a-tag nhsd-a-tag--bg-dark-grey">Responsible Person</span>
                              </div>
                              <#if directorate.mainbusinessunit.responsibleperson.title?has_content >
                                <span class="nhsd-t-heading-s">${directorate.mainbusinessunit.responsibleperson.title}</span>
                                </#if>
                                <#if directorate.mainbusinessunit.responsibleperson.shortsummary?has_content >
                                <p class="nhsd-t-body-s">${directorate.mainbusinessunit.responsibleperson.shortsummary}</p>
                                </#if>
                            </div>
                            <div class="nhsd-m-card__button-box">
                              <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                  <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                </svg>
                              </span>
                            </div>
                          </div>
                        </div>
                      </a>
                    </article>
                  <#else>
                    <span class="nhsd-t-body">Responsible person: Vacancy</span>
                  </#if>
                  </div>
                </#if>

                <#list directorate.componentbusinessunits as subbusinessunit>
                  <#assign subitemStyle='' />
                  <#if directorate.embedbackgroundcolor?has_content>
                    <#assign subitemStyle = 'style=background-color:${directorate.embedbackgroundcolor}' />
                  </#if>

                  <div class="nhsd-a-box ${(subbusinessunit?is_last)?then("", "nhsd-!t-margin-bottom-5")}" ${subitemStyle}>
                    <#if subbusinessunit.businessunit?has_content>
                    <article class="nhsd-m-card">
                      <a href="<@hst.link hippobean=subbusinessunit.businessunit/>" class="nhsd-a-box-link " aria-label="${subbusinessunit.businessunit.title}" >
                        <div class="nhsd-a-box nhsd-a-box--bg-white nhsd-a-box--border-grey">
                          <div class="nhsd-m-card__content_container">
                            <div class="nhsd-m-card__content-box">
                              <div class="nhsd-m-card__tag-list">
                                <span class="nhsd-a-tag nhsd-a-tag--bg-dark-grey">Business Unit</span>
                              </div>
                              <span class="nhsd-t-heading-s">${subbusinessunit.businessunit.title}</span>
                              <p class="nhsd-t-body-s">${subbusinessunit.businessunit.shortsummary}</p>
                              </div>
                            <div class="nhsd-m-card__button-box">
                              <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                  <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                </svg>
                              </span>
                            </div>
                          </div>
                        </div>
                      </a>
                    </article>
                    </#if>
                    <div class="nhsd-!t-margin-top-3">
                      <#if subbusinessunit.responsibleperson?has_content>
                        <#assign aiText = subbusinessunit.interimappointment?then('(interim)', '') />
                        <span class="nhsd-t-body">Responsible person: </span>
                        <a class="nhsd-a-link" href="<@hst.link hippobean=subbusinessunit.responsibleperson/>">${subbusinessunit.responsibleperson.title}</a> ${aiText}
                      <#else>
                        <span class="nhsd-t-body">Responsible person: Vacancy</span>
                      </#if>
                    </div>
                  </div>
                </#list>
              </div>
            </#list>
          </#if>

          <@lastModified document.lastModified false />
      </div>
    </div>
  </div>
</article>
