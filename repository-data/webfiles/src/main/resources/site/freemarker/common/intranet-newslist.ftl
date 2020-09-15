<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="homepage.intranet.labels"/>

<#--TODO: ADD CSS-->
<#if pageable?? && pageable.items?has_content>

    <div id="home-main-hero-banner">
        <div class="grid-wrapper">
            <div class="grid-row">
                <div class="column column--one-half column--left">
                    <#assign featured=document/>
                    <#if featured.leadImageSection.leadImage?has_content>
                        <@hst.link var="img" hippobean=featured.leadImageSection.leadImage/>
                        <img src="<#if img??>${img!}</#if>"
                             alt="${featured.leadImageSection.alttext}"/>
                    </#if>


                    <h2><a href="<@hst.link hippobean=featured/>"
                           title="${featured.title}">${featured.title}</a></h2>
                    <p>${featured.shortsummary}</p>
                </div>
                <#if taskDocuments?? && taskDocuments.items?has_content>
                    <div class="home-tasks">
                        <div class="home-tasks-margin">
                            <#if cparam.tasksTitle??>
                                <h2>${cparam.tasksTitle}</h2>
                            </#if>
                            <div class="home-tasks-grid">
                                <#--TODO: ADD CSS AND DEFAULT TASK ICON-->
                                <#list taskDocuments.items as item>

                                    <div class="home-tasks-grid-item">
                                        <#--<a href="<@hst.link hippobean=item/>" title="${item.title}">-->

                                            <article>
                                                <img src="<@hst.webfile path="/images/intranet/homepage/page-svg-blue.svg"/>" alt="${item.title}" class="blog-social-icon__img" />
                                                <h2>
                                                    ${item.title}
                                                </h2>
                                                <a href="<@hst.link hippobean=item/>"${item.shortsummary}</a>
                                            </article>
                                       <#-- </a>-->
                                    </div>
                                </#list>

                            </div>
                            <#if cparam.tasksPageUrl??>
                                <a href="${cparam.tasksPageUrl}"><@fmt.message key="about-us.viewAllTasksButtonLabel"/></a>
                            </#if>
                        </div>
                    </div>
                </#if>
            </div>
        </div>
    </div>

    <div class="grid-wrapper">
        <ol>
            <#list pageable.items as item>
                <#if item!=featured>
                    <li class="has-edit-button">
                        <article>
                            <#if item.leadImageSection.leadImage?has_content>
                                <@hst.link var="img" hippobean=item.leadImageSection.leadImage/>
                                <img src="<#if img??>${img!}</#if>"
                                     alt="${item.leadImageSection.alttext}"/>
                            </#if>
                            <h3>
                                <a href="<@hst.link hippobean=item/>"
                                   title="${item.title}">${item.title}</a>
                            </h3>

                            <#if item.publicationDate??>
                                <p class="cta__meta">
                                    <@fmt.formatDate value=item.publicationDate.time type="Date" pattern="d MMMM yyyy" var="publicationDate" timeZone="${getTimeZone()}"/>
                                    <time datetime="${publicationDate}">${publicationDate}</time>
                                </p>
                            </#if>
                        </article>
                    </li>
                </#if>
            </#list>
        </ol>
        <a href="/news"
           class="button"><@fmt.message key="about-us.latestNewsButtonLabel"/></a>

    </div>








</#if>
