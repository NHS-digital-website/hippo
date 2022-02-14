<#ftl output_format="HTML">

<#-- @ftlvariable name="section" type="uk.nhs.digital.website.beans.Biography" -->

<#include "../../include/imports.ftl">

<#macro biography biographies idsuffix>
    <#if document.biographies?has_content >
        <#assign profbiography><@hst.html hippohtml=biographies.profbiography contentRewriter=brContentRewriter/></#assign>
        <#assign prevpositions><@hst.html hippohtml=biographies.prevpositions contentRewriter=brContentRewriter/></#assign>
        <#assign nonnhspositions><@hst.html hippohtml=biographies.nonnhspositions contentRewriter=brContentRewriter/></#assign>
        <#assign additionalbiography><@hst.html hippohtml=biographies.additionalbiography contentRewriter=brContentRewriter/></#assign>
        <#assign personalbiography><@hst.html hippohtml=biographies.personalbiography contentRewriter=brContentRewriter/></#assign>
        <#assign biographiesMap = {} />

        <#if profbiography?has_content>
            <#assign biographiesMap += {"profbiography": profbiography } />
        </#if>
        <#if prevpositions?has_content>
            <#assign biographiesMap += {"prevpositions": prevpositions } />
        </#if>
        <#if nonnhspositions?has_content>
            <#assign biographiesMap += {"nonnhspositions": nonnhspositions } />
        </#if>
        <#if additionalbiography?has_content>
            <#assign biographiesMap += {"additionalbiography": additionalbiography } />
        </#if>
        <#if personalbiography?has_content>
            <#assign biographiesMap += {"personalbiography": personalbiography } />
        </#if>

        <#if biographiesMap?size != 0 >
            <div id="biography-${slugify(idsuffix)}">
                <h2 class="nhsd-t-heading-xl">Biography</h2>

                <#if profbiography?has_content>
                    <#if biographiesMap?size != 1 >
                        <h3 class="nhsd-t-heading-l">Professional biography</h3>
                    </#if>
                    <div data-uipath="biographies.profbiography">
                        ${biographiesMap['profbiography']}
                    </div>
                </#if>

                <#if prevpositions?has_content>
                    <#if biographiesMap?size != 1 >
                        <h3 class="nhsd-t-heading-l">Previous positions / experience</h3>
                    </#if>
                    <div data-uipath="biographies.prevpositions">
                        ${biographiesMap['prevpositions']}
                    </div>
                </#if>

                <#if nonnhspositions?has_content>
                    <#if biographiesMap?size != 1 >
                        <h3 class="nhsd-t-heading-l">Non-NHS Digital positions / awards</h3>
                    </#if>
                    <div data-uipath="biographies.nonnhspositions">
                       ${biographiesMap['nonnhspositions']}
                    </div>
                </#if>

                <#if additionalbiography?has_content>
                    <#if biographiesMap?size != 1 >
                        <h3 class="nhsd-t-heading-l">Additional biography</h3>
                    </#if>
                    <div data-uipath="biographies.additionalbiography">
                        ${biographiesMap['additionalbiography']}
                    </div>
                </#if>

                <#if personalbiography?has_content>
                    <#if biographiesMap?size != 1 >
                        <h3 class="nhsd-t-heading-l">Personal biography</h3>
                    </#if>
                    <div data-uipath="biographies.personalbiography">
                        ${biographiesMap['personalbiography']}
                    </div>
                </#if>
            </div>
        </#if>
    </#if>
</#macro>
