<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include '../macro/docGetImage.ftl'>
<#include '../macro/gridColumnGenerator.ftl'>
<#include '../macro/iconGenerator.ftl'>
<#include '../macro/cardItem.ftl'>

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
                    <#assign itemProps = item />
                    <#assign itemProps += {
                        "image": imageData[0],
                        "altText": imageDataAlt,
                        "imageClass": "nhsd-a-image--cover",
                        "cardClass": "nhsd-m-card--full-height nhsd-!t-padding-bottom-3 nhsd-!t-padding-bottom-s-6",
                        "link": linkDestination,
                        "background": "pale-grey",
                        "featured": true
                    }/>
                    <#if item?index < 1>
                        <@cardItem itemProps />
                        <#if item?has_next>
                            </div>
                            <div class="nhsd-t-col-s-4">
                        </#if>
                    <#else>
                        <#assign itemProps += {
                            "image": "",
                            "altText": "",
                            "shortsummary": "",
                            "cardClass": "nhsd-!t-padding-bottom-3 nhsd-!t-padding-bottom-s-6",
                            "featured": false
                        }/>
                        <@cardItem itemProps />
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




