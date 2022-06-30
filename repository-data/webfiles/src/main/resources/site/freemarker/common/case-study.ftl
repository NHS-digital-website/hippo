<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.CallToActionRich" -->

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<#assign hasDocument = document?has_content />
<#assign hasAlignment = textAlignment?has_content />
<#assign isRightAlignment = (hasAlignment && textAlignment == "right")?then("nhsd-o-case-study--mirrored", "") />

<#if hasDocument>
    <#assign hasTitle = document.title?has_content />
    <#assign hasCategoryInfo = document.categoryInfo?has_content />
    <#assign hasContent = document.content?has_content />
    <#assign hasRichContent = document.richContent?has_content />
    <#assign hasImage = document.image?has_content />
    <#assign hasIcon = document.icon?has_content />
    <#assign hasLink = (document.external?has_content || document.internal?has_content) && document.label?has_content />
    <#assign isCtaDoc = document.class.simpleName?starts_with("Calltoaction") />
    <#assign isDecorativeOnly = isCtaDoc && document.isDecorative?if_exists == "true" />

    <article class="nhsd-o-case-study nhsd-o-case-study--no-label nhsd-!t-margin-bottom-9 ${isRightAlignment}">
        <div class="nhsd-t-grid">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-xs-12">
                    <div class="nhsd-o-case-study__wrapper nhsd-t-row--centred">
                        <div class="nhsd-o-case-study__content-box">

                            <div class="nhsd-o-case-study__contents">
                                <#if hasCategoryInfo>
                                    <p class="nhsd-t-body">${document.categoryInfo}</p>
                                </#if>
                                <#if hasTitle>
                                    <h2 class="nhsd-t-heading-xl nhsd-!t-margin-bottom-3">${document.title}</h2>
                                </#if>
                                <figure class="nhsd-a-image">
                                    <picture class="nhsd-a-image__picture" style="background-color: #e8edee">
                                        <#if hasImage>
                                            <@hst.link hippobean=document.image var="image" />
                                            <#assign altText = (isCtaDoc && document.altText?has_content)?then(document.altText, document.title) />
                                            <img src="${image}" alt="<#if !isDecorativeOnly>${altText}</#if>">
                                        <#elseif hasIcon>
                                            <@hst.link hippobean=document.icon var="icon" />
                                            <#if icon?ends_with("svg")>
                                                <img src="data:image/svg+xml;base64,${base64(colour(document.svgXmlFromRepository, "231f20"))}" aria-hidden="true" />
                                            <#else>
                                                <img src="${icon}">
                                            </#if>
                                        <#else>
                                            <img src="https://digital.nhs.uk/binaries/content/gallery/website/about-nhs-digital/fibre_57101102_med.jpg" alt="<#if !isDecorativeOnly>${document.title}</#if>">
                                        </#if>
                                    </picture>
                                </figure>

                                <#if hasContent>
                                    <p class="nhsd-t-body nhsd-!t-margin-bottom-4">
                                    <#if hst.isBeanType(document.content, 'org.hippoecm.hst.content.beans.standard.HippoHtml')>
                                        <@hst.html hippohtml=document.content contentRewriter=gaContentRewriter />
                                    <#else>
                                        ${document.content}
                                    </#if>
                                    </p>
                                    <#elseif hasRichContent>
                                        <@hst.html hippohtml=document.richContent contentRewriter=gaContentRewriter />
                                </#if>

                                <#if hasLink>
                                    <#assign linkLabel = document.label />

                                    <#if document.internal?has_content>
                                        <@hst.link hippobean=document.internal var="link"/>
                                    <#elseif document.external?has_content>
                                        <#assign link = document.external/>
                                    </#if>

                                    <#if document.external?has_content>
                                        <a class="nhsd-a-button nhsd-!t-margin-bottom-0" href="${link}" target="_blank" rel="external">
                                    <#else>
                                        <a class="nhsd-a-button nhsd-!t-margin-bottom-0" href="${link}">
                                    </#if>

                                    <span class="nhsd-a-button__label">${linkLabel}</span>

                                    <#if document.external?has_content>
                                        <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                                    </#if>
                                    </a>
                                </#if>
                            </div>
                        </div>
                        <div class="nhsd-o-case-study__picture-skeleton"></div>
                    </div>
                </div>
            </div>
        </div>
    </article>
</#if>
