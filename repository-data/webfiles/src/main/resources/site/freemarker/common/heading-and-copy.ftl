<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#-- @ftlvariable name="source" type="uk.nhs.digital.website.beans.Calltoaction" -->
<#-- @ftlvariable name="alignment" type="java.lang.String" -->

<#assign alignmentClass="nhsd-t-text-align-left">
<#if alignment?? && alignment == "center">
    <#assign alignmentClass="nhsd-t-text-align-center">
</#if>
<#if source??>
    <div class="nhsd-t-grid">
        <div class="${alignmentClass}">
            <#if source.title??>
                <h2>${source.title}</h2>
            </#if>
            <#if source.content??>
                <p>${source.content}</p>
            </#if>
        </div>
    </div>
</#if>
