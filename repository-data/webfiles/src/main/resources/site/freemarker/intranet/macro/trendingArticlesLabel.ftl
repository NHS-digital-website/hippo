<#ftl output_format="HTML">

<#macro trendingArticleLabel trending>

    <#if trending.docType == 'NewsInternal'>
        News
    <#elseif trending.docType == 'Blog'>
        Blog
    <#elseif trending.docType == 'Announcement'>
        Announcement
    </#if>

</#macro>
