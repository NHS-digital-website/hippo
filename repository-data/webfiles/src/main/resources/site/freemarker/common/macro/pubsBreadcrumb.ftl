<#ftl output_format="HTML">

<#macro pubsBreadcrumb title>
<div class="grid-wrapper">
    <div class="grid-row">
        <div class="column column--reset">
            <div class="breadcrumb list list--inline list--reset">
                <a href="<@hst.link siteMapItemRefId='root'/>" class="breadcrumb__link">NHS Digital</a>
                <#if title?has_content>
                <img src="<@hst.webfile  path="images/icon-arrow.png"/>" alt="" class="breadcrumb__sep"/>
                <span class="breadcrumb__link breadcrumb__link--secondary">${title}</span>
                </#if>
            </div>
        </div>
    </div>
</div>
</#macro>
