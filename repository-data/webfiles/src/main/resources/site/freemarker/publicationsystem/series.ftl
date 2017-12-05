<#include "../include/imports.ftl">

<@hst.setBundle basename="publicationsystem.headers"/>

<#if series??>

<section class="document-header">
  <div class="document-header__inner">
    <div class="layout-5-6">
      <h1 class="push--bottom" data-uipath="ps.document.title">${series.title}</h1>
    </div>
  </div>
</section>

<section class="document-content">
  <div class="layout layout--large">
    <div class="layout__item layout-3-4">
      <h2><@fmt.message key="headers.summary"/></h2>
      <#list series.summary.elements as element>
        <#if element.type == "Paragraph">
          <p data-uipath="ps.series.summary">${element?html}</p>
        </#if>
      </#list>
      <ul class="simple-list simple-list--publications" data-uipath="ps.series.publications-list">
        <#list publications as publication>
          <li>
            <a href="<@hst.link hippobean=publication.selfLinkBean/>"
              title="${publication.title}">
              ${publication.title}
            </a>
            <p><@truncate text=publication.summary size="300"/></p>
          </li>
        </#list>
      </ul>
    </div>
  </div>
</section>

<#else>
  <span>${error}</span>
</#if>
