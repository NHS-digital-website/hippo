<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "./digiBlock.ftl">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<#macro navigationBlockLarge item id colourVariant isDarkMolecule isYellowLink isDarkButton position hasHeading options={}>
    <#assign hasTitle = item.title?has_content />
    <#assign hasContent = item.content?has_content />
    <#assign hasIcon = item.icon?has_content />
    <#assign hasImage = item.image?has_content />
    <#assign hasLink = item.external?has_content || item.internal?has_content />
    <#assign hasLabel = item.label?has_content />
    <#assign colVarWithYellowCheck = (colourVariant == yellow)?then("light-yellow", colourVariant) />

    <#assign classes = "" />
    <#if options.fullHeight?has_content && options.fullHeight>
        <#assign classes += "nhsd-m-nav-block--full-height" />
    </#if>

    <div class="nhsd-m-nav-block ${isDarkMolecule} ${classes}" id="${id}">
        <#if hasLink>
            <#if item.internal?has_content>
                <a href="<@hst.link hippobean=item.internal/>" class="nhsd-a-box-link ${isYellowLink}">
            <#else>
                <a href="${item.external}" class="nhsd-a-box-link ${isYellowLink}">
            </#if>
        </#if>
        <div class="nhsd-a-box nhsd-a-box--bg-${colVarWithYellowCheck}">
            <div class="nhsd-m-nav-block__content-box">
                <div class="nhsd-a-icon nhsd-a-icon--size-xxxl" aria-hidden="true">

                    <#assign variant = "FFFFFF" />
                    <#if colourVariant == "yellow">
                        <#assign variant = "231F20" />
                    <#elseif colourVariant == "light-grey">
                        <#assign variant = "231F20" />
                    </#if>


                    <#if hasIcon>
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16">
                            <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                            <image href="data:image/svg+xml;base64,${base64(colour(item.svgXmlFromRepository, variant))}" x="0" y="0" width="100%" height="100%" />
                        </svg>
                    <#elseif hasImage>
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16">
                            <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                            <@hst.link hippobean=item.image var="image"/>
                            <#if image?contains(".svg")>
                                <image href="data:image/svg+xml;base64,${base64(colour(item.svgXmlFromRepositoryImage, variant))}" x="0" y="0" width="100%" height="100%" />
                            <#else>
                                <image x="4" y="4" width="8" height="8" href="${image}"/>
                            </#if>
                        </svg>
                    <#else>
                        <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16">
                            <path d="M8,16l-6.9-4V4L8,0l6.9,4v8L8,16z M2,11.5L8,15l6-3.5v-7L8,1L2,4.5V11.5z"/>
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16" width="66%" height="66%" x="17%" y="17%">
                                <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                            </svg>
                        </svg>
                    </#if>
                </div>
                <#if hasTitle>
                    <#if hasHeading>
                        ${hasHeading}
                        <h3 class="nhsd-t-heading-s">${item.title}</h3>
                    <#else>
                        <h2 class="nhsd-t-heading-s">${item.title}</h2>
                    </#if>
                </#if>
                <#if hasContent>
                    <p class="nhsd-t-body">${item.content}</p>
                </#if>

                <#if hasLink && hasLabel>
                    <span class="nhsd-a-button ${isDarkButton}">
                        <span class="nhsd-a-button__label">${item.label}</span>
                        <#if item.external?has_content>
                            <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                        </#if>
                    </span>
                </#if>
            </div>
            <@digiBlock colourVariant position/>
        </div>
        <#if hasLink>
            </a>
        </#if>
    </div>
</#macro>
