<#ftl output_format="HTML">
<#include "../../../../include/imports.ftl">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro __caseStudyImage options>
    <div class="nhsd-o-case-study__image-container nhsd-t-round">
        <#assign imageSrc = 'https://digital.nhs.uk/binaries/content/gallery/website/about-nhs-digital/fibre_57101102_med.jpg' />
        <#if options.image?has_content && options.image.src?has_content>
            <#assign imageSrc = options.image.src />
        <#elseif options.image?has_content && options.image.bean?has_content>
            <@hst.link hippobean=options.image.bean var="image" />
            <#assign imageSrc = image />
        </#if>
        <img class="nhsd-a-image nhsd-a-image--cover" src="${imageSrc}" alt="${options.image.alt?has_content?then(options.image.alt, '')}">
    </div>
</#macro>

<#macro caseStudy options>
    <#assign mirroredClass = (options.mirrored?has_content && options.mirrored)?then("nhsd-o-case-study--mirrored", "") />
    <#assign caseStudyImageSection><@__caseStudyImage options /></#assign>

    <article class="nhsd-o-case-study nhsd-o-case-study--no-label nhsd-!t-margin-bottom-9 ${mirroredClass}">
        <div class="nhsd-o-case-study__wrapper nhsd-t-row--centred">
            <div class="nhsd-o-case-study__content-box">
                <div class="nhsd-o-case-study__contents">
                    <#nested caseStudyImageSection />
                </div>
            </div>
            <div class="nhsd-o-case-study__picture-skeleton"></div>
        </div>
    </article>
</#macro>
