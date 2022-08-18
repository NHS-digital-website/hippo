<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#-- @ftlvariable name="socialMedia" type="uk.nhs.digital.website.beans.SocialMedia" -->

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />
<#list socialmedia as socialmediaItem>
    <#assign isFooterChecked_TOP = socialmediaItem.isForFooter?string('Yes', 'No')/>
</#list>

<#if isFooterChecked_TOP == 'Yes'>
    <div id="footer-section-wrapper-social-media"
         class="nhsd-t-body-s nhsd-!t-font-weight-bold nhsd-!t-margin-bottom-1">
        <h2>Follow us on social media</h2>
    </div>
<#else>
    <div class="nhsd-t-grid nhsd-!t-margin-bottom-4">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <div class="nhsd-t-heading-l">
                    <h2>${title}</h2>
                </div>
            </div>
        </div>
    </div>
</#if>

<#if socialmedia??>
    <#list socialmedia as socialmediaItem>
        <@hst.link hippobean=socialmediaItem.linkIcon var="linkIcon" />
        <#assign iconDesc = socialmediaItem.linkIcon.description />
        <#assign isFooterChecked_BOTTOM = socialmediaItem.isForFooter?string('Yes', 'No')/>
        <#assign iconFetchLink = socialmediaItem.icon/>
        <#assign altText = iconDesc?has_content?then(iconDesc, "Icon of ${socialmediaItem.linkName}") />
        <#if isFooterChecked_BOTTOM == 'Yes'>
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-l-3 nhsd-!t-padding-0">
                <ul class="nhsd-t-list nhsd-t-list--links">
                    <li class="nhsd-t-body-s">
                        <span class="nhsd-a-icon nhsd-a-icon--size-l nhsd-a-icon--col-dark-grey">
                            <img src="${iconFetchLink}" alt="${socialmediaItem.linkName}">
                        </span>
                       <a class="nhsd-a-link nhsd-a-link--col-dark-grey" href="${socialmediaItem.nhsDigitalUrl}" target="_blank" rel="external">${socialmediaItem.linkName}</a>
                    </li>
                </ul>
            </div>
        <#else>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-12">
                    <a class="nhsd-a-icon-link nhsd-a-icon-link--dark-grey"
                       href="${socialmediaItem.nhsDigitalUrl}" target="_blank"
                       rel="external"
                       onClick="logGoogleAnalyticsEvent('Link click','Social media - ${socialmediaItem.linkName}','${socialmediaItem.nhsDigitalUrl}');"
                       onKeyUp="return vjsu.onKeyUp(event)"
                    >
                        <#if linkIcon?has_content>
                            <span class="nhsd-a-icon nhsd-a-icon--size-xxl">
                                <svg xmlns="http://www.w3.org/2000/svg"
                                     preserveAspectRatio="xMidYMid meet"
                                     aria-hidden="true" focusable="false"
                                     viewBox="0 0 16 16">
                                    <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>

                                    <#if linkIcon?ends_with("svg")>
                                        <#if altText?? && altText?has_content>
                                            <image x="4" y="4" width="8" height="8" href="data:image/svg+xml;base64,${base64(colour(socialmediaItem.svgXmlFromRepository, "3f525f"))}">
                                        <#else>
                                            <image x="4" y="4" width="8" height="8" href="data:image/svg+xml;base64,${base64(colour(socialmediaItem.svgXmlFromRepository, "3f525f"))}">
                                        </#if>
                                    <#else>
                                        <image x="4" y="4" width="8" height="8" href="${linkIcon}">
                                    </#if>
                                </svg>
                            </span>
                        <#else>
                            <span class="nhsd-a-icon nhsd-a-icon--size-xxl nhsd-a-icon--col-dark-grey">
                                <img src="${iconFetchLink}" alt="${socialmediaItem.linkName}">
                            </span>
                        </#if>
                        <span class="nhsd-a-icon-link__label">${socialmediaItem.linkName}</span>
                        <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                    </a>
                </div>
            </div>
        </#if>
    </#list>
</#if>
