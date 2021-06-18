<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "metaTags.ftl">
<#include "documentHeader.ftl">
<#include "component/showAll.ftl">
<#include "contentPixel.ftl">
<#include "../macro/gridColumnGenerator.ftl">

<#macro hubArticles latestArticles>
    <div class="nhsd-t-grid nhsd-t-grid--nested">
        <div class="nhsd-t-row">
            <#list latestArticles as latest>
                <#if latest?is_first>
                    <div class="nhsd-t-col nhsd-!t-margin-bottom-6">
                        <@hubArticleItem item=latest feature=true />
                    </div>
                <#else>
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4 nhsd-!t-margin-bottom-6">
                        <@hubArticleItem item=latest />
                    </div>
                </#if>
            </#list>
        </div>
    </div>
</#macro>

<#macro hubArticleItem item feature=false>
    <#assign cardClass = "" />
    <#assign imgClass = "" />
    <#if feature>
        <#assign cardClass = "nhsd-m-card--image-position-adjacent" />
        <#assign imgClass = "nhsd-a-image--cover" />
    </#if>
    <div class="nhsd-m-card nhsd-m-card--full-height ${cardClass}">
        <a href="<@hst.link hippobean=item/>" class="nhsd-a-box-link nhsd-a-box-link--focus-orange" aria-label="${item.title}">
            <div class="nhsd-a-box nhsd-a-box--bg-light-grey">
                <#if item.leadImage?has_content>
                    <div class="nhsd-m-card__image_container">
                        <figure class="nhsd-a-image ${imgClass}">
                            <@hst.link hippobean=item.leadImage.newsPostImage fullyQualified=true var="leadImage" />
                            <@hst.link hippobean=item.leadImage.newsPostImage2x fullyQualified=true var="leadImage2x" />
                            <picture class="nhsd-a-image__picture">
                                <img srcset="${leadImage}, ${leadImage2x} 2x" src="${leadImage}" alt="">
                            </picture>
                        </figure>
                    </div>
                </#if>
                <div class="nhsd-m-card__content_container">
                    <div class="nhsd-m-card__content-box">
                        <span class="nhsd-m-card__date" itemprop="datePublished">
                            <@fmt.formatDate value=item.dateOfPublication.time type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                        </span>
                        <h1 class="nhsd-t-heading-s" itemprop="headline">${item.title}</h1>
                        <p class="nhsd-t-body-s" data-uipath="website.blog.summary" itemprop="articleBody">${item.shortsummary}</p>
                    </div>

                    <div class="nhsd-m-card__button-box">
                        <span class="nhsd-a-icon nhsd-a-arrow nhsd-a-icon--size-s nhsd-a-icon--col-black">
                            <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" aria-hidden="true" focusable="false" viewBox="0 0 16 16"  width="100%" height="100%">
                            <path d="M8.5,15L15,8L8.5,1L7,2.5L11.2,7H1v2h10.2L7,13.5L8.5,15z"/>
                            </svg>
                        </span>
                    </div>
                </div>
            </div>
        </a>
    </div>
</#macro>
