<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if ciBreadcrumb?? && ciBreadcrumb.items?size gte 1>
<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">
            <#if ciBreadcrumb?? && ciBreadcrumb.items?size gte 1>
                <ol class="breadcrumb" title="Navigation">
                    <li class="breadcrumb__crumb">
                        <a href="<@hst.link siteMapItemRefId='root'/>" class="breadcrumb__link" title="NHS Digital">NHS Digital</a>
                    </li>

                    <#if ciBreadcrumb.clinicalIndicator>
                        <li class="breadcrumb__crumb">
                            <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="Right arrow icon" class="breadcrumb__sep"/>
                            <@hst.link var="cilink" path="/data-and-information#clinicalindicators" />
                            <a href="${cilink}" class="breadcrumb__link" title="Data and information">Data and information</a>
                        </li>
                    </#if>

                    <#list ciBreadcrumb.items as item>
                        <li class="breadcrumb__crumb" title="${item.title}">
                            <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="Right arrow icon" class="breadcrumb__sep"/>
                            <#if !item?is_last>
                                <@hst.link var="link" link=item.link/>
                                <a href="${link}" class="breadcrumb__link">${item.title}</a>
                            <#else>
                                <span class="breadcrumb__link breadcrumb__link--secondary">${item.title}</span>
                            </#if>
                        </li>
                    </#list>
                </ol>
            </#if>
        </div>
    </div>
</div>
</#if>
