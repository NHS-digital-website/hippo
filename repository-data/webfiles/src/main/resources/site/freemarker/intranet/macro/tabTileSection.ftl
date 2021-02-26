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

                <h5 class="nhsd-t-heading-s nhsd-!t-margin-0">
                    <#if options.linkType == "internal">
                        <#assign link><@hst.link hippobean=options.link/></#assign>
                    <#elseif options.linkType == "external">
                        <#assign link = options.link/>
                    </#if>

                    <#if options.linkType??>
                        <a class="nhsd-a-link" style="border-bottom:none" href="${link}" intra-result-rank="${indexId}" intra-result-type="${options.type?lower_case}">
                    </#if>
                        ${title}
                    <#if options.linkType??>
                        </a>
                    </#if>
                </h5>

                <#if mainTag?has_content>
                    <p class="nhsd-t-body">${mainTag}</p>
                </#if>

                <#if options.type?has_content>
                    <span class="nhsd-a-tag nhsd-a-tag--bg-light-grey">${options.type}</span>
                </#if>
            </div>
        </div>
    </#if>
</#macro>
