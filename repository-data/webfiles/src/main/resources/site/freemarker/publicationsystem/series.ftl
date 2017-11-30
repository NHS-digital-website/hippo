<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.headers"/>

<#if series??>

<div class="content-section content-section--highlight">
    <h2 class="doc-title" data-uipath="ps.series.title">${series.title}</h2>
</div>

<div class="content-section">
    <h3><@fmt.message key="headers.summary"/></h3>
    <#list series.summary.elements as element>
        <#if element.type == "Paragraph">
        <p class="doc-summary" data-uipath="ps.series.summary">${element?html}</p>
        </#if>
    </#list>


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
</div>

<#else>
    <span>${error}</span>
</#if>
