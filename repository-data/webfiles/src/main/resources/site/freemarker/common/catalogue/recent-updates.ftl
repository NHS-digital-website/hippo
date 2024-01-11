<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "../../nhsd-common/macro/pagination.ftl">
<#include "../../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../../nhsd-common/macro/heroes/hero.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ComponentList" -->

<@hero getHeroOptions(document) />
<br/>
<#assign dateGroupHash = {} />
<#list pageable.items as item>
    <@fmt.formatDate value=item.getPublishedDateCommon() type="Date" pattern="dd MMMM yyyy" var="date" timeZone="${getTimeZone()}" />
    <#assign dateGroupHash = dateGroupHash + {  date : (dateGroupHash[date]![]) + [ item ] } />
</#list>
<div class="nhsd-!t-margin-bottom-2">
    <div class="nhsd-t-grid">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <#list dateGroupHash?keys as groupDate>
                    <@group groupDate dateGroupHash />
                </#list>
                <div class="nhsd-!t-padding-top-6">
                    <#if pageable?? && pageable.total gt 0>
                        <@pagination />
                    </#if>
                </div>
            </div>
        </div>
    </div>
</div>

<#macro recentUpdate item>
    <div class="nhsd-t-col-12">
        <div class="nhsd-t-row">
            <@hst.link var="link" hippobean=item/>
            <a href="${link}" class="nhsd-a-link" title="${item.title}">${item.title}</a>
        </div>
        <div class="nhsd-t-row" style="margin-top: 5px">
            <p class="nhsd-t-body nhsd-t-word-break">${item.shortsummary}</p>
        </div>
    </div>
</#macro>

<#macro group groupDate dateGroupHash>
    <div class="nhsd-t-row" style="border-bottom: 1px solid lightgrey; padding-bottom: 30px; margin-top: 30px">
        <div class="nhsd-t-col-12">
            <div class="nhsd-t-row">
                <div class="nhsd-t-col-1">
                    <h2 style="font-weight: lighter">Updated:${" "}</h2>
                </div>
                <div class="nhsd-t-col-3">
                    <h2>${groupDate}</h2>
                </div>
            </div>
            <#list dateGroupHash[groupDate] as item>
                <div class="nhsd-t-row" style="margin-bottom: 50px;">
                    <@recentUpdate item/>
                </div>
            </#list>
            <div class="nhsd-t-row">
                <div style="border-bottom: grey; height: 2px; width: 200px"></div>
            </div>
        </div>
    </div>
</#macro>
