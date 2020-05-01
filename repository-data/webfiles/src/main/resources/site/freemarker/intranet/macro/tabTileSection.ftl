<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro tabTileSection options indexId>
    <#if options??>
        <#assign title = options.title???then(options.title, "") />

        <div class="tile-item">
            <div class="tile-item__content">

                <#assign mainTag = ""/>
                <#if options.dept?has_content>
                    <#assign mainTag = options.dept/>
                </#if>

                <#if mainTag?has_content>
                    <div class="tile-item__content-tag">
                        <p class="intra-info-tag intra-info-tag--bg-flat intra-info-tag--txt-black">${mainTag?upper_case}</p>
                    </div>
                </#if>

                <#if options.type?has_content>
                    <div class="tile-item__content-tag intra-info-tag--inline-right">
                        <p class="intra-info-tag intra-info-tag--bg-grey">${options.type}</p>
                    </div>
                </#if>

                <h2 class="tile-item__content-title" intra-result-rank="${indexId}" intra-result-type="${options.type?lower_case}">
                    <#if options.linkType == "internal">
                        <#assign link><@hst.link hippobean=options.link/></#assign>
                    <#elseif options.linkType == "external">
                        <#assign link = options.link/>
                    </#if>

                    <#if options.linkType??>
                        <a href="${link}">
                    </#if>
                        ${title}
                    <#if options.linkType??>
                        </a>
                    </#if>
                </h2>
            </div>
        </div>
    </#if>
</#macro>
