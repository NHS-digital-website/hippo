<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#--
Macro for Card Item

Params:

image                   Image   An image object
alttext                 String  Image alt text
feedItemTitle           String  Item Title
feedItemShortSummary    String  Short summary
linkDestination         String  Where to link the entire item
cardColour=""           String  (Optional) Color token
-->
<#macro cardItem
    image
    alttext
    itemTitle
    itemShortSummary
    linkDestination
    cardColour=""
>
    <article class="nhsd-m-card nhsd-m-card--full-height">
        <a href="${linkDestination}" class="nhsd-a-box-link">
            <div class="nhsd-a-box ${cardColour?has_content?then("nhsd-!t-bg-" + cardColour, "")}">
                <#if image?has_content>
                    <div class="nhsd-m-card__image_container">
                        <figure class="nhsd-a-image">
                            <picture class="nhsd-a-image__picture">
                                <@hst.link hippobean=image.original fullyQualified=true var="leadImage" />
                                <img src="${leadImage}" alt="${alttext}" />
                            </picture>
                        </figure>
                    </div>
                </#if>
                <div class="nhsd-m-card__content_container">
                    <div class="nhsd-m-card__content-box">
                        <h1 class="nhsd-t-heading-s">${itemTitle}</h1>
                        <#if itemShortSummary?has_content>
                            <p class="nhsd-t-body-s">${itemShortSummary}</p>
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
