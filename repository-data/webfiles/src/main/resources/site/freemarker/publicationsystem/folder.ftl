<#ftl output_format="HTML">
<#include "../include/imports.ftl">

  <#if series??>
      <#include "./series.ftl">
  <#elseif publication??>
      <#include "./publication.ftl">
  <#else>
    <h1>Page not found</h1>
    <p>The page you're looking for does not exist. But we did find some other content.</p>
    <ul>
      <#list publications as publication>
        <li>
            <a href="<@hst.link hippobean=publication.selfLinkBean/>">${publication.title}</a>
            <p><@truncate text=publication.summary.firstParagraph size="300"/></p>
        </li>
      </#list>
    </ul>
  </#if>
