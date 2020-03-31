<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../publicationsystem/macro/structured-text.ftl">
<#include "../common/macro/sections/sections.ftl">
<#include "../common/macro/documentHeader.ftl">
<#include "../common/macro/stickyNavSections.ftl">
<#-- TODO - Once DW-1199 is merged into master, use `updateGroup`  -->
<#-- <#include "../common/macro/updateGroup.ftl"> -->


<#-- Add meta tags -->
<#include "../common/macro/metaTags.ftl">

<@hst.setBundle basename="intranet.headers, intranet.labels" />

<@fmt.message key="headers.responsible-teams" var="responsibleTeamsSectionHeader" />
<@fmt.message key="headers.dates" var="datesSectionHeader" />

<#assign hasSectionContent = document.sections?has_content />
<#assign hasResponsibleTeams = document.responsibleTeams?has_content />

<article class="article article--intranet-task">
    <@documentHeader document 'intranet-task'></@documentHeader>

    <#if document.fullTaxonomyList?has_content>
    <h3>META</h3>
        <meta itemprop="keywords" content="${document.fullTaxonomyList?join(", ")}"/>
    </#if>

    <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <#-- TODO - Once DW-1199 is merged into master, use `updateGroup`  -->
        <#-- <@updateGroup /> -->

        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign links = [] />
                    <#if hasResponsibleTeams>
                        <#assign links += [{ "url": "#" + slugify(responsibleTeamsSectionHeader), "title": responsibleTeamsSectionHeader }] />
                    </#if>
                    <#assign links += [{ "url": "#" + slugify(datesSectionHeader), "title": datesSectionHeader }] />
                    
                    <@stickyNavSections getStickySectionNavLinks({"document": document, "appendSections": links, "includeTopLink": true})></@stickyNavSections>

                    <#-- Restore the bundle -->
                    <@hst.setBundle basename="intranet.headers, intranet.labels" />
                </div>
            </div>
            

            <div class="column column--two-thirds page-block page-block--main">
                <@sections sections=document.sections wrap=true />

                <#if hasResponsibleTeams>
                    <div id="${slugify(responsibleTeamsSectionHeader)}" class="article-section no-border">
                        <h2>${responsibleTeamsSectionHeader}</h2>
                        <ul>
                            <#list document.responsibleTeams as team>
                                <li><a href="<@hst.link hippobean=team />">${team.title}</a></li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <div class="article-section" id="${slugify(datesSectionHeader)}">
                    <h2>${datesSectionHeader}</h2>
                    <div class="detail-list-grid detail-list-grid--regular">
                        <#if document.reviewDate?has_content>
                        <dl class="detail-list">
                            <dt class="detail-list__key"><@fmt.message key="labels.review-date"/></dt>
                            <dd class="detail-list__value"><@fmt.formatDate value=document.reviewDate.time?date type="date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /></dd>
                        </dl>
                        </#if>

                        <dl class="detail-list">
                            <dt class="detail-list__key"><@fmt.message key="labels.last-modified"/></dt>
                            <dd class="detail-list__value">
                                <@fmt.formatDate value=document.lastModified?date type="date" pattern="d MMMM yyyy h:mm a" timeZone="${getTimeZone()}" var="lastModifiedDate" />
                                ${lastModifiedDate?replace("AM", "am")?replace("PM", "pm")}
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>

    </div>
</article>

<#-- <#if document.children?has_content>
    <#list document.children as child>
        ${child.title} <br>
    </#list>
</#if>
<#if document.parents?has_content>
    <#list document.parents as parent>
        ${parent.title} <br>
    </#list>
</#if> -->
