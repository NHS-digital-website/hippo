<#include "../include/imports.ftlh">

  <#if series??>
      <#include "./series.ftlh">
  <#elseif publication??>
      <#include "./publication.ftlh">
  <#else>
    <h2>Page not found</h2>
    <p>The page you're looking for does not exist. But we did find some other content</p>
    <ul>
      <#list publications as publication>
        <li>
            <a href="<@hst.link hippobean=publication.selfLinkBean/>">${publication.title}</a>
            <p><@truncate text=publication.summary size="300"/></p>
        </li>
      </#list>
    </ul>
  </#if>
