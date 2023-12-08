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
            <#assign title = options.link.title />
        <#elseif options.linkType == "external">
            <#assign summary = options.shortsummary />
        </#if>

        <article class="nhsd-m-card nhsd-m-card--full-height nhsd-m-card--with-icon">
            <#if options.linkType == "internal">
            <a href="<@hst.link hippobean=options.link />" class="nhsd-a-box-link" aria-label="${title}">
            <#elseif options.linkType == "external">
            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, options.title) />
            <a href="${options.link}" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)" class="nhsd-a-box-link" aria-label="${title}">
            <#elseif options.linkType == "asset">
            <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, options.link) />
            <a href="<@hst.link hippobean=options.link />" onClick="${onClickMethodCall}" onKeyUp="return vjsu.onKeyUp(event)" class="nhsd-a-box-link" aria-label="${title}">
            </#if>
                <div class="nhsd-a-box nhsd-!t-bg-pale-grey">
                    <div class="nhsd-m-card__content_container">
                        <div class="nhsd-m-card__content-box">
                            <p class="nhsd-t-heading-s">${title}</p>
                            <p class="nhsd-t-body-s">${summary}</p>
                        </div>

                        <div class="nhsd-m-card__button-box">
                            <span class="arrow-circle">
                                <svg width="15" class="arrow-icon-valign" height="15" viewBox="0 0 15 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path fill-rule="evenodd" clip-rule="evenodd" d="M8.23006 0.990157C8.78784 0.454693 9.67409 0.472779 10.2096 1.03055L15.0096 6.03056C15.5296 6.5723 15.5296 7.4279 15.0096 7.96964L10.2096 12.9696C9.67409 13.5274 8.78784 13.5455 8.23006 13.01C7.67229 12.4746 7.6542 11.5883 8.18967 11.0306L10.7149 8.4001H1.99961C1.22641 8.4001 0.599609 7.7733 0.599609 7.0001C0.599609 6.2269 1.22641 5.6001 1.99961 5.6001H10.7149L8.18967 2.96964C7.6542 2.41187 7.67229 1.52562 8.23006 0.990157Z" fill="#ffffff"></path>
                                </svg>
                            </span>
                            </span>
                        </div>

                        <div class="nhsd-m-card__icon-container">
                            <#if (options.icon.original)??>
                                <span class="nhsd-a-icon nhsd-a-icon--size-xxxl nhsd-m-card__icon">
                                   <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16">
                                        <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>

                                        <@hst.link var="icon" hippobean=options.icon.original fullyQualified=true />
                                        <#if icon?ends_with("svg") && options.svgXmlFromRepository?? && options.svgXmlFromRepository?has_content>
                                            <#if title?? && title?has_content>
                                                <image x="0" y="0" width="100%" height="100%" href="data:image/svg+xml;base64,${base64(colour(options.svgXmlFromRepository, "231f20"))}" alt="${title}" />
                                        <#else>
                                                <image x="0" y="0" width="100%" height="100%" href="data:image/svg+xml;base64,${base64(colour(options.svgXmlFromRepository, "231f20"))}" />
                                            </#if>
                                        <#else>
                                            <image x="4" y="4" width="8" height="8" href="${icon}" />
                                        </#if>
                                    </svg>
                                </span>
                            </#if>
                        </div>
                    </div>
                </div>
            </a>
        </article>
    </#if>
</#macro>
