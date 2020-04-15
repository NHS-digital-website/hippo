<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro tabTileSection options>
    <#if options??>
        <#if options.title??>
            <#assign title = options.title>
        </#if>
        <#if options.linkType == "internal">
            <#assign title = options.link.title>
        </#if>

        <div class="tile-item">
            <div class="tile-item__content">

                <div class="tile-item__content-tags">
                    <#if options.type?has_content>
                        <p class="tile-item__tag tile-item__tag--left">${options.dept?upper_case}</p>

                        <div class="tile-item__tag tile-item__tag--right">
                            <p class="tile-item__tag--boxed">${options.type}</p>
                        </div>
                    </#if>
                </div>

                <h2 class="tile-item__content-title">
                    <#if options.linkType == "internal">
                    <a href="<@hst.link hippobean=options.link />">
                        <#elseif options.linkType == "external">
                    <a href="${options.link}">
                        </#if>
                        ${title}
                    </a>
                </h2>
            </div>
        </div>
    </#if>
</#macro>
