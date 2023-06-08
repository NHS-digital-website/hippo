<#ftl output_format="HTML">

<#include "../include/imports.ftl">
<#include "../nhsd-common/macro/heroes/hero.ftl">
<#include "../nhsd-common/macro/heroes/hero-options.ftl">
<#include "../nhsd-common/macro/component/lastModified.ftl">

<#if document?? && (document.enableRapiDoc!isDevEnv!false)>
    <#include "../common/macro/metaTags.ftl">
    <@metaTags></@metaTags>

    <script>
        const specification = ${document.json?no_esc};
        const isDevEnv = ${isDevEnv?c};
    </script>
    <script src="<@hst.webfile path="/apispecification/rapidoc-min.js"/>"></script>
    <script src="<@hst.webfile path="/apispecification/rapidoc-customisation.js"/>"></script>

    <style>
        <#--
        Most of the custom RapiDoc styling overrides have been implemented in `src/styles/custom-styles.js` by building RapiDoc from source.
        See https://rapidocweb.com/api.html#developers

        A copy of this file can be found at `site/apispecification/rapidoc-custom-styles.js`. This file has no purpose other than to
        document the styling overrides. To update the styling, clone the RapiDoc repo (https://github.com/rapi-doc/RapiDoc) and copy the
        contents of the `rapidoc-custom-styles.js` file into the `src/styles/custom-styles.js` source file. After adding your custom styles,
        build RapiDoc and the generated `rapidoc-min.js` in the `dist` folder will have your styles. Copy and paste this file into the
        `site/apispecification` folder, overwriting the old one. Make sure to keep our `rapidoc-custom-styles.js` copy up to date.

        We may look to automate this in the future.

        CSS parts can also be used to style RapiDoc (https://rapidocweb.com/css-parts.html). However, this didn't give us to level of
        customisation required and has been used only where required below.
        -->

        .rapi-doc-component {
            overflow: unset;
        }

        rapi-doc::part(section-navbar-search) {
            justify-content: flex-start;
            padding: 0;
            margin-bottom: 15px;
        }

        rapi-doc::part(btn-search) {
            margin: 0;
            width: unset;
        }

        rapi-doc::part(btn-try) {
            margin: 16px 5px 0 0;
        }

        rapi-doc::part(wrap-request-btn) {
            flex-direction: row-reverse;
            flex-wrap: wrap;
            justify-content: flex-end;
            padding-top: 12px;
        }
    </style>

    <@hero getExtendedHeroOptions(document) />

    <div class="nhsd-t-grid nhsd-!t-display-no-js-show">
        <div class="nhsd-t-row">
            <div class="nhsd-t-col-12">
                <p>Enable Javascript to view the interactive API specification.</p>
                <p>Alternatively, you can download the OpenAPI Specification (OAS)
                    file for this API using the button above.</p>
            </div>
        </div>
    </div>

    <div class="nhsd-t-grid nhsd-!t-display-no-js-hide">
        <div class="nhsd-t-row">
            <rapi-doc
                class="rapi-doc-component nhsd-!t-margin-top-6"
                id="rapi-doc-spec"
                theme="light"
                bg-color="#ffffff"
                text-color="#3f525f"
                primary-color="#005bbb"
                regular-font="Frutiger W01, Arial, sans-serif"
                load-fonts="false"
                font-size="largest"
                nav-bg-color="#ffffff"
                nav-text-color="#3f525f"
                info-description-headings-in-navbar="true"
                show-header="false"
                allow-search="false"
                allow-advanced-search="true"
                allow-server-selection="false"
                schema-description-expanded="true"
                render-style="focused"
                sort-endpoints-by="none"
                css-file="nhsd-frontend.css"
                show-curl-before-try="true"
                do-api-catalogue-nav-bar-tweaks="true">
                <div slot="footer">
                    <p class="nhsd-t-body nhsd-!t-margin-top-3"><a
                            class="nhsd-a-link" href="">Back to top</a></p>
                    <@lastModified document.lastPublicationDate></@lastModified>
                </div>
            </rapi-doc>
        </div>
    </div>

<#elseif document?? >
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
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-4">
                    </div>
                    <div class="nhsd-t-col-xs-12 nhsd-t-col-s-8">
                        <@lastModified document.lastPublicationDate></@lastModified>
                    </div>
                </div>
            </div>

        </article>

        <script>
            // used in function tryEndpointNow from apispecification.js
            <@hst.renderURL var="tryItNowUrl"/>
            const tryEndpointNowBaseUrl = '${tryItNowUrl}';
        </script>
        <script src="<@hst.webfile path="/apispecification/apispecification.js"/>"></script>
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
            "text": "Get OAS file",
            "target": "_blank"
        }]
    }/>
    <#return options/>
</#function>
