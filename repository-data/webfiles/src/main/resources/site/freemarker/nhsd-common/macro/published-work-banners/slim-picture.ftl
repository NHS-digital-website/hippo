<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">
<#include "../../macros/header-banner.ftl">

<#macro slimPicture config>
    <#local document = config.document />

    <#if config.bannerImage == "">
        <@headerBanner document />
    <#else>
        <#local bannerImage = config.bannerImage />
        <#local bannerImageAltText = config.bannerImageAltText />
        <#if config.topText??>
            <#local topText = config.topText />
        <#else>
            <#local topText = "" />
        </#if>
        <#if config.topTextLink??>
            <#local topTextLink = config.topTextLink />
        <#else>
            <#local topTextLink = "" />
        </#if>
        <#local hasSummaryContent = document.summary?? && document.summary.content?has_content />

        <div class="nhsd-o-hero nhsd-!t-bg-grad-black nhsd-!t-col-white nhsd-o-hero--image-banner">
            <div class="nhsd-t-grid nhsd-!t-no-gutters  nhsd-t-grid--full-width">
                <div class="nhsd-t-row">
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6">
                        <div class="nhsd-o-hero__content-box">
                            <div class="nhsd-o-hero__content">

                                <#if
                                    topText?is_string && topText?length gt 0 &&
                                    topTextLink?is_string && topTextLink?length gt 0>
                                    <p class="hero-module__toptext">
                                        Part of 
                                        <a class="nhsd-a-link nhsd-a-link--col-white" href=${topTextLink}>
                                            ${topText}
                                        </a>
                                    </p>
                                <#elseif topText?is_string && topText?length gt 0>
                                    <p class="hero-module__toptext">Part of ${topText}</p>
                                </#if>

                                <span class="nhsd-t-heading-xl nhsd-!t-col-white" data-uipath="document.title">${document.title}</span>
                                
                                <#if hasSummaryContent>
                                    <span data-uipath="website.publishedwork.summary"
                                    >
                                    <@hst.html hippohtml=document.summary contentRewriter=brContentRewriter/>
                                    </span>
                                </#if>
                            </div>
                        </div>
                    </div>

                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-!t-no-gutters">
                        <figure class="nhsd-a-image nhsd-a-image--contain" aria-hidden="true">
                            <picture class="nhsd-a-image__picture">
                                <img src="${bannerImage}" alt="${bannerImageAltText}">
                            </picture>
                        </figure>
                    </div>
                </div>
            </div>
        </div>
    </#if>
</#macro>
