<#ftl output_format="HTML">
<#include "../include/imports.ftl">



<#--<#include "../publicationsystem/macro/structured-text.ftl">-->
<#--<#include "../common/macro/sections/sections.ftl">


<#include "../common/macro/component/calloutBox.ftl">
<#include "macro/taskChapterNav.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/googleTags.ftl">-->

<#--<#assign teamTitles = []/>
<#list document.responsibleTeams as team>
<#assign teamTitles = [team.title] + teamTitles/>
</#list>
<@googleTags documentBean=document pageSubject=teamTitles?join(",")/>-->

<#-- Add meta tags -->
<#--<#assign pageShortSummary = document.shortsummary />
<@metaTags></@metaTags>

<@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />

<@fmt.message key="headers.responsible-teams" var="responsibleTeamsSectionHeader" />
<@fmt.message key="headers.dates" var="datesSectionHeader" />

<#assign hasSectionContent = document.sections?has_content />
<#assign hasResponsibleTeams = document.responsibleTeams?has_content />

<#assign hasExpired = (document.expiryDate?? && document.expiryDate.time?date lte .now?date) />
<#assign hasUpdates = document.updates?has_content || document.changenotice?has_content />
<#assign childTasks = document.children />
<#assign hasChildTasks = childTasks?has_content />
<#assign parentTask = document.parents?has_content?then(document.parents[0], '') />
<#assign hasParentTask = parentTask?has_content />
<#assign hasChapters = hasParentTask || hasChildTasks />-->

<#--<#if hasChapters>
    <#assign firstTask = [] />
    <#assign childTasks = [] />
    <#if hasParentTask>-->
        <#-- If current document is a child task -->
      <#--  <#assign firstTask = parentTask />
        <#assign childTasks = parentTask.children?has_content?then(parentTask.children, []) />
    <#else>-->
        <#-- If current document is a parent task -->
       <#-- <#assign firstTask = document />
        <#assign childTasks = document.children?has_content?then(document.children, []) />
    </#if>-->

    <#-- Cache the first task -->
    <#--   <@hst.link hippobean=firstTask var="link" />
    <#assign taskChapters = [{ "index": 0, "id": firstTask.identifier, "title": firstTask.title, "link": link }] /> -->

    <#-- Cache the remaining tasks -->
     <#--  <#list childTasks as chapter>
        <@hst.link hippobean=chapter var="link" />
        <#assign taskChapters += [{ "index": chapter?counter, "id": chapter.identifier, "title": chapter.title, "link": link }] />
    </#list> -->

    <#-- Cache the previous and next tasks for the chapter nav -->
<#--  <#list taskChapters as task>
   <#if task.id == document.identifier>
       <#assign nextTask = (task?counter lt taskChapters?size)?then(taskChapters[(task?counter)], "") />
       <#assign previousTask = (task?counter gt 1)?then(taskChapters[(task?counter - 2)], "") />
   </#if>
</#list>
</#if>
-->

<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/stickyNavSections.ftl">
<#include "../nhsd-common/macro/sections/sections.ftl">

<#assign introduction><@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter /></#assign>
<@hero { 'digiblocks': ['tr', 'bl'], 'alignment': 'centre', 'title': document.title, 'summary': document.shortsummary } />

<article class="nhsd-t-grid nhsd-!t-margin-top-7">
    <div class="nhsd-t-row ">
        <div class="nhsd-t-col-3">
            <div id="sticky-nav">
                <#assign links = [] />
                <#if hasResponsibleTeams>
                    <#assign links += [{ "url": "#" + slugify(responsibleTeamsSectionHeader), "title": responsibleTeamsSectionHeader }] />
                </#if>
                <#assign links += [{ "url": "#" + slugify(datesSectionHeader), "title": datesSectionHeader }] />
                <@stickyNavSections getStickySectionNavLinks({"document": document, "appendSections": links, "includeTopLink": true})></@stickyNavSections>
            </div>
        </div>
        <div class="nhsd-t-col-9">
            <@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter />
            <#if document.sections?has_content>
                <@sections sections=document.sections />
            </#if>
        </div>
    </div>
</article>

<#if hasChapters>
    <@taskChapterNav previousTask=previousTask currentTask=document nextTask=nextTask />
