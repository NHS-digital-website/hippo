<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#assign alignmentClass="nhsd-t-text-align-left">
<#if alignment?? && alignment == "center">
    <#assign alignmentClass="nhsd-t-text-align-center">
</#if>
<#if source??>
    <div class="${alignmentClass}">
        <#if source.title??>
            <h2>${source.title}</h2>
        </#if>
        <#if source.content??>
            <p>${source.content}</p>
        </#if>
    </div>
</#if>
