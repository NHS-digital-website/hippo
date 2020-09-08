<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.intranet.labels"/>

<#--TODO: ADD CSS-->
<#if pageable?? && pageable.items?has_content>
<div>
    <#assign featured=document/>
    <#if featured.leadImageSection.leadImage?has_content>
        <@hst.link var="img" hippobean=featured.leadImageSection.leadImage/>
        <img src="<#if img??>${img!}</#if>" alt="${featured.leadImageSection.alttext}"/>
    </#if>
    <h2><a href="<@hst.link hippobean=featured/>" title="${featured.title}">${featured.title}</a></h2>
    <p>${featured.shortsummary}</p>

    <ol>
    <#list pageable.items as item>
        <#if item!=featured>
            <li class="has-edit-button">
                <article>
                    <#if item.leadImageSection.leadImage?has_content>
                        <@hst.link var="img" hippobean=item.leadImageSection.leadImage/>
                        <img src="<#if img??>${img!}</#if>" alt="${item.leadImageSection.alttext}"/>
                    </#if>
                    <h3>
                        <a href="<@hst.link hippobean=item/>" title="${item.title}">${item.title}</a>
                    </h3>

                    <#if item.publicationDate??>
                        <p class="cta__meta"><span class="strong"><@fmt.message key="about-us.newsDateLabel"/>: </span>
                            <@fmt.formatDate value=item.publicationDate.time type="Date" pattern="d MMMM yyyy" var="publicationDate" timeZone="${getTimeZone()}"/>
                            <time datetime="${publicationDate}">${publicationDate}</time>
                        </p>
                    </#if>
                </article>
            </li>
        </#if>
    </#list>
    </ol>
    <a href="/news-and-events/news" class="button"><@fmt.message key="about-us.latestNewsButtonLabel"/></a>


    <#--TODO: ADD CSS AND DEFAULT TASK ICON-->
    <#if taskDocuments?? && taskDocuments.items?has_content>
    <div>
        <#if cparam.tasksTitle??>
        <h2>${cparam.tasksTitle}</h2>
        </#if>
        <ol>
            <#list taskDocuments.items as item>
                <li class="has-edit-button">
                    <article>
                        <h2>
                            <a href="<@hst.link hippobean=item/>" title="${item.title}">${item.title}</a>
                        </h2>
                        <p>${item.shortsummary}</p>
                    </article>
                </li>
            </#list>
        </ol>
        <#if cparam.tasksPageUrl??>
            <a href="${cparam.tasksPageUrl}" class="button"><@fmt.message key="about-us.viewAllTasksButtonLabel"/></a>
        </#if>
    </div>
    </#if>


</div>

</#if>
