<#ftl output_format="HTML">
<#include "../heroes/quote-hero-content.ftl">
<#macro nhsdBannerContainer options mirrored = false>
    <div class="nhsd-o-banner ${mirrored?then("nhsd-o-banner--mirrored", "")} nhsd-o-hero  nhsd-o-hero--left-align nhsd-!t-text-align-left nhsd-!t-bg-yellow-20-tint data-banner-wrap">
        <#assign bgClass="">
        <#assign buttonClass="">
        <#assign textClass="nhsd-!t-text-align-left">
        <#if options.colour?has_content>
            <#if options.colour == "Blue grey">
                <#assign bgClass="nhsd-!t-bg-bright-blue-10-tint">
            <#elseif options.colour == "Black">
                <#assign bgClass="nhsd-!t-bg-black">
                <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
            <#elseif options.colour == "Dark Blue Multicolour">
                <#assign bgClass="nhsd-!t-bg-blue">
                <#assign textClass += "nhsd-o-hero--light-text nhsd-!t-col-white">
            <#elseif options.colour == "Mid blue">
                <#assign bgClass="nhsd-!t-bg-accessible-blue">
                <#assign textClass += " nhsd-o-hero--light-text nhsd-!t-col-white">
            <#elseif options.colour == "Light Blue" || options.colour == "Light blue">
                <#assign bgClass="nhsd-!t-bg-bright-blue-20-tint">
            <#elseif options.colour == "Yellow" || options.colour == "yellow">
                <#assign bgClass="nhsd-!t-bg-yellow-20-tint">
            </#if>
        </#if>
        <div class="nhsd-o-hero__content-container data-banner-img">
            <div class="nhsd-o-hero__inner-content-container">
                <#if options.quote?has_content>
                <@quoteHeroContent options/>
                <#else>
                    <p class="nhsd-t-body ${textClass}">${options.categoryInfo}</p>
                </#if>
                <h1 class="nhsd-t-heading-xxl nhsd-!t-margin-bottom-0 ${textClass}">${options.title}</h1>
                <div class="nhsd-!t-margin-top-6" data-uipath="document.summary">
                    <p>
                        <#if hst.isBeanType(content, 'org.hippoecm.hst.content.beans.standard.HippoHtml')>
                        <@hst.html hippohtml=options.summary contentRewriter=stripTagsContentRewriter />
                        <#else>
                            ${ options.summary }
                        </#if>
                    </p>
                </div>
                <#if options.buttons?has_content && options.buttons?size gt 0>
                    <nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed nhsd-!t-text-align-left nhsd-!t-margin-top-6">
                        <#list options.buttons as button>
                            <#if options.colour == "Dark Blue Multicolour">
                                <#assign buttonClass="nhsd-a-button--invert">
                            <#else >
                                <#assign buttonClass="nhsd-a-button">
                            </#if>
                            <a class="nhsd-a-button ${buttonClass}" href="${button.src}">
                                <span class="nhsd-a-button__label">${button.text}</span>
                                <#if button.srText?has_content>
                                    <span class="nhsd-t-sr-only">${button.srText}</span>
                                </#if>
                            </a>
                        </#list>
                    </nav>
                </#if>
            </div>
        </div>
        <div class="nhsd-a-digiblocks nhsd-a-digiblocks--pos-tr nhsd-a-digiblocks--col-yellow data-banner-img hero-banner-img">
             <#if options.video?has_content>
                 <div class="nhsd-o-banner__iframe-wrapper">
                     <iframe class="nhsd-o-banner__iframe" src="${options.video}" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
                 </div>
             <#else>
                 <figure class="nhsd-a-image nhsd-a-image--cover">
                     <picture class="nhsd-a-image__picture">
                        <#if options.image?has_content && options.image.src?has_content>
                            <img src="${options.image.src}" alt="<#if options.image.alt?has_content>${options.image.alt}</#if>">
                        <#else>
                            <img src="https://digital.nhs.uk/binaries/content/gallery/website/about-nhs-digital/fibre_57101102_med.jpg" alt="" />
                        </#if>
                     </picture>
                 </figure>
             </#if>
        </div>
    </div>
</#macro>

