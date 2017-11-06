<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.headers"/>

<#if series??>

<h2 class="doc-title" data-uipath="ps.series.title">${series.title}</h2>
<p class="doc-summary" data-uipath="ps.series.summary">${series.summary}</p>

<ul class="simple-list simple-list--publications"
    data-uipath="ps.series.publications-list"
>
    <#list publications as publication>
        <li>
            <a
                href="<@hst.link hippobean=publication.selfLinkBean/>"
                title="${publication.title}"
            >${publication.title}</a>
            <p><@truncate text=publication.summary size="300"/></p>
        </li>
    </#list>
</ul>

<#else>
    <span>${error}</span>
</#if>
