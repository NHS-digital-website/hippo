<#ftl output_format="HTML">
<#include "../../../include/imports.ftl">

<#macro simpleHeroContent options>
    <#assign uiPath = options.uiPath?has_content?then(options.uiPath, "website.hero") />

    <#if options.introText?has_content>
        <p class="nhsd-t-body">${options.introText?no_esc}</p>
    </#if>
    <#if options.categoryInfo?has_content && !options.quote?has_content>
        <p class="nhsd-t-body">${options.categoryInfo}</p>
    </#if>
    <#if options.title?has_content>
        <#assign heroHeadingLevel = headingLevel?has_content?then(headingLevel, 1) />
        <h${heroHeadingLevel} class="nhsd-t-heading-xxl nhsd-!t-margin-bottom-0"
                              data-uipath="document.title"
                              itemprop="name">${options.title}</h${heroHeadingLevel}>
    </#if>

    <#if options.summary?has_content>
        <#if hst.isBeanType(options.summary, 'org.hippoecm.hst.content.beans.standard.HippoHtml')>
            <@hst.html var="htmlSummary" hippohtml=options.summary contentRewriter=brContentRewriter />
            <#if htmlSummary?has_content>
                <div
                    class="${options.title?has_content?then('nhsd-!t-margin-top-6', '')}"
                    data-uipath="document.summary">${htmlSummary?no_esc}</div>
            </#if>
        <#else>
            <div class="nhsd-t-body nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-0"
                 data-uipath="document.summary">${options.summary}</div>
        </#if>
    </#if>

    <#if (document.class.name?contains("FeedHub"))!false>
        <div class="nhsd-t-body nhsd-!t-margin-top-6 nhsd-!t-margin-bottom-0"
             data-uipath="document.ctabutton">
            <#if document.ctabutton?? && document.ctabutton.items[0]?has_content>
                <#assign ctaLink = document.ctabutton.items[0] />
                <#if ctaLink.linkType == "internal">
                    <a class="nhsd-a-button nhsd-!t-margin-1 nhsd-a-button--invert"
                       href="<@hst.link hippobean=ctaLink.link/>">
                    <span
                        class="nhsd-a-button__label">${document.ctabutton.title}</span>
                    </a>
                <#elseif ctaLink.linkType == "external">
                    <a class="nhsd-a-button nhsd-!t-margin-1 nhsd-a-button--invert"
                       href="${ctaLink.link}">
                    <span
                        class="nhsd-a-button__label">${document.ctabutton.title}</span>
                    </a>
                </#if>
            </#if>
            <#if document.ctabutton1?? && document.ctabutton1.items[0]?has_content>
                <#assign ctaLink1 = document.ctabutton1.items[0] />
                <#if ctaLink1.linkType == "internal">
                    <a class="nhsd-a-button nhsd-!t-margin-1 nhsd-a-button--invert"
                       href="<@hst.link hippobean=ctaLink1.link/>">
                    <span
                        class="nhsd-a-button__label">${document.ctabutton1.title}</span>
                    </a>
                <#elseif ctaLink1.linkType == "external">
                    <a class="nhsd-a-button nhsd-!t-margin-1 nhsd-a-button--invert"
                       href="${ctaLink1.link}">
                    <span
                        class="nhsd-a-button__label">${document.ctabutton1.title}</span>
                    </a>
                </#if>
            </#if>
        </div>
    </#if>

    <#nested />

    <#if options.buttons?has_content && options.buttons?size gt 0>
        <#assign buttonAlignmentClass = "nhsd-!t-text-align-left"/>
        <#if options.alignment?has_content && options.alignment == "centre">
            <#assign buttonAlignmentClass = "nhsd-!t-text-align-center"/>
        </#if>
        <nav
            class="nhsd-m-button-nav nhsd-m-button-nav--condensed ${buttonAlignmentClass} nhsd-!t-margin-top-6">
            <#list options.buttons as button>
                <#assign buttonClasses = ""/>
                <#assign buttonClasses += button.type?has_content?then("nhsd-a-button--${button.type} ", "")/>
                <#assign buttonClasses += button.classes?has_content?then(button.classes, "")/>
                <#if options.colour?has_content && options.colour == "Dark Blue Multicolour">
                    <#assign buttonClasses += "nhsd-a-button--invert"/>
                </#if>
                <a class="nhsd-a-button  ${buttonClasses}"
                    href="${button.src?has_content?then(button.src, "#")}"
                    ${button.target?has_content?then("target=\"${button.target}\"","")}
                >
                    <span class="nhsd-a-button__label">${button.text}</span>
                    <#if button.srText?has_content>
                        <span class="nhsd-t-sr-only">${button.srText}</span>
                    </#if>
                </a>
            </#list>
        </nav>
    </#if>
</#macro>
