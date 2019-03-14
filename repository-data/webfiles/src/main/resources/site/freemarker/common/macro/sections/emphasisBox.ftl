<#ftl output_format="HTML">

<#macro emphasisBox section>
    <#assign slug = "emphasis-" + section.emphasisType + "-" + section?keep_after('@') />

    <#if section.heading?has_content>
        <#assign ariaAttribute = " aria-labelledby=" + slugify(slug) />
    <#else>
        <#assign ariaAttribute = " aria-label=" + section.heading + "" />
        <#if section.emphasisType == 'Warning'>
            <#assign ariaAttribute = ' aria-label=Warning' />
        <#elseif section.emphasisType == 'Important'>
            <#assign ariaAttribute = ' aria-label=Important information' />
        <#elseif section.emphasisType == 'Emphasis'>
            <#assign ariaAttribute = ' aria-label=Highlighted information' />
        <#elseif section.emphasisType == 'Note'>
            <#assign ariaAttribute = ' aria-label=Information' />
        </#if>
    </#if>

    <div class="emphasis-box emphasis-box-${slugify(section.emphasisType)}"${ariaAttribute}>
        <#if section.image??>
            <div class="emphasis-box__image">
                <img src="<@hst.link hippobean=section.image />" alt="" class="svg" />
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
