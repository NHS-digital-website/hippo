<#ftl output_format="HTML">
<#-- @ftlvariable name="dateFm" type="uk.nhs.digital.website.beans.EffectiveDate" -->
<#include "../../include/imports.ftl">

<#macro roadmapItemBox options>
    <#if options??>
        <div class="roadmapitem-box cta">
            <#--<details>
                <summary><span data-uipath="website.roadmap.${options.title}">${options.title}</span></summary>
                    <div class="details-body">-->

            <h3 class="cta__title">
                <#if options.link??>
                    <a href="${options.link}">
                </#if>
                    <span data-uipath="website.roadmapitem.link.${options.title}">${options.title}</span>
                <#if options.link??>
                    </a>
                </#if>
            </h3>

            <p class="cta__meta">
                <time datetime="${options.datetime}">${options.datelabel}</time>
            </p>

            <#if options.text??>
                <p class="cta__text">${options.text}</p>
            </#if>

                        <#if options.category??>
                         <#list options.category as category>
                            <div ><span>${category.name?cap_first}</span></div>
                         </#list>
                        </#if>

                        <#--<#if options.monthsDuration?? && options.monthsDuration?has_content >
                           <div class="months-display"
                            <#list options.monthsDuration as months>
                                <span>${months}</span><#sep> |
                             </#list>
                           </div>
                        </#if>-->
                   <#-- </div>
            </details>-->
        </div>
    </#if>
</#macro>
