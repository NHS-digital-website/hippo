<#ftl output_format="HTML">

<#include "../../include/imports.ftl">
<#include "../macro/trendingArticlesImage.ftl">
<#include "../macro/trendingArticlesLabel.ftl">
<#include "../macro/trendingArticlesDate.ftl">
<#include "../macro/trendingArticlesShortSummary.ftl">

<div class="intra-box">
    <#if wrappingDocument?? && wrappingDocument.title?? && wrappingDocument.title?has_content>
        <div class="nhsd-t-col">
            <h2 class="nhsd-t-heading-xl nhsd-t-text-align-center nhsd-!t-margin-bottom-7">${wrappingDocument.title}</h2>
        </div>
    </#if>
    <#if pageable?? && pageable.items?has_content>
        <div class="nhsd-o-card-list">
            <div class="nhsd-t-grid">
                <div class="nhsd-t-row nhsd-o-card-list__items">
                    <#list pageable.items as trending>
                        <div class="nhsd-t-col-xs-12 nhsd-t-col-s-6 nhsd-t-col-m-6 nhsd-t-col-l-3">
                            <!-- Only render Announcements if they haven't expired -->
                            <#if trending.docType == 'Announcement' && currentDate.after(trending.expirydate)>
                                <#continue>
                            </#if>
                            <article class="nhsd-m-card">
                                <a href="<@hst.link hippobean=trending/>" class="nhsd-a-box-link " aria-label="${trending.title}">
                                    <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                                        <@trendingArticleImage trending=trending/>
                                        <div class="nhsd-m-card__content-box">
                                            <div style="position:relative">
                                                <@hst.manageContent hippobean=trending />
                                            </div>
                                            <div class="nhsd-m-card__tag-list">
                                                <span class="nhsd-a-tag nhsd-a-tag--bg-light-grey">
                                                    <@trendingArticleLabel trending=trending/>
                                                </span>
                                            </div>

                                            <@trendingArticleDate trending=trending/>

                                            <#if trending.title?? && trending.title?has_content>
                                                <h1 class="nhsd-t-heading-s">${trending.title}</h1>
                                            </#if>

                                            <@trendingArticleShortSummary trending=trending index=trending?index/>
                                        </div>
                                        <div class="nhsd-m-card__button-box">
                                            <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s">
                                                <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                                                    <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                                                </svg>
                                            </span>
                                        </div>

                                    </div>
                                </a>
                            </article>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
    </#if>
    <#if wrappingDocument?? && wrappingDocument.title?? && wrappingDocument.title?has_content>
        <@hst.link var="link" hippobean=wrappingDocument/>
         <nav class="nhsd-m-button-nav">
            <a class="nhsd-a-button" href="${link}">
                <span class="nhsd-a-button__label">${wrappingDocument.title}</span>
            </a>
        </nav>
    </#if>
</div>
