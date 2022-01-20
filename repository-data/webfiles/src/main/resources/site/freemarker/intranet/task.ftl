<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../publicationsystem/macro/structured-text.ftl">
<#include "../nhsd-common/macro/sections/sections.ftl">
<#include "../common/macro/documentHeader.ftl">
<#include "../nhsd-common/macro/stickyNavSections.ftl">
<#include "../nhsd-common/macro/component/calloutBox.ftl">
<#include "macro/taskChapterNav.ftl">
<#include "macro/metaTags.ftl">
<#include "macro/googleTags.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/stickyNavSections.ftl">
<#include "../nhsd-common/macro/component/chapter-pagination.ftl">

<#assign teamTitles = []/>
<#list document.responsibleTeams as team>
<#assign teamTitles = [team.title] + teamTitles/>
</#list>
<@googleTags documentBean=document pageSubject=teamTitles?join(",")/>

<#-- Add meta tags -->
<#assign pageShortSummary = document.shortsummary />
<@metaTags></@metaTags>

<@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />

<@fmt.message key="headers.responsible-teams" var="responsibleTeamsSectionHeader" />
<@fmt.message key="headers.dates" var="datesSectionHeader" />

<#assign hasResponsibleTeams = document.responsibleTeams?has_content />

<#assign hasExpired = (document.expiryDate?? && document.expiryDate.time?date lte .now?date) />
<#assign childTasks = document.children />
<#assign hasChildTasks = childTasks?has_content />
<#assign parentTask = document.parents?has_content?then(document.parents[0], '') />
<#assign hasParentTask = parentTask?has_content />
<#assign hasChapters = hasParentTask || hasChildTasks />

<#if hasChapters>
    <#assign firstTask = [] />
    <#assign childTasks = [] />
    <#if hasParentTask>
        <#-- If current document is a child task -->
        <#assign firstTask = parentTask />
        <#assign childTasks = parentTask.children?has_content?then(parentTask.children, []) />
    <#else>
        <#-- If current document is a parent task -->
        <#assign firstTask = document />
        <#assign childTasks = document.children?has_content?then(document.children, []) />
    </#if>

    <#-- Cache the first task -->
    <@hst.link hippobean=firstTask var="link" />
    <#assign taskChapters = [{ "index": 0, "id": firstTask.identifier, "title": firstTask.title, "link": link }] />

    <#-- Cache the remaining tasks -->
    <#list childTasks as chapter>
        <@hst.link hippobean=chapter var="link" />
        <#assign taskChapters += [{ "index": chapter?counter, "id": chapter.identifier, "title": chapter.title, "link": link }] />
    </#list>

    <#-- Cache the previous and next tasks for the chapter nav -->
    <#list taskChapters as task>
        <#if task.id == document.identifier>
            <#assign nextTask = (task?counter lt taskChapters?size)?then(taskChapters[(task?counter)], "") />
            <#assign previousTask = (task?counter gt 1)?then(taskChapters[(task?counter - 2)], "") />
        </#if>
    </#list>
</#if>

