<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="socialMedia" type="uk.nhs.digital.website.beans.SocialMedia" -->

<div class="social-media-component">
    <div class="article-section__header">
        <h2>${title}</h2>
    </div>

    <#if socialmedia??>
        <#list socialmedia as socialmediaItem>
            <div class="social-media-component__item">
                <a href="${socialmediaItem.nhsDigitalUrl}" class="black-link" onClick="logGoogleAnalyticsEvent('Link click','Social media - ${socialmediaItem.linkName}','${socialmediaItem.nhsDigitalUrl}');">
                    <@hst.link hippobean=socialmediaItem.linkIcon fullyQualified=true var="linkIcon" />
                    <#t><div class="social-media-component__image-wrapper"><img src="${linkIcon}" alt="" class="social-media-component__image" /></div>
                    <#t><span class="social-media-component__link-name">${socialmediaItem.linkName}</span>
                </a>
            </div>
        </#list>
    </#if>
</div>
