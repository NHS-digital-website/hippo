<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro trendingArticleShortSummary trending index>
    <#if trending.docType == 'Blog' || trending.docType == 'NewsInternal'>
        <#if trending.shortsummary?? && trending.shortsummary?has_content>
            <div class="intra-home-article-grid-article__summary"
                 id="summary-${index}">
                <p>${trending.shortsummary}</p>
            </div>

        </#if>
    <#elseif trending.docType == 'Announcement'>
        <#if trending.details?? && trending.details?has_content>
            <div class="intra-home-article-grid-article__summary"
                 id="summary-${index}">
                <@hst.html hippohtml=trending.details />
            </div>
        </#if>
    </#if>
    <script>
        webkitLineClamp(document.getElementById("summary-${index}"), 3);
    </script>

</#macro>
