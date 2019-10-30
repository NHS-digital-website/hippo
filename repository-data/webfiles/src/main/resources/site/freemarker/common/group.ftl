<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Group" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/furtherInformationSection.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#if document.metadata?has_content>
    <#list document.metadata as md>
        <@hst.headContribution category="metadata">
            ${md?no_esc}
        </@hst.headContribution>
    </#list>
</#if>

<@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>

<#assign hasSummaryContent = document.summary.content?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasHtmlCode = document.htmlCode?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = ((hasSummaryContent || hasChildPages) && sectionTitlesFound gte 1) || sectionTitlesFound gt 1 || (hasSummaryContent && hasChildPages) />

<article class="article article--group">

    <@documentHeader document 'group'></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <#if renderNav>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <!-- start sticky-nav -->
                <div id="sticky-nav">
                    <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
                    <#assign links += [{ "url": "#about", "title": 'About this group' }] />
                    <#assign links += [{ "url": "#members", "title": 'Members' }] />
                    <#assign links += getStickySectionNavLinks({ "document": document, "includeTopLink": false}) />
                    <@stickyNavSections links></@stickyNavSections>
                </div>
                <!-- end sticky-nav -->
                <#-- Restore the bundle -->
                <@hst.setBundle basename="rb.generic.headers,publicationsystem.headers"/>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

                <#if document.about?has_content>
                  <div id="about" class="article-section" data-uipath="website.group.about">
                      <h2>About this group</h2>
                      <@hst.html hippohtml=document.about contentRewriter=gaContentRewriter />
                  </div>
                </#if>

                <#if document.groupreports?has_content>
                  <div id="groupreports" class="article-section-with-sub-heading" data-uipath="website.group.groupreports">
                      <h3>This group reports to</h3>
                      <a href="<@hst.link hippobean=document.groupreports/>" >
                       ${document.groupreports.title}
                      </a>
                  </div>
                </#if>
                <#if document.quorate?has_content>
                  <div class="article-section-with-sub-heading">
                    <p>This board is quorate with ${document.quorate} members</p>
                  </div>
                </#if>

                <#if document.roles?has_content>
                  <div id="members" class="article-section">
                    <h2>Members</h2>
                    <#list document.roles as role>
                      <div class="group-member-item">
                        <#if role.role.roles?? && role.role.roles?has_content>
                          <div class="group-member-item-role">
                            <#if role.role.roles.primaryroles?has_content>
                              ${role.role.roles.firstprimaryrole}
                            </#if>
                            <#if role.role.roles.primaryroleorg?has_content>
                              at ${role.role.roles.primaryroleorg}
                            </#if>
                          </div>
                        </#if>

                        <div class="group-member-item-subitem">
                          <#if role.role.personimages?? && role.role.personimages.picture?has_content>
                            <div class="group-member-item-left">
                                <@hst.link hippobean=role.role.personimages.picture.original fullyQualified=true var="roleImage" />
                                <img itemprop="image" class="group-member-item-img" src="${roleImage}" alt="${role.role.title}" />
                            </div>
                          </#if>

                          <div class="group-member-item-right">
                            <div class="group-member-item-title">${role.role.title}</div>
                            <#if role.role.shortsummary?has_content>
                              <div class="group-member-item-summary">
                                ${role.role.shortsummary}
                              </div>
                            </#if>
                            <#if role.responsibilities?has_content>
                              <div class="group-member-item-summary">
                                <@hst.html hippohtml=role.responsibilities contentRewriter=gaContentRewriter />
                              </div>
                            </#if>
                            <#if role.groupposition?has_content>
                              <div class="group-member-item-groupposition">${role.groupposition}</div>
                            </#if>
                          </div>
                        </div>
                      </div>
                    </#list>
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

<#if hasHtmlCode>
    ${document.htmlCode?no_esc}
</#if>
