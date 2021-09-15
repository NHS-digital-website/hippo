<#ftl output_format="HTML">

<#macro nhsdBanner options mirrored = false>
    <div class="nhsd-o-banner ${mirrored?then("nhsd-o-banner--mirrored", "")}">
        <div class="nhsd-o-banner__content-container nhsd-!t-bg-bright-blue-10-tint">
            <div class="nhsd-o-banner__inner-content-container">
                <span class="nhsd-t-heading-l">${options.title}</span>
                <#if options.summary?has_content>
                    <div class="nhsd-t-heading-s nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-0" data-uipath="${uiPath}.summary">
                        <#if hst.isBeanType(content, 'org.hippoecm.hst.content.beans.standard.HippoHtml')>
                            <@hst.html hippohtml=options.summary contentRewriter=stripTagsContentRewriter />
                        <#else>
                            ${ options.summary }
                        </#if>
                    </div>
                </#if>
                <#if options.buttons?has_content && options.buttons?size gt 0>
                    <nav class="nhsd-m-button-nav nhsd-m-button-nav--condensed nhsd-!t-text-align-left nhsd-!t-margin-top-6">
                        <#list options.buttons as button>
                            <a class="nhsd-a-button" href="${button.src}">
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

        <div class="nhsd-o-banner__image-container">
            <#if options.video?has_content>
                <div class="nhsd-o-banner__iframe-wrapper">
                    <iframe class="nhsd-o-banner__iframe" type="text/html" src="${options.video}" frameborder="0" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
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

