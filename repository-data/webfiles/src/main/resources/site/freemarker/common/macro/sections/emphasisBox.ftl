<#ftl output_format="HTML">

<#macro emphasisBox section>
    <#assign slug = 'emphasis-' + section.emphasisType + '-' + section?keep_after('@') />

    <#if section.heading?has_content>
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

    <div class="emphasis-box emphasis-box-${slugify(section.emphasisType)}" ${ariaAttribute}="${ariaValue}">
        <#if section.image??>
            <div class="emphasis-box__image">
                <@hst.link hippobean=section.image fullyQualified=true var="iconImage" />
                <#if iconImage?ends_with("svg")>
                    <img src="${iconImage?replace("/binaries", "/svg-magic/binaries")}?colour=005eb8" alt="${section.heading}" />
                <#else>
                    <img src="${iconImage}" alt="${section.heading}" />
                </#if>
            </div>
        </#if>

        <div class="emphasis-box__content">
            <#if section.heading?has_content>
                <h3 id="${slugify(slug)}" data-uipath="website.contentblock.emphasis.heading">${section.heading}</h3>
            </#if>

            <#if section.body.content?has_content>
                <div data-uipath="website.contentblock.emphasis.content"><@hst.html hippohtml=section.body contentRewriter=gaContentRewriter /></div>
            </#if>
        </div>
    </div>
</#macro>
