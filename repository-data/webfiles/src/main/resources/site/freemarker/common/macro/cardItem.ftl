<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro cardItem cardProps>
    <#assign cardClass = ""/>
    <#if cardProps.featured?? && cardProps.featured && cardProps.image?has_content>
        <#assign cardClass = "nhsd-m-card--image-position-adjacent nhsd-m-card--full-height"/>
    </#if>
    <article class="nhsd-m-card ${cardClass} ${cardProps.cardClass?has_content?then(cardProps.cardClass, '')}">
        <a href="${cardProps.link}" class="nhsd-a-box-link">
            <div class="nhsd-a-box ${cardProps.background?has_content?then("nhsd-!t-bg-" + cardProps.background, "")}">
                <#if cardProps.image?has_content>
                    <div class="nhsd-m-card__image_container">
                        <figure class="nhsd-a-image ${cardProps.imageClass?has_content?then(cardProps.imageClass, '')}">
                            <picture class="nhsd-a-image__picture">
                                <@hst.link hippobean=cardProps.image.original fullyQualified=true var="leadImage" />
                                <img src="${leadImage}" alt="${cardProps.altText}" />
                            </picture>
                        </figure>
                    </div>
                </#if>
                <div class="nhsd-m-card__content_container">
                    <div class="nhsd-m-card__content-box">
                        <h1 class="nhsd-t-heading-s">${cardProps.title}</h1>
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
    </article>
</#macro>
