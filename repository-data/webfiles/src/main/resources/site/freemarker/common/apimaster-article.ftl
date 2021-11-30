<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiMaster" -->

<#include "macro/releaseinfo.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/apiinfobuilder.ftl">
<#include "macro/apiendpointgroup.ftl">
<#include "macro/aboutapi.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/contentPixel.ftl">

<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#assign renderNav = document.aboutapis />

<#assign sectionTitlesFound = countSectionTitlesInMultiple(document.aboutapis) />
<#assign renderNav = sectionTitlesFound gte 1 || document.aboutapis?has_content | document.apiinfobuilders?has_content || document.apiendpointgroups?has_content  />

<#-- Content Page Pixel -->
<@contentPixel document.getCanonicalUUID() document.title></@contentPixel>

<#-- ACTUAL TEMPLATE -->
<article class="article article--apimaster">

    <@releaseInfo article=document/>

    <div class="grid-wrapper grid-wrapper--article">
      <div class="grid-row">

        <#if renderNav>
          <div class="column column--one-third page-block--sticky-nav page-block--sidebar article-section-nav-outer-wrapper">
              <!-- start sticky-nav -->
                <div id="sticky-nav">
                  <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
                  <#assign links += [{ "url": "#summary", "title": 'Summary' }] />
                  <#assign links += getNavLinksInMultiple(document.aboutapis, "aboutapi-") />
                  <#assign links += getNavLinksInMultiple(document.apiinfobuilders, "apiinfobuilder-") />
                  <#assign links += getNavLinksInMultiple(document.apiendpointgroups, "apiendpoint-") />
                  <@stickyNavSections getStickySectionNavLinks({"sections": links})></@stickyNavSections>
              </div>
              <!-- end sticky-nav -->
              <#-- Restore the bundle -->
              <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
          </div>
        </#if>

        <div class="column column--two-thirds page-block page-block--main">


          <div class="column">
              <div id="summary" class="article-section--summary-separator article-section--summary">
                  <h2>Summary</h2>
                <div itemprop="summary" data-uipath="article.summary" class="article-section--summary">
                    <@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/>
                </div>
              </div>
          </div>

          <#if document.aboutapis?has_content>
            <div class="column">
              <#list document.aboutapis as aboutapi>
                <@aboutapimacro aboutapi=aboutapi />
              </#list>
            </div>
          </#if>

          <#if document.apiinfobuilders?has_content>
            <div class="column">
              <#list document.apiinfobuilders as builder>
                <@apiinfobuilder builder=builder />
              </#list>
            </div>
          </#if>

          <#if document.apiendpointgroups?has_content>
            <div class="column">
              <#list document.apiendpointgroups as endpoint>
                <@apiendpointgroup endpoint=endpoint />
              </#list>
            </div>
          </#if>
        </div>
        </div>

      </div>

</article>
