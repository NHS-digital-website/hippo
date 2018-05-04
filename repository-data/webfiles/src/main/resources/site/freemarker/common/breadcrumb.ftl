<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if breadcrumb?? && breadcrumb.items?size gte 1>
<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">
            <div class="breadcrumb">
                <a href="<@hst.link siteMapItemRefId='root'/>" class="breadcrumb__link">NHS Digital</a>
                <#list breadcrumb.items as item>
                    <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="" class="breadcrumb__sep"/>
                    <#if !item?is_last >
                    <@hst.link var="link" link=item.link/>
                    <a href="${link}" class="breadcrumb__link">${item.title}</a>
                    <#else>
                    <span class="breadcrumb__link breadcrumb__link--secondary">${item.title}</span>
                    </#if>
                </#list>
            </div>
        </div>
    </div>
</div>
</#if>
