<#ftl output_format="HTML">
<#include "./include/imports.ftl">
<#if breadcrumb?? && breadcrumb.items?size gte 1>
<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">
            <nav aria-label="Breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb__crumb">
                        <a href="<@hst.link siteMapItemRefId='root'/>" class="breadcrumb__link" data-text="NHS Digital">NHS Digital</a>
                    </li>
                    <#list breadcrumb.items as item>
                        <li class="breadcrumb__crumb">
                            <img src="<@hst.webfile  path="images/icon-arrow.svg"/>" alt="Right arrow icon" class="breadcrumb__sep" aria-hidden="true" />
                            <#if !item?is_last>
                            <@hst.link var="link" link=item.link/>
                            <a href="${link}" class="breadcrumb__link" data-text="${item.title}">${item.title}</a>
                            <#else>
                            <span class="breadcrumb__link breadcrumb__link--secondary" data-text="${item.title}" aria-current="page">${item.title}</span>
                            </#if>
                        </li>
                    </#list>
                </ol>
            </nav>
        </div>
    </div>
</div>
</#if>
