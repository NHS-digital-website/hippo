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

                <#assign mainTag = ""/>
                <#if options.dept?has_content>
                    <#assign mainTag = options.dept/>
                <#else>
                    <#assign mainTag = options.type />
                </#if>

                <#if mainTag?has_content>
                    <div class="tile-item__content-tag">
                        <p class="intra-info-tag intra-info-tag--bg-flat intra-info-tag--txt-black">${mainTag?upper_case}</p>
                    </div>
                </#if>

                <#if options.type?has_content>
                    <div class="tile-item__content-tag intra-info-tag--right">
                        <p class="intra-info-tag intra-info-tag--bg-grey">${options.type}</p>
                    </div>
                </#if>

                <h2 class="tile-item__content-title">
                    <#if options.linkType == "internal">
                        <#assign link><@hst.link hippobean=options.link/></#assign>
                    <#elseif options.linkType == "external">
                        <#assign link = options.link/>
                    <#else>
                        <#assign link = ""/>
                    </#if>
                    <a href="${link}">${title}</a>
                </h2>
            </div>
        </div>
    </#if>
</#macro>
