<#ftl output_format="HTML">

<#macro graphicBlock options>
    <div class="nhsd-m-graphic-block">
        <#if options.image?has_content>
            <div class="nhsd-m-graphic-block__picture">
                <figure class="nhsd-a-image nhsd-a-image--square">
                    <picture class="nhsd-a-image__picture">
                        <img src="${options.image}" alt="${options.altText}" />
                    </picture>
                </figure>
            </div>
        </#if>

        <#if options.heading?has_content || options.headlineDesc?has_content>
            <div class="nhsd-m-graphic-block__heading">
                <#if options.heading?has_content>
                    <span class="nhsd-t-heading-xl nhsd-!t-margin-bottom-0">
                        ${options.heading}
                    </span>
                </#if>
                <#if options.headlineDesc?has_content>
                    <span class="nhsd-t-heading-xs">
                        ${options.headlineDesc}
                    </span>
                </#if>
            </div>
        </#if>

        <#if options.furtherInfo?has_content>
            <p class="nhsd-t-body-s">${options.furtherInfo}</p>
        </#if>

        <#if link?has_content>
            <div class="nhsd-!t-margin-top-3 nhsd-!t-margin-bottom-3">
                <a class="nhsd-a-link" href="${options.link.href}">
                    <span class="nhsd-a-link__label nhsd-t-body-s">${options.link.title}</span>
                    <#if options.link.external>
                        <@fmt.message key="text.sr-only-link" var="srOnlyLinkText" />
                        <span class="nhsd-t-sr-only">${srOnlyLinkText}</span>
                    </#if>
                </a>
            </div>
        </#if>
    </div>
</#macro>