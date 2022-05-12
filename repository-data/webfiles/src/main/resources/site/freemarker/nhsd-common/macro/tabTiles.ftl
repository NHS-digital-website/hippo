<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro tabTiles options>
    <#if options??>
        <#if options.title??>
            <#assign title = options.title>
        </#if>
        <#if options.summary??>
            <#assign summary = options.summary>
        </#if>

        <#if options.linkType == "internal">
            <#assign title = options.link.title>
        <#elseif options.linkType == "external">
            <#assign summary = options.shortsummary>
        </#if>

        <article class="nhsd-m-card nhsd-m-card--with-icon">
            <#if options.linkType == "internal">
            <a href="<@hst.link hippobean=options.link />" class="nhsd-a-box-link" aria-label="${title}"/>
            <#elseif options.linkType == "external">
            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, options.title) />
            <a href="${options.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)" class="nhsd-a-box-link" aria-label="${title}"/>
            <#elseif options.linkType == "asset">
            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, options.link) />
            <a href="<@hst.link hippobean=options.link />" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)" class="nhsd-a-box-link" aria-label="${title}">
            </#if>
                <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                    <div class="nhsd-m-card__content_container">
                        <div class="nhsd-m-card__content-box">
                            <p class="nhsd-t-heading-s">${title}</p>
                            <#if (options.icon.original)??>
                            <span class="nhsd-a-icon nhsd-a-icon--size-xl nhsd-a-icon--col-black nhsd-m-card__icon">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                                    <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                                    <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="42%" height="42%" x="29%" y="29%"/>
                                </svg>
                                <@hst.link var="icon" hippobean=options.icon.original fullyQualified=true />
                                <#if icon?ends_with("svg") && options.svgXmlFromRepository?? && options.svgXmlFromRepository?has_content>
                                    <#if title?? && title?has_content>
                                        <img src="data:image/svg+xml;base64,${base64(colour(options.svgXmlFromRepository, "231f20"))}" alt="${title}" />
                                    <#else>
                                        <img src="data:image/svg+xml;base64,${base64(colour(options.svgXmlFromRepository, "231f20"))}" />
                                    </#if>
                                <#else>
                                    <img src="${icon}" alt="${title}" />
                                </#if>

                            </span>
                            </#if>
                            <p class="nhsd-t-body-s">${summary}</p>
                        </div>

                        <div class="nhsd-m-card__button-box">
                            <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                    <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                </svg>
                            </span>
                        </div>
                    </div>
                </div>
            </a>
        </article>
    </#if>
</#macro>
