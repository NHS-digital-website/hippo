<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Biography" -->

<#include "../../include/imports.ftl">

  <#macro biography biographies idsuffix>
    <#if document.biographies?has_content && document.biographies.profbiography.content?has_content >
    <div id="biography-${slugify(idsuffix)}" class="biography--div article-section no-border">
        <h2>Biography</h2> 
        <p data-uipath="biographies.profbiography">
        <span itemprop="description"><@hst.html hippohtml=biographies.profbiography contentRewriter=gaContentRewriter/></span>
        </p>
    </div>
    </#if>
  </#macro>
