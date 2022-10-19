<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.SupplementaryInformation" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/component/downloadBlock.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasRequest = document.request.content?has_content />
<#assign hasRelatedDocuments = document.relateddocuments?has_content && document.relateddocuments?size gt 0 />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = hasSummaryContent && sectionTitlesFound gte 1 || sectionTitlesFound gt 1 />
<#assign idsuffix = slugify(document.title) />

<#assign relatedDocsText = "Information related to" />
<#assign requestText = "Request received" />
<#assign responseText = "Our response" />

<article class="article article--supplementary-info">

    <#assign pageIcon = '' />
    <#if document.bannercontrols?has_content && document.bannercontrols.icon?has_content>
      <#assign pageIcon = document.bannercontrols.icon />
    </#if>

    <!-- for schema.org tags use ex. => "schemaOrgTag": "publishedDate" -->
    <#assign metadata = [] />
    <#if document.responsibleperson?has_content >
      <#assign metadata += [
        {
         "key": "Lead Analyst",
         "value": document.responsibleperson,
         "uipath": "website.supplementaryinfo.responsibleperson",
         "type": "link"
        }
      ] />
    </#if>
    <#if document.responsibleteam?has_content >
      <#assign metadata += [
        {
         "key": "Responsible team",
         "value": document.responsibleteam,
         "uipath": "website.supplementaryinfo.responsibleteam",
         "type": "link"
        }
      ] />
    </#if>
    <#if document.publishedDate?has_content >
      <#assign metadata += [
        {
         "key": "Date published",
         "value": document.publishedDate.time,
         "uipath": "website.supplementaryinfo.publisheddate",
         "type": "date"
        }
      ] />
    </#if>
    <#if document.interval?has_content >
      <#assign dateRange = "" />

      <#if document.interval.startdatetime?has_content>
        <#assign dateRange = document.interval.startdatetime.time?string["d MMMM yyyy"] />
      </#if>

      <#if document.interval.startdatetime?has_content && document.interval.enddatetime?has_content>
        <#assign dateRange += " - " + document.interval.enddatetime.time?string["d MMMM yyyy"] />
      <#elseif document.interval.enddatetime?has_content>
        <#assign dateRange = document.interval.enddatetime.time?string["d MMMM yyyy"] />
      </#if>

      <#assign metadata += [
        {
         "key": "Date range covered",
         "value": dateRange,
         "uipath": "website.supplementaryinfo.datepublished",
         "type": "span"
        }
      ] />
    </#if>

    <@documentHeader document 'supplementaryinfo' pageIcon "" document.shortsummary "" true metadata></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
                    <#if hasRelatedDocuments>
                      <#assign links += [{ "url": "#related-docs", "title": relatedDocsText }] />
                    </#if>
                    <#if hasRequest>
                      <#assign links += [{ "url": "#request", "title": requestText }] />
                    </#if>
                    <#if sectionTitlesFound gt 0>
                      <#assign links += [{ "url": "#response", "title": responseText }] />
                    </#if>

                    <@stickyNavSections links></@stickyNavSections>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

                <#if hasRelatedDocuments>
                  <div id="related-docs" class="article-section" data-uipath="website.supplementaryinfo.relateddocs">
                      <h2>${relatedDocsText}</h2>
                      <#list document.relateddocuments as child>
                        <div class="article-header">
                          <@downloadBlock child />
                        </div>
                      </#list>
                  </div>
                </#if>

                <#if hasRequest>
                  <div id="request" class="article-section" data-uipath="website.supplementaryinfo.request">
                      <h2>${requestText}</h2>
                      <@hst.html hippohtml=document.request contentRewriter=gaContentRewriter />
                  </div>
                </#if>

                <#if sectionTitlesFound gt 0>
                  <div id="response" class="article-section" data-uipath="website.supplementaryinfo.response">
                      <h2>${responseText}</h2>
                    <#assign mainHeadingLevel = 3 />
                    <@sections document.sections mainHeadingLevel></@sections>
                  </div>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>
