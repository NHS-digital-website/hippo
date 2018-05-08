<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if ciBreadcrumb?? && ciBreadcrumb.items?size gte 1>
    <ul class="breadcrumb" title="Navigation">
        <li class="breadcrumb__crumb">
            <span class="label">You are here:</span>
        </li>
        <li class="breadcrumb__crumb">
            <@hst.link var="homelink" path="/" />
            <a href="${homelink}" title="NHS Digital">NHS Digital</a>
        </li>

        <#if ciBreadcrumb.clinicalIndicator>
            <li class="separator">${ciBreadcrumb.separator}</li>
            <li class="breadcrumb__crumb">
                <@hst.link var="cilink" path="/data-and-information#clinicalindicators" />
                <a href="${cilink}" title="Data and information">Data and information</a>
            </li>
        </#if>

        <#list ciBreadcrumb.items as item>
            <li class="separator">${ciBreadcrumb.separator}</li>
            <#if !item?is_last>
                <@hst.link var="link" link=item.link/>
                <li><a href="${link}" class="breadcrumb__link" title="${item.title}">${item.title}</a></li>
            <#else>
                <li><span class="breadcrumb__link breadcrumb__link--secondary" title="${item.title}">${item.title}</span></li>
            </#if>
        </#list>
    </ul>
</#if>
