<#ftl output_format="HTML">
<#-- @ftlvariable name="dateFm" type="uk.nhs.digital.website.beans.EffectiveDate" -->

<#include "../../include/imports.ftl">

<#assign inArray="uk.nhs.digital.freemarker.InArray"?new() />

<#macro roadmapItemBox options>
    <#if options??>
        <div class="roadmapitem-box cta">
            <h3 class="cta__title">
                <#if options.link??>
                <a href="${options.link}">
                    </#if>
                    <span data-uipath="website.roadmap.${options.title}">${options.title}</span>
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

            <#if options.status??>
                <div class="align-middle">
                    <#if inArray(options.status?lower_case, ['complete'])>
                        <img src="<@hst.webfile path="/images/icon/Icon-tick-v3_v2.svg"/>"
                             alt="Tick Image" alt="Tick"
                             class="roadmap-status-icon"/> ${options.status?cap_first}
                    <#elseif inArray(options.status?lower_case, ['cancelled', 'superseded']) >
                        <img src="<@hst.webfile path="/images/icon/Icon-cross-v2_v2.svg"/>"
                             alt="Tick Image" alt="Tick"
                             class="roadmap-status-icon"/> ${options.status?cap_first}
                    </#if>
                </div>
            </#if>

            <#if options.category??>
                <#list options.category as category>
                    <#if selectedTypes?size == 0>
                        <span class="tag-link">${category.name?cap_first}</span>
                    <#else>
                        <#assign linkout = "&type=" + selectedTypes?join("&type=") + "&order-by=" + options.order + "-date" />
                            <span class="tag-link">${category.name?cap_first}</span>
                    </#if>
                </#list>
            </#if>
        </div>
    </#if>
</#macro>
