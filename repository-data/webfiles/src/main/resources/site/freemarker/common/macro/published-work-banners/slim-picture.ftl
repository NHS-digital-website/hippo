<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro slimPicture document bannerImage bannerImageAltText="">
    <div class="banner-image" aria-label="Document Header" style="background-image: url(${bannerImage});">
        <#if document.bannerImageAltText??>
            <#-- Add some descriptive text to the otherwise inaccessible background image -->
            <span role="img" aria-label="${bannerImageAltText}"> </span>
        </#if>
        <div class="grid-wrapper">
            <div class="grid-row">
                <div class="column column--reset banner-image-title">
                    <div class="banner-image-title-background">
                        <h1 data-uipath="document.title">${document.title}</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>
