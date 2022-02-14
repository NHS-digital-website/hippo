<#ftl output_format="HTML">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Team" -->

<#include "../include/imports.ftl">
<#include "./macro/metaTags.ftl">

<#include "./macro/heroes/hero.ftl">
<#include "./macro/heroes/hero-options.ftl">
<#include "./macro/stickyNavSections.ftl">
<#include "./macro/sections/sections.ftl">
<#include "./macro/personitem.ftl">
<#include "macro/contactdetail.ftl">

<#if document.responsiblePeople?has_content>
    <#assign responsiblePeopleText = "Responsible ${(document.responsiblePeople?size gt 1)?then('people', 'person')}" />
</#if>

<#assign contactDetailsText = "Contact details" />
<#assign teamMemebrsText = "Team members" />

<#-- Add meta tags -->
<@metaTags></@metaTags>

<article>
    <@hero getHeroOptionsWithMetaData(document) />

    <div class="nhsd-t-grid nhsd-!t-margin-top-8">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-3">
                <!-- start sticky-nav -->
                <#assign links = [{ "url": "#top", "title": 'Top of page' }] />
                <#if document.responsiblePeople?has_content>
                    <#assign links += [{ "url": "#responsible-people", "title": responsiblePeopleText }] />
                </#if>
                <#assign links += getStickySectionNavLinks({ "document": document, "includeTopLink": false }) />
                <#if document.contactdetails?has_content>
                    <#assign links += [{ "url": "#contact-details", "title": contactDetailsText }] />
                </#if>
                <#if document.teamMembers?has_content>
                    <#assign links += [{ "url": "#team-members", "title": teamMemebrsText }] />
                </#if>
                <@stickyNavSections links></@stickyNavSections>
                <!-- end sticky-nav -->
            </div>

            <div class="nhsd-t-col-xs-12 nhsd-t-off-s-1 nhsd-t-col-s-8">
                <#if document.responsiblePeople?has_content>
                    <div class="nhsd-!t-margin-bottom-4">
                        <div id="responsible-people" data-uipath="website.team.responsiblepeople">
                            <h2 class="nhsd-t-heading-xl">${responsiblePeopleText}</h2>
                            <div class="nhsd-t-grid nhsd-t-grid--nested">
                                <div class="nhsd-t-row">
                                    <#list document.responsiblePeople as author>
                                        <div class="nhsd-t-col-4 nhsd-!t-margin-bottom-3">
                                            <@personitem author />
                                        </div>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr class="nhsd-a-horizontal-rule">
                </#if>

                <#if document.sections?has_content>
                    <div class="nhsd-!t-margin-bottom-6">
                        <@sections sections=document.sections orgPrompt=downloadPrompt />
                    </div>
                    <hr class="nhsd-a-horizontal-rule">
                </#if>

                <#if document.contactdetails?has_content>
                    <div id="contact-details" class="nhsd-!t-margin-bottom-6">
                        <@contactdetail document.contactdetails idsuffix '' '' '' contactDetailsText false />
                    </div>
                    <hr class="nhsd-a-horizontal-rule nhsd-!t-margin-top-0">
                </#if>

                <#if document.teamMembers?has_content>
                    <div id="team-members" class="nhsd-!t-margin-bottom-6" data-uipath="website.team.teammembers">
                    <h2 class="nhsd-t-heading-xl">${teamMemebrsText}</h2>
                    <div class="nhsd-t-grid nhsd-t-grid--nested">
                        <div class="nhsd-t-row">
                            <#list document.teamMembers as member>
                                <div class="nhsd-t-col-4 nhsd-!t-margin-bottom-3">
                                    <@personitem member.person />
                                </div>
                            </#list>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
    </div>
</article>
