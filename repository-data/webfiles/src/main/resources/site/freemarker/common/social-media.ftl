<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="socialMedia" type="uk.nhs.digital.website.beans.SocialMedia" -->

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<div class="nhsd-t-grid nhsd-!t-margin-bottom-4">
    <div class="nhsd-t-row">
        <div class="nhsd-t-col-12">
            <div class="nhsd-t-heading-l">
                <h2>${title}</h2>
            </div>
        </div>
    </div>
    <#if socialmedia??>
        <#list socialmedia as socialmediaItem>
            <@hst.link hippobean=socialmediaItem.linkIcon var="linkIcon" />
            <#assign iconDesc = socialmediaItem.linkIcon.description />
            <#assign altText = iconDesc?has_content?then(iconDesc, "Icon of ${socialmediaItem.linkName}") />

            <#if linkIcon?has_content>
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-12">
                        <a class="nhsd-a-icon-link nhsd-a-icon-link--dark-grey"
                           href="${socialmediaItem.nhsDigitalUrl}" target="_blank" rel="external"
                           onClick="logGoogleAnalyticsEvent('Link click','Social media - ${socialmediaItem.linkName}','${socialmediaItem.nhsDigitalUrl}');"
                           onKeyUp="return vjsu.onKeyUp(event)"
                        >
                            <span class="nhsd-a-icon nhsd-a-icon--size-xxl">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                                    <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                                </svg>
                                <#if linkIcon?ends_with("svg")>
                                    <#assign colour = "3f525f" />
                                    <#assign imageUrl = '${linkIcon?replace("/binaries", "/svg-magic/binaries")}' />
                                    <#assign imageUrl += "?colour=${colour}" />
                                    <img src="${imageUrl}" alt="${altText}" aria-hidden="true">
                                <#else>
                                    <img src="${linkIcon}" alt="${altText}" aria-hidden="true">
                                </#if>
                            </span>
                            <span class="nhsd-a-icon-link__label">${socialmediaItem.linkName}</span>
                            <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                        </a>
                    </div>
                </div>
            </#if>
        </#list>
    </#if>
</div>
