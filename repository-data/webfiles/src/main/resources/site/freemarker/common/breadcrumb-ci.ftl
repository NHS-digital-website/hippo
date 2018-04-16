<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">
            <div class="breadcrumb list list--inline list--reset">

                <#if ciBreadcrumb?? && ciBreadcrumb.items?size gte 1>

                    <ul class="breadcrumb" title="Navigation">

                        <li class="breadcrumb__crumb">
                            <a href="<@hst.link siteMapItemRefId='root'/>" class="breadcrumb__link">NHS Digital</a>
                        </li>

                        <#if ciBreadcrumb.clinicalIndicator>
                            <li class="breadcrumb__crumb">
                                <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="" class="breadcrumb__sep"/>
                                <@hst.link var="cilink" path="/data-and-information#clinicalindicators" />
                                <a href="${cilink}" class="breadcrumb__link" title="Data and information">Data and information</a>
                            </li>
                        </#if>

                        <#list ciBreadcrumb.items as item>
                            <li class="breadcrumb__crumb">
                                <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="" class="breadcrumb__sep"/>
                                <#if !item?is_last>
                                    <@hst.link var="link" link=item.link/>
                                    <a href="${link}" class="breadcrumb__link">${item.title}</a>
                                <#else>
                                    <span class="breadcrumb__link breadcrumb__link--secondary">${item.title}</span>
                                </#if>
                            </li>
                        </#list>
                    </ul>


                </#if>

            </div>
        </div>
    </div>
</div>
