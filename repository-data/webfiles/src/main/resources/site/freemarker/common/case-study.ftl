<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/toolkit/organisms/caseStudy.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.CallToActionRich" -->

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<#if document?has_content>
    <#assign alignment = (textAlignment?has_content && textAlignment == "right")?then(true, false) />
    <#assign options = {
        "mirrored": alignment
    }/>
    <#if document.image?has_content>
        <@hst.link hippobean=document.image var="image" />
        <#assign altText = (document.altText?has_content)?then(document.altText, document.title) />
        <#assign options += {
            "image": {
                "src": image,
                "alt": (document.isDecorative == "true")?then('', altText)
            }
        }/>
    <#elseif document.icon?has_content>
        <@hst.link hippobean=document.icon var="icon" />
        <#if icon?ends_with("svg")>
            <#assign options += {
                "image": {
                    "src": "data:image/svg+xml;base64,${base64(colour(document.svgXmlFromRepository, '231f20'))}"
                }
            }/>
        <#else>
            <#assign options += {
                "image": {
                    "src": icon
                }
            }/>
        </#if>
    </#if>

    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col">
                <@caseStudy options; imageSection>
                    <#if document.categoryInfo?has_content>
                        <p class="nhsd-t-body">${document.categoryInfo}</p>
                    </#if>
                    <#if document.title?has_content>
                        <h2 class="nhsd-t-heading-xl nhsd-!t-margin-bottom-3">${document.title}</h2>
                    </#if>

                    ${imageSection}

                    <#if document.content?has_content>
                        <p class="nhsd-t-body nhsd-!t-margin-bottom-4">
                        <#if hst.isBeanType(document.content, 'org.hippoecm.hst.content.beans.standard.HippoHtml')>
                            <@hst.html hippohtml=document.content contentRewriter=gaContentRewriter />
                        <#else>
                            ${document.content}
                        </#if>
                        </p>
                    <#elseif document.richContent?has_content>
                        <@hst.html hippohtml=document.richContent contentRewriter=gaContentRewriter />
                    </#if>

                    <#if document.internal?has_content || document.external?has_content>
                        <#assign linkLabel = document.label />

                        <#if document.internal?has_content>
                            <@hst.link hippobean=document.internal var="link"/>
                        <#elseif document.external?has_content>
                            <#assign link = document.external/>
                        </#if>

                        <a class="nhsd-a-button nhsd-!t-margin-bottom-0" href="${link}" ${document.external?has_content?then('target="_blank" rel="external"', '')}>
                            <span class="nhsd-a-button__label">${linkLabel}</span>
                            <#if document.external?has_content>
                                <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                            </#if>
                        </a>
                    </#if>
                </@caseStudy>
            </div>
        </div>
    </div>
</#if>
