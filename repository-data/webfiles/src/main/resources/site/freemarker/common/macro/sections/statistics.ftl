<#ftl output_format="HTML">

<#include 'statistic.ftl'>

<#macro statistics section>
    <#local heading = section.heading />
    <#local colourScheme = section.colourScheme />
    <#local animated = section.animation />

    <#local headingTag = 'h3' />
    <#if section.headingLevel == 'main'>
        <#local headingTag = 'h2' />
    </#if>

    <#local colourClass = "statistics-section--${colourScheme}" />
    <#local animatedClass = "statistics-section--${animated}" />
    <#local containerClasses = ["statistics-section", colourClass, animatedClass] />
    <div class="${containerClasses?join(' ')}">
        <#if section.heading?has_content>
            <${headingTag} id="${slugify(heading)}" class="statistics-section__heading">${heading}</${headingTag}>
        </#if>
        <#if section.modules?size gt 0 >
            <div class="statistics-section__modules">
                <#list section.modules as stat>
                    <@statistic stat/>
                </#list>
            </div>
        </#if>
    </div>
</#macro>
