<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers"/>
<section class="document-content">
    <#if document??>
        <h1 data-uipath="ps.document.title">${document.title}</h1>
        <p data-uipath="ps.document.content">
            <@structuredText item=document.summary uipath="ps.document.summary" />
        </p>
        <h2 class="push--bottom"><@fmt.message key="headers.ci-hub-overview"/></h2>

        <div class="layout layout--large">
            <#list document.ciHubLink as link><!--
                --><div class="layout__item layout-1-3">
                    <@hst.link var="landingPageLink" hippobean=link.pageLink />
                    <h3><a href="${landingPageLink}" title="${link.title}">${link.title}</a></h3>
                    <p class="zeta">${link.summary}</p>
                </div><!--
            --></#list>
        </div>
    </#if>
</section>
