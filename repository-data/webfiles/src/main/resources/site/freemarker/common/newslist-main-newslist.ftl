<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.website.labels"/>

<#if pageable?? && pageable.items?has_content>
<div>
    <h3 class="list-title"><@fmt.message key="about-us.latestNewsTitle"/></h3>

    <ol class="list list--reset cta-list">
        <#list pageable.items as item>
        <li class="has-edit-button">
            <article class="cta">
                <h2 class="cta__title">
                    <a href="<@hst.link hippobean=item/>" title="${item.title}">${item.title}</a>
                </h2>
                
                <#if item.publisheddatetime?? && item.publisheddatetime.time??>
                    <p class="cta__meta"><@fmt.message key="about-us.newsDateLabel"/>: 
                        <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="yyyy-MM-dd HH:mm:ss" var="publishedDateTime" timeZone="Europe/London" />
                        <@fmt.formatDate value=item.publisheddatetime.time type="Date" pattern="d MMMM yyyy" var="publishedDateTimeForHumans"/>
                        <time datetime="${publishedDateTime}">${publishedDateTimeForHumans}</time>
                    </p>
                </#if>
            </article>
        </li>

        </#list>
    </ol>

    <a href="/news-and-events/latest-news" class="button"><@fmt.message key="about-us.latestNewsButtonLabel"/></a>
  
    <div class="has-new-content-button">
        <@hst.manageContent templateQuery="new-news-document" rootPath="news" defaultPath="${currentYear}/${currentMonth}"/>
    </div>
</div>
    
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->
<#elseif editMode>
<div>
    <img src="<@hst.link path='/images/essentials/catalog-component-icons/news-list.png'/>"> Click to edit News List
    <div class="has-new-content-button">
        <@hst.manageContent templateQuery="new-news-document" rootPath="news" defaultPath="${currentYear}/${currentMonth}"/>
    </div>
</div>
</#if>
