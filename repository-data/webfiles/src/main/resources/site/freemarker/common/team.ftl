<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Team" -->

<#include "../include/imports.ftl">
<#include "macro/sections/sections.ftl">
<#include "macro/documentHeader.ftl">
<#include "macro/stickyNavSections.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/component/lastModified.ftl">
<#include "macro/personitem.ftl">
<#include "macro/contactdetail.ftl">

<#-- Add meta tags -->
<@metaTags></@metaTags>

<#assign hasSummaryContent = document.shortsummary?has_content />
<#assign hasSectionContent = document.sections?has_content />
<#assign hasResponsiblePeople = document.responsiblePeople?has_content />
<#assign hasContactDetailsContent = document.contactdetails?? && document.contactdetails?has_content />
<#assign hasTeamMembers = document.teamMembers?has_content />
<#assign sectionTitlesFound = countSectionTitles(document.sections) />
<#assign renderNav = hasSummaryContent || hasResponsiblePeople || hasTeamMembers || sectionTitlesFound gt 1 />
<#assign idsuffix = slugify(document.title) />

<#assign teamMemebrsText = "Team members" />
<#assign contactDetailsText = "Contact details" />
<#assign responsiblePeopleText = "Responsible person" />

<#if hasResponsiblePeople && document.responsiblePeople?size gt 1 >
    <#assign responsiblePeopleText = "Responsible people" />
</#if>

<article class="article article--team">

    <@documentHeader document 'team' '' '' document.shortsummary></@documentHeader>

    <div class="grid-wrapper grid-wrapper--article">

        <div class="grid-row">
            <#if renderNav?has_content>
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
                    <#if hasResponsiblePeople>
                        <#assign links += [{ "url": "#responsiblepeople", "title": responsiblePeopleText }] />
                    </#if>
                    <#assign links += getStickySectionNavLinks({ "document": document, "includeTopLink": false, "ignoreSummary": true}) />
                    <#if hasContactDetailsContent>
                        <#assign links += [{ "url": "#contactdetail-${slugify(idsuffix)}", "title": contactDetailsText }] />
                    </#if>
                    <#if hasTeamMembers>
                        <#assign links += [{ "url": "#teammembers", "title": teamMemebrsText }] />
                    </#if>
                    <@stickyNavSections links></@stickyNavSections>
                </div>
            </div>
            </#if>

            <div class="column column--two-thirds page-block page-block--main">

                <#if hasResponsiblePeople>
                  <div id="responsiblepeople" class="article-section" data-uipath="website.team.responsiblepeople">
                      <h2>${responsiblePeopleText}</h2>
                      <#list document.responsiblePeople as author>
                          <@personitem author />
                      </#list>
                </#if>

                <#if hasSectionContent>
                    <@sections document.sections></@sections>
                </#if>

                <#if hasContactDetailsContent>
                    <div class="article-section article-section--contact" id="contactdetails">
                        <@contactdetail document.contactdetails idsuffix '' '' '' contactDetailsText false />
                    </div>
                </#if>

                <#if hasTeamMembers>
                  <div id="teammembers" class="article-section" data-uipath="website.team.teammembers">
                      <h2>${teamMemebrsText}</h2>
                      <#list document.teamMembers as member>
                          <@personitem member.person />
                      </#list>
                </#if>

                <div class="article-section muted">
                    <@lastModified document.lastModified false></@lastModified>
                </div>
            </div>
        </div>
    </div>
</article>
