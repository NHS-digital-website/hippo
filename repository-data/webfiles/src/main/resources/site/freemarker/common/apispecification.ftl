<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiSpecification" -->
<#-- @ftlvariable name="hstRequestContext" type="org.hippoecm.hst.core.request.HstRequestContext" -->

<#if document?? >

    <#assign isTryItNow = hstRequestContext.servletRequest.parameterMap?keys?seq_contains("tryitnow")>
    <#if isTryItNow >

        <#include "api-try-it-now.ftl">

    <#else>
        <#assign renderHtml = "uk.nhs.digital.common.components.apispecification.ApiSpecificationRendererDirective"?new() />

    <#-- Add meta tags -->
        <#include "../common/macro/metaTags.ftl">
        <@metaTags></@metaTags>

        <article itemscope>

            <@hero getExtendedHeroOptions(document) />
            <div class="nhsd-t-grid nhsd-!t-margin-top-6">
                <div class="nhsd-t-row">
                    <@renderHtml specificationJson=document.json path=path documentHandleUuid=document.getCanonicalHandleUUID()/>
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4"></div>
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
                        <@lastModified document.lastModified></@lastModified>
                    </div>
                </div>
            </div>

        </article>

        <script>
            // used in function tryEndpointNow from apispecification.js
            <@hst.renderURL var="tryItNowUrl"/>
            const tryEndpointNowBaseUrl = '${tryItNowUrl}';
        </script>
        <script
            src="<@hst.webfile path="/apispecification/apispecification.js"/>"></script>
    </#if>

</#if>

<#function getExtendedHeroOptions document>
    <#assign options = getHeroOptions(document) />

    <@hst.html var="htmlSummary" hippohtml=options.summary contentRewriter=brContentRewriter />
    <@hst.link var="oasHintLink" path='developer/guides-and-documentation/our-api-technologies#oas/'/>
    <#assign oasHintText = "<p style=\"margin-left:0px; margin-right:0px\">This specification is written from an <a href=\"${oasHintLink}\">OAS</a> file.</p>">

    <@hst.link var="oasLink" path='/restapi/oas/${document.getSpecificationId()}/'/>

    <#assign options += {
    "summary": (htmlSummary + oasHintText)?no_esc,
    "buttons": [{
    "src": oasLink,
    "text": "Get this specification in OAS format",
    "target": "_blank"
    }]
    }/>
    <#return options/>
</#function>
