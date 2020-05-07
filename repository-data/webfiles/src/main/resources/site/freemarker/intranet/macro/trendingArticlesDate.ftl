<#ftl output_format="HTML">

<#macro trendingArticleDate trending>
    <#if trending.docType == 'NewsInternal'>
        <#if trending.publicationDate??>
            <span class="intra-home-article-grid-article__date">
                 <@fmt.formatDate value=trending.publicationDate.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}"/>
            </span>
        </#if>
    <#elseif trending.docType == 'Blog'>
        <#if trending.dateOfPublication??>
            <span class="intra-home-article-grid-article__date">
                <@fmt.formatDate value=trending.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}"/>
            </span>
        </#if>
    </#if>

</#macro>
