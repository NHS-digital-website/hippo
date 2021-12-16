<#ftl output_format="HTML">

<#include 'statistic.ftl'>

<#macro statistics section>
    <#local heading = section.heading />
    <#local colourScheme = section.colourScheme />
    <#local animated = section.animation />

    <#if section.headingLevel?has_content && section.headingLevel == 'Main heading'>
        <#local headingTag = "h2" />
        <#local headingClass = "nhsd-t-heading-xl" />
    <#else>
        <#local headingTag = "h3" />
        <#local headingClass = "nhsd-t-heading-l" />
    </#if>

    <#if section.heading?has_content>
        <${headingTag} id="${slugify(heading)}" class=${headingClass}>${heading}</${headingTag}>
    </#if>

    <#list section.modules>
        <div class="nhsd-o-statistics-block-list">
            <div class="nhsd-t-grid nhsd-t-grid--nested">
                <div class="nhsd-t-row nhsd_o_statistics-block-list__item">
                    <#items as stat>
                        <div class="nhsd-t-col-xs-12 <#if section.modules?size gt 1>nhsd-t-col-s-6</#if>">
                            <div class="nhsd_o_statistics-block-list__container">
                                <#if colourScheme == "single">
                                    <@statistic stat stat?item_cycle('1')/>
                                <#elseif colourScheme == "muted">
                                    <@statistic stat stat?item_cycle('1', '2', '3')/>
                                <#elseif colourScheme == "bold" || colourScheme == "boldtakeover">
                                    <@statistic stat stat?item_cycle('4', '5', '6')/>
                                </#if>
                            </div>
                        </div>
                    </#items>
                </div>
            </div>
        </div>
    </#list>
</#macro>
