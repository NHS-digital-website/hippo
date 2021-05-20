<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include '../macro/media-item.ftl'>
<#include '../macro/docGetImage.ftl'>

<#list pageable.items>
    <section class="feedlist">
        <#if (titleText)?has_content >
            <h2 class="section-title feedlist__title">${titleText}</h2>
        </#if>
        <div class="feedlist__items media-items media-items--small media-items--${pageable.items?size}up">
            <#items as feedItem>
                <#assign imageData = getImageData(feedItem) />
                <#assign feedItemTitle = feedItem.title />
                <#assign feedItemShortSummary = feedItem.shortsummary />

                <#assign date = '' />
                <#assign dateTo = '' />
                <#if feedItem.dateOfPublication?has_content>
                    <#assign date = feedItem.dateOfPublication.time />
                <#elseif feedItem.publisheddatetime?has_content>
                    <#assign date = feedItem.publisheddatetime.time />
                <#elseif (feedItem.events)??>
                <#-- event start and end dates -->
                    <#assign dateRangeData = getEventDateRangeData(feedItem.events) />
                    <#if dateRangeData?size gt 0>
                        <#assign date = dateRangeData.minStartTime />
                        <#if dateRangeData.comparableStartDate != dateRangeData.comparableEndDate>
                            <#assign dateTo = dateRangeData.maxEndTime />
                        </#if>
                    </#if>
                </#if>

                <@hst.link hippobean=feedItem var="linkDestination"/>

                <@mediaItem
                image=imageData[0]
                alttext=imageData[1]
                feedItemTitle=feedItemTitle
                feedItemShortSummary=feedItemShortSummary
                linkDestination=linkDestination
                date=date
                dateTo=dateTo
                className="feedlist__item"
                />
            </#items>
        </div>
        <#if (buttonDestination)?has_content && (buttonText)?has_content >
            <div class="feedlist__button-container">
                <a class="nhsd-a-button feedlist__button" href="${buttonDestination}">
                    <span class="nhsd-a-button__label">${buttonText}</span>
                </a>
            </div>
        </#if>
    </section>
</#list>
