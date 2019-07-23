<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#macro documentHeader document doctype icon="" >

    <#assign hasSummaryContent = document.summary.content?has_content />
    <#assign hasPageIcon = icon?has_content />

    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 id="top" class="local-header__title" data-uipath="document.title">${document.title}</h1>
                            <#if hasSummaryContent>
                              <div class="article-header__subtitle" data-uipath="website.${doctype}.summary"><@hst.html hippohtml=document.summary contentRewriter=gaContentRewriter/></div>
                            </#if>
                        </div>
                        <#if hasPageIcon>
                            <div class="column--one-third column--reset local-header__icon">
                                <@hst.link hippobean=icon.original fullyQualified=true var="image" />
                                <#if image?ends_with("svg")>
                                    <img src="${image?replace("/binaries", "/svg-magic/binaries")}?colour=ffcd60" alt="${document.title}" />
                                <#else>
                                    <img src="${image}" alt="${document.title}" />
                                </#if>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>

</#macro>
