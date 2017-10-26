<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.headers"/>

<#if series??>

<h2 class="doc-title" data-uipath="ps.series.title">${series.title}</h2>
<p class="doc-summary">${series.summary}</p>

<h3><@fmt.message key="headers.publications-list"/><h3>
<ul class="simple-list simple-list--publications"
    data-uipath="ps.series.publications-list"
>
    <#list publications as publication>
        <li>
            <a href="<@hst.link hippobean=publication/>">${publication.title}</a>
        </li>
    </#list>
</ul>

<#else>
    <span>${error}</span>
</#if>
