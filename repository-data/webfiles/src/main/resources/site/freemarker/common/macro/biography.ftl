<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Biography" -->

<#include "../../include/imports.ftl">

<#macro biography biographies idsuffix>
    <#if document.biographies?has_content >
        <#assign profbiography><@hst.html hippohtml=biographies.profbiography contentRewriter=gaContentRewriter/></#assign>

        <#if profbiography?has_content>
            <div id="biography-${slugify(idsuffix)}" class="biography--div article-section no-border">
                <h2>Biography</h2>
                <h3>Short biography</h3>
                <div data-uipath="biographies.profbiography">
                    ${profbiography}
                </div>
            </div>
        </#if>
    </#if>
</#macro>
