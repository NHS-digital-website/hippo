<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#include "../intranet/macro/stickyNavSections.ftl">

<#--<#include "../publicationsystem/macro/structured-text.ftl">-->
<#--<#include "../common/macro/sections/sections.ftl">

<#include "../common/macro/stickyNavSections.ftl">
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



<#--
Todo: find out where topics go
<#assign metaData = [] />
<#if document.topics?has_content>
<#assign metaData += [{"key":"Topics", "value": document.topics?join(", "), "uipath": "taxonomy", "schemaOrgTag": "", "type": ""}] />
</#if>-->

<div class="nhsd-o-hero nhsd-!t-text-align-center">
    <div class="nhsd-o-hero__content-container">
        <div class="nhsd-o-hero__inner-content-container">
            <h1 class="nhsd-t-heading-xxl">${document.title}</h1>
            <#if document.introduction?has_content>
                <p class="nhsd-t-body"><@hst.html hippohtml=document.introduction contentRewriter=gaContentRewriter /></p>
            </#if>
        </div>
    </div>
    <div class="nhsd-a-digiblocks nhsd-a-digiblocks--pos-bl">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550">
            <g>
                <g transform="translate(222, 224)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
                <g transform="translate(328.5, 367.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
                <g transform="translate(151, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon>
                </g>
                <g transform="translate(80, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(186.5, 203.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
                <g transform="translate(186.5, 285.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(222, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(9, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(257.5, 449.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(186.5, 203.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon>
                </g>
                <g transform="translate(399.5, 326.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
                <g transform="translate(222, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(328.5, 162.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon>
                </g>
                <g transform="translate(399.5, 244.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(115.5, 162.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon>
                </g>
                <g transform="translate(186.5, 244.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
                <g transform="translate(328.5, 326.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
                <g transform="translate(257.5, 326.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(328.5, 244.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon>
                </g>
                <g transform="translate(257.5, 285.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
                <g transform="translate(44.5, 203.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon>
                </g>
                <g transform="translate(151, 265)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(435, 142)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(328.5, 39.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
                <g transform="translate(222, 19)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(257.5, 80.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon>
                </g>
            </g>
        </svg>
    </div>
    <div class="nhsd-a-digiblocks nhsd-a-digiblocks--pos-tr">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 550 550">
            <g>
                <g transform="translate(222, 224)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
                <g transform="translate(328.5, 367.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
                <g transform="translate(151, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon>
                </g>
                <g transform="translate(80, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(186.5, 203.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
                <g transform="translate(186.5, 285.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(222, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(9, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(257.5, 449.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(186.5, 203.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon>
                </g>
                <g transform="translate(399.5, 326.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
                <g transform="translate(222, 306)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#FBFAFA"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F5F5F4"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EFF2F1"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(328.5, 162.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#F5D507"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#F2CB0C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#EEC000"></polygon>
                </g>
                <g transform="translate(399.5, 244.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(115.5, 162.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon>
                </g>
                <g transform="translate(186.5, 244.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
                <g transform="translate(328.5, 326.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
                <g transform="translate(257.5, 326.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#3C4D57"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#32434C"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#313D45"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(328.5, 244.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon>
                </g>
                <g transform="translate(257.5, 285.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
                <g transform="translate(44.5, 203.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#6D7B86"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#62717A"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#5C6B75"></polygon>
                </g>
                <g transform="translate(151, 265)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(435, 142)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#0062CC"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#005ABE"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#0050B5"></polygon>
                </g>
            </g>
            <g>
                <g transform="translate(328.5, 39.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#BFD7ED"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#B2CFEA"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#A6C7E6"></polygon>
                </g>
                <g transform="translate(222, 19)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#00267A"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#001F75"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#001766"></polygon>
                </g>
                <g transform="translate(257.5, 80.5)">
                    <polygon points="0,20.5 35.5,0 71,20.5 35.5,41" fill="#DADFDF"></polygon>
                    <polygon points="35.5,82 71,61.4 71,20.5 35.5,41" fill="#CDD5D6"></polygon>
                    <polygon points="0,20.5 0,61.4 35.5,82 35.5,41" fill="#C5CDCF"></polygon>
                </g>
            </g>
        </svg>
    </div>
</div>

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
            <h2>Title</h2>
        </div>
    </div>
</article>

<#--<#if hasChapters>
    <@taskChapterNav previousTask=previousTask currentTask=document nextTask=nextTask />
</#if>-->

    <#-- Updates, changes and expiration notice block -->
   <#-- <#if hasUpdates || hasExpired>
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
    </#if> -->
    <#-- End of Updates, changes and expiration notice block -->

   <#-- <div class="grid-wrapper grid-wrapper--article" aria-label="Document Content">
        <div class="grid-row">
            <div class="column column--one-third page-block page-block--sidebar article-section-nav-outer-wrapper">
                <div id="sticky-nav">
                    <#assign links = [] />
                    <#if hasResponsibleTeams>
                        <#assign links += [{ "url": "#" + slugify(responsibleTeamsSectionHeader), "title": responsibleTeamsSectionHeader }] />
                    </#if>
                    <#assign links += [{ "url": "#" + slugify(datesSectionHeader), "title": datesSectionHeader }] />

                    <@stickyNavSections getStickySectionNavLinks({"document": document, "appendSections": links, "includeTopLink": true})></@stickyNavSections>
-->
                    <#-- Restore the bundle -->
                    <#--<@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />
                </div>
            </div>


            <div class="column column--two-thirds page-block page-block--main">

                <#if document.sections?has_content>
                    <div class="article-section">
                        <@sections sections=document.sections />
                    </div>
                </#if>-->

                <#-- Restore the bundle -->
               <#-- <@hst.setBundle basename="intranet.headers, intranet.labels, intranet.task" />

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

