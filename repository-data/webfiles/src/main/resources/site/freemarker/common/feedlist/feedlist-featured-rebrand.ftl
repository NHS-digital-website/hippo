<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include '../macro/feedlist-getimage.ftl'>
<#include '../macro/gridColumnGenerator.ftl'>
<#include '../macro/iconGenerator.ftl'>

<div class="nhsd-t-grid nhsd-!t-margin-bottom-9">
    <#if titleText?has_content>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col">
                <h2 class="nhsd-t-heading-xl nhsd-t-text-align-center nhsd-!t-margin-bottom-7">${titleText}</h2>
            </div>
        </div>
    </#if>

    <#if pageable.items??>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-s-8">
                <#list pageable.items as item>
                    <#assign imageData = getImageData(item) />
                    <#assign imageDataAlt = (imageData[1]?has_content)?then(imageData[1], 'homepage-news-article-img-${item?index}') />
                    <@hst.link hippobean=item var="linkDestination"/>
                    <#if item?index < 1>
                        <article class="nhsd-m-card nhsd-!t-padding-bottom-3 nhsd-!t-padding-bottom-s-6 nhsd-m-card--image-position-adjacent nhsd-m-card--full-height">
                            <a href="${linkDestination}" class="nhsd-a-box-link nhsd-a-box-link--focus-orange" aria-label="${item.title}">
                                <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                    <div class="nhsd-m-card__image_container">
                                        <#if imageData[0]?has_content>
                                            <figure class="nhsd-a-image nhsd-a-image--cover">
                                                <picture class="nhsd-a-image__picture">
                                                    <@hst.link hippobean=imageData[0].original fullyQualified=true var="leadImage" />
                                                    <img src="${leadImage}" alt="${imageDataAlt}">
                                                </picture>
                                            </figure>
                                        </#if>
                                    </div>
                                    <div class="nhsd-m-card__content_container">
                                        <div class="nhsd-m-card__content-box">
                                            <#if item.publisheddatetime?has_content>
                                                <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="d MMMM yyyy" var="publishedDateTimeString" timeZone="${getTimeZone()}" />
                                                <span class="nhsd-m-card__date">${publishedDateTimeString}</span>
                                            </#if>
                                            <#if item.title?has_content>
                                                <h3 class="nhsd-t-heading-s" id="homepage-news-article-title-0">${item.title}</h3>
                                            </#if>
                                            <#if item.shortsummary?has_content>
                                                <p class="nhsd-t-body-s">${item.shortsummary}</p>
                                            </#if>
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
                        <#if item?has_next>
                            </div>
                            <div class="nhsd-t-col-s-4">
                        </#if>
                    <#else>
                        <article class="nhsd-m-card nhsd-!t-padding-bottom-3 nhsd-!t-padding-bottom-s-6">
                            <a href="${linkDestination}" class="nhsd-a-box-link nhsd-a-box-link--focus-orange" aria-label="${item.title}">
                                <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                    <div class="nhsd-m-card__content_container">
                                        <div class="nhsd-m-card__content-box">
                                            <#if item.publisheddatetime?has_content>
                                                <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="d MMMM yyyy" var="publishedDateTimeString" timeZone="${getTimeZone()}" />
                                                <span class="nhsd-m-card__date">${publishedDateTimeString}</span>
                                            </#if>
                                            <#if item.title?has_content>
                                                <h3 class="nhsd-t-heading-s" id="homepage-news-article-title-${item?index}">${item.title}</h3>
                                            </#if>
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
                </#list>
            </div>
        </div>
    </#if>

    <#assign hasPrimaryButton = (buttonText?has_content && buttonDestination?has_content) />
    <#assign hasSecondaryButton = (secondaryButtonText?has_content && secondaryButtonDestination?has_content) />

    <#if hasPrimaryButton || hasSecondaryButton>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col">
                <nav class="nhsd-m-button-nav">
                    <#if hasPrimaryButton>
                        <a class="nhsd-a-button" href="${buttonDestination}">
                            <span class="nhsd-a-button__label">${buttonText}</span>
                            <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                        </a>
                    </#if>
                    <#if hasSecondaryButton>
                        <a class="nhsd-a-button nhsd-a-button--outline" href="${secondaryButtonDestination}">
                            <span class="nhsd-a-button__label">${secondaryButtonText}</span>
                            <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                        </a>
                    </#if>
                </nav>
            </div>
        </div>
    </#if>
</div>




