<#ftl output_format="HTML">
<#include "../../include/imports.ftl">
<#include "metaTags.ftl">
<#include "documentHeader.ftl">
<#include "component/showAll.ftl">
<#include "contentPixel.ftl">
<#include "feedlist-getimage.ftl">

<#macro resultsList documentList>
    <#list documentList as item>
        <@resultItem item=item />

        <#if !item?is_last>
            <hr class="nhsd-a-horizontal-rule" />
        </#if>
    </#list>
</#macro>

<#macro resultItem item>
    <#if item.class.name?contains("uk.nhs.digital.website.beans.CyberAlert")>
        <div class="nhsd-!t-margin-bottom-2">
            <span class="nhsd-a-tag nhsd-a-tag--bg-dark-grey">${item.threatId}</span>
            <span class="nhsd-a-tag nhsd-a-tag--bg-light-${(item.severity?lower_case == "high")?then("red", "blue")}">${item.severity}</span>
        </div>
    </#if>
    <div><a href="<@hst.link hippobean=item/>" class="nhsd-a-link">${item.title}</a></div>
    <p class="nhsd-t-body nhsd-!t-margin-top-2" data-uipath="website.blog.summary" itemprop="articleBody">${item.shortsummary}</p>
    <#if item.class.name?contains("uk.nhs.digital.website.beans.CyberAlert")>
        <div>
            <span class="nhsd-a-tag nhsd-a-tag--bg-light-grey">${item.threattype}</span>
        </div>
    </#if>
</#macro>
