<#ftl output_format="HTML">

<#include "../../include/imports.ftl">

<#function isAnyNotPersonDoctype articles>
    <#list articles as article>
        <#if article.class.name != "uk.nhs.digital.website.beans.Person">
            <#return true />
        </#if>
    </#list>
    <#return false />
</#function>

<#macro relatedarticles articles articleType showDates=true idsuffix='id' title='Related documents' showSummary=false>
    <#if articles?has_content && isAnyNotPersonDoctype(articles) >
        <div id="related-articles-${slugify(idsuffix)}">
            <h2 class="nhsd-t-heading-xl">${title}</h2>

            <ul class="nhsd-t-list nhsd-t-list--bullet nhsd-t-list--links">
                <#list articles as article>
                    <#if article.class.name != "uk.nhs.digital.website.beans.Person">
                        <#-- do NOT display Person doctypes as related documents-->
                        <li>
                            <@hst.link hippobean=article var="link"/>
                            <a class="nhsd-a-link" href="${link}" title="${article.title}">${article.title}</a>
                            <#if showDates>
                                <#assign dateandtime = "" />

                                <#if article.publisheddatetime?? && article.publisheddatetime?has_content>
                                    <#-- ex. for news doctype-->
                                    <#assign dateandtime = article.publisheddatetime.time />
                                <#elseif article.events?? && article.events?has_content>
                                    <#-- .ex. for events doctype - get startdate of latest event-->
                                    <#list article.events as event>
                                        <#assign dateandtime = event.startdatetime.time />
                                    </#list>
                                </#if>

                                <#if dateandtime?has_content>
                                    <div class="nhsd-t-body-s nhsd-!t-margin-bottom-0">
                                        <@fmt.formatDate value=dateandtime type="Date" pattern="d MMMM yyyy" var="publishedDateTimeString" timeZone="${getTimeZone()}" />
                                        <@fmt.formatDate value=dateandtime type="Date" pattern="yyyy-MM-dd HH:mm:ss" var="publishedDateTime" timeZone="${getTimeZone()}" />
                                        <time  datetime="${publishedDateTime}">${publishedDateTimeString}</time>
                                    <div>
                                </#if>
                            </#if>

                            <#if showSummary && article.shortsummary?? && article.shortsummary?has_content>
                                <div class="nhsd-t-body-s nhsd-!t-margin-bottom-0">${article.shortsummary}</div>
                            </#if>
                        </li>
                    </#if>
                </#list>
            </ul>
        </div>
    </#if>
</#macro>
