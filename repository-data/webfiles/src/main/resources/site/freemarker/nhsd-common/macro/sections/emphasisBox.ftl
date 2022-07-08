<#ftl output_format="HTML">

<#assign base64="uk.nhs.digital.freemarker.utils.StringToBase64"?new() />
<#assign colour="uk.nhs.digital.freemarker.svg.SvgChangeColour"?new() />

<#macro emphasisBox section>
    <#if section?is_string >
      <#assign slug = 'emphasis-' + section.emphasisType + '-' + section?keep_after('@') />
    <#else>
      <#assign slug = 'emphasis-' + section.emphasisType />
    </#if>

    <#if section.heading?has_content && slug??>
        <#assign ariaAttribute = 'aria-labelledby' />
        <#assign ariaValue = slugify(slug) />
    <#else>
        <#assign ariaAttribute = 'aria-label' />
        <#assign ariaValue = section.heading />
        <#if section.emphasisType == 'Warning'>
            <#assign ariaValue = 'Warning' />
        <#elseif section.emphasisType == 'Important'>
            <#assign ariaValue = 'Important Information' />
        <#elseif section.emphasisType == 'Emphasis'>
            <#assign ariaValue = 'Highlighted Information' />
        <#elseif section.emphasisType == 'Note'>
            <#assign ariaValue = 'Information' />
        </#if>
    </#if>

    <#if section.emphasisType == 'Warning'>
        <#assign borderColour = 'red' />
    <#elseif section.emphasisType == 'Important'>
        <#assign borderColour = 'yellow' />
    <#elseif section.emphasisType == 'Emphasis'>
        <#assign borderColour = 'blue' />
    <#elseif section.emphasisType == 'Note'>
        <#assign borderColour = 'grey' />
    </#if>

    <section class="nhsd-m-emphasis-box nhsd-m-emphasis-box--${slugify(section.emphasisType)} nhsd-!t-margin-bottom-6" ${ariaAttribute}="${ariaValue}">
        <div class="nhsd-a-box nhsd-a-box--border-${borderColour}">

            <#if section.image??>
                <div class="nhsd-m-emphasis-box__image-box">
                    <figure class="nhsd-a-image">
                        <picture class="nhsd-a-image__picture">
                            <@hst.link hippobean=section.image fullyQualified=true var="iconImage" />
                            <#if iconImage?ends_with("svg")>
                                <#if section.heading?? && section.heading?has_content>
                                    <img src="data:image/svg+xml;base64,${base64(colour(section.svgXmlFromRepository, "231f20"))}" alt="${section.heading}" style="object-fit:fill" />
                                <#else>
                                    <img src="data:image/svg+xml;base64,${base64(colour(section.svgXmlFromRepository, "231f20"))}" alt="" style="object-fit:fill" />
                                </#if>
                            <#else>
                                <img src="${iconImage}" alt="${section.heading}" style="object-fit:contain" />
                            </#if>
                        </picture>
                    </figure>
                </div>
            </#if>

            <div class="nhsd-m-emphasis-box__content-box">

                <#if section.heading?has_content && slug??>
                    <p class="nhsd-t-heading-s nhsd-t-word-break" id="${slugify(slug)}" data-uipath="website.contentblock.emphasis.heading">${section.heading}</p>
                </#if>

                <#if section.body?? && section.body.content?has_content>
                    <div data-uipath="website.contentblock.emphasis.content" class="nhsd-t-word-break"><@hst.html hippohtml=section.body contentRewriter=brContentRewriter /></div>
                <#elseif section.bodyCustom??>
                    <div data-uipath="website.contentblock.emphasis.content">
                        <p class="nhsd-t-body-s nhsd-t-word-break">${section.bodyCustom}</p>
                    </div>
                </#if>

            </div>
        </div>
    </section>
</#macro>
