<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.intranet.labels"/>

<div class="home-news-grid-container">
<div class="home-news-grid-container__force-size"></div>
<#if pageable?? && pageable.items?has_content>

    <#assign featured=document/>

    <#if featured.leadImageSection.leadImage?has_content>
        <@hst.link var="img" hippobean=featured.leadImageSection.leadImage />
        <img class="home-news-news-top-featured-image" src="<#if img??>${img!}</#if>" alt="${featured.leadImageSection.alttext}" />
    </#if>

    <div class="home-news-news-top ${featured.leadImageSection.leadImage?has_content?then('featured-image', 'no-featured-image')}">
        <h2 class="h3">${featured.title}</h2>
        <p>${featured.shortsummary}</p>
        <a href="<@hst.link hippobean=featured/>" title="${featured.title}">Read more</a>
    </div>

    <div class="home-news-news-items">
        <div class="home-news-news-items-grid">
            <#list pageable.items as item>
                <#if item!=featured>
                    <article class="home-news-news-items-grid-item">
                        <#if item.leadImageSection.leadImage?has_content>
                            <@hst.link var="img" hippobean=item.leadImageSection.leadImage/>
                            <img class="home-news-news-items-grid-item-img" src="<#if img??>${img!}</#if>"
                                 alt="${item.leadImageSection.alttext}"/>
                        </#if>
                        <div class="home-news-news-items-grid-item-content">
                        <h3 class="h5">
                            <a href="<@hst.link hippobean=item/>"
                               title="${item.title}">${item.title}</a>
                        </h3>

                        <#if item.publicationDate??>
                            <p class="cta__meta">
                                <@fmt.formatDate value=item.publicationDate.time type="Date" pattern="d MMMM yyyy" var="publicationDate" timeZone="${getTimeZone()}"/>
                                <time datetime="${publicationDate}">${publicationDate}</time>
                            </p>
                        </#if>
                        </div>
                    </article>
                </#if>
                <#if item_index==1><#if cparam.newsNumberOfRows=="1"><#break></#if></#if>
                <#if item_index==3><#break></#if>
            </#list>
        </div>
    </div>

    <div class="home-news-news-items-group-2">
        <a href="${cparam.newsPageUrl}"><@fmt.message key="about-us.latestNewsButtonLabel"/></a>
    </div>



    <div class="home-news-tasks">

        <#if taskDocuments?? && taskDocuments.items?has_content>
            <div class="home-tasks">
                <div class="home-tasks-margin">

                    <#if cparam.tasksTitle??>
                        <h2 class="h3">${cparam.tasksTitle}</h2>
                    </#if>

                    <div class="home-tasks-grid">
                        <#--TODO: ADD CSS AND DEFAULT TASK ICON-->
                        <#list taskDocuments.items as item>
                            <div class="home-tasks-grid-item">
                                <img src="<@hst.webfile path="/images/intranet/homepage/page-svg-blue.svg"/>" alt="${item.title}" class="blog-social-icon__img" />
                                <a href="<@hst.link hippobean=item/>">${item.title}</a>
                            </div>
                        </#list>
                    </div>

                    <#if cparam.tasksPageUrl??>
                        <div class="nhsuk-action-link">
                            <a class="nhsuk-action-link__link" href="<a href="${cparam.tasksPageUrl}">
                                <svg class="nhsuk-icon nhsuk-icon__arrow-right-circle" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true">
                                    <path d="M0 0h24v24H0z" fill="none"></path>
                                    <path d="M12 2a10 10 0 0 0-9.95 9h11.64L9.74 7.05a1 1 0 0 1 1.41-1.41l5.66 5.65a1 1 0 0 1 0 1.42l-5.66 5.65a1 1 0 0 1-1.41 0 1 1 0 0 1 0-1.41L13.69 13H2.05A10 10 0 1 0 12 2z"></path>
                                </svg>
                                <span class="nhsuk-action-link__text"><@fmt.message key="about-us.viewAllTasksButtonLabel"/></span>
                            </a>
                        </div>
                    </#if>

                </div>
            </div>
        </#if>

    </div>

</#if>
</div>

