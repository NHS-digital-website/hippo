<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.BusinessUnit" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/component/downloadBlock.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasVisionContent = document.vision.content?has_content />
<#assign hasPartOfBusinessUnit = document.ispartofbusinessunit?has_content />
<#assign hasChildren = document.children?has_content />
<#assign hasResponsibleRoleContent = document.responsiblerole?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = hasSummaryContent || hasVisionContent || hasResponsibleRoleContent || hasPartOfBusinessUnit || sectionTitlesFound gt 1 />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<article class="article article--businessunit">

    <@documentHeader document 'businessunit' ''></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
                    <#if hasVisionContent>
                      <#assign links += [{ "url": "#vision", "title": 'Vision' }] />
                    </#if>
                    <#if hasResponsibleRoleContent>
                      <#assign links += [{ "url": "#responsiblerole", "title": 'Responsible person' }] />
                    </#if>
                    <#if hasPartOfBusinessUnit>
                      <#assign links += [{ "url": "#ispartofbusinessunit", "title": 'This business unit is part of' }] />
                    </#if>
                    <#if hasChildren>
                      <#assign links += [{ "url": "#children", "title": 'Programmes of work' }] />
                    </#if>
                    <#if document.purposes?has_content>
                      <#assign links += [{ "url": "#purposes", "title": 'Purposes' }] />
                    </#if>
                    <#assign links += getStickySectionNavLinks({ "document": document, "includeTopLink": false, "ignoreSummary": true}) />
                    <@stickyNavSections links></@stickyNavSections>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

                <#if hasVisionContent>
                  <div id="vision" class="article-section" data-uipath="website.businessunit.vision">
                      <h2>Vision</h2>
                      <@hst.html hippohtml=document.vision contentRewriter=gaContentRewriter />
                  </div>
                </#if>

                <#if hasResponsibleRoleContent>
                  <div id="responsiblerole" class="article-section" data-uipath="website.businessunit.responsiblerole">
                      <h2>Responsible person</h2>
                      <div>${document.responsiblerole.title}</div>
                        <#if document.responsiblerole.relatedPeople?size == 1>
                          <div>
                            <#list document.responsiblerole.relatedPeople as person>
                              <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, person.title) />
                              <a href="<@hst.link hippobean=person/>" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)">${person.title}</a>
                            </#list>
                          </div>
                        </#if>
                  </div>
                </#if>
                <#if hasPartOfBusinessUnit>
                  <div id="ispartofbusinessunit" class="article-section" data-uipath="website.businessunit.ispartofbusinessunit">
                      <h2>This business unit is part of</h2>

                      <@downloadBlock document.ispartofbusinessunit />
                  </div>
                </#if>
                <#if hasChildren>
                  <div id="children" class="article-section" data-uipath="website.businessunit.children">
                      <h2>Programmes of work</h2>


                      <#list document.children as child>
                        <div>
                          <@downloadBlock child />
                        </div>
                      </#list>
                  </div>
                </#if>
                <#if document.purposes?has_content>
                  <div id="purposes" class="article-section" data-uipath="website.businessunit.purposes">
                      <h2>Purposes</h2>
                      <ul>
                        <#list document.purposes as purpose>
                          <li><@hst.html hippohtml=purpose contentRewriter=gaContentRewriter /></li>
                        </#list>
                      </ul>
                  </div>
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>
