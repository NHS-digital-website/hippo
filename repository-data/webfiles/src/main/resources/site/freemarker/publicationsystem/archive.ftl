<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "./macro/structured-text.ftl">
<@hst.setBundle basename="publicationsystem.headers,publicationsystem.labels"/>

<#if archive??>
    <section class="document-header" aria-label="Archive Title">
        <div class="document-header__inner">
            <h3 class="flush--bottom push-half--top"><@fmt.message key="labels.archive"/></h3>
            <div class="layout-5-6">
                <h1 class="push--bottom" data-uipath="document.title">${archive.title}</h1>
            </div>
        </div>
    </section>

    <section class="document-content" aria-label="Archive Content">
        <div class="layout layout--large">
            <div class="layout__item layout-3-4">
                <h2><@fmt.message key="headers.summary"/></h2>
                <@structuredText item=archive.summary uipath="ps.archive.summary" />

                <ul class="simple-list simple-list--publications" data-uipath="ps.archive.publications-list"><#list publications as publication>
                    <li>
                        <a href="<@hst.link hippobean=publication.selfLinkBean/>"
                            title="${publication.title}">
                            ${publication.title}
                        </a>
                        <p><@truncate text=publication.summary.firstParagraph size="300"/></p>
                    </li>
                </#list></ul>
            </div>
        </div>
    </section>
<#else>
  <span>${error}</span>
</#if>
