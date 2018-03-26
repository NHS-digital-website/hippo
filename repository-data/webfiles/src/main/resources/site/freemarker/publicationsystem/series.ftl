<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers,publicationsystem.labels"/>

<h2>template: series.ftl</h2>

<#if series??>
    <section class="document-header" aria-label="Series Title">
        <div class="document-header__inner">
            <h3 class="flush--bottom push-half--top"><@fmt.message key="labels.series"/></h3>
            <div class="layout-5-6">
                <h1 class="push--bottom" data-uipath="ps.document.title">${series.title}</h1>
            </div>
        </div>
    </section>

    <section class="document-content" aria-label="Series Content">
        <div class="layout layout--large">
            <div class="layout__item layout-3-4">
                <h2><@fmt.message key="headers.summary"/></h2>
                <@structuredText item=series.summary uipath="ps.series.summary" />

                <#if publications?has_content>
                    <#if series.showLatest>
                        <h3 class="flush push--bottom"><@fmt.message key="headers.latest-version"/></h3>
                        <ul class="simple-list simple-list--publications" data-uipath="ps.series.publications-list.latest">
                            <@publicationItem publication=publications?first/>
                        </ul>

                        <#if publications?size gt 1>
                            <h3 class="flush push--bottom"><@fmt.message key="headers.previous-versions"/></h3>
                            <ul class="simple-list simple-list--publications" data-uipath="ps.series.publications-list.previous">
                                <#list publications[1..] as publication>
                                    <@publicationItem publication=publication/>
                                </#list>
                            </ul>
                        </#if>
                    <#else>
                        <ul class="simple-list simple-list--publications" data-uipath="ps.series.publications-list">
                            <#list publications as publication>
                                <@publicationItem publication=publication/>
                            </#list>
                        </ul>
                    </#if>
                </#if>
            </div>
        </div>
    </section>
<#else>
  <span>${error}</span>
</#if>

<#macro publicationItem publication>
<li>
    <a href="<@hst.link hippobean=publication.selfLinkBean/>"
       title="${publication.title}">
        ${publication.title}
    </a>
    <#if publication.class.name == "uk.nhs.digital.ps.beans.Publication">
        <p><@truncate text=publication.summary.firstParagraph size="300"/></p>
    </#if>
</li>
</#macro>
