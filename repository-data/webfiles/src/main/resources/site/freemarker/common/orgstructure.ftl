<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.OrgStructure" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasIntroduction = document.introduction.content?has_content />
<#assign isNavigationEmbedTrue = document.navigationembed />
<#assign hasDirectorates = document.directorates?has_content && document.directorates?size gt 0 />
<#assign renderNav = hasIntroduction || isNavigationEmbedTrue || hasDirectorates />

<#assign ourStrctureText  = "Our structure" />
<#assign introductionText = "Introduction" />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article class="article article--orgstructure">

    <#assign pageIcon = '' />
    <#if document.bannercontrols?has_content && document.bannercontrols.icon?has_content>
      <#assign pageIcon = document.bannercontrols.icon />
    </#if>
    <@documentHeader document 'orgstructure' pageIcon></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
                    <#if hasIntroduction>
                    <#assign links += [{ "url": "#introduction", "title": introductionText }] />
                    </#if>
                    <#if isNavigationEmbedTrue>
                      <#assign links += [{ "url": "#ourstructure", "title": ourStrctureText }] />
                    </#if>
                    <#list document.directorates as directorate>
                      <#if directorate.mainbusinessunit?has_content && directorate.mainbusinessunit.businessunit?has_content>
                        <#assign links += [{ "url": "#" + slugify(directorate.mainbusinessunit.businessunit.title), "title": directorate.mainbusinessunit.businessunit.title }] />
                      </#if>
                    </#list>
                    <@stickyNavSections links></@stickyNavSections>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

                <#if hasIntroduction>
                  <div id="introduction" class="article-section" data-uipath="website.orgstructure.introduction">
                      <h2>${introductionText}</h2>
                      <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter />
                  </div>
                </#if>

                <div id="ourstructure" class="article-section" data-uipath="website.orgstructure.ourstructure">
                    <h2>${ourStrctureText}</h2>
                </div>

                <#if isNavigationEmbedTrue>

                  <div class="orgstructure-container">

                    <!-- for now Owain suggested to implement this as fixed structure as on the picture of DW-1060 -->
                    <#assign i = 1 />

                    <#if hasDirectorates>

                        <!-- only first 7 items will be drawn as w have fixed structure for now - see comment above -->
                        <#list document.directorates as directorate>

                          <#assign directorateStyle='' />
                          <#if directorate.backgroundcolor?has_content>
                            <#assign directorateStyle = 'style=background-color:${directorate.backgroundcolor}' />
                          </#if>

                          <#if directorate.fontcolor?has_content>
                            <#assign directorateStyle = '${directorateStyle};color:${directorate.fontcolor}' />
                          </#if>

                          <#if i <= 7 >
                              <div class="orgstructure-block orgstructure-block-${i}" ${directorateStyle}>
                                <#if directorate.mainbusinessunit?has_content && directorate.mainbusinessunit.businessunit?has_content>
                                  <span><a href="#${slugify(directorate.mainbusinessunit.businessunit.title)}" >
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
                      </#if>

                      <#if directorate.fontcolor?has_content>
                        <#assign directorateStyle = '${directorateStyle};color:${directorate.fontcolor}' />
                      </#if>

                      <div class="article-section orgstructure-item" ${directorateStyle}>
                        <#if directorate.mainbusinessunit?has_content>
                          <#if directorate.mainbusinessunit.businessunit?has_content>
                            <h3 id="${slugify(directorate.mainbusinessunit.businessunit.title)}" class="orgstructure-title sticky-nav-spy-item">
                              <a href="<@hst.link hippobean=directorate.mainbusinessunit.businessunit/>">${directorate.mainbusinessunit.businessunit.title}</a>
                            </h3>
                            <div class="orgstructure-summary">
                                <p>${directorate.mainbusinessunit.businessunit.shortsummary}</p>
                            </div>
                          </#if>
                          <div class="orgstructure-person">
                            <#if directorate.mainbusinessunit.responsibleperson?has_content>
                                <#assign aiText = directorate.mainbusinessunit.interimappointment?then('(interim)', '') />
                                Responsible person: <a class="orgstructure-person-bold" href="<@hst.link hippobean=directorate.mainbusinessunit.responsibleperson/>">${directorate.mainbusinessunit.responsibleperson.title}</a> ${aiText}
                            <#else>
                                Responsible person: <span>Vacancy</span>
                            </#if>
                          </div>
                        </#if>
                        <#list directorate.componentbusinessunits as subbusinessunit>

                          <#assign subitemStyle='' />
                          <#if directorate.embedbackgroundcolor?has_content>
                            <#assign subitemStyle = 'style=background-color:${directorate.embedbackgroundcolor}' />
                          </#if>

                          <div class="orgstructure-subitem" ${subitemStyle}>
                            <#if subbusinessunit.businessunit?has_content>
                              <h4 class="orgstructure-subtitle">
                                <a href="<@hst.link hippobean=subbusinessunit.businessunit/>">${subbusinessunit.businessunit.title}</a>
                              </h4>
                            </#if>
                            <div class="orgstructure-subperson">
                              <#if subbusinessunit.responsibleperson?has_content>
                                  <#assign aiText = subbusinessunit.interimappointment?then('(interim)', '') />
                                  <a href="<@hst.link hippobean=subbusinessunit.responsibleperson/>">${subbusinessunit.responsibleperson.title}</a> ${aiText}
                              <#else>
                                  <span>Vacancy</span>
                              </#if>
                            </div>
                          </div>

                        </#list>
                      </div>
                    </#list>
                </#if>


                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>
