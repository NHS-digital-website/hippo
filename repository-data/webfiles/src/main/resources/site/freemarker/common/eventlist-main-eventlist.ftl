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

                <#assign dateRangeData = getEventDateRangeData(item.events) />
                <#if dateRangeData?size gt 0>
                    <#if dateRangeData.comparableStartDate == dateRangeData.comparableEndDate>
                    <p class="cta__meta">
                        <span class="strong"><@fmt.message key="labels.date-label"/>: </span>
                        <span><@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /></span>
                    </p>
                    <#else>
                    <p class="cta__meta">
                        <span class="strong"><@fmt.message key="labels.date-label"/>: </span>
                        <span>
                            <@fmt.formatDate value=dateRangeData.minStartTime type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" /> - <@fmt.formatDate value=dateRangeData.maxEndTime type="Date" pattern="d MMMM yyyy" timeZone="${getTimeZone()}" />
                        </span>
                    </p>
                    </#if>
                </#if>
            </article>
        </li>

        </#list>
    </ol>

    <a href="/news-and-events/events" class="button"><@fmt.message key="labels.cta"/></a>
</div>
</#if>
