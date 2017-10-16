<#include "../include/imports.ftl">

<#if series??>

<h1>${series.title}</h1>

<ul id="publicationsWithinSeries">
    <#list publications as publication>

        <@hst.link hippobean=publication var="link"/>

        <li class="publicationWithinSeries">
            <a href="${link}">${publication.title}</a>
        </li>

    </#list>


</ul>

<#else>
    <span>${error}</span>
</#if>
