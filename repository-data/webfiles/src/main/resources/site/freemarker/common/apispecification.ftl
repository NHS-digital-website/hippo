<#ftl output_format="HTML">
<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">

<#-- @ftlvariable name="document" type="uk.nhs.digital.website.beans.ApiSpecification" -->
<#-- @ftlvariable name="hstRequestContext" type="org.hippoecm.hst.core.request.HstRequestContext" -->

<!DOCTYPE html>
<html lang="en" class="no-js">

<#--<#include "app-layout-head.ftl">-->

<meta charset="utf-8">

<script type="module"
        src="https://unpkg.com/rapidoc/dist/rapidoc-min.js"></script>

<body>

<#include "../common/macro/metaTags.ftl">
<@metaTags></@metaTags>

<#if document?? >

    <@hero getExtendedHeroOptions(document) />
    <div class="nhsd-t-grid">
        <div class="nhsd-t-row nhsd-!t-margin-top-4" style="height:auto">
            <rapi-doc
                id="rapi-doc-spec"
                bg-color="#ffffff"
                text-color="#3f525f"
                header-color="#231f20"
                primary-color="#005bbb"
                regular-font="Frutiger W01"
                font-size="largest"
                nav-item-spacing="relaxed"
                info-description-headings-in-navbar="true"
                show-header="false"
                allow-search="false"
                allow-advanced-search="false"
                allow-server-selection="false"
                schema-description-expanded="true"
                render-style="focused"
            >

                <div slot="footer">
                    <@lastModified document.lastPublicationDate></@lastModified>
                </div>

            </rapi-doc>

            <script>
                // https://github.com/rapi-doc/RapiDoc/blob/master/docs/examples/example9.html
                const specification = ${document.json?no_esc}

                // hacky until figure out better solution
                delete specification.info["contact"];
                delete specification.info["version"];
                delete specification.info["title"];

                document.addEventListener('DOMContentLoaded', () => {
                    let rapidocEl = document.getElementById("rapi-doc-spec");
                    rapidocEl.loadSpec(specification);

                    let tryThisApiDisabled = specification["x-spec-publication"]?.["try-this-api"]?.disabled;
                    if (tryThisApiDisabled === true) {
                        rapidocEl.setAttribute("allow-try", "false");
                    }

                    let allowAuth = specification.components?.securitySchemes;
                    if (!allowAuth) {
                        rapidocEl.setAttribute("allow-authentication", "false");
                    }
                })

            </script>
        </div>
    </div>


</#if>
</body>
</html>

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
    "text": "Get OAS file",
    "target": "_blank"
    }]
    }/>
    <#return options/>
</#function>
