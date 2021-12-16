<#ftl output_format="HTML">
<#include "../../common/macro/component/actionLink.ftl">
<#include "../../common/macro/svgIcons.ftl">
<#include "../../intranet/macro/trendingArticlesImage.ftl">
<#include "../../intranet/macro/trendingArticlesLabel.ftl">
<#include "../../intranet/macro/trendingArticlesDate.ftl">
<#include "../../intranet/macro/trendingArticlesShortSummary.ftl">
<#include "../../include/imports.ftl">

<div class="intra-box">

    <#if wrappingDocument.title?? && wrappingDocument.title?has_content>
        <h2 class="intra-box__title"> ${wrappingDocument.title}</h2>
    </#if>

    <#if pageable?? && pageable.items?has_content>
    <#-- Initialise webkitLineClamp -->
        <script>
            window.webkitLineClamp = []
        </script>
        <ul class="intra-home-article-grid">
            <#list pageable.items as trending>

                <!-- Only render Announcements if they haven't expired -->
                <#if trending.docType == 'Announcement' && currentDate.after(trending.expirydate)>
                    <#continue>
                </#if>

                <li class="intra-home-article-grid-item">
                    <article class="intra-home-article-grid-article">
                        <div style="position:relative">
                            <@hst.manageContent hippobean=trending />
                        </div>
                        <div class="intra-home-article-grid-article__header">
                             <span class="intra-info-tag">
                                 <@trendingArticleLabel trending=trending/>
                             </span>
                            <@trendingArticleImage trending=trending/>
                        </div>

                        <div class="intra-home-article-grid-article__contents">
                            <#if trending.title?? && trending.title?has_content>
                                <h1 class="intra-home-article-grid-article__title">
                                    <a href="<@hst.link hippobean=trending/>">
                                        ${trending.title}</a>
                                </h1>
                            </#if>
                            <@trendingArticleShortSummary trending=trending index=trending?index/>
                        </div>
                        <@trendingArticleDate trending=trending/>
                    </article>
                </li>
            </#list>
        </ul>
    </#if>

    <#if wrappingDocument.internal?has_content>
        <@hst.link var="link" hippobean=wrappingDocument.internal/>
    <#else>
        <#assign link=wrappingDocument.external/>
    </#if>
    <@actionLink title="${wrappingDocument.getLabel()}" link="${link}" />

</div>