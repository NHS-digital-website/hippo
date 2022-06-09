<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.Calltoaction" -->

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#assign hasDocument = document?has_content />

<#if hasDocument>
    <#assign hasTitle = document.title?has_content />
    <#assign hasContent = document.content?has_content />
    <#assign hasImage = document.image?has_content />
    <#assign hasIcon = document.icon?has_content />
    <#assign hasLink = (document.external?has_content || document.internal?has_content) && document.label?has_content />

    <div class="nhsd-t-grid">
        <div class="nhsd-t-row nhsd-t-row--centred">
            <div class="nhsd-t-col-12">
                <div class="nhsd-t-flex nhsd-t-flex--justify-content-centre">
                    <div class="nhsd-t-flex-item nhsd-t-flex-item--grow nhsd-m-emphasis-box nhsd-m-emphasis-box--centred">
                        <div class="nhsd-a-box nhsd-a-box--border-blue">
                            <#if hasImage>
                                <div class="nhsd-m-emphasis-box__icon-box">
                                    <div class="nhsd-a-icon nhsd-a-icon--size-xxl">
                                        <figure class="nhsd-a-image nhsd-a-image--square" aria-hidden="true">
                                            <picture class="nhsd-a-image__picture">
                                                <@hst.link hippobean=document.image var="image"/>
                                                <img src="${image}" alt="${document.title}">
                                            </picture>
                                        </figure>
                                    </div>
                                </div>
                            <#elseif hasIcon>
                                <div class="nhsd-m-emphasis-box__icon-box">
                                    <div class="nhsd-a-icon nhsd-a-icon--size-xxl">
                                        <figure class="nhsd-a-image nhsd-a-image--square" aria-hidden="true">
                                            <picture class="nhsd-a-image__picture">
                                                <@hst.link hippobean=document.icon var="icon"/>
                                                <#if icon?ends_with("svg")>
                                                    <img src="data:image/svg+xml;base64,${base64(colour(document.svgXmlFromRepository, "41b6e6"))}" aria-hidden="true" />
                                                <#else>
                                                    <img src="${icon}">
                                                </#if>
                                            </picture>
                                        </figure>
                                    </div>
                                </div>
                            </#if>

                            <div class="nhsd-m-emphasis-box__content-box">
                                <#if hasTitle>
                                    <h2 class="nhsd-t-heading-s">${document.title}</h2>
                                </#if>

                                <#if hasContent>
                                    <p class="nhsd-t-body-s">${document.content}</p>
                                </#if>


                                <#if hasLink>
                                    <#assign linkLabel = document.label />

                                    <#if document.internal?has_content>
                                        <@hst.link hippobean=document.internal var="link"/>
                                    <#elseif document.external?has_content>
                                        <#assign link = document.external/>
                                    </#if>

                                    <a class="nhsd-a-button nhsd-!t-margin-top-4 nhsd-!t-margin-bottom-0" href="${link}">

                                    <span class="nhsd-a-button__label">${linkLabel}</span>

                                    <#if document.external?has_content>
                                        <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                                    </#if>
                                    </a>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#if>
