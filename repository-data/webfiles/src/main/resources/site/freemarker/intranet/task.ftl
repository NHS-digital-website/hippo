<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../publicationsystem/macro/structured-text.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/stickyNavSections.ftl">
<#include "../common/macro/component/lastModified.ftl">
<#-- TODO - Once DW-1199 is merged into master, use `updateGroup`  -->
<#-- <#include "../common/macro/updateGroup.ftl"> -->


<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">

<@hst.setBundle basename="publicationsystem.change,publicationsystem.survey,publicationsystem.interactive,publicationsystem.labels,publicationsystem.headers"/>
<@fmt.message key="headers.summary" var="summaryHeader" />

<article class="article article--intranet-task">
    <div class="grid-wrapper grid-wrapper--full-width grid-wrapper--wide" aria-label="Document Header">
        <div class="local-header article-header article-header--with-icon">
            <div class="grid-wrapper">
                <div class="article-header__inner">
                    <div class="grid-row">
                        <div class="column--two-thirds column--reset">
                            <h1 id="top" class="local-header__title" data-uipath="document.title">${document.title}</h1>

                            <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter />
                            <@hst.html hippohtml=document.shortSummary contentRewriter=gaContentRewriter />
                            <#list document.alternativeNames as altName>
                                altName
                            </#list>
                            <#list document.responsibleTeams as team>
                                team
                            </#list>

                            <div class="rich-text-content">
                                <#-- <h2>Short summary: ${document.responsibleTeam}</h2> -->
                                
                                <#-- <ul>
                                <#list document?keys as key>
                                    <li>${key}</li>
                                </#list>
                                </ul> -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <#-- TODO - Once DW-1199 is merged into master, use `updateGroup`  -->
        <#-- <@updateGroup /> -->

        <div class="grid-row">
            <#-- <#if index?has_content && index?size gt 1>
                <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                    <div id="sticky-nav">
                        <#assign links = [] />
                        <#list index as i>
                            <#assign links += [{ "url": "#" + slugify(i), "title": i }] />
                        </#list>
                        <@stickyNavSections getStickySectionNavLinks({"document": document, "sections": links})></@stickyNavSections>
                    </div>
                </div>
            </#if> -->

            <div class="column column--two-thirds page-block page-block--main">
                ${document.lastModified}
                <#-- <@lastModified document.lastModified?date></@lastModified> -->
            </div>

    </div>
</article>

<#-- <article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <h2>${document.title}</h2>
        <h2>${document.reviewDate}</h2>
        <#if document.children?has_content>
            <#list document.children as child>
                ${child.title} <br>
            </#list>
        </#if>
        <#if document.parents?has_content>
            <#list document.parents as parent>
                ${parent.title} <br>
            </#list>
        </#if>
    </div>
</article> -->
