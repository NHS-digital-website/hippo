<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#if ciBreadcrumb?? && ciBreadcrumb.items?size gte 1>
<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">    
            <ol title="Navigation" class="breadcrumb">
                <li>
                    <a href="<@hst.link siteMapItemRefId='root'/>" class="breadcrumb__link" title="NHS Digital">NHS Digital</a>
                </li>

                <#if ciBreadcrumb.clinicalIndicator>
                    <li>
                        <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="Right arrow icon" class="breadcrumb__sep"/>
                        <@hst.link var="cilink" path="/data-and-information#clinicalindicators" />
                        <a href="${cilink}" class="breadcrumb__link" title="Data and information">Data and information</a>
                    </li>
                </#if>
                
                <#list ciBreadcrumb.items as item>
                    <li title="${item.title}">
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
        </div>
    </div>
</div>
</#if>
