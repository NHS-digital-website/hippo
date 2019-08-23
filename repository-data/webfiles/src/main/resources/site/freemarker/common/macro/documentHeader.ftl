<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro documentHeader document doctype icon="" title="" summary="">

    <#assign custom_title = title />
    <#if ! custom_title?has_content >
      <#assign custom_title = document.title />
    </#if>

    <#assign custom_summary = summary />
    <#assign hasDocumentSummary = false />
    <#if ! custom_summary?has_content >
      <#assign hasDocumentSummary = document.summary?? && document.summary.content?has_content />
      <#if hasDocumentSummary >
        <#assign custom_summary = document.summary />
      </#if>
    </#if>

    <#assign hasPageIcon = icon?has_content />

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 id="top" class="local-header__title" data-uipath="document.title">${custom_title}</h1>
                            <#if hasDocumentSummary>
                              <div class="article-header__subtitle" data-uipath="website.${doctype}.summary">
                                <@hst.html hippohtml=custom_summary contentRewriter=gaContentRewriter/>
                              </div>
                            <#else>
                              <div class="article-header__subtitle" data-uipath="website.${doctype}.summary">
                                <p>${custom_summary}</p>
                              </div>
                            </#if>
                        </div>
                        <#if hasPageIcon>
                            <div class="column--one-third column--reset local-header__icon">
                              <#if hasDocumentSummary> 
                                  <#-- ex. Service case - image from HippoGalleryImageSet -->
                                  <@hst.link hippobean=icon.original fullyQualified=true var="image" />
                                  <#if image?ends_with("svg")>
                                      <img src="${image?replace("/binaries", "/svg-magic/binaries")}?colour=ffcd60" alt="${custom_title}" />
                                  <#else>
                                      <img src="${image}" alt="${custom_title}" />
                                  </#if>
                                <#else>
                                  <#-- ex. Events or News case - image from provided path -->
                                  <img src="<@hst.webfile path="${icon}" fullyQualified=true/>" alt="${custom_title}">
                                </#if>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

</#macro>
