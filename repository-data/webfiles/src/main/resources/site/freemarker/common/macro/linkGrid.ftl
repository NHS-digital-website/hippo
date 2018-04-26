<#ftl output_format="HTML">
<#include "../../include/imports.ftl">

<#macro linkGrid links>
<#if links?has_content>
<#list links as block>
    <#if block?is_odd_item>
    <div class="grid-row">
    </#if>
        <div class="column column--one-half ${block?is_odd_item?then("column--left", "column--right")}">
            <article class="cta">
                <#if block.getType() == "internal">
                <h2 class="cta__title"><a href="<@hst.link hippobean=block.link />">${block.link.title}</a></h2>
                <p class="cta__text">${block.link.shortsummary}</p>
                <#else>
                <#assign onClickMethodCall = getOnClickMethodCall(document.class.name, block.link) />
                <h2 class="cta__title"><a href="${block.link}" onClick="${onClickMethodCall}">${block.title}</a></h2>
                <p class="cta__text">${block.shortsummary}</p>
                </#if>
            </article>
        </div>
    <#if block?is_even_item || block?counter==links?size>
    </div>
    </#if>
</#list>
</#if>
</#macro>
