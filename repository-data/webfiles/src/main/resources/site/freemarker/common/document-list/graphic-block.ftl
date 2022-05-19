<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../macro/sections/statistic.ftl">
<#include "../macro/iconGenerator.ftl">
<#include "../macro/gridColumnGenerator.ftl">
<#include "../macro/graphicBlock.ftl">

<@hst.setBundle basename="rb.generic.texts"/>

<div class="nhsd-o-graphic-block-list nhsd-!t-margin-bottom-9">
    <div class="nhsd-t-grid nhsd-!t-no-gutters">
        <div class="nhsd-t-row nhsd-t-row--centred nhsd-o-graphic-block-list__items">
            <#if pageable?? && pageable.items?has_content>
                <#list pageable.items as item>
                    <#assign hasStats = item.modules?has_content />

                    <#assign graphicBlockOptions={}>

                    <#if item.image?has_content>
                        <#assign altText = item.altText?has_content?then(item.altText, "image for graphic block ${item?index}") />
                        <@hst.link hippobean=item.image fullyQualified=true var="graphicImage" />

                        <#assign graphicBlockOptions += {
                            "image": graphicImage,
                            "altText": altText
                        } />
                    </#if>

                    <#if hasStats>
                        <#assign stats = item.modules[0] />

                        <#if stats.statisticType == "staticStatistic">
                            <#assign number = stats.number />
                        <#elseif stats.statisticType == "feedStatistic">
                            <#assign remoteStat="uk.nhs.digital.freemarker.statistics.RemoteStatisticFromUrl"?new() />
                            <#assign remote = (remoteStat(stats.urlOfNumber))! />

                            <#if remote??>
                                <#if (remote.number)??>
                                    <#assign number = remote.number />
                                </#if>
                            </#if>
                        </#if>

                        <#assign heading>
                            <#if number?has_content>
                                ${getPrefix(stats.prefix)}${number}${getSuffix(stats.suffix)}
                                <#assign trend = stats.trend />
                                <#if trend == "auto">
                                    <#if number?number == 0>
                                        <#assign trend = "none"/>
                                    <#else>
                                        <#assign trend = (number?number gt 0)?then("up", "down") />
                                    </#if>
                                </#if>
                                <#if trend != "none">
                                    <@buildInlineSvg trend />
                                </#if>
                            </#if>
                        </#assign>

                        <#assign graphicBlockOptions += { "heading": heading } />
                    </#if>

                    <#if item.title?has_content>
                        <#assign graphicBlockOptions += { "heading": item.title } />
                    </#if>

                    <@hst.html hippohtml=stats.headlineDescription var="headlineDesc"/>
                    <#if headlineDesc?has_content>
                        <#assign graphicBlockOptions += { "headlineDesc": headlineDesc?replace('<[^>]+>','','r') } />
                    </#if>

                    <@hst.html hippohtml=stats.furtherQualifyingInformation var="furtherQualInfo"/>
                    <#if furtherQualInfo?has_content>
                        <#assign graphicBlockOptions += { "furtherInfo": furtherQualInfo?replace('<[^>]+>','','r') } />
                    </#if>

                    <#if item.content?has_content>
                        <#assign graphicBlockOptions += { "furtherInfo": item.content } />
                    </#if>

                    <#assign external = false />
                    <#if item.items?has_content>
                        <#assign itemLink = item.items[0] />

                        <#if itemLink.linkType == "internal">
                            <@hst.link hippobean=itemLink.link var="internalLink" />
                            <#assign title = itemLink.link.title />
                            <#assign link = internalLink />
                        <#elseif itemLink.linkType == "external">

                            <#assign externalLink = itemLink.link />
                            <#assign link = externalLink />
                            <#assign title = itemLink.title />
                            <#assign external = true />
                        </#if>

                    <#elseif item.internal?has_content>
                        <#assign title = item.label />
                        <@hst.link hippobean=item.internal var="internalLink" />
                        <#assign link = internalLink />
                    <#elseif item.external?has_content>
                        <#assign title = item.label />
                        <#assign externalLink = item.external />
                        <#assign external = true />
                        <#assign link = externalLink />
                    </#if>
                    <#if link?has_content && title?has_content >
                        <#assign graphicBlockOptions += {
                            "link": {
                                "title": title,
                                "href": link,
                                "external": external
                            }
                        } />
                    </#if>

                    <div class="nhsd-t-col-xs-12 ${getGridCol(pageable.items?size, "small")}">
                        <@graphicBlock graphicBlockOptions />
                    </div>
                </#list>
            </#if>
        </div>
    </div>
</div>
