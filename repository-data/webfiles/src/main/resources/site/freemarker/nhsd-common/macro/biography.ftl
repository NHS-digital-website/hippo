<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Biography" -->

<#include "../../include/imports.ftl">
<#include "sections/sections.ftl">

<#macro biography biographies idsuffix>
    <#if document.biographies?has_content >
        <#assign profbiography><@hst.html hippohtml=biographies.profbiography contentRewriter=gaContentRewriter/></#assign>

        <#if profbiography?has_content>
            <div id="biography-${slugify(idsuffix)}" class="biography--div article-section no-border">
                <div data-uipath="biographies.profbiography">
                    ${profbiography}
                </div>
            </div>
        </#if>

        <#if biographies.sections?has_content>
            <@sections biographies.sections/>
        </#if>
    </#if>
</#macro>
