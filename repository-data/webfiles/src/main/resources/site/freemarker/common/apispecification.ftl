<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "macro/documentHeader.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiSpecification" -->
<#-- @ftlvariable name="hstRequestContext" type="org.hippoecm.hst.core.request.HstRequestContext" -->

<#if document?? >

    <#assign isTryItNow = hstRequestContext.servletRequest.parameterMap?keys?seq_contains("tryitnow")>
    <#if isTryItNow >

        <#include "api-try-it-now.ftl">

    <#else>

        <article class="article article--apispecification" itemscope>
            <@documentHeader document 'general'></@documentHeader>

            <style type="text/css">
                .ctabtn--nhs-digital-button--try-it-now { float: right; }

                pre {
                    color: #FFF;
                    background-color: #000;
                    display:block;
                    text-align:left;
                    overflow: auto;
                    overflow-y: auto;
                    max-height: 500px;
                    box-sizing:border-box;
                    padding:0.5em;
                }

            </style>

            <div class="grid-wrapper grid-wrapper--article">
                <div class="grid-row">

                    ${document.html?no_esc}

                </div>
            </div>
        </article>

        <script>
            // used in function tryEndpointNow from apispecificaion.js
            const tryEndpointNowBaseUrl = '<@hst.link siteMapItemRefId='root'/>';
        </script>
        <script src="<@hst.webfile path="/apispecification/apispecification.js"/>"> </script>
    </#if>

</#if>