</#if>

    <#-- Updates, changes and expiration notice block -->
   <#if hasUpdates || hasExpired>
        <div class="grid-wrapper">
            <div class="grid-row">
                <div class="column column--no-padding">
                    <div class="callout-box-group">
                        <#if document.updates?has_content>
                            <#assign item = {} />
                            <#list document.updates as update>
                                <#assign item += update />
                                <#assign item += {"calloutType":"update", "index":update?index} />
                                <@calloutBox item document.class.name />
                            </#list>
                        </#if>

                        <#if document.changenotice?has_content>
                            <#assign item = {} />
                            <#list document.changenotice as changeData>
                                <#assign item += changeData />
                                <@fmt.formatDate value=changeData.date.time type="Date" pattern="d MMMM yyyy HH:mm a" timeZone="${getTimeZone()}" var="changeDateTime" />
                                <#assign item += {"date":changeDateTime, "dateLabel":changeDateLabel} />
                                <#assign item += {"calloutType":"change", "severity":"information", "index":changeData?index} />
                                <@calloutBox item document.class.name />
                            </#list>
                        </#if>

                        <#if hasExpired>
                            <div class="callout-box callout-box--important" role="complementary" aria-labelledby="callout-box-heading-expired">
                                <div class="callout-box__icon-wrapper">
                                    <svg class="callout-box__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 217.07 187.02" preserveAspectRatio="xMidYMid meet" role="img" focusable="false">
                                        <path d="M42.7,148.5,73.9,95.9l36.8-62.1a11,11,0,0,1,18.8,0L225,195.1a10.86,10.86,0,0,1-9.4,16.4H24.4A10.88,10.88,0,0,1,15,195.1h0C23,181.7,35.4,160.8,42.7,148.5Z" transform="translate(-11.47 -26.48)"></path>
                                        <path fill="#005EB8" d="M124.4,161.7h-8.5L114,82.3h12.1ZM114,174.4h12.1V187H114Z" transform="translate(-11.47 -26.48)"></path>
                                    </svg>
                                </div>

                                <div class="callout-box__content">
                                    <h2 class="callout-box__content-heading" id="callout-box-heading-expired"><@fmt.message key="task.expired.title" /></h2>

                                    <div class="callout-box__content-description">
                                       <p><@fmt.message key="task.expired.description" /></p>
                                    </div>
                                </div>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </#if>
    <#-- End of Updates, changes and expiration notice block -->

<#-- <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
       <div class="grid-row">-->
           <#--<div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
               <div id="sticky-nav">
                   <#assign links = [] />
                   <#if hasResponsibleTeams>
                       <#assign links += [{ "url": "#" + slugify(responsibleTeamsSectionHeader), "title": responsibleTeamsSectionHeader }] />
                   </#if>
                   <#assign links += [{ "url": "#" + slugify(datesSectionHeader), "title": datesSectionHeader }] />

                   <@stickyNavSections getStickySectionNavLinks({"document": document, "appendSections": links, "includeTopLink": true})></@stickyNavSections>



               </div>
           </div>-->

            <#-- Restore the bundle -->
<#--<@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />

            <div class="column column--two-thirds page-block page-block--main">



                <#-- Restore the bundle --
               <@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />

                <#if hasResponsibleTeams>
                    <div id="${slugify(responsibleTeamsSectionHeader)}" class="article-section no-border">
                        <h2>${responsibleTeamsSectionHeader}</h2>
                        <ul>
                            <#list document.responsibleTeams as team>
                                <li><a href="<@hst.link hippobean=team />">${team.title}</a></li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <div class="article-section" id="${slugify(datesSectionHeader)}">
                    <h2>${datesSectionHeader}</h2>
                    <div class="detail-list-grid detail-list-grid--regular">
                        <#if document.reviewDate?has_content>
                        <dl class="detail-list">
                            <dt class="detail-list__key"><@fmt.message key="labels.review-date"/></dt>
                            <dd class="detail-list__value"><@fmt.formatDate value=document.reviewDate.time?date type="date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /></dd>
                        </dl>
                        </#if>

                        <dl class="detail-list">
                            <dt class="detail-list__key"><@fmt.message key="labels.last-modified"/></dt>
                            <dd class="detail-list__value">
                                <@fmt.formatDate value=document.lastModified?date type="date" pattern="d MMMM yyyy h:mm a" timeZone="${getTimeZone()}" var="lastModifiedDate" />
                                ${lastModifiedDate?replace("AM", "am")?replace("PM", "pm")}
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <#if hasChapters>
        <@fmt.message key="headers.task-chapters" var="chapterIndexNavTitle" />
        <@taskChapterIndexNav documents=taskChapters title=chapterIndexNavTitle />
    </#if>-->

