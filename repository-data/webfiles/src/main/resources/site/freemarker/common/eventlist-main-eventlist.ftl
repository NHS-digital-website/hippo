<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<@hst.setBundle basename="rb.component.latest-events"/>

<#if pageable?? && pageable.items?has_content>
<div>
    <h3 class="list-title"><@fmt.message key="headers.title"/></h3>

    <ol class="list list--reset cta-list">
        <#list pageable.items as item>
        <li class="has-edit-button">
            <article class="cta">
                <h2 class="cta__title">
                    <a href="<@hst.link hippobean=item/>" title="${item.title}">${item.title}</a>
                </h2>

                <#assign minStartTimeStamp = 0/>
                <#assign maxEndTimeStamp = 0/>
                <#-- Gather the earliest start date and the latest end date for each event -->
                <#list item.events as event>
                    <#-- Store the earliest start date values -->
                    <#assign startTimeStamp = event.startdatetime.time?long/>
                    <#if minStartTimeStamp == 0 || startTimeStamp lt minStartTimeStamp>
                        <#assign minStartTime = event.startdatetime.time/>
                        <#assign minStartTimeStamp = startTimeStamp/>
                    </#if>

                    <#-- Store the latest end date values -->
                    <#assign endTimeStamp = event.enddatetime.time?long/>
                    <#if maxEndTimeStamp == 0 || endTimeStamp gt maxEndTimeStamp>
                        <#assign maxEndTime = event.enddatetime.time/>
                        <#assign maxEndTimeStamp = endTimeStamp/>
                    </#if>
                </#list>

                <@fmt.formatDate value=minStartTime type="Date" pattern="yyyy-MM-dd" pattern="yyyy-MM-dd" var="comparableStartDate" timeZone="Europe/London" />
                <@fmt.formatDate value=maxEndTime type="Date" pattern="yyyy-MM-dd" var="comparableEndDate" timeZone="Europe/London" />

                <#if comparableStartDate == comparableEndDate>
                <p class="cta__meta">
                    <span><@fmt.message key="labels.date-label"/>: </span>
                    <span><@fmt.formatDate value=minStartTime type="Date" pattern="d MMMM yyyy" timeZone="Europe/London" /></span>
                </p>
                <#else>
                <p class="cta__meta">
                    <span><@fmt.message key="labels.date-label"/>: </span>
                    <span>
                        <@fmt.formatDate value=minStartTime type="Date" pattern="d MMMM yyyy" timeZone="Europe/London" /> - <@fmt.formatDate value=maxEndTime type="Date" pattern="d MMMM yyyy" timeZone="Europe/London" />
                    </span>
                </p>
                </#if>
            </article>
        </li>

        </#list>
    </ol>

    <a href="/news-and-events/events" class="button"><@fmt.message key="labels.cta"/></a>
</div>
</#if>
