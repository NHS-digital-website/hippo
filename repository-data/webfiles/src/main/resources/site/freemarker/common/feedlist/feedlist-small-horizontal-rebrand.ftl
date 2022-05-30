<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include '../macro/docGetImage.ftl'>
<#include '../macro/gridColumnGenerator.ftl'>
<#include '../macro/iconGenerator.ftl'>
<#include '../macro/cardItem.ftl'>

<@hst.setBundle basename="rb.generic.texts"/>
<@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />

<div class="nhsd-o-card-list nhsd-!t-margin-bottom-9">
    <div class="nhsd-t-grid">
        <#if titleText?has_content>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <h2 class="nhsd-t-heading-xl nhsd-t-text-align-center nhsd-!t-margin-bottom-7">${titleText}</h2>
                </div>
            </div>
        </#if>

        <#if pageable.items??>
            <div class="nhsd-t-row nhsd-o-card-list__items nhsd-t-row--centred">
                <#list pageable.items as item>
                    <#assign imageData = getImageData(item) />
                    <@hst.link hippobean=item var="linkDestination"/>
                    <div class="nhsd-t-col-xs-12 ${getGridCol(pageable.items?size)}">
                        <#assign itemProps = item />
                        <#assign itemProps += {
                            "image": imageData[0],
                            "altText": imageData[1],
                            "shortsummary": item.shortsummary,
                            "link": linkDestination,
                            "background": "pale-grey"
                        }/>
                        <@cardItem itemProps />
                    </div>
                </#list>
            </div>
        </#if>

        <#assign hasPrimaryButton = (buttonText?has_content && buttonDestination?has_content) />
        <#assign hasSecondaryButton = (secondaryButtonText?has_content && secondaryButtonDestination?has_content) />

        <#if hasPrimaryButton || hasSecondaryButton>
            <div class="nhsd-t-row">
                <div class="nhsd-t-col">
                    <nav class="nhsd-m-button-nav nhsd-t-flex--justify-content-centre">
                        <#if hasPrimaryButton>
                            <a class="nhsd-a-button " href="${buttonDestination}">
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
</div>




