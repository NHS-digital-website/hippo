<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "../nhsd-common/macros/header-banner.ftl">
<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiSpecification" -->
<#-- @ftlvariable name="hstRequestContext" type="org.hippoecm.hst.core.request.HstRequestContext" -->

<#if document?? >

    <#assign isTryItNow = hstRequestContext.servletRequest.parameterMap?keys?seq_contains("tryitnow")>
    <#if isTryItNow >

        <#include "api-try-it-now.ftl">

    <#else>

        <#-- Add meta tags -->
        <#include "../common/macro/metaTags.ftl">
        <@metaTags></@metaTags>

        <article itemscope>
            <@headerBanner document />

            <div class="nhsd-t-grid">
                <div class="nhsd-t-row">${document.html?no_esc}</div>
            </div>
        </article>

        <script>
            // used in function tryEndpointNow from apispecificaion.js
            <@hst.renderURL var="tryItNowUrl"/>
            const tryEndpointNowBaseUrl = '${tryItNowUrl}';
        </script>
        <script src="<@hst.webfile path="/apispecification/apispecification.js"/>"> </script>
    </#if>

</#if>