<article>
    <#assign metaData = [] />

    <#assign options = getHeroOptions(document) />

    <#if document.topics?has_content>
        <#assign metaData += [{"title":"Topics", "value": document.topics?join(", ")}] />
        <#assign options += {
            "metaData": metaData
        } />
    </#if>

    <#assign options += {
        "colour": "blue"
    } />
    <@hero options/>

    <#-- expiration notice block -->
    <#if hasExpired>
        <div class="nhsd-m-notification-banner nhsd-m-notification-banner--warning">
            <div class="nhsd-a-box nhsd-a-box--bg-dark-grey">
                <div class="nhsd-t-grid">
                    <div class="nhsd-t-row">
                        <div class="nhsd-t-col-12">
                            <div class="nhsd-m-notification-banner__row">
                                <div class="nhsd-m-notification-banner__left-col">
                                    <span class="nhsd-a-icon nhsd-a-icon--col-white nhsd-a-icon--size-xl ">
                                      <svg xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid meet" focusable="false" viewBox="0 0 16 16"><path d="M8,16c-4.4,0-8-3.6-8-8s3.6-8,8-8s8,3.6,8,8S12.4,16,8,16z M8,2.3C4.9,2.3,2.3,4.9,2.3,8s2.6,5.7,5.7,5.7 s5.7-2.6,5.7-5.7S11.1,2.3,8,2.3z M8,12.9c-0.8,0-1.4-0.7-1.4-1.4C6.6,10.7,7.2,10,8,10c0.8,0,1.4,0.7,1.4,1.4 C9.4,12.2,8.8,12.9,8,12.9z M9.1,3.4l-0.3,6H7.1l-0.3-6H9.1z" /></svg>
                                  </span>
                                </div>
                                <div class="nhsd-m-notification-banner__middle-col">
                                    <p class="nhsd-t-body"><@fmt.message key="task.expired.description" /></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </#if>
    <#-- End of Updates, changes and expiration notice block -->

    <#if hasChapters>
        <@taskChapterNav previousTask=previousTask currentTask=document nextTask=nextTask />
    </#if>

    <div class="nhsd-t-grid nhsd-!t-margin-top-8 nhsd-!t-margin-bottom-6" aria-label="Document Content">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-3">
                <#assign links = [] />
                <#if hasResponsibleTeams>
                    <#assign links += [{ "url": "#" + slugify(responsibleTeamsSectionHeader), "title": responsibleTeamsSectionHeader }] />
                </#if>
                <#assign links += [{ "url": "#" + slugify(datesSectionHeader), "title": datesSectionHeader }] />

                <@stickyNavSections getStickySectionNavLinks({"document": document, "appendSections": links, "includeTopLink": true})></@stickyNavSections>

                <#-- Restore the bundle -->
                <@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />
            </div>


            <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8 nhsd-t-off-s-1">
                <#if document.sections?has_content>
                    <@sections sections=document.sections />
                </#if>

                <#-- Restore the bundle -->
                <@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />

                <#if hasResponsibleTeams>
                    <div id="${slugify(responsibleTeamsSectionHeader)}"
                         class="article-section no-border">
                        <h2 class="nhsd-t-heading-xl">${responsibleTeamsSectionHeader}</h2>
                        <ul>
                            <#list document.responsibleTeams as team>
                                <li class="nhsd-t-list nhsd-t-list--bullet">
                                    <a href="<@hst.link hippobean=team />">${team.title}</a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </#if>

                <hr class="nhsd-a-horizontal-rule"/>

                <div id="${slugify(datesSectionHeader)}">
                    <h2 class="nhsd-t-heading-xl">${datesSectionHeader}</h2>
                    <dl class="nhsd-a-summary-list">
                        <#if document.reviewDate?has_content>
                            <div class="nhsd-a-summary-list__item">
                                <dt><@fmt.message key="labels.review-date"/></dt>
                                <dd><@fmt.formatDate value=document.reviewDate.time?date type="date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /></dd>
                            </div>
                        </#if>

                        <div class="nhsd-a-summary-list__item">
                            <dt><@fmt.message key="labels.last-modified"/></dt>
                            <dd>
                                <@fmt.formatDate value=document.lastModified?date type="date" pattern="d MMMM yyyy h:mm a" timeZone="${getTimeZone()}" var="lastModifiedDate" />
                                ${lastModifiedDate?replace("AM", "am")?replace("PM", "pm")}
                            </dd>
                        </div>
                    </dl>
                </div>
            </div>
        </div>
        <div class="nhsd-t-row">
            <div class="nhsd-t-col">
                <#if hasChapters>
                    <@fmt.message key="headers.task-chapters" var="chapterIndexNavTitle" />
                    <@taskChapterIndexNav documents=taskChapters title=chapterIndexNavTitle />
                </#if>
            </div>
        </div>
    </div>

</article>
