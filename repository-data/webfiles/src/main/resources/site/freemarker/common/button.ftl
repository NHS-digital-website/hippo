<#ftl output_format="HTML">

<#include "../include/imports.ftl">

<div class="nhsd-t-grid">
    <#if heading?has_content>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col">
                <h2 class="nhsd-t-heading-xl nhsd-t-text-align-center">${heading}</h2>
            </div>
        </div>
    </#if>
    <div class="nhsd-m-button-nav">
        <div class="nhsd-t-col nhsd-!t-text-align-${alignment}">
            <#if button1Title?has_content>
                <a class="nhsd-a-button" href="${button1Link}">
                    <span class="nhsd-a-button__label">${button1Title}</span>
                </a>
            </#if>
            <#if button2Title?has_content>
                <a class="nhsd-a-button nhsd-a-button--outline"
                   href="${button2Link}">
                    <span class="nhsd-a-button__label">${button2Title}</span>
                </a>
            </#if>
            <#if button3Title?has_content>
                <a class="nhsd-a-button nhsd-a-button--outline"
                   href="${button3Link}">
                    <span class="nhsd-a-button__label">${button3Title}</span>
                </a>
            </#if>
        </div>

    </div>
</div>
