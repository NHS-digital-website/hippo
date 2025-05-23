<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro cardItem cardProps>
    <#assign cardClass = ""/>
    <#if cardProps.featured?? && cardProps.featured && cardProps.image?has_content>
        <#assign cardClass = "nhsd-m-card--image-position-adjacent nhsd-m-card--full-height"/>
    </#if>
    <#if cardProps.authorsInfo?has_content && cardProps.authorsInfo?size gt 0>
        <#assign cardClass += " nhsd-m-card--author"/>
    </#if>
    <#if cardProps.fullHeight?has_content && cardProps.fullHeight>
        <#assign cardClass += " nhsd-m-card--full-height"/>
    </#if>
    <article class="nhsd-m-card ${cardClass} ${cardProps.cardClass?has_content?then(cardProps.cardClass, '')}">
        <a href="${cardProps.link}" class="nhsd-a-box-link" >
            <div class="nhsd-a-box ${cardProps.background?has_content?then("nhsd-!t-bg-" + cardProps.background, "")}">
                <#if cardProps.image?has_content>
                    <div class="nhsd-m-card__image_container">
                        <figure class="nhsd-a-image ${cardProps.imageClass?has_content?then(cardProps.imageClass, '')}">
                            <picture class="nhsd-a-image__picture">
                                <@hst.link hippobean=cardProps.image.newsPostImageLarge fullyQualified=true var="leadImage" />
                                <img src="${leadImage}" alt="${cardProps.altText}" />
                            </picture>
                        </figure>
                    </div>
                <#elseif cardProps.svgXmlFromRepository?has_content>
                    <div class="nhsd-m-card__image_container">
                        <figure class="nhsd-a-image ${cardProps.imageClass?has_content?then(cardProps.imageClass, '')}">
                            <picture class="nhsd-a-image__picture">
                                <#if cardProps.altText?? && cardProps.altText?has_content>
                                    <img src="data:image/svg+xml;base64,${base64(colour(cardProps.svgXmlFromRepository, "005EB8"))}" alt="${cardProps.altText} icon" aria-hidden="true">
                                <#else>
                                    <img src="data:image/svg+xml;base64,${base64(colour(cardProps.svgXmlFromRepository, "000000"))}" aria-hidden="true">
                                </#if>
                            </picture>
                        </figure>
                    </div>
                </#if>
                <div class="nhsd-m-card__content_container">
                    <div class="nhsd-m-card__content-box">
                        <h5 class="nhsd-t-heading-s">${cardProps.title}</h5>
                        <#if cardProps.shortsummary?has_content>
                            <p class="nhsd-t-body-s">${cardProps.shortsummary}</p>
                        </#if>
                    </div>
                    <div class="nhsd-m-card__button-box">
                        <span class="nhsd-a-icon nhsd-a-icon--size-s nhsd-a-arrow">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                            </svg>
                      </span>
                    </div>
                </div>
            </div>
        </a>
        <#if cardProps.authorsInfo?has_content && cardProps.authorsInfo?size gt 0>
            <@authorSection cardProps.authorsInfo />
        </#if>
    </article>
</#macro>

<#macro authorSection authors>
    <div class="nhsd-m-card__author">
        <#if authors?size == 1>
            <#assign author = authors[0] />
            <div class="nhsd-m-author">
                <#if author.image?has_content>
                    <@hst.link hippobean=author.image.authorPhoto2x fullyQualified=true var="authorImage" />
                    <div class="nhsd-a-avatar" title="${author.name}" aria-label="${author.name}">
                        <figure class="nhsd-a-image nhsd-a-image--cover" aria-hidden="true">
                            <picture class="nhsd-a-image__picture">
                                <img itemprop="image"
                                     class="bloghub__item__content__author__img"
                                     src="${authorImage}"
                                     alt="${author.name}"/>
                            </picture>
                        </figure>
                    </div>
                </#if>

                <div class="nhsd-m-author__details">
                    <#if author.link?has_content>
                        <a href="${author.link}" class="nhsd-a-link nhsd-t-body-s">${author.name}</a>
                    <#elseif author.name?has_content>
                        <span class="nhsd-t-heading-xs nhsd-!t-margin-0 nhsd-!t-col-black">${author.name}</span>
                    </#if>
                    <p class="nhsd-t-body-s nhsd-!t-margin-top-2 nhsd-!t-margin-bottom-0 nhsd-!t-col-black">
                        <#if author.role?has_content>${author.role}</#if><#if author.role?has_content>, ${author.org}</#if>
                    </p>
                </div>
            </div>
        <#elseif authors?size gt 1>
            <div class="nhsd-m-avatar-list nhsd-!t-margin-right-2">
                <#list authors as author>
                    <#if author.image?has_content>
                        <@hst.link hippobean=author.image.authorPhoto2x fullyQualified=true var="authorImage" />
                        <a href="${author.link}" class="nhsd-a-avatar" title="${author.name}" aria-label="${author.name}">
                            <figure class="nhsd-a-image nhsd-a-image--cover" aria-hidden="true">
                                <picture class="nhsd-a-image__picture">
                                    <img itemprop="image"
                                         class="bloghub__item__content__author__img"
                                         src="${authorImage}"
                                         alt="${author.name}"/>
                                </picture>
                            </figure>
                        </a>
                    <#else>
                        <a href="${author.link}" class="nhsd-a-avatar nhsd-a-avatar--initials" title="${author.name}" aria-label="${author.name}">${author.initials}</a>
                    </#if>
                </#list>
            </div>
        </#if>
    </div>
</#macro>
