<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#assign hasTopTasks = document.toptasks?has_content />

<#if hasTopTasks>
    <div class="article-section article-section--highlighted">
        <div class="callout callout--attention">
            <h2>Top tasks</h2>
            <div class="rich-text-content">
                <#list document.toptasks as toptask>
                    <@hst.html hippohtml=toptask contentRewriter=gaContentRewriter/>
                </#list>
            </div>
        </div>
    </div>
</#if>
